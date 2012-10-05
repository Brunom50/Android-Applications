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

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

/**
 * Receives broadcast intent (at system startup).
 * 
 * @author pjv
 * 
 *
 */
public class BroadcastReceiver extends android.content.BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		 	if(intent.getAction()!=null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){//At system startup an intent is received.
		 		//Load the default preferences values. Should be done first at every entry into the app.
		        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
		        PreferencesActivity.setDynamicDefaultValues(context);
		        PreferencesActivity.setDefaultValuesNotInView(context);
		 		
		        //Start the notifications service and let it know a notification should be shown at once.
		 		context.startService(new Intent(context, 
	                    NotifyingService.class).putExtra("onStartup", true));
		        
		        //Consider doing a version check.
				context.sendBroadcast(new Intent("net.lp.androidsfortune.VEECHECK_CONSIDER_CHECK"));
				context.sendBroadcast(new Intent("net.lp.androidsfortune.VEECHECK_RESCHEDULE_CHECKS"));
		 	}
	}

}
