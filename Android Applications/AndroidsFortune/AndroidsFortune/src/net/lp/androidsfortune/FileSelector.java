/*   
 * 	 Copyright (C) 2008-2010 pjv (and others, see About dialog)
 * 
 * 	 This file is part of Android's Fortune.
 *
 *   Android's Fortune is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Android's Fortune is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Android's Fortune.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */

package net.lp.androidsfortune;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A browser showing the files inside a folder and letting the user (de)select them.
 * 
 * @author pjv
 *
 */
public class FileSelector extends ListActivity {
	
	/**
	 * The list of files to display.
	 */
	private ArrayList<ShortFile> fileList;
	
	/**
	 * The list widget.
	 */
	private ListView listView;
	
    /**
     * Preferences source.
     */
	private static SharedPreferences settings;
	
    /**
     * Preferences editor/sink.
     */
	private SharedPreferences.Editor editor;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setIntent(new Intent().setData(Uri.fromFile(new File("/sdcard/"))));//TODO for test, move to junit test

		//Check external storage
		try{
			FortuneActivity.check_external_storage_readable(this);
		}catch(IllegalArgumentException e){
			finish();
			return;
		}
		
		//Get the directory if it's valid.
		ShortFile dir = new ShortFile(getIntent().getData().getPath());
		if(!dir.isDirectory()){
			Toast.makeText(this, getString(R.string.app_name_prefix)+" "+getString(R.string.toast_not_a_dir)+" "+dir.getPath(), Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		//Get the files in the dir (filter out .num files as they need not be selected, and only keep existing files). Unfortunately it's not possible to filter for the text/plain MIME type or anything more specific.
		ShortFile[] fileArray=dir.listFiles(new FileFilter(){

			/* (non-Javadoc)
			 * @see java.io.FileFilter#accept(java.io.File)
			 */
			public boolean accept(File file) {
			return file.isFile() && !file.getName().endsWith(".num");
			}
		
		});
		
		//Convert to an ArrayList and sort it
		fileList=new ArrayList<ShortFile>();
		for(int i=0; i<fileArray.length; i++){
			if(fileArray[i]!=null){
				fileList.add(fileArray[i]);
			}
		}
		Collections.sort(fileList);

		//Set up the adapter for the list widget using this list, as well as the widget itself.
		ArrayAdapter<ShortFile> aa=new ArrayAdapter<ShortFile>(this,
			    R.layout.simple_list_item_multiple_choice, fileList);
		setListAdapter(aa);
        listView = getListView();
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        // Restore preferences (and save). Obtain a connection to the preferences.
        settings = getSharedPreferences(PreferencesActivity.PREFS, MODE_PRIVATE);
	    editor = settings.edit();
        restorePreferences();
        savePreferences();
     
        
	}

	/**
	 * Restore preferences (which files were selected).
	 */
	private void restorePreferences() {
		//Get the list of selected files from the preferences.
		String str=settings.getString("str_files", "");
		if(!str.equals("")){
			String[] strList=str.split(" ");
	        for(int i=0; i<strList.length; i++){
	        	int index=fileList.indexOf(new ShortFile(Uri.parse(strList[i]).getPath()));
	        	if(index>=0 && index<fileList.size()){
	            	listView.setItemChecked(index, true);
	            	if(FortuneActivity.DEBUG) Log.i(FortuneActivity.TAG, "CHECK_STR "+strList[i]);
	        	}else{
	        		Toast.makeText(this, getString(R.string.app_name_prefix)+" "+getString(R.string.toast_selected_file_lost)+" "+strList[i], Toast.LENGTH_LONG).show();
	    			if(FortuneActivity.DEBUG) Log.i(FortuneActivity.TAG, "CHECK_ERROR");
	        	}
	        }
		}
	}
	
	/**
	 * A File that is being displayed by its filename only (not the entire path).
	 * 
	 * @author pjv
	 *
	 */
	private class ShortFile extends File implements Comparable<File>{

		/**
		 * Pretty useless serial id.
		 */
		private static final long serialVersionUID = 1399808494695437016L;

		/**
		 * Constructs a new ShortFile using the specified path. 
		 * 
		 * @param path the path to be used for the file 
		 */
		public ShortFile(String path) {
			super(path);
		}

		/* (non-Javadoc)
		 * @see java.io.File#toString()
		 */
		@Override
		public String toString() {
			//Only the filename, not the entire path.
			return super.getName();
		}

		/* (non-Javadoc)
		 * @see java.io.File#listFiles(java.io.FileFilter)
		 */
		@Override
		public ShortFile[] listFiles(FileFilter filter) {
			//The same as for File, but just convert every item from File to ShortFile.
			File[] fileArray=super.listFiles(filter);
			if(fileArray==null){
				return null;
			}
			ShortFile[] sflist=new ShortFile[fileArray.length];
			for(int i=0; i<fileArray.length; i++){
				if(fileArray[i]!=null){
					sflist[i]=new ShortFile(fileArray[i].toString());
				}
			}
			return sflist;
		}

		/* (non-Javadoc)
		 * @see java.io.File#compareTo(java.io.File)
		 */
		@Override
		public int compareTo(File other) {
			if(this.toString() != null)
				return this.toString().compareTo(other.toString()); 
			else 
				throw new IllegalArgumentException();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		//Save preferences. 
		savePreferences();
	}

	/**
	 * Save preferences (which files are selected).
	 */
	private void savePreferences() {
		//Get an overview of the items that are checked in the list widget. This overview matches fileList in order.
		SparseBooleanArray sba=listView.getCheckedItemPositions();
		if(FortuneActivity.DEBUG) Log.e(FortuneActivity.TAG, sba.get(0)+" "+sba.get(1)+" "+sba.get(2)+" "+sba.get(3)+" "+sba.get(4)+" "+sba.get(5)+" "+sba.get(6)+" "+sba.get(7)+" "+sba.get(8)+" "+sba.get(9)+" "+sba.get(10)+" "+sba.get(11)+" "+sba.get(12)+" "+sba.get(13)+" "+sba.get(14)+" "+sba.get(15)+" "+sba.get(16)+" "+sba.get(17));
	    String str="";
	    
	    //Run over all the items and if checked then add the filepath to a string that will be remembered.
	    for(int i=0; i<fileList.size(); i++){//Solved a bug by using fileList.size() instead of sba.size().
	    	if(sba.get(i)){
	    		str+=Uri.fromFile(fileList.get(i))+" ";
	    	}
	    }
	    //Change in editor and commit to save preferences.
	    editor.putString("str_files", str);
		if(FortuneActivity.DEBUG) Log.i(FortuneActivity.TAG, "CLICK SAVE "+str);
	    editor.commit();
		//TODO: in Gingerbread: editor.apply()
	}


}
