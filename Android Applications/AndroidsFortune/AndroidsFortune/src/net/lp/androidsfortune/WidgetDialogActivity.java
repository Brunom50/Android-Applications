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
 *   Idea based on well-known fortune program.
 */

package net.lp.androidsfortune;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.flurry.android.FlurryAgent;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class WidgetDialogActivity extends Activity implements OnClickListener {

    private Button refreshButton;
	private Button showCookieButton;
	private Button cancelButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Request dialog icon
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        
        //Enable analytics session. Should be first (at least before any Flurry calls).
    	FlurryAgent.onStartSession(this, FortuneActivity.FLURRY_API_KEY);
        
        //Load the default preferences values. Should be done first at every entry into the app.
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PreferencesActivity.setDynamicDefaultValues(this);
        PreferencesActivity.setDefaultValuesNotInView(this);

        setContentView(R.layout.widget_dialog_activity);

        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, 
                R.drawable.ic_dialog_menu_generic);
        
        refreshButton = (Button) findViewById(R.id.b_refresh);
        showCookieButton = (Button) findViewById(R.id.b_show_cookie);
        cancelButton = (Button) findViewById(R.id.b_cancel);
        
        refreshButton.setOnClickListener(this);
        showCookieButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        
        setResult(RESULT_CANCELED);
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//End analytics session.
		FlurryAgent.onEndSession(this);
	}

	public void onClick(View v) {
		if(refreshButton.equals(v)){
			FlurryAgent.onEvent("widget_refresh");
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("WidgetInteraction", "WidgetRefresh", null, 0);
			//Update any widgets
			final AppWidgetManager awm = AppWidgetManager.getInstance(this);
			final int[] appWidgetIds=awm.getAppWidgetIds(new ComponentName(this, WidgetBroadcastReceiver.class));
	        Intent intent = new Intent(this, WidgetBroadcastReceiver.class);
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			sendBroadcast(intent);
			
			//Stop
	        setResult(RESULT_OK);
	        finish();
		}else if(showCookieButton.equals(v)){
			FlurryAgent.onEvent("widget_show_cookie");
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("WidgetInteraction", "ShowCookieInApp", null, 0);
			// Launch the main app
			Intent intent=new Intent(Intent.ACTION_VIEW);//meant for FortuneActivity.class
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.putExtra("text", getIntent().getStringExtra("text"));
			intent.setType("text/plain");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK+Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
			//Stop
	        setResult(RESULT_OK);
	        finish();
		}else if(cancelButton.equals(v)){
			FlurryAgent.onEvent("widget_cancel");
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("WidgetInteraction", "Cancel", null, 0);
			//Stop with cancel result
	        finish();
		}else{
			throw new IllegalArgumentException("What was clicked?");
		}
	}
}