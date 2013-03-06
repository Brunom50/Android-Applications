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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.flurry.android.FlurryAgent;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

/**
 * Home screen widget provider
 * 
 * @author pjv
 *
 */
public class WidgetBroadcastReceiver extends AppWidgetProvider{
	
	private static PendingIntent alarmPendingIntent;
	
	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
        int[] appWidgetIds) {
        // To prevent any ANR timeouts, we perform the update in a service
    	if (FortuneActivity.DEBUG) Log.d(FortuneActivity.TAG, "widget onUpdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.startService(serviceIntent);
        
        //Check if all the widgets are included in the alarm (one might have been added)
        AppWidgetManager awm = AppWidgetManager.getInstance(context);
		final int[] allAppWidgetIds=awm.getAppWidgetIds(new ComponentName(context, WidgetBroadcastReceiver.class));
    	for (int i=0; i<allAppWidgetIds.length; i++) {
    		if(appWidgetIds.length!=allAppWidgetIds.length || appWidgetIds[i]!=allAppWidgetIds[i]){
    			setWidgetUpdateAlarm(context);
    			break;
    		}
    	}
    }
    
    /* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onDisabled(android.content.Context)
	 */
	@Override
	public void onDisabled(Context context) {
    	if (FortuneActivity.DEBUG) Log.d(FortuneActivity.TAG, "widget onDisabled");
		super.onDisabled(context);
		cancelWidgetUpdateAlarm(context);
	}

	public static void cancelWidgetUpdateAlarm(Context context) {
		//Cancel widget update alarm
		AlarmManager mAM = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		mAM.cancel(alarmPendingIntent);
		if (FortuneActivity.DEBUG) Log.d(FortuneActivity.TAG, "alarm cancelled for widget update");
	}

	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onEnabled(android.content.Context)
	 */
	@Override
	public void onEnabled(Context context) {
    	if (FortuneActivity.DEBUG) Log.d(FortuneActivity.TAG, "widget onEnabled");
		super.onEnabled(context);
		setWidgetUpdateAlarm(context);
	}

	public static void setWidgetUpdateAlarm(Context context) {
		//Obtain a connection to the preferences.
		SharedPreferences settings = context.getSharedPreferences(PreferencesActivity.PREFS, Context.MODE_PRIVATE);
		
		//Get the period preference
		long period = 1000 * 60 * Long.parseLong(settings.getString("ep_widget_period", PreferencesActivity.DEFAULT_EP_WIDGET_PERIOD));
		
		//Set widget update alarm.
		AlarmManager mAM = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		try{
			alarmPendingIntent = getWidgetUpdateAlarmPendingIntent(context);
			mAM.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis()+period, period, alarmPendingIntent);
		} catch (IndexOutOfBoundsException e){
			//There were no app widgets to update. Don't really set an alarm.
		}
		if (FortuneActivity.DEBUG) Log.d(FortuneActivity.TAG, "alarm set for widget update every "+period+" ms, alarmPendingIntent="+alarmPendingIntent);
	}
	
	public static PendingIntent getWidgetUpdateAlarmPendingIntent(Context context) throws IndexOutOfBoundsException{
		final AppWidgetManager awm = AppWidgetManager.getInstance(context);
		final int[] appWidgetIds=awm.getAppWidgetIds(new ComponentName(context, WidgetBroadcastReceiver.class));
        
		if(appWidgetIds.length==0){
			throw new IndexOutOfBoundsException("No app widgets to update");
		}
		
		Intent alarmIntent = new Intent(context, WidgetBroadcastReceiver.class);
		alarmIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		alarmIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

		if (FortuneActivity.DEBUG) Log.d(FortuneActivity.TAG, "alarmIntent="+alarmIntent);
		for (int i=0; i<appWidgetIds.length; i++) {
			if (FortuneActivity.DEBUG) Log.d(FortuneActivity.TAG, "alarmIntent appWidgetIds="+appWidgetIds[i]);
    	}
		
		PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 
				0, alarmIntent, 
				PendingIntent.FLAG_UPDATE_CURRENT);

		return alarmPendingIntent;
	}

	/**
     * A service that will show a fortune cookie home screen widget.
     *
     */
    public static class WidgetService extends Service {

		private static final String MONOSPACE = "monospace";

		private static final String SANS = "sans";

		/**
         * The text of the notification.
         */
        private static String text;
    	
    	/**
         * Preferences source.
         */
        private static SharedPreferences settings;
        
        /* (non-Javadoc)
         * @see android.app.Service#onCreate()
         */
        @Override
        public void onCreate() {
        	//if (FortuneActivity.DEBUG) Log.d(FortuneActivity.TAG, "widget onCreate update service");
            //Obtain a connection to the preferences.
            settings = getSharedPreferences(PreferencesActivity.PREFS, MODE_PRIVATE);
            
            //More in onStart().
        }
        
        @Override
        public void onStart(Intent intent, int startId) {
    		
            // Build the widget update
            updateWidget(this, intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS));
            
            stopSelf();
        }

        /**
         * Build a widget update to show a cookie. Will block until the cookie text returns.
         */
        public void updateWidget(Context context, int[] appWidgetIds) {
        	//Enable analytics session. Should be before any Flurry calls.
        	FlurryAgent.onStartSession(context, FortuneActivity.FLURRY_API_KEY);

    		for (int i=0; i<appWidgetIds.length; i++) {

    			try{
    				//Retrieve the cookie text from the same source as the main screen.
    				text = FortuneActivity.getFortune(this, settings);
    			}catch(IllegalArgumentException e){
    				//Do nothing, this is just a pass through so finally (elsewhere only) can be used.
    				text = null;
    			}

    			RemoteViews updateViews = null;

    			if (text==null || text.equals(getString(R.string.no_cookie_files))) {
    				// Didn't succeed in getting a cookie, so show error message
    				updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_message);
    				CharSequence errorMessage = context.getText(R.string.widget_error);
    				updateViews.setTextViewText(R.id.message, errorMessage);

    				// When user clicks on widget, launch the main app, forcefully
    				Intent intent=new Intent().setClass(this, FortuneActivity.class);
    				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK+Intent.FLAG_ACTIVITY_CLEAR_TOP);
    				PendingIntent contentIntent = PendingIntent.getActivity(WidgetService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

    				updateViews.setOnClickPendingIntent(R.id.widget, contentIntent);
    			} else {
    				// Modify according to settings
    				final int layout;
    		        final String font = settings.getString("lp_widget_font", PreferencesActivity.DEFAULT_LP_WIDGET_FONT);
    		        if(SANS.equals(font)){
    		        	layout = R.layout.widget_cookie_sans;
    		        }else if(MONOSPACE.equals(font)){
    		        	layout = R.layout.widget_cookie_monospace;
    		        }else{
    		        	layout = R.layout.widget_cookie;
    		        }
    		        
    				// Build an update that holds the updated widget contents
    				updateViews = new RemoteViews(context.getPackageName(), layout);
    				
    				// Set text
    				updateViews.setTextViewText(R.id.cookie, text);

    				// When user clicks on widget, launch a dialog
    				Intent intent=new Intent("net.lp.androidsfortune.SHOW_WIDGET_DIALOG");//meant for WidgetDialogActivity.class
    				intent.addCategory(Intent.CATEGORY_DEFAULT);
    				intent.putExtra("text", text);
    				intent.setType("text/plain");
    				PendingIntent contentIntent = PendingIntent.getActivity(WidgetService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    				
    				updateViews.setOnClickPendingIntent(R.id.widget, contentIntent);

        			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Backend", "WidgetUpdated", text, 0);
    			}

    			// Push update for this widget to the home screen
    			AppWidgetManager manager = AppWidgetManager.getInstance(this);
    			manager.updateAppWidget(appWidgetIds[i], updateViews);
            	
        		FlurryAgent.onEvent("update_widget");
        		FlurryAgent.onPageView();
        		FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackPageView("/CookieOnWidgetUpdate");
    			
    			if (FortuneActivity.DEBUG) Log.d(FortuneActivity.TAG, "widget service updateWidget appWidgetId="+appWidgetIds[i]);
            }

            //End analytics session.
    		FlurryAgent.onEndSession(context);
        }

        @Override
        public IBinder onBind(Intent intent) {
            // We don't need to bind to this service
            return null;
        }
    }

}
