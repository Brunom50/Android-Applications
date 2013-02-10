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
import java.util.ArrayList;

import net.lp.androidsfortune.R;

/**
 * This is the main class which calls the other objects/classes, analyses input and then prints the quote or other information as told.
 * @version 0.6
 * @author Rishabh Manocha, pjv (small changes)
 */
public class jfortuneclass
{
	private FileRead myNum;
	private BufferedReader myFile;
	private getOpts myOpts;
	private ArrayList<String> myArgs;
	private FilePref myPrefs;
	private String myOutput;
	
	/**
	 * Constructor for a jfortuneclass object.Throws a IOException when the file parsed cannot be found..:)...silly you.
	 * @param args the command line options parsed.
	 * @throws IOException 
	 */
	public jfortuneclass(String[] args) throws IOException {
		this.myOpts = new getOpts(args);
		this.myArgs = this.myOpts.getRealArgs();
		if(!this.myOpts.getAllEqual())
			this.myPrefs = new FilePref(this.myArgs);
		else
			this.myPrefs = new FilePref(this.myArgs,true);
	}

	/**
	 * This does the main work of calling the various other methods and displaying the quote.
	 * @throws IOException 
	 */
	public void run() throws IOException {
		this.myOutput = "";
		int rand = 0;
		String quote = "";
		if(this.myOpts.getPrintSyntax()) {
			//this.setOutput(jfortuneclass.syntax());
		}
		if(this.myOpts.getPrintVersion()) {
			this.setOutput(jfortuneclass.version());
		}
		FileReader myFileReader=null;
		try {
			if(this.myOpts.getPrintPrefs()) {
				ArrayList<String> prefs = this.myPrefs.getPrefs();
				if(prefs.size() == 0)
					throw new IllegalArgumentException(""+R.string.error_no_file_name);
				for(int i = 0;i < prefs.size(); i++)
					this.setOutput(Float.parseFloat((String)prefs.get(i)) * 100 + "% : " + this.myArgs.get(i));
			}
			if(this.myArgs.size() == 0)
					throw new IllegalArgumentException(""+R.string.error_no_file_name);
			rand = this.getNumFile();
			myFileReader = new FileReader((String)this.myArgs.get(rand));
			this.myFile = new BufferedReader(myFileReader, 250000);
			this.myNum = new FileRead((String)this.myArgs.get(rand));
			this.setOutput(quote = this.getQuote());
		}catch (IOException e) {
				throw new IOException((String)this.myArgs.get(rand));
		}finally{
			if(this.myFile!=null){
				this.myFile.close();
			}
			if(myFileReader!=null){
				myFileReader.close();
			}
		}
		if(this.myOpts.getPrintFile())
			this.setOutput("\nThe fortune cookie file used was: " + this.myArgs.get(rand));
		if(this.myOpts.getWait()) {
			try {
				Thread.sleep(quote.length() * 42);
			}catch(InterruptedException e) { }
		}
	}
	
	/**
	 * This method returns which file I should look for the quote in. I use this method so that I can take care of the probabilities for each file parsed.
	 * @return int An integer which is the index no. for the fortune file to be read.
	 */
	private int getNumFile() {
		ArrayList<String> prefs = this.myPrefs.getPrefs();
		float rand = (float)Math.random();
		float bottom = 0, top = 0;
		for(int i = 0;i < prefs.size();i++) {
			top += Float.parseFloat((String)prefs.get(i));
			if((rand >= bottom) && (rand < top))
				return i;
			else
				bottom = top;
		}
		return prefs.size() - 1;
	}
				
	/**	
	 * The method does all the work of getting a quote from the file and sending it back to the caller function in a nice sorted way.
	 * @return String A String which contains the quote to be displayed.
	 * @throws IOException If we recieve an IOException from the given file. This shouldn't ever happen since there are enough checks before reaching this step which acertain that there
	 * are no such loose cannons.
	 */
	public String getQuote() throws IOException {
		int index = this.myNum.getNum();
/*		if(index == -1)
			return "";*/
		String temp = "";
		for(int i = 0;i < index;)
		{
				temp = this.myFile.readLine();
				if(temp.equals("%"))
					i++;
		}
		String quote = "";
		do
		{
			if(!(temp.equals("%")))
					quote = quote + temp + "\n";
			temp = this.myFile.readLine();
		}while(!(temp.equals("%")));
		return quote.substring(0,quote.length() - 1);		//To remove the newline character at the end of the quote so that we dont have a new line after the quote ends.
	}
	
	private void setOutput(String text) {
		if(!this.myOpts.getNoPrint())
			System.out.println(text);
		else
			this.myOutput += text;
	}
	
	/**
	 * This returns the output(if we are passed the -r option, then this method should be called to get back all the output from the program).
	 * @return String A String containing all the output that needs to be displayed.
	 */
	public String getOutput() {	return this.myOutput;	}

	/**
	 * This displays version information for jfortune.
	 * @return String A String containing the required information.
	 */
	public static String version() {
		return "JFortune Version 0.5.\nThis Program is released under the GPL License.\n(c) Rishabh Manocha 2004";
	}
	
/*	*//**
	 * This displays jfortune Syntax information.
	 * @return String A String containing the required iinformation.
	 *//*
	public static String syntax() {
		String syntax = "JFortune Usage: [options] file(s)\n Options:";
		syntax += "\n -c\t\tPrints out which file the fortune cookie was taken from.";
		syntax += "\n -f\t\tPrints out the percentage preference given to each file parsed but prints no fortune cookie.";
		syntax += "\n -e\t\tGive all files equal preference.";
		syntax += "\n -r\t\tReturn all output as a String. This is useful for applications using jfortune (like jfortuneui).";
		syntax += "\n -w\t\tWaits for an amount of time determined by the length of the fortune cookie displayed.";
		syntax += "\n -v\t\tPrints out the version of jfortune you are running.";
		syntax += "\n -h\t\tPrints out this help message";
		syntax += "\n" + jfortuneclass.version();
		return syntax;
	}*/
}