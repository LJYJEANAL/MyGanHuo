package com.ng.ganhuoapi.util;

import android.content.Context;

/**
 * Android platform
 * 
 */
public class AndroidFactory {
	/**
	 * Android application context
	 */
	private static Context context = null;

	/**
	 * Returns the application context
	 * 
	 * @return Context
	 */
	public static  Context getApplicationContext() {
		return context;
	}

	/**
	 * Load factory
	 * 
	 * @param context
	 *            Context
	 */
	public static void setApplicationContext(Context context) {
		AndroidFactory.context = context;
	}
}
