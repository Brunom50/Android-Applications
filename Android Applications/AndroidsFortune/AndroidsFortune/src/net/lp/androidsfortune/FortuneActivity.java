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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;

import net.lp.androidsfortune.utils.FullScrollView;

import org.jfortune.jfortuneclass;
import org.jstrfile.jstrfile;
import org.openintents.intents.AboutIntents;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.View.OnCreateContextMenuListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

/**
 * Main activity in which fortune cookies are shown.
 * 
 * @author pjv
 *
 */
public class FortuneActivity extends Activity implements OnCreateContextMenuListener {

    public static final String FLURRY_API_KEY = "13M4EX92CTBYEXMM5A4Q";
    public static final String GOOGLE_ANALYTICS_WEB_PROPERTY_ID = "UA-5843799-4";
	public static final boolean DEBUG = false;

	public static GoogleAnalyticsTracker TRACKER;
	
	/**
     * Request code for subactivities
     */
    private static final int REQUEST_CODE_PREFERENCES = 1;
    
    /**
     * Preferences source.
     */
    private SharedPreferences settings;
        
    /**
     * Scroll widget.
     */
    private FullScrollView svFullScroll;
    private ScrollView svScroll;
        
    /**
     * Text widget.
     */
    private TextSwitcher tvText;
    
    /**
     * Text.
     */
    private String text="";

    /**
     * Progress dialog.
     */
    private ProgressDialog mProgressDialog;

	/**
	 * Handler.
	 */
	private Handler mHandler;
	
	/**
	 * Gesture detection
	 */
	private GestureDetector gestureDetector; 
	
	/**
	 * If the view is wrapped (e.g. non-scrollable horizontally)
	 */
	private boolean wrap;
	
	/**
	 * Menu item id's.
	 */
	public static final int MENU_ITEM_PREFS = Menu.FIRST;
	public static final int MENU_ITEM_ABOUT = Menu.FIRST+1;
	public static final int MENU_ITEM_REFRESH = Menu.FIRST+2;
	public static final int MENU_ITEM_GET_MORE_COOKIES = Menu.FIRST+3;
	public static final int MENU_ITEM_SEND = Menu.FIRST+4;
	public static final int MENU_ITEM_FEEDBACK = Menu.FIRST+5;
	public static final int MENU_ITEM_WEB_SEARCH = Menu.FIRST+6;
	public static final int MENU_ITEM_COPY = Menu.FIRST+7;
	public static final int MENU_ITEM_SHARE_APP = Menu.FIRST+8;

	public static final String TAG = "Android's Fortune";

	private static final String KEY_TEXT = "key_text";

	/**
	 * Dialog keys.
	 */
	private static final int DIALOG_KEY_INSTALL_SAMPLE_COOKIE_FILE = 1;

	/**
	 * Handler message id.
	 */
	protected static final int MSG_INSTALL_SAMPLE_COOKIE_FILE_DONE = 1;
	
	public static GoogleAnalyticsTracker getGoogleAnalyticsTrackerInstance(Context context){
		if(TRACKER == null){
			TRACKER = GoogleAnalyticsTracker.getInstance();
	        
	        // Start the TRACKER with a dispatch interval (in seconds) (for batch network bursts).
	        TRACKER.start(GOOGLE_ANALYTICS_WEB_PROPERTY_ID, 600, context);
		}
		return TRACKER;
	}
	
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request progress bar
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        
        //Enable analytics session. Should be first (at least before any Flurry calls).
    	FlurryAgent.onStartSession(this, FLURRY_API_KEY);

        //Load the default preferences values. Should be done first at every entry into the app.
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PreferencesActivity.setDynamicDefaultValues(this);
        PreferencesActivity.setDefaultValuesNotInView(this);
        
        //Obtain a connection to the preferences.
        settings=getSharedPreferences(PreferencesActivity.PREFS, MODE_PRIVATE);
        
        //Set handler.
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

