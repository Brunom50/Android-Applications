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
 *   
 *   
 *   Some elements based on veecheck-sample (GPL2).
 */

package net.lp.androidsfortune;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jstrfile.jstrfile;

import android.app.AlertDialog;
import android.app.backup.BackupManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.tomgibara.android.veecheck.Veecheck;
import com.tomgibara.android.veecheck.util.PrefSettings;

/**
 * Preferences manager.
 * 
 * @author pjv
 *
 */
/**
 * @author pjv
 *
 */
public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	/**
     * Request code for the "pick files" action (to FileSelector)
     */
    private static final int PICK_FILES_REQUEST = 1;
    
	/**
	 * Id for the preferences registry.
	 */
	public static final String PREFS ="AndroidsFortunePrefs";
	
    /**
     * The widget leading to the file selector.
     */
    private PreferenceScreen psFiles;
    
    /**
     * Preferences source.
     */
    private SharedPreferences settings;
    
	/**
	 * Preference default values constants. Some should match default values in preferences.xml.
	 */
	public static final String DEFAULT_EP_FONTSIZE = "12";
	public static final String DEFAULT_LP_FONT = "serif";
	public static final String DEFAULT_EP_TIME = "60";
	public static final String DEFAULT_LP_WIDGET_FONT = "serif";
	public static final String DEFAULT_EP_WIDGET_PERIOD = "60";
	public static final long DEFAULT_CHECK_INTERVAL = 24 * 60 * 60 * 1000L;
	public static final long DEFAULT_PERIOD = 1 * 60 * 60 * 1000L;

	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		//Obtain a connection to the preferences.
        settings =getSharedPreferences(PREFS, MODE_PRIVATE);
        
        //Make sure these preferences are coupled to the right preferences file and mode.
        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
		getPreferenceManager().setSharedPreferencesName(PREFS);
		
        //Load the preferences from an XML resource.
        addPreferencesFromResource(R.xml.preferences);
        
        //Get the PreferenceScreen widget.
		psFiles = (PreferenceScreen) findPreference("ps_files");
		
		//Restrict other preference dialog widgets.
		EditText etFolder = (EditText) ((EditTextPreference) findPreference("ep_folder")).getEditText();
		etFolder.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		
		EditText etFontsize = (EditText) ((EditTextPreference) findPreference("ep_fontsize")).getEditText();
		etFontsize.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		etFontsize.setKeyListener(DigitsKeyListener.getInstance()); 

		EditText etTime = (EditText) ((EditTextPreference) findPreference("ep_time")).getEditText();
		etTime.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		etTime.setKeyListener(DigitsKeyListener.getInstance()); 

		EditText etWidgetPeriod = (EditText) ((EditTextPreference) findPreference("ep_widget_period")).getEditText();
		etWidgetPeriod.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		etWidgetPeriod.setKeyListener(DigitsKeyListener.getInstance());
		
        //Check if the selection of files is complete (.num files included).
        checkNumFiles();
    }

	/**
	 * Set default values for preferences that are runtime based.
	 * 
	 * @param context Current context.
	 */
	public static void setDynamicDefaultValues(Context context) {
		//Only if not set to defaults yet.
		SharedPreferences settings = context.getSharedPreferences(PREFS, MODE_PRIVATE);
		if(!settings.getBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, false)){
			//Set default for folder (dynamic)
			Editor editor = settings.edit();
			editor.putString("ep_folder", FortuneActivity.getDefaultDirectory(context, context.getPackageName()).getPath());
			editor.commit();
			//TODO: in Gingerbread: editor.apply()
		}
	}
    
    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        
        //Sync the folder for the file selector with the entered folder. 
        updateFolder();
        
        //Set up a listener for whenever a preference changes.
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        
        //Always return OK
        setResult(RESULT_OK);

		FlurryAgent.onPageView();
		FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackPageView("/Preferences");
    }

	/**
	 * Sync the folder for the file selector with the entered folder. 
	 */
	private void updateFolder() {
		//Obtain the default intent from the XML.
        Intent intent=psFiles.getIntent();
        //Update the folder value.
		intent.setDataAndType(Uri.fromFile(new File(getPreferenceScreen().getSharedPreferences().getString("ep_folder", "/"))), "text/plain");
        
		//intent.setClassName(this, "net.lp.androidsfortune.FileSelector");//TODO for testing, make into junit test
		
		//Change what happens when the PreferenceScreen widget (to the file selector) is clicked on: launch the file selector and wait for its result.
		psFiles.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			/* (non-Javadoc)
			 * @see android.preference.Preference.OnPreferenceClickListener#onPreferenceClick(android.preference.Preference)
			 */
			public boolean onPreferenceClick(Preference preference) {
				startActivityForResult(preference.getIntent(), PICK_FILES_REQUEST);
				return true;
			}
			
		});
	}

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();

        //Unregister the listener for whenever a preference changes.
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    /* (non-Javadoc)
     * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
     */
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //On a change in the preferences.

    	if(key.equals("ep_folder")){//The folder for cookie files changed.
			FlurryAgent.onEvent("folder_changed");
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "FolderChanged", null, 0);
    		//Sync the folder for the file selector with the entered folder. 
    		updateFolder();
    	}else if(key.equals("cp_equal")){//Equal chances en/dis-abled.
        	if(sharedPreferences.getBoolean(key, false)){
				FlurryAgent.onEvent("equal_chance_enabled");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "EqualChanceEnabled", null, 0);
        	}else{
				FlurryAgent.onEvent("equal_chance_disabled");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "EqualChanceDisabled", null, 0);
        	}
			FlurryAgent.onEvent("equal_property_changed");
        }else if(key.equals("lp_font")){//The font changed.
			FlurryAgent.onEvent("font_changed");
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "FontChanged", null, 0);
        }else if(key.equals("ep_fontsize")){//The font size changed.
			FlurryAgent.onEvent("font_size_changed");
			//Check if value is valid.
			String value=sharedPreferences.getString("ep_fontsize", "");
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "FontSizeChanged", value, 0);
			if(TextUtils.isEmpty(value) || 
					!TextUtils.isDigitsOnly(value)){
				Editor editor = settings.edit();
				editor.putString("ep_fontsize", DEFAULT_EP_FONTSIZE);
				editor.commit();
				//TODO: in Gingerbread: editor.apply()
				Toast.makeText(PreferencesActivity.this, getString(R.string.toast_value_invalid)+" "+DEFAULT_EP_FONTSIZE, Toast.LENGTH_SHORT).show();
			}
        }else if(key.equals("cp_wrap")){//Wrapping en/dis-abled.
        	if(sharedPreferences.getBoolean(key, false)){
				FlurryAgent.onEvent("wrap_enabled");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "WrapEnabled", null, 0);
        	}else{
				FlurryAgent.onEvent("wrap_disabled");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "WrapDisabled", null, 0);
        	}
			FlurryAgent.onEvent("wrap_property_changed");
        }else if(key.equals("lp_widget_font")){//The widget font changed.
			FlurryAgent.onEvent("widget_font_changed");
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "WidgetFontChanged", null, 0);
			//Update any widgets
			final AppWidgetManager awm = AppWidgetManager.getInstance(this);
			final int[] appWidgetIds=awm.getAppWidgetIds(new ComponentName(this, WidgetBroadcastReceiver.class));
	        final Intent intent = new Intent(this, WidgetBroadcastReceiver.class);
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			sendBroadcast(intent);
        }else if(key.equals("ep_widget_period")){//The widget update period changed.
			FlurryAgent.onEvent("widget_update_period_changed");
			//Check if value is valid.
			String value=sharedPreferences.getString("ep_widget_period", "");
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "WidgetUpdatePeriodChanged", value, 0);
			if(TextUtils.isEmpty(value) || 
					!TextUtils.isDigitsOnly(value)){
				Editor editor = settings.edit();
				editor.putString("ep_widget_period", DEFAULT_EP_WIDGET_PERIOD);
				editor.commit();
				//TODO: in Gingerbread: editor.apply()
				Toast.makeText(PreferencesActivity.this, getString(R.string.toast_value_invalid)+" "+DEFAULT_EP_WIDGET_PERIOD, Toast.LENGTH_SHORT).show();
			}else if(Long.parseLong(value)<60){
				Toast.makeText(PreferencesActivity.this, getString(R.string.toast_widget_period_short), Toast.LENGTH_LONG).show();
			}
        	//Reset widget update alarm so the new period is used immediately.
        	WidgetBroadcastReceiver.setWidgetUpdateAlarm(this);
    	}else if (key.equals("cp_notif_boot")) {//Notification at boot en/dis-abled.
        	if(sharedPreferences.getBoolean(key, false)){
				FlurryAgent.onEvent("boot_notifs_enabled");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "BootNotificationsEnabled", null, 0);
        	}else{
				FlurryAgent.onEvent("boot_notifs_disabled");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "BootNotificationsDisabled", null, 0);
        	}
    	}else if (key.equals("cp_notif_time")) {//Periodic notification en/dis-abled.
        	//Either start or stop notifying service if not already started/stopped.
        	if(sharedPreferences.getBoolean(key, false)){
				FlurryAgent.onEvent("per_notifs_enabled");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "PeriodicNotificationsEnabled", null, 0);
        		startService(new Intent(PreferencesActivity.this, 
                        NotifyingService.class));
        	}else{
				FlurryAgent.onEvent("per_notifs_disabled");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "PeriodicNotificationsDisabled", null, 0);
        		stopService(new Intent(PreferencesActivity.this, 
                        NotifyingService.class));
        	}
        }else if(key.equals("ep_time")){//The time period changed.
			FlurryAgent.onEvent("time_period_changed");
			//Check if value is valid.
			String value=sharedPreferences.getString("ep_time", "");
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Preferences", "NotificationsPeriodChanged", value, 0);
			if(TextUtils.isEmpty(value) || 
					!TextUtils.isDigitsOnly(value)){
				Editor editor = settings.edit();
				editor.putString("ep_time", DEFAULT_EP_TIME);
				editor.commit();
				//TODO: in Gingerbread: editor.apply()
				Toast.makeText(PreferencesActivity.this, getString(R.string.toast_value_invalid)+" "+DEFAULT_EP_TIME, Toast.LENGTH_SHORT).show();
			}
        	//Restart notifying service so the new period is used immediately, if it was running already.
        	if(sharedPreferences.getBoolean("cp_notif_time", false)){
        		stopService(new Intent(PreferencesActivity.this, 
                    NotifyingService.class));
        		startService(new Intent(PreferencesActivity.this, 
                    NotifyingService.class));
        	}
        }else if(key.equals(PrefSettings.KEY_ENABLED)||key.equals(PrefSettings.KEY_CHECK_URI)){
        	//Reschedule the checking in case the user has changed anything.
    		sendBroadcast(new Intent(Veecheck.getRescheduleAction(this)));
        }
    	
    	//Preferences changed so make a backup.
    	if (mBackupManagerAvailable) {
            WrapBackupManager.dataChanged(getPackageName());
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Backend", "BackupMade", null, 0);
        } else {
        	FlurryAgent.onEvent("PreferencesActivity:onSharedPreferenceChanged: BackupManager not available (older API).");
			Log.i(FortuneActivity.TAG, "BackupManager not available (older API).");
        }

    }
    
    //Reflection construction because backup storage is an API from version 8.

    static class WrapBackupManager {    	   
    	   /* class initialization fails when this throws an exception */
    	   static {
    	       try {
    	           Class.forName("android.app.backup.BackupManager");
    	       } catch (Exception ex) {
    	           throw new RuntimeException(ex);
    	       }
    	   }

    	   /* calling here forces class initialization */
    	   public static void checkAvailable() {}

    	   public static void dataChanged(String packageName) {
    	       BackupManager.dataChanged(packageName);
    	   }
    }

    private static boolean mBackupManagerAvailable;

    /* establish whether the "BackupManager" class is available to us */
    static {
        try {
            WrapBackupManager.checkAvailable();
            mBackupManagerAvailable = true;
        } catch (Throwable t) {
            mBackupManagerAvailable = false;
        }
    }

	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
		if(FortuneActivity.DEBUG) Log.i(FortuneActivity.TAG, "GOT RESULT reqc="+requestCode+" , resc="+resultCode);
        if (requestCode == PICK_FILES_REQUEST) {//Result came from "pick files" action.
			FlurryAgent.onEvent("files_picked");
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("FilePicker", "Done", null, 0);
        	//Check if the selection of files is complete (.num files included).
        	checkNumFiles();
        }
    }

	/**
	 * Check if the selection of files is complete (.num files included or not).
	 */
	private void checkNumFiles() {
		//Unshow the warning as it will be set back below if the problem still exists.
		unshowNotAllFixedWarning();
		
		//Check external storage
		try{
			FortuneActivity.check_external_storage_readable(this);
		}catch(IllegalArgumentException e){
			return;
		}
		
		//Get files list, which will be built into a list of files without a .num file associated to them.
		String str=settings.getString("str_files", "");
		ArrayList<File> numlessList=new ArrayList<File>();
		if(!(str.equals(""))){
			String[] strList=str.split(" ");
			for(int i=0; i<strList.length; i++){
				File file=new File(Uri.parse(strList[i]).getPath());
				File numFile=new File(Uri.parse(strList[i]).getPath()+".num");
				//If associated .num file does not exist, then add the file to the num-less list.
				if(!numFile.isFile()){
					if(FortuneActivity.DEBUG) Log.e(FortuneActivity.TAG, "NUMFILE "+numFile.getPath());
					numlessList.add(file);
				}
			}
			//Offer to generate .num files for cookie files (in a list) that don't have associated .num files.
			offerFixForNumlessCookieFiles(numlessList);
		}
	}

	/**
	 * Offer to generate .num files for cookie files (in a list) that don't have associated .num files.
	 * 
	 * @param numlessList List of cookie files without associated .num files.
	 */
	private void offerFixForNumlessCookieFiles(final ArrayList<File> numlessList) {
		//There should be at least one problem file.
		if(numlessList.size()>0){
			
			//Predefine two click listeners for a dialog.
			OnClickListener okButtonListener = new OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
					//Fix (or at least try) by generating the .num files for the cookie files without.
					fixNumlessCookieFiles(numlessList);
				}
			};
			
			OnClickListener cancelButtonListener = new OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
					psFiles = (PreferenceScreen) getPreferenceManager().findPreference("ps_files");//TODO BUG why doesn't psFiles change, except for in onCreate, even if this is included?
					//Refused so show a warning.
					showNotAllFixedWarning();
				}
			};
			
			//Build and show the dialog to ask to fix.
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle(R.string.dialog_title_num);
			builder.setMessage(R.string.dialog_message_num_warning);
			builder.setPositiveButton("Yes", okButtonListener);
			builder.setNegativeButton("No", cancelButtonListener);
			builder.setCancelable(true);
			builder.show();
		}
	}

	/**
	 * Fix (or at least try) by generating the .num files for the cookie files without, by myself.
	 * 
	 * @param numless List List of cookie files without associated .num files.
	 */
	private void fixNumlessCookieFiles(ArrayList<File> numlessList) {
		//Check external storage
		try{
			FortuneActivity.check_external_storage_writable(this);
		}catch(IllegalArgumentException e){
			return;
		}
		
		ArrayList<File> numlessListCopy=new ArrayList<File>(numlessList);
		//For each problem file:
		for(File numlessFile:numlessListCopy){
			//Initialize the generator (in the org.jstrfile backend).
			jstrfile generator = new jstrfile();
			try {
				//Generate the .num file (in the org.jstrfile backend).
				generator.makeIndex(numlessFile.toString());
				//Drop the problem file if successful.
				numlessList.remove(numlessFile);
			} catch (IOException e) {
				//Failed so show a warning.
				handleNotAllFixedFailure(numlessFile, e);
			} catch (IllegalArgumentException e){
				//Failed so show a warning.
				handleNotAllFixedFailure(numlessFile, e);
			}
		}
	}

	/**
	 * Failed so show a warning.
	 * 
	 * @param numlessFile List List of cookie files without associated .num files.
	 * @param e The "reason for failure" exception. 
	 */
	private void handleNotAllFixedFailure(File numlessFile, Exception e) {
		FlurryAgent.onError("PreferencesActivity:handleNotAllFixedFailure", getString(R.string.app_name_prefix)+" "+getString(R.string.toast_generation_num_failed)+numlessFile.toString()+" "+getString(Integer.parseInt(e.getMessage())), e.getMessage());
		//Show both a notif as a warning summary.
		Toast.makeText(PreferencesActivity.this, getString(R.string.app_name_prefix)+" "+getString(R.string.toast_generation_num_failed)+numlessFile.toString()+" "+getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_LONG).show();
		showNotAllFixedWarning();
	}

	/**
	 * Not all problems could be fixed (or were refused) so display a warning.
	 */
	private void showNotAllFixedWarning() {
		//Change the summary on the files widget.
		psFiles.setSummary(getString(R.string.summary_ps_files_warning));
		//Solved (circumvented) bug (http://code.google.com/p/android/issues/detail?id=931) by adding these two superfluous lines below.
		EditTextPreference et = (EditTextPreference) getPreferenceManager().findPreference("ep_folder");//TODO to be removed when bug gets fixed
		et.setSummary(et.getSummary()+" ");//TODO to be removed when bug gets fixed
	}

	/**
	 * Remove the warning.
	 */
	private void unshowNotAllFixedWarning() {
		//Change the summary on the files widget.
		psFiles.setSummary(getString(R.string.summary_ps_files));
		//Solved (circumvented) bug (http://code.google.com/p/android/issues/detail?id=931) by adding these two superfluous lines below.
		EditTextPreference et = (EditTextPreference) getPreferenceManager().findPreference("ep_folder");//TODO to be removed when bug gets fixed
		et.setSummary(et.getSummary()+" ");//TODO to be removed when bug gets fixed
	}

	/**
	 * Set default values for preferences not in the xml file (or the view). Currently these are only those related to version checking.
	 * 
	 * @param context Current context.
	 */
	public static void setDefaultValuesNotInView(Context context) {
		SharedPreferences settings2 =context.getSharedPreferences(PrefSettings.SHARED_PREFS_NAME, MODE_PRIVATE);
		//Assign some default settings if necessary
		if(settings2.getString(PrefSettings.KEY_CHECK_URI, null) == null){
			Editor editor = settings2.edit();
			editor.putBoolean(PrefSettings.KEY_ENABLED, true);
			editor.putLong(PrefSettings.KEY_PERIOD, DEFAULT_PERIOD);
			editor.putLong(PrefSettings.KEY_CHECK_INTERVAL, DEFAULT_CHECK_INTERVAL);
			editor.putString(PrefSettings.KEY_CHECK_URI, context.getString(R.string.version_file_url));
			editor.commit();
			//TODO: in Gingerbread: editor.apply()

			//Reschedule the version checks - we need to do this if the settings have changed (as above)
			//it may also necessary in the case where an application has been updated
			//here for simplicity, we do it every time the application is launched
			Intent intent = new Intent(Veecheck.getRescheduleAction(context));
			context.sendBroadcast(intent);
		}
	}
}
