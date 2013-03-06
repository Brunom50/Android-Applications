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
 * 	Copied from org.jstrfile, which had the following license:
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

package org.jstrfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.lp.androidsfortune.R;

/**
 * This class creates the .num files which are later read by FileRead to give a
 * random quote no.
 * 
 * @author Rishabh Manocha, pjv (small changes)
 * @version 0.1
 */
public class jstrfile {

	/*	*//**
	 *Main method. This takes in the list of files as arguments and calls
	 * makeIndex to make the .num files.
	 * 
	 * @param args
	 *            An array of strings representing the various files.
	 * @throws IOException
	 */
	/*
	 * public static void main(String[] args) throws IOException { try{ jstrfile
	 * strfile = new jstrfile(); for(int i = 0; i < args.length; i++) { File
	 * file = new File(args[i] + ".num"); if(!file.exists()) {
	 * if(strfile.makeIndex(args[i])) System.out.println(args[i] +
	 * ": This is the first time jfortune is being run on this file. A file named "
	 * + args[i] +
	 * ".num has been created in the source direcrory of the input file."+"\n"+
	 * "If you ever change the source file please do remove the .num file created and run jfortune on it again so that jfortune does not have a wrong belief to the number of quotes in the source file."
	 * ); } else throw new
	 * IllegalArgumentException(""+R.string.error_num_file_exists_a + args[i] +
	 * R.string.error_num_file_exists_b); } }catch (IOException e) { throw new
	 * IOException(""+R.string.error_no_write_perms); } }
	 */

	/**
	 * This method makes counts the no. of quotes in the fortune cookie file and
	 * creates a .num file with that no.
	 * 
	 * @param infile
	 *            path/name of the fortune cookie file.
	 * @throws IOException
	 *             for any I/O exceptions.
	 * @throws IllegalArgumentException
	 */
	public void makeIndex(String infile) throws IOException {
		FileReader myFileReader=null;
		BufferedReader myNumReader=null;
		FileWriter myFileOut=null;
		BufferedWriter myOut=null;
		try {
			myFileReader = new FileReader(infile);
			myNumReader = new BufferedReader(myFileReader);
			File file = new File(infile);
			File numFile = new File(infile+ ".num");
			if (!numFile.exists()) {
				String input = "";
				int index = 0;
				while ((input = myNumReader.readLine()) != null)
					if (input.equals("%"))
						index++;
				if (index == 0) {
					throw new IllegalArgumentException("" + R.string.error_no_valid_cookie_file);
				}
				myFileOut=new FileWriter(infile+ ".num");
				myOut = new BufferedWriter(myFileOut);
				myOut.write(Integer.toString(index) + "\n");
				myOut.write(Long.toString(file.length()));
			} else {
				throw new IllegalArgumentException("" + R.string.error_num_file_exists);
			}
		} catch (IOException e) {
			throw new IOException("" + R.string.error_no_write_perms);
		} finally {
			if(myNumReader!=null){
				myNumReader.close();
			}
			if(myFileReader!=null){
				myFileReader.close();
			}
			if(myOut!=null){
				myOut.close();
			}
			if(myFileOut!=null){
				myFileOut.close();
			}
		}
	}
}
