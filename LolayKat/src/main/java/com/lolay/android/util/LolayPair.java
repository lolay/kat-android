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
package com.lolay.android.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

public class LolayPair<F, S> implements Serializable {
	private static final String TAG = LolayPair.class.getSimpleName();
	private static final long serialVersionUID = 1L;
	public F first;
	public S second;
	
	public LolayPair(F first, S second) {
		this.first = first;
		this.second = second;
	}
	
	public static List<LolayPair<String,String>> loadStream(InputStream stream) {
		if (stream == null) {
			return null;
		}
		
		Properties pairProperties = new Properties();
		try {
			pairProperties.load(stream);
		} catch (IOException e) {
			Log.e(TAG, "Could not load stream", e);
			return null;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					Log.e(TAG, "Could not close stream", e);
				}
			}
		}
		
		List<LolayPair<String,String>> pairs = new ArrayList<LolayPair<String,String>>(pairProperties.size());
		for (Entry<Object, Object> entryPair : pairProperties.entrySet()) {
			String key = String.class.cast(entryPair.getKey());
			String value = String.class.cast(entryPair.getValue());
			LolayPair<String,String> pair = new LolayPair<String,String>(key, value);
			pairs.add(pair);
		}
		
		return pairs;
	}
	
	public static List<LolayPair<String,String>> loadResource(Context context, int id) {
		InputStream stream = context.getResources().openRawResource(id);
		return loadStream(stream);
	}
	
	public static List<LolayPair<String,String>> loadAsset(Context context, String name) {
		InputStream stream = null;
		try {
			stream = context.getAssets().open(name);
		} catch (IOException e) {
			Log.d(TAG, String.format("Could not open asset=%s", name), e);
		}
		return loadStream(stream);
	}
}
