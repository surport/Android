package com.ruyicai.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.os.Process;

/**
 * 
 * @author haojie
 * 
 */
public class RuyicaiActivityManager extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static RuyicaiActivityManager instance;

	private RuyicaiActivityManager() {

	}

	public static RuyicaiActivityManager getInstance() {
		if (instance == null) {
			instance = new RuyicaiActivityManager();
		}
		return instance;

	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {

		for (int i = activityList.size() - 1; i >= 0; i--) {
			Activity activity = activityList.get(i);
			activity.finish();
		}

		// System.exit(0);
		Process.killProcess(Process.myPid());
	}
}