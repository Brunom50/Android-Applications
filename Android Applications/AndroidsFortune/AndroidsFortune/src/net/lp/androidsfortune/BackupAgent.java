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
 */

package net.lp.androidsfortune;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

import com.tomgibara.android.veecheck.util.PrefSettings;

/**
 * A Backup Agent (as to the API 8)
 * 
 * Data backup is working and can be shown on an emulator-2.2 using these steps: http://developer.android.com/guide/topics/data/backup.html#Testing
 * 
 * @author pjv
 *
 */
public class BackupAgent extends BackupAgentHelper {

    // A key to uniquely identify the set of backup data
    static final String PREFS_BACKUP_KEY = "prefs";

	/* (non-Javadoc)
	 * @see android.app.backup.BackupAgent#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();

	    // Allocate a backup agent helper for shared preferences and add it to the backup agent
		SharedPreferencesBackupHelper sharedPreferencesBackupHelper = 
			new SharedPreferencesBackupHelper(this, new String[]{PreferencesActivity.PREFS, PrefSettings.SHARED_PREFS_NAME});
        addHelper(PREFS_BACKUP_KEY, sharedPreferencesBackupHelper);
        
        //Backup storage for external storage files is not possible yet.
	}

}