				if(FortuneActivity.DEBUG) Log.d(TAG, "Handler called.");
                switch (msg.what) {
                    case MSG_INSTALL_SAMPLE_COOKIE_FILE_DONE: {
            			if(FortuneActivity.DEBUG) Log.v(TAG, "REFRESH");
                        refreshFortune();
                        
                        //Update any widgets
            			final AppWidgetManager awm = AppWidgetManager.getInstance(FortuneActivity.this);
            			final int[] appWidgetIds=awm.getAppWidgetIds(new ComponentName(FortuneActivity.this, WidgetBroadcastReceiver.class));
            	        final Intent intent = new Intent(FortuneActivity.this, WidgetBroadcastReceiver.class);
            			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            			sendBroadcast(intent);

        				// Hide progress dialog. Should also be outside of thread otherwise dialog might not have been shown yet before it is dismissed. Possible cause of BUG #392882 (and bug where the dialog keeps open).
        				try{
        					dismissDialog(DIALOG_KEY_INSTALL_SAMPLE_COOKIE_FILE);
        					if(FortuneActivity.DEBUG) Log.v(TAG, "DISMISS1");
        				} catch (IllegalArgumentException iae) {
        					// Fail silently because likely it never was shown or whatever but it doesn't matter.
        					if(FortuneActivity.DEBUG) Log.v(TAG, "NO_DISMISS");
        				}
                    }
                }
            }
        };
			
        //Set the window.
        setContentView(R.layout.main);
        
        findAndSetViews();
        
        //Start the periodic notifications service if enabled.
        if(settings.getBoolean("cp_notif_time", false)){
    		startService(new Intent(FortuneActivity.this, 
                    NotifyingService.class));
    	}
        
        //Put back the old text.
        text=savedInstanceState!=null?savedInstanceState.getString(KEY_TEXT):"";
        
        displayOnStart();
        
        //Gesture detection
        GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){

        	@Override
        	public boolean onFling(MotionEvent e1, MotionEvent e2, float
        			velocityX, float velocityY)
        	{
        		if(wrap){//Do not fling for new cookie if not wrapped (because then you need to be able to scroll).
        			if((velocityX >= 1200 || velocityX <= -1200)
        					&& !(velocityY >= 1200 || velocityY <= -1200)){

        				FlurryAgent.onEvent("fling");
        				getGoogleAnalyticsTrackerInstance(FortuneActivity.this).trackEvent("CookieInteraction", "Fling", //TODO: more data mining possible 
        						"velocityX=" + velocityX + ", velocityY=" + velocityY + ", text=" + text, Math.round(Math.max(velocityX, velocityY)));
        				refreshFortune();
            			return true;
        			}
        		}
        		return super.onFling(e1, e2, velocityX, velocityY);
        	}

        	/* (non-Javadoc)
        	 * @see android.view.GestureDetector.SimpleOnGestureListener#onDoubleTap(android.view.MotionEvent)
        	 */
        	@Override
        	public boolean onDoubleTap(MotionEvent e) {
        		FlurryAgent.onEvent("double-tap");
				getGoogleAnalyticsTrackerInstance(FortuneActivity.this).trackEvent("CookieInteraction", "DoubleTap", text, 0);
        		refreshFortune();
        		return true;
        	}
        };
        gestureDetector = new GestureDetector(this, gestureListener);

        //On first run install sample cookie file.
        if(settings.getBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, true)){
        	installSampleCookieFile();
		}else{//Still install sample cookie file if it is configured (1 particular case only) but not there anymore.
        	String sampleCookieFileString = getDefaultDirectory(this, getPackageName()).toURI().toString()+getResources().getResourceEntryName(R.raw.work);
			if(settings.getString("str_files", "").contains(sampleCookieFileString)){
				//Check external storage
				try{
					FortuneActivity.check_external_storage_writable(FortuneActivity.this);
	        		
	        		if(!new File(URI.create(sampleCookieFileString)).exists()){
	        			//BUGFIX: Workaround for Froyo in any case: Android bug that data in external storage is also deleted on reinstall(=update). This means that "work" is no longer there and found, resulting in empty cookies and error toasts. http://groups.google.com/group/android-developers/browse_thread/thread/b68d40b1f13e12df?pli=1 
	        			installSampleCookieFile();
	        		}
				}catch(Exception e){
					//Do nothing
				}
        	}
        }
    }
    
    /* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		

        //Update the settings from the preferences.
        updateFromPreferences();
        
		FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackPageView("/CookieOnResume");

        //Hide progress bar in any case
        setProgressBarIndeterminateVisibility(false);
        
	}

	@Override
    public boolean dispatchTouchEvent(MotionEvent ev){
    	//Makes sure scrolling still is possible.
        super.dispatchTouchEvent(ev);
        return gestureDetector.onTouchEvent(ev);
    } 

	private void displayOnStart() {
        //Display a fortune.
		if(getIntent()!=null && (getIntent().getAction()!=null && getIntent().getAction().equals(Intent.ACTION_VIEW)) &&
        		(getIntent().getType()!=null && getIntent().getType().equals("text/plain"))){//This activity was (re)started because of a click on a notification.
            //Look up the notification manager service and cancel the source (=last) notification.
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.cancel(NotifyingService.UNIQUE_NOTIFS_ID);

			FlurryAgent.onEvent("notif_cookie");
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Backend", "ShowCookieFrom", text, 0);
            //View that fortune from the notif.
            displayFortune(getIntent().getStringExtra("text"));
        }else{//This activity was launched in the normal way.
        	//Display a new fortune.
            if(TextUtils.isEmpty(text)){
            	refreshFortune();
            }else{
            	displayFortune(text);
        	}
        }
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

        //Enable analytics session. Should be first (at least before any Flurry calls).
    	FlurryAgent.onStartSession(this, FLURRY_API_KEY);
		
    	//Keep the intent.
    	setIntent(intent);
    	
    	//Display.
    	displayOnStart();
	}

	private void findAndSetViews() {
		// Get the text widget.
        tvText=(TextSwitcher)findViewById(R.id.tv_quote);
        // Get the scroll view.
        svFullScroll=(FullScrollView)findViewById(R.id.sv_fscroll);
        svScroll=(ScrollView)findViewById(R.id.sv_scroll);
        
        //Set the animations for the switcher
        Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);
        
        //Set animations.
        tvText.setInAnimation(in);
        tvText.setOutAnimation(out);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putString(KEY_TEXT, text);
		
		//The install sample progress will be aborted but will restart later, but we need to remove the dialog.
		try{
			dismissDialog(DIALOG_KEY_INSTALL_SAMPLE_COOKIE_FILE);
			if(FortuneActivity.DEBUG) Log.v(TAG, "DISMISS2");
		}catch(IllegalArgumentException e){
			//FlurryAgent.onError("FortuneActivity:onSaveInstanceState", "<There was no dialog being shown. Ignore. No real error.>", e.getMessage());
			//There was no dialog being shown. Ignore.
		}
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
		getGoogleAnalyticsTrackerInstance(this).stop();
	}

	/**
	 * Display a new fortune.
	 */
	private void refreshFortune() {
        // Show progress bar
		//TODO BUG: For some reason all this progress bar displaying does not work here, not root activity or Android bug??
        setProgressBarIndeterminateVisibility(true);
		try{
			//Get the fortune and display.
			displayFortune(getFortune(this, settings));

			FlurryAgent.onPageView();
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackPageView("/CookieRefreshed");
		}catch(IllegalArgumentException e){
			//Do nothing, this is just a pass through so finally can be used.
		}finally{
	        //Hide progress bar in any case
	        setProgressBarIndeterminateVisibility(false);
		}
	}
	
	/**
	 * Display the fortune.
	 */
	private void displayFortune(String newText) {
		//Remember it.
		text=newText;
		
		//Display it.
        //tvText.setText("a\na\na\na\na\na\na\na\na\nb\nb\nb\nb\nb\nb\nb\nb\nb\nb\nb\nb\nb\nb\nb\nb\nc\nc\nc\nc\nc\nc\nc\nc\nc\nc\nc\nc\n");//TODO make a unit test instead
        tvText.setText(text);
        
        //Recenter.
        if(svScroll!=null){
        	svScroll.scrollTo(0, 0);
        }
        if(svFullScroll!=null){
        	svFullScroll.scrollTo(0, 0);
        }
	}

	/**
	 * Get a new fortune.
	 * 
	 * @return The new fortune.
	 */
	public static String getFortune(Context context, SharedPreferences settings2) {   
		FlurryAgent.onEvent("getfortune");
		//Make a list of arguments to be passed on to the org.jfortune backend.
		ArrayList<String> args=new ArrayList<String>();
		
		//Flag: output will be collected.
		args.add("-r");
		
		//Flags: files.
		//Check external storage
		check_external_storage_readable(context);
		
		//Get the selection of files from the preferences.
		String str=settings2.getString("str_files", "");
		if (!(str.equals(""))){
			String[] strList=str.split(" ");
			for(int i=0; i<strList.length; i++){
				//Add files one by one (separate args).
				args.add(Uri.parse(strList[i]).getPath());
			}
		}else{//No files selected, display instructions.
			return context.getString(R.string.no_cookie_files);
		}
		
		//Flag: equality (as to random distribution). Based on preferences.
        if(settings2.getBoolean("cp_equal", false)){
        	args.add("-e");
        }
        
        
        String s=null;
        try {
        	//Initialize backend.
        	jfortuneclass fortune = new jfortuneclass(args.toArray(new String[0]));
        	//Run the backend (will collect the new fortune).
			fortune.run();
			//Actually retrieve the fortune.
	        s = fortune.getOutput();
		} catch (IllegalArgumentException e) {//A bug broke something internally.
			FlurryAgent.onError("FortuneActivity:getFortune1", context.getString(R.string.app_name_prefix)+" "+context.getString(R.string.toast_read_cookie_failed_internal)+" "+context.getString(Integer.parseInt(e.getMessage())), e.getMessage());
			Toast.makeText(context, context.getString(R.string.app_name_prefix)+" "+context.getString(R.string.toast_read_cookie_failed_internal)+" "+context.getString(Integer.parseInt(e.getMessage())), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		} catch (IOException e) {//Some failures are possible, like invalid or unexistant files or more heavier IO problems.
			FlurryAgent.onError("FortuneActivity:getFortune2", context.getString(R.string.app_name_prefix)+" "+context.getString(R.string.toast_read_cookie_failed)+" "+context.getString(R.string.error_file_not_exists_or_no_num)+" "+e.getMessage(), e.getMessage());
			Toast.makeText(context, context.getString(R.string.app_name_prefix)+" "+context.getString(R.string.toast_read_cookie_failed)+" "+context.getString(R.string.error_file_not_exists_or_no_num)+" "+e.getMessage()+" "+context.getString(R.string.toast_read_cookie_failed_suggestion), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		getGoogleAnalyticsTrackerInstance(context).trackEvent("Backend", "GetFortune", s, 0);
		return s;
	}

	/**
	 * Check external storage readable
	 * 
	 * @param context
	 * @throws IllegalArgumentException
	 */
	public static void check_external_storage_readable(Context context) {
		check_external_storage(context, false);
	}

	/**
	 * Check external storage writable
	 * 
	 * @param context
	 * @throws IllegalArgumentException
	 */
	public static void check_external_storage_writable(Context context) {
		check_external_storage(context, true);
	}

	/**
	 * Check external storage
	 * 
	 * @param context
	 * @throws IllegalArgumentException
	 */
	private static void check_external_storage(Context context, boolean writable_too) {
		//Check external storage
		String state = Environment.getExternalStorageState();
		if (!(Environment.MEDIA_MOUNTED.equals(state) || (!writable_too && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)))) {
			// Fail silently.
			FlurryAgent.onError("FortuneActivity:check_external_storage_readable1", "I/O error, the SD card is not mounted properly and read-enabled. The state is : "+state, state);
			final String message = "I/O error, the SD card is not mounted properly and read-enabled. The state is : "+state;
			Log.e(TAG, message);
			Toast.makeText(context, context.getString(R.string.app_name_prefix)+" "+context.getString(R.string.toast_read_cookie_failed)+" "+context.getString(R.string.error_external_storage_not_mounted)+" "+message+" "+context.getString(R.string.toast_read_cookie_failed_suggestion), Toast.LENGTH_LONG).show();
			throw new IllegalArgumentException(message);
		}
	}
        
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_CENTER:
			FlurryAgent.onEvent("dpad_center");
			FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("CookieInteraction", "DpadCenter", text, 0);
        	refreshFortune();
            return true;
    	}
    	return super.onKeyDown(keyCode, event); 
	}
    
    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // If the request code matches (from PreferencesActivity) then update the settings/behaviour from preferences again as these might have changed.
        if (requestCode == REQUEST_CODE_PREFERENCES && resultCode == RESULT_OK) {
            updateFromPreferences();
            //On first run/configure display a cookie immediately.
            if(TextUtils.isEmpty(text) || text.equals(getString(R.string.no_cookie_files))){
            	refreshFortune();
            }
        }
    }

    /**
     * Update the settings from the preferences, such as the font style and font size.
     */
    private void updateFromPreferences() {
    	if(FortuneActivity.DEBUG) Log.v(TAG, "updateFromPreferences");
        //Update wrapping.
        wrap = settings.getBoolean("cp_wrap", true);
        if(wrap){
        	setContentView(R.layout.main_wrap);
        }else{
        	setContentView(R.layout.main);
        }
        findAndSetViews();
        ((TextView)tvText.getChildAt(0)).setHorizontallyScrolling(!wrap);
        ((TextView)tvText.getChildAt(1)).setHorizontallyScrolling(!wrap);
    	//Update font style.
        final String font = settings.getString("lp_font", PreferencesActivity.DEFAULT_LP_FONT);
        ((TextView)tvText.getChildAt(0)).setTypeface(Typeface.create(font, Typeface.NORMAL));
        ((TextView)tvText.getChildAt(1)).setTypeface(Typeface.create(font, Typeface.NORMAL));
        //Update font size.
        final int fontsize = Integer.parseInt(settings.getString("ep_fontsize", PreferencesActivity.DEFAULT_EP_FONTSIZE));
        ((TextView)tvText.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_PT, fontsize);
        ((TextView)tvText.getChildAt(1)).setTextSize(TypedValue.COMPLEX_UNIT_PT, fontsize);
        //Display.
        displayFortune(text);
    }
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		// Add a menu item to refresh the fortune cookie
		menu.add(ContextMenu.NONE, MENU_ITEM_REFRESH, ContextMenu.NONE, R.string.menu_refresh).setIcon(R.drawable.ic_menu_refresh);
		
		//Show preferences action
		menu.add(ContextMenu.NONE, MENU_ITEM_PREFS, ContextMenu.NONE, R.string.menu_preferences).setIcon(android.R.drawable.ic_menu_preferences);

		//About action
		menu.add(ContextMenu.NONE, MENU_ITEM_ABOUT, ContextMenu.NONE, R.string.menu_about).setIcon(android.R.drawable.ic_menu_info_details);

		//Get more cookies action
		menu.add(ContextMenu.NONE, MENU_ITEM_GET_MORE_COOKIES, ContextMenu.NONE, R.string.menu_get_more_cookies).setIcon(android.R.drawable.ic_menu_set_as);

		//Send action
		menu.add(ContextMenu.NONE, MENU_ITEM_SEND, ContextMenu.NONE, R.string.menu_send).setIcon(android.R.drawable.ic_menu_send);

		//Web search action
		menu.add(ContextMenu.NONE, MENU_ITEM_WEB_SEARCH, ContextMenu.NONE, R.string.menu_web_search).setIcon(android.R.drawable.ic_menu_search);

		//Feedback action
		menu.add(ContextMenu.NONE, MENU_ITEM_FEEDBACK, ContextMenu.NONE, R.string.menu_feedback).setIcon(R.drawable.ic_menu_star);

		//Copy action
		menu.add(ContextMenu.NONE, MENU_ITEM_COPY, ContextMenu.NONE, R.string.menu_copy).setIcon(android.R.drawable.ic_menu_upload);

		//Share app action
		menu.add(ContextMenu.NONE, MENU_ITEM_SHARE_APP, ContextMenu.NONE, R.string.menu_share_app).setIcon(android.R.drawable.ic_menu_share);

		// Generate any additional actions that can be performed on the
		// overall list. In a normal install, there are no additional
		// actions found here, but this allows other applications to extend
		// our menu with their own actions.
		Intent intent = new Intent(null, getIntent().getData());
		intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
				new ComponentName(this, FortuneActivity.class), null,
				intent, 0, null);

		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_ITEM_REFRESH: {
				FlurryAgent.onEvent("refresh");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("CookieInteraction", "MenuRefresh", text, 0);
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("MenuMainWindow", "MenuRefresh", text, 0);
				// Refresh the cookie
		        refreshFortune();
				return true;
			}
			case MENU_ITEM_ABOUT: {
				FlurryAgent.onEvent("about");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("MenuMainWindow", "MenuAbout", text, 0);
				// Show the about dialog for this app.
				showAboutDialog();
				return true;
			}
			case MENU_ITEM_PREFS: {
				FlurryAgent.onEvent("show_prefs");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("MenuMainWindow", "MenuPreferences", text, 0);
				// Show the preferences.
		        Intent launchPreferencesIntent = new Intent().setClass(this, PreferencesActivity.class);
		        
		        //Make it a subactivity so we know when it returns.
		        startActivityForResult(launchPreferencesIntent, REQUEST_CODE_PREFERENCES);
				return true;
			}
			case MENU_ITEM_GET_MORE_COOKIES: {
				FlurryAgent.onEvent("more_cookies");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("MenuMainWindow", "MenuGetMoreCookies", text, 0);
				// Start the browser.
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.more_cookies_uri))));
				return true;
			}
			case MENU_ITEM_SEND: {
				FlurryAgent.onEvent("send");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("MenuMainWindow", "MenuSend", text, 0);
				// Send out the send intent with a chooser
				startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, text).setType("text/plain"), getString(R.string.chooser_send_action)));
				return true;
			}
			case MENU_ITEM_WEB_SEARCH: {
				FlurryAgent.onEvent("web_search");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("MenuMainWindow", "MenuWebSearch", text, 0);
				// Send out the web search intent with a chooser
				startActivity(Intent.createChooser(new Intent(Intent.ACTION_WEB_SEARCH).putExtra(SearchManager.QUERY, text), getString(R.string.chooser_web_search_action)));
				return true;
			}
			case MENU_ITEM_FEEDBACK: {
				FlurryAgent.onEvent("feedback");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("MenuMainWindow", "MenuFeedback", text, 0);
				// Send out the feedback intent with a chooser
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.about_feedback))));
				return true;
			}
			case MENU_ITEM_COPY: {
				FlurryAgent.onEvent("copy");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("MenuMainWindow", "MenuCopy", text, 0);
				// Copy text to clipboard via manager, and display Toast.
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
				clipboard.setText(text);
				Toast.makeText(this, getString(R.string.toast_copied_to_clipboard), Toast.LENGTH_LONG).show();
				return true;
			}
			case MENU_ITEM_SHARE_APP: {
				FlurryAgent.onEvent("share_app");
				FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("MenuMainWindow", "MenuShareApp", text, 0);
				// Send out the send/share_app intent with a chooser, and with a template text
				startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, getString(R.string.template_share_app)).putExtra(Intent.EXTRA_SUBJECT, getString(R.string.template_share_app_subject)).setType("text/plain"), getString(R.string.chooser_send_action)));
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void installSampleCookieFile(){
		FlurryAgent.onEvent("install_sample_cookie");
		FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackEvent("Backend", "InstallSampleCookieFile", null, 0);
		
		// Show progress dialog. Should be outside of thread.
		showDialog(DIALOG_KEY_INSTALL_SAMPLE_COOKIE_FILE);
		if(FortuneActivity.DEBUG) Log.v(TAG, "SHOW");
		
		new Thread(new Runnable(){
			synchronized public void run(){
				//Check external storage
				try{
					FortuneActivity.check_external_storage_writable(FortuneActivity.this);
				}catch(IllegalArgumentException e){
					return;
				}
				
				InputStream in=null;
				FileOutputStream out=null;
				try {
					// Open the raw resource.
					in = getResources().openRawResource(R.raw.work);

					// Get the directory if it's valid.
					File dir = getDirectory();

					if (!dir.exists()) {
						if(!dir.mkdirs()){
							//Fail silently.
							FlurryAgent.onError("FortuneActivity:installSampleCookieFile1", dir.toString(), "Could not create dir, are you sure the SD card is mounted and not full?");
							Log.e(TAG, "Could not create dir, are you sure the SD card is mounted and not full?: "+dir.toString());
							return;
						}
					}
					
					if (!dir.isDirectory()) {
						// Fail silently.
						FlurryAgent.onError("FortuneActivity:installSampleCookieFile2", dir.toString(), "Is not a directory");
						Log.e(TAG, "Is not a directory: "+dir.toString());
						return;
					}

					// Create
					File outputFile = new File(dir.getPath() + File.separator + getResources().getResourceEntryName(R.raw.work));
					if(!outputFile.createNewFile()){
						// Fail silently.
						FlurryAgent.onError("FortuneActivity:installSampleCookieFile3", outputFile.toString(), "File exists");
						Log.e(TAG, "File exists, but continuing assuming it is the expected file: "+outputFile.toString());
						
						//Possible that the file remained from a previous version/install, so try to continue.
						finishInstall(outputFile);
						
						in.close();
						in=null;
					}else{
						// Copy
						out = new FileOutputStream(outputFile);
						int c;

						while ((c = in.read()) != -1) {
							out.write(c);
						}

						in.close();
						in=null;
						out.close();
						out=null;

						// Generate .num file.
						generateSampleNumFile(outputFile);

						finishInstall(outputFile);

						if(FortuneActivity.DEBUG) Log.v(TAG, "RUN END");
					}
				} catch (FileNotFoundException fnfe) {
					// Fail silently.
					FlurryAgent.onError("FortuneActivity:installSampleCookieFile4", "<Fail silently.>", fnfe.getMessage());
					Log.e(TAG, "File cannot be opened: "+fnfe.getMessage());
					//e.printStackTrace();
					return;
				} catch (IOException ioe) {
					// Fail silently.
					FlurryAgent.onError("FortuneActivity:installSampleCookieFile1", "<Fail silently.>", ioe.getMessage());
					Log.e(TAG, "I/O error, are you sure the SD card is mounted and not full?: "+ioe.getMessage());
					//e.printStackTrace();
					return;
				} catch (IllegalArgumentException iae) {
					// Fail silently.
					FlurryAgent.onError("FortuneActivity:installSampleCookieFile2", "<Fail silently.>", iae.getMessage());
					Log.e(TAG, "Error in generating .num file (or error in closing progress dialog): "+iae.getMessage());
					//e.printStackTrace();
					return;
				} catch (NotFoundException nfe) {
					// Fail silently.
					FlurryAgent.onError("FortuneActivity:installSampleCookieFile3", "<Fail silently.>", nfe.getMessage());
					Log.e(TAG, "Package error, sample cookie file resource not found: "+nfe.getMessage());
					//e.printStackTrace();
					return;
				} finally {
					try{
						if(in!=null){
							in.close();
						}
						if(out!=null){
							out.close();
						}
					} catch (IOException ioe) {
						// Fail silently because likely they were already closed above or whatever but it doesn't matter.
						return;
					}
				}
			}

			synchronized private File getDirectory() {
				//Get from preferences or fallback to default.
				return new File(settings.getString("ep_folder", getDefaultDirectory(FortuneActivity.this, getPackageName()).getPath()));
			}


			synchronized private void finishInstall(File outputFile) {
				// Set preferences.
				Editor editor = settings.edit();
				editor.putBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, false);
	        	if(!settings.getString("str_files", "").contains(outputFile.toURI().toString())){//Only add if not in str_files yet.
	        		editor.putString("str_files", outputFile.toURI()+" "+settings.getString("str_files", ""));
	        	}
				editor.commit();
				//TODO: in Gingerbread: editor.apply()
				
				// Refresh the cookie by sending a message to the handler.
				mHandler.sendEmptyMessage(MSG_INSTALL_SAMPLE_COOKIE_FILE_DONE);
			}
		}, "InstallSampleCookieFile").start();
	}

	private void generateSampleNumFile(File numlessFile) throws IOException, IllegalArgumentException{
		//Initialize the generator (in the org.jstrfile backend).
		jstrfile generator = new jstrfile();
		
		//Generate the .num file (in the org.jstrfile backend).
		generator.makeIndex(numlessFile.toString());
	}
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_KEY_INSTALL_SAMPLE_COOKIE_FILE: {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(getString(R.string.dialog_install));
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                return mProgressDialog;
            }
        }
        return null;
    }

	private void showAboutDialog() {
		FlurryAgent.onPageView();
		FortuneActivity.getGoogleAnalyticsTrackerInstance(this).trackPageView("/About");
		
		Intent intent=new Intent(AboutIntents.ACTION_SHOW_ABOUT_DIALOG);
		
		//Supply the image name and package.
		intent.putExtra(AboutIntents.EXTRA_ICON_RESOURCE, getResources().getResourceName(R.drawable.cookie));
		intent.putExtra(AboutIntents.EXTRA_PACKAGE_NAME, getPackageName());
		
		intent.putExtra(AboutIntents.EXTRA_APPLICATION_LABEL, getString(R.string.app_name));
		
		//Get the app version
		String version = "?";
		try {
		    PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
		    version = pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			FlurryAgent.onError("FortuneActivity:showAboutDialog1", "(Package name not found, not a real error, just need to install OI About)", e.getMessage());
		    if(FortuneActivity.DEBUG) Log.e(TAG, "Package name not found", e);
		};
		intent.putExtra(AboutIntents.EXTRA_VERSION_NAME, version);
		intent.putExtra(AboutIntents.EXTRA_COMMENTS, getString(R.string.about_comments));
		intent.putExtra(AboutIntents.EXTRA_COPYRIGHT, getString(R.string.about_copyright));
		intent.putExtra(AboutIntents.EXTRA_WEBSITE_LABEL, getString(R.string.about_website_label));
		intent.putExtra(AboutIntents.EXTRA_WEBSITE_URL, getString(R.string.about_website_url));
		intent.putExtra(AboutIntents.EXTRA_EMAIL, getString(R.string.about_feedback));
		intent.putExtra(AboutIntents.EXTRA_AUTHORS, getResources().getStringArray(R.array.about_authors));
		intent.putExtra(AboutIntents.EXTRA_DOCUMENTERS, getResources().getStringArray(R.array.about_documenters));
		
		//Create string array of translators from translated string from Launchpad or (for English) from the array.
		String translatorsString=getString(R.string.about_translators);
		if(translatorsString.equals("translator-credits")){
			intent.putExtra(AboutIntents.EXTRA_TRANSLATORS, getResources().getStringArray(R.array.about_translators));
		}else{
			String[] translatorsArray=translatorsString.replaceFirst("Launchpad Contributions: ", "").split("(; )|(;)");
			intent.putExtra(AboutIntents.EXTRA_TRANSLATORS, translatorsArray);
		}
		intent.putExtra(AboutIntents.EXTRA_ARTISTS, getResources().getStringArray(R.array.about_artists));
		
		// Supply resource name of raw resource that contains the license:
		intent.putExtra(AboutIntents.EXTRA_LICENSE_RESOURCE, getResources()
				.getResourceName(R.raw.license_short));
		//intent.putExtra(AboutIntents.EXTRA_WRAP_LICENSE, false);
		
		try{
			startActivityForResult(intent, 0);
		}catch(ActivityNotFoundException e){
			
			try{
				FlurryAgent.onError("FortuneActivity:showAboutDialog2", getString(R.string.about_backup), e.getMessage());
				Toast.makeText(this, getString(R.string.about_backup), Toast.LENGTH_LONG).show();
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_about_dialog))));
			}catch(ActivityNotFoundException e2){
				FlurryAgent.onError("FortuneActivity:showAboutDialog3", getString(R.string.market_backup), e2.getMessage());
				Toast.makeText(this, getString(R.string.market_backup), Toast.LENGTH_LONG).show();
			}
		}
	}

	
	public static File getDefaultDirectory(Context context, String packageName) {
		File dir;
		if(mActivity_getExternalFilesDir!=null){//API >=8
			dir = invokeGetExternalFilesDir(context, "fortune");
		}else if(mEnvironment_getExternalStorageDirectory!=null){//API <=7
			dir = invokeGetExternalStorageDirectory();
			dir = new File(dir.getPath() + "/Android/data/" + packageName + "/files/");
		}else{
			//should never occur
			
			Exception e1 = null, e2 = null;
			File dir1 = null, dir2 = null;
			try{
				dir1 = invokeGetExternalFilesDir(context, "fortune");
			} catch (Exception e){
				e1=e;
			}
			try{
				dir2 = invokeGetExternalStorageDirectory();
			} catch (Exception e){
				e2=e;
			}
			FlurryAgent.onError("FortuneActivity:getDefaultDirectory1", "Could not get default directory in any API.", "dir1="+((dir1==null)?"null":dir1.toString())+"; e1="+((e1==null)?"null":e1.toString()+", "+e1.getMessage())+"; dir2="+((dir2==null)?"null":dir2.toString())+"; e2="+((e2==null)?"null":e2.toString()+", "+e2.getMessage()));
			
			//BUGFIX: "Else case" still occured. On Droid 2? With SD card unmounted? "NullPointerException in PreferencesActivity.setDynamicDefaultValues()". This function should never return null again.
			dir = new File("/sdcard/Android/data/" + packageName + "/files/");
		}
		return dir;
	}

    //Reflection construction because writing to external storage is an API different in version 8.
	private static Method mEnvironment_getExternalStorageDirectory;
	private static Method mActivity_getExternalFilesDir;

   static {
       initCompatibility();
   };

   private static void initCompatibility() {
	   //mEnvironment_getExternalStorageDirectory
       try {
    	   mEnvironment_getExternalStorageDirectory = Environment.class.getMethod(
                   "getExternalStorageDirectory", (Class[]) null );
           /* success, this is a newer device */
       } catch (NoSuchMethodException nsme) {
           /* failure, must be older device */
       }
       
	   //mActivity_getExternalFilesDir
       try {
    	   mActivity_getExternalFilesDir = FortuneActivity.class.getMethod(
                   "getExternalFilesDir", new Class[] { String.class } );
           /* success, this is a newer device */
       } catch (NoSuchMethodException nsme) {
           /* failure, must be older device */
       }
   }

   private static File invokeGetExternalStorageDirectory() {
       try {
    	   return (File) mEnvironment_getExternalStorageDirectory.invoke(null);
       } catch (InvocationTargetException ite) {
           /* unpack original exception when possible */
           Throwable cause = ite.getCause();
           if (cause instanceof RuntimeException) {
               throw (RuntimeException) cause;
           } else if (cause instanceof Error) {
               throw (Error) cause;
           } else {
               /* unexpected checked exception; wrap and re-throw */
               throw new RuntimeException(ite);
           }
       } catch (IllegalAccessException ie) {
			// Fail silently.
			FlurryAgent.onError("FortuneActivity:invokeGetExternalStorageDirectory", "<Fail silently.>", ie.getMessage());
			Log.e(TAG, "Incompatible code error: "+ie.getMessage());
			//e.printStackTrace();
       }
       return null;
   }

   private static File invokeGetExternalFilesDir(Context context, String type) {
       try {
    	   return (File) mActivity_getExternalFilesDir.invoke(context, type);
       } catch (InvocationTargetException ite) {
           /* unpack original exception when possible */
           Throwable cause = ite.getCause();
           if (cause instanceof RuntimeException) {
               throw (RuntimeException) cause;
           } else if (cause instanceof Error) {
               throw (Error) cause;
           } else {
               /* unexpected checked exception; wrap and re-throw */
               throw new RuntimeException(ite);
           }
       } catch (IllegalAccessException ie) {
			// Fail silently.
			FlurryAgent.onError("FortuneActivity:invokeGetExternalFilesDir", "<Fail silently.>", ie.getMessage());
			Log.e(TAG, "Incompatible code error: "+ie.getMessage());
			//e.printStackTrace();
       }
       return null;
   }
}