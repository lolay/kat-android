//
//  Copyright 2011, 2012, 2013 Lolay, Inc.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//
package com.lolay.android.ui;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lolay.android.error.LolayErrorManager;
import com.lolay.android.log.LolayLog;
import com.lolay.android.progress.LolayProgressManager;
import com.lolay.android.task.LolayTaskManager;
import com.lolay.android.tracker.LolayLogTracker;
import com.lolay.android.tracker.LolayTracker;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class LolayBaseApplication extends Application {
	private static String TAG = LolayLog.buildTag(LolayBaseApplication.class);
	private LolayErrorManager errorManager = null;
	private LolayProgressManager progressManager = null;
	private LolayTaskManager taskManager = null;	
	private LolayTracker tracker = null;
	private Lock initLock = new ReentrantLock();
	
	protected LolayProgressManager buildProgressManager() {
		return new LolayProgressManager();
	}
	
	public LolayProgressManager getProgressManager() {
		return getProgressManager(true);
	}
	
	public LolayProgressManager getProgressManager(boolean create) {
		if (create && progressManager == null) {
			initLock.lock();
			try {
				if (progressManager == null) {
					progressManager = buildProgressManager();
					LolayLog.i(TAG, "getProgressManager", "Progress Manager Initialized");
				}
			} finally {
				initLock.unlock();
			}
		}
		
		return progressManager;
	}
	
	protected LolayErrorManager buildErrorManager() {
		return new LolayErrorManager(this);
	}
	
	public LolayErrorManager getErrorManager() {
		return getErrorManager(true);
	}
	
	public LolayErrorManager getErrorManager(boolean create) {
		if (create && errorManager == null) {
			initLock.lock();
			try {
				if (errorManager == null) {
					errorManager = buildErrorManager();
					LolayLog.i(TAG, "getErrorManager", "Error Manager Initialized");
				}
			} finally {
				initLock.unlock();
			}
		}
		
		return errorManager;
	}
	
	public LolayTaskManager buildTaskManager() {
		return new LolayTaskManager(getProgressManager());
	}
	
	public LolayTaskManager getTaskManager() {
		return getTaskManager(true);
	}
	
	public LolayTaskManager getTaskManager(boolean create) {
		if (create && taskManager == null) {
			initLock.lock();
			try {
				if (taskManager == null) {
					taskManager = buildTaskManager();
					LolayLog.i(TAG, "getTaskManager", "Task Manager Initialized");
				}
			} finally {
				initLock.unlock();
			}
		}
		
		return taskManager;
	}
	
	protected LolayTracker buildTracker() {
		return new LolayLogTracker();
	}
	
	public LolayTracker getTracker() {
		return getTracker(true);
	}
	
	public LolayTracker getTracker(boolean create) {
		if (create && tracker == null) {
			initLock.lock();
			try {
				if (tracker == null) {
					tracker = buildTracker();
					LolayLog.i(TAG, "getTracker", "Tracker Initialized");
				}
			} finally {
				initLock.unlock();
			}
		}
		
		return tracker;
	}
	
	public void onCreate() {
		LolayLog.i(TAG, "onCreate", "enter");
		super.onCreate();
	}
	
	public void onTerminate() {
		LolayLog.i(TAG, "onTerminate", "enter");
		super.onTerminate();
		LolayTaskManager taskManager = getTaskManager(false);
		if (taskManager != null) {
			taskManager.cancelAllTasks();
		}
	}
	
	protected Lock getInitLock() {
		return initLock;
	}
	
    public int identifierForName(String name) {
        int identifier = 0;
        Resources resources = getResources();
        
        if (resources != null) {
            identifier = resources.getIdentifier(name, "string", getPackageName());
        }
        
        if (identifier == 0) {
        	LolayLog.w(TAG, "identifierForName", "Did not find resource identifier for resource named %s", name);
        }
        
        return identifier;
    }

	public LayoutInflater getLayoutInflater() {
		return  LayoutInflater.from(getBaseContext());
	}
	
	public View getLayoutById(int id, ViewGroup root) {
		return getLayoutInflater().inflate(id, root, false);
	}
	
	public View getLayoutById(int id) {
		return getLayoutById(id, null);
	}
	
	public View getLayoutByName(String name) {
		return getLayoutByName(name, null);
	}
	
	public View getLayoutByName(String name, ViewGroup root) {
		int id = identifierForName(name);
		View view = getLayoutById(id, root);
		return view;
	}
	
	public String getProcessName() {
        Context context = getApplicationContext();
        ActivityManager actMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appList = actMgr.getRunningAppProcesses();
        for (RunningAppProcessInfo info : appList) {
            if (info.pid == android.os.Process.myPid()) {
                return info.processName;
            }
        }
        return null;
	}
	
    public boolean isProcess(String processName) {
    	return processName.equals(getProcessName());
    }
    
    public RunningAppProcessInfo getProcessInfo(String name) {
        Context context = getApplicationContext();
        ActivityManager actMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appList = actMgr.getRunningAppProcesses();
        for (RunningAppProcessInfo info : appList) {
        	if (name.equals(info.processName)) {
        		return info;
        	}
        }
        return null;
    }
}
