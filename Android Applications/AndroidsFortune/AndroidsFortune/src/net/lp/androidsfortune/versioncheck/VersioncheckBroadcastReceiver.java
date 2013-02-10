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

import android.content.Context;

import com.tomgibara.android.veecheck.VeecheckReceiver;
import com.tomgibara.android.veecheck.VeecheckSettings;
import com.tomgibara.android.veecheck.VeecheckState;
import com.tomgibara.android.veecheck.util.PrefSettings;
import com.tomgibara.android.veecheck.util.PrefState;

/**
 * @author pjv
 *
 */
public class VersioncheckBroadcastReceiver extends VeecheckReceiver {

	@Override
	protected VeecheckSettings createSettings(Context context) {
		return new PrefSettings(context);
	}
	
	@Override
	protected VeecheckState createState(Context context) {
		return new PrefState(context);
	}
	
}
