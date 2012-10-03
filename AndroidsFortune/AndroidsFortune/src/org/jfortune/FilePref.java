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

/*	
 * 	Copied from org.jfortune, which had the following license:
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
 
package org.jfortune;
 
 import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
 
 /**
  * This is used to get percentages of each file. Something like fortune -f in the original fortune program
  * @author Rishabh Manocha, pjv (small changes)
  * @version 0.1
  */
public class FilePref {

	private ArrayList<String> myPrefs;
	
	/**
	 * Constructor for FilePref. Use this one when you do not want all the files to have the same probabilities.
	 * @param files An arraylist of file names to be searched.
	 * @throws IOException 
	 */
	public FilePref(ArrayList<String> files) throws IOException {
		this.myPrefs = this.calcPref(files);
	}
	
	/**
	 * Use this constructor when you want all files to have the same probability.
	 * @param files An arraylist of file names to be searched.
	 * @param flag This is not used. This is present to distinguish between the two constructors.
	 * @throws IOException 
	 */
	public FilePref(ArrayList<String> files, boolean flag) throws IOException {
		this.myPrefs = this.makeEqual(files);
	}
	
	private ArrayList<String> calcPref(ArrayList<String> files) throws IOException{
		float[] nums = new float[files.size()];
		int total = 0;
		for(int i = 0; i < files.size(); i++) {
			File tmp = new File(files.get(i) + ".num");
			if(!tmp.isFile())
				throw new IOException(files.get(i) + ".num");
			FileReader myFileReader=new FileReader(files.get(i) + ".num");
			BufferedReader myNumReader = new BufferedReader(myFileReader, 15);
			myNumReader.readLine();
			nums[i] = Integer.parseInt(myNumReader.readLine());
			total += nums[i];
			myNumReader.close();
			myFileReader.close();
		}
		ArrayList<String> prefs = new ArrayList<String>(nums.length);
		for(int i = 0; i < nums.length; i++) {
			prefs.add(Float.toString(nums[i] / total));
		}
		return prefs;
	}
	
	private ArrayList<String> makeEqual(ArrayList<String> files) throws IOException {
		ArrayList<String> prefs = new ArrayList<String>(files.size());
		for(int i = 0; i < files.size();i++) {
			File tmp = new File((String)files.get(i));
			if(!tmp.isFile())
				throw new IOException((String)files.get(i));
			prefs.add(Float.toString((float)1 / (float)files.size()));
		}
		return prefs;
	}
	
	/**
	 * Returns an arraylist represeting the probability for each file.
	 * @return ArrayList An arraylist represeting the probability for each file.
	 */
	public ArrayList<String> getPrefs() {	return this.myPrefs;	}
}
