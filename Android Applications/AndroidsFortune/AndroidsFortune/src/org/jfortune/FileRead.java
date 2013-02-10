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
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * This class is used to read information from the .num files.
 * @author Rishabh Manocha, pjv (small changes)
 * @version 0.6
 */
public class FileRead
{
	private FileReader myFileReader;
	private BufferedReader myNumReader;
	
	/**
	 * Constructor for FileRead.Here i check to see if a a .num file of the parsed file exists or not.If not...i then create it...print out
	 * that this is the first time the program was run on this file and exit.A cookie is not actually printed out the first time jfortune will be
	 * run on a given file
	 * @param input A String representing the filename whose .num file needs to be read.
	 */
	public FileRead(String input) throws IOException{
		try {
			this.myFileReader=new FileReader(input + ".num");
			this.myNumReader = new BufferedReader(this.myFileReader, 15);
		}
		catch (IOException e) {
			throw new IOException("No .num file.");
		}
	}

	
	/**
	 * This is the method which reads in the no. in the .num of the input file and returns a random integer.The random int is chosen from between
	 * 0 and the number of cookies in the file.If the file contains null...i then scold the user saying that he/she was told that the file he/she
	 * run jfortune on was not a fortune cookie file and that they should learn to interpret error messages and use google.
	 * @return int An integer containing the no. of the quote we need to display from this file.
	 * @throws IOException 
	 */
	public int getNum() throws IOException {
		Random temp = new Random();
		try{
			int nr=temp.nextInt(Integer.parseInt(this.myNumReader.readLine()));
			return nr;
		}catch (IOException e) {
			throw new IOException("No .num file");
		}finally{
			if(this.myNumReader!=null){
				this.myNumReader.close();
			}
			if(this.myFileReader!=null){
				this.myFileReader.close();
			}
		}
	}
}
