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

package net.lp.androidsfortune.versioncheck;

import net.lp.androidsfortune.R;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.text.method.SingleLineTransformationMethod;
import android.widget.EditText;

import com.tomgibara.android.veecheck.Veecheck;
import com.tomgibara.android.veecheck.util.PrefSettings;

public class VersioncheckPreferencesActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(PrefSettings.SHARED_PREFS_NAME);
		addPreferencesFromResource(R.xml.versioncheck_preferences);
		
		//Restrict preference dialog widgets.
		EditText etCheckUri = (EditText) ((EditTextPreference) findPreference("check_uri")).getEditText();
		etCheckUri.setTransformationMethod(SingleLineTransformationMethod.getInstance());
		/*etCheckUri.setAutoLinkMask(Linkify.WEB_URLS);*/
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	//reschedule the checking in case the user has changed anything
		sendBroadcast(new Intent(Veecheck.getRescheduleAction(this)));

    }
}