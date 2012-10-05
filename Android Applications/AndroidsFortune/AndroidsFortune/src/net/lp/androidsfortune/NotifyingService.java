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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.flurry.android.FlurryAgent;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

/**
 * A service that will periodically show a notification.
 * 
 * @author pjv
 * 
 *
 */
public class NotifyingService extends Service {
    
    /**
     * Use a layout id for a unique identifier (dirty hack by android team if you ask me, pjv)
     */
    public static int UNIQUE_NOTIFS_ID = R.layout.main;
    
    /**
     * The text of the notification.
     */
    private String text;

    /**
     * Variable which controls the notification thread.
     */
    private ConditionVariable mCondition;

    /**
     * Set the semaphore to open.
	 */
	public void setMConditionOpen() {
		mCondition.open();
	}

	/**
	 * The notification manager.
	 */
	private NotificationManager mNM;
	
	/**
     * Preferences source.
     */
    private static SharedPreferences settings;
    
    /**
     * The thread to run this service in.
     */
    private Thread notifyingThread=null;
    
    /* (non-Javadoc)
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {
    	//Get the notification manager.
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        //Obtain a connection to the preferences.
        settings = getSharedPreferences(PreferencesActivity.PREFS, MODE_PRIVATE);
        
        //Obtain a semaphore to the notifications. 
        mCondition = new ConditionVariable(false);
        
        //More in onStart().
    }

	public void handleCommand(Intent intent) {
		//What was the reason for this (re)start of the service?
        if(intent!=null && intent.getBooleanExtra("onStartup", false)==true){//At system startup.
        	if(settings.getBoolean("cp_notif_boot", true)){
        		//Display a new fortune notif (at startup) if enabled.
                showNotification();
            }else{
            	//If disabled then just do nothing else.
            	return;
            }
        }
        
        //If periodic notifications are enabled then spawn a thread that will keep running. Also if startup notification is enabled this thread will start running from boot.
        if(settings.getBoolean("cp_notif_time", false)){
        	if(notifyingThread==null){//If not already running.
            	// Start up the thread running the service.  Note that we create a
            	// separate thread because the service normally runs in the process's
            	// main thread, which we don't want to block.
            	notifyingThread = new Thread(null, mTask, "NotifyingService");
            	notifyingThread.start();
            }
    	}
        
	}
	
	// This is the old onStart method that will be called on the pre-2.0
	// platform.  On 2.0 or later we override onStartCommand() so this
	// method will not be called.
    /* (non-Javadoc)
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	    handleCommand(intent);
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    handleCommand(intent);
	    return START_NOT_STICKY;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
    public void onDestroy() {
		if(notifyingThread!=null){//Dont't remove the notification if it was put there at system startup.
			//Cancel the persistent notification.
			mNM.cancel(UNIQUE_NOTIFS_ID);
		}
		
		//Remove the memory of the (soon to be stopped) thread.
        notifyingThread=null;
        // Stop the thread from generating further notifications (will stop the thread).
        setMConditionOpen();
    }

    /**
     * Runnable task.
     */
    private Runnable mTask = new Runnable() {
        public void run() {
        	//Loop forever.
            while (true) {
            	//Retrieve period each time from preferences.
                final int period = Integer.parseInt(settings.getString("ep_time", PreferencesActivity.DEFAULT_EP_TIME));
                //Block for the time period (or break stopping thread if semaphore opened).
                if (mCondition.block(period * 60 * 1000)) 
                    break;
                //Display a new fortune notif.
                showNotification();
				
            }
            // Done with our work...  stop the service!
            NotifyingService.this.stopSelf();
        }
    };

    /* (non-Javadoc)
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    /**
     * Show a new fortune cookie notification.
     */
    private void showNotification() {
    	//Enable analytics session. Should be before any Flurry calls.
    	FlurryAgent.onStartSession(this, FortuneActivity.FLURRY_API_KEY);
		FlurryAgent.onPageView();
		FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackPageView("/Notification");
		
        try{
            //Retrieve the cookie text from the same source as the main screen.
            text = FortuneActivity.getFortune(this, settings);
		}catch(IllegalArgumentException e){
			//Do nothing, this is just a pass through so finally (elsewhere only) can be used.
		}
    	
		FlurryAgent.onEvent("show_notif");
		FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Notifications", "ShowNew", text, 0);
		
        //Set the icon, scrolling text and timestamp. (Maybe a good idea to set text to null to stop showing a text preview.
        Notification notification = new Notification(R.drawable.cookie, text, System.currentTimeMillis());

        //The PendingIntent to launch our main activity if the user selects this notification
        Intent intent=new Intent(Intent.ACTION_VIEW);//meant for FortuneActivity.class
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("text", text);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK+Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(NotifyingService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT+PendingIntent.FLAG_ONE_SHOT);
        
        //Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.status_bar_notification_title),
                       text, contentIntent);

        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNM.notify(UNIQUE_NOTIFS_ID, notification);
        
        //End analytics session.
		FlurryAgent.onEndSession(this);
    }

    /**
     * Object that receives interactions from clients.
     */
    private final IBinder mBinder = new Binder() {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply,
                int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };
}
