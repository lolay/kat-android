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

import java.net.URI;

import com.lolay.android.log.LolayLog;
import com.lolay.android.strands.task.LolayTaskManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public abstract class LolayBaseActivity extends Activity {
	private static final String TAG = LolayLog.buildTag(LolayBaseActivity.class);
	
	public LolayBaseApplication getLolayApplication() {
		return (LolayBaseApplication) getApplicationContext();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LolayLog.v(TAG, String.format("%s.onCreate", this.getClass().getSimpleName()), "enter");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onRestart() {
		LolayLog.v(TAG, String.format("%s.onRestart", this.getClass().getSimpleName()), "enter");
		super.onRestart();
	}
	
	@Override
	protected void onStart() {
		LolayLog.v(TAG, String.format("%s.onStart", this.getClass().getSimpleName()), "enter");
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		LolayLog.v(TAG, String.format("%s.onResume", this.getClass().getSimpleName()), "enter");
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		LolayLog.v(TAG, String.format("%s.onStop", this.getClass().getSimpleName()), "enter");
		super.onStop();
	}
	
	@Override
	protected void onPause() {
		LolayLog.v(TAG, String.format("%s.onPause", this.getClass().getSimpleName()), "enter");
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		LolayLog.v(TAG, String.format("%s.onDestroy", this.getClass().getSimpleName()), "enter");
		super.onDestroy();
		cancelTasks();
	}
	
	public <Params,Progress,Result> void addTaskAndExecute(AsyncTask<Params,Progress,Result> task, Params... params) {
		LolayTaskManager taskManager = getLolayApplication().getTaskManager();
		if (taskManager != null) {
			taskManager.addTaskAndExecute(this, task, params);
		}
	}
	
	public void cancelTasks() {
		LolayTaskManager taskManager = getLolayApplication().getTaskManager();
		if (taskManager != null) {
			taskManager.cancelTasks(this);
		}
	}

	/**
	 * Thanks to http://nex-otaku-en.blogspot.com/2010/12/android-put-listview-in-scrollview.html
	 * WARNING: Only works with Linear Layouts or you'll get a NullPointerException in the measure code
	 */
	public void listViewSetHeightFromChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
	}
	
	public void viewSetVisibility(int id, int visibility) {
		View view = findViewById(id);
		if (view != null) {
			view.setVisibility(visibility);
		}
	}
	
	public Intent youTubeIntent(Uri url, String title) {
		Intent intent = new Intent(Intent.ACTION_VIEW, url);
		return Intent.createChooser(intent, title);
	}
	
	public Intent youTubeIntent(URI url, String title) {
		Uri uri = Uri.parse(url.toASCIIString());
		return youTubeIntent(uri, title);
	}
	
	public Intent youTubeIntent(URI url) {
		return youTubeIntent(url, "View on YouTube");
	}

	public Intent youTubeIntent(Uri url) {
		return youTubeIntent(url, "View on YouTube");
	}

	public Intent youTubeIntent(URI url, int titleId) {
		String title = getResources().getString(titleId);
		if (title != null) {
			return youTubeIntent(url, title);
		} else {
			LolayLog.w(TAG, "youTubeIntent", "Could not find titleId=%s", titleId);
			return youTubeIntent(url);
		}
	}

	public Intent youTubeIntent(Uri url, int titleId) {
		String title = getResources().getString(titleId);
		if (title != null) {
			return youTubeIntent(url, title);
		} else {
			LolayLog.w(TAG, "youTubeIntent", "Could not find titleId=%s", titleId);
			return youTubeIntent(url);
		}
	}
}
