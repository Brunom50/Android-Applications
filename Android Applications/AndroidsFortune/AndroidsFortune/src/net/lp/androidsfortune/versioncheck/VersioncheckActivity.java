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
 *   Based on veecheck-sample.
 */

package net.lp.androidsfortune.versioncheck;

import net.lp.androidsfortune.FortuneActivity;
import net.lp.androidsfortune.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;

import com.flurry.android.FlurryAgent;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.tomgibara.android.veecheck.VeecheckActivity;
import com.tomgibara.android.veecheck.VeecheckState;
import com.tomgibara.android.veecheck.util.PrefState;

public class VersioncheckActivity extends VeecheckActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.versioncheck);

    	//Enable analytics session. Should be before any Flurry calls.
    	FlurryAgent.onStartSession(this, FortuneActivity.FLURRY_API_KEY);
    	
		FlurryAgent.onEvent("versioncheck_activity_displayed");
		FlurryAgent.onPageView();
		FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackPageView("/UpdateAvailable");

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//End analytics session.
		FlurryAgent.onEndSession(this);

		// Stop the TRACKER when it is no longer needed.
		if (!FortuneActivity.DEBUG) FortuneActivity.getGoogleAnalyticsTrackerInstance(this).stop();
	}
	
	@Override
	protected VeecheckState createState() {
		return new PrefState(this);
	}
	
	@Override
	protected View getNoButton() {
		return findViewById(R.id.no);
	}
	
	@Override
	protected View getYesButton() {
		return findViewById(R.id.yes);
	}

	@Override
	protected Checkable getStopCheckBox() {
		return (Checkable) findViewById(R.id.stop);
	}

	/* (non-Javadoc)
	 * @see com.tomgibara.android.veecheck.VeecheckActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		if(view!=null && view instanceof Button){
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Versionchecking", "Updates_"+((Button)view).getText(), null, 0);
		} else {
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Versionchecking", "Updates_miscellaneous", null, 0);
		}
		
		super.onClick(view);
	}
}
