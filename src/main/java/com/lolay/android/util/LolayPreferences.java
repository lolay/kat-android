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

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.*;

public class LolayPreferences {
	private static final String TAG = LolayPreferences.class.getSimpleName();
	
    @SuppressWarnings("unchecked")
	public static <T> T getObject(SharedPreferences preferences, String key) {
        String base64 = preferences.getString(key, null);
        if (base64 == null || base64.length() == 0) {
            Log.d(TAG, String.format("getObject: Null or empty %s", key));
        	return null;
        }
        
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        ObjectInputStream inputStream = null;
        Object deserialized;
		try {
			try {
				inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
				deserialized = inputStream.readObject();
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		} catch (IOException e) {
			Log.e(TAG, String.format("getObject: Had an error reading %s", key), e);
			return null;
		} catch (ClassNotFoundException e) {
			Log.e(TAG, String.format("getObject: Couldn't find the class for %s", key), e);
			return null;
		}
		
		return (T) deserialized;
    }
    
    public static <T extends Serializable> void setObject(SharedPreferences preferences, T object, String key) {
        Log.d(TAG, String.format("setObject: enter key=%s,object=%s", key, object));
    	String base64 = null;
    	
    	if (object != null) {
        	try {
            	ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        		ObjectOutputStream outputStream = null;
        		try {
        			outputStream = new ObjectOutputStream(byteStream);
        			outputStream.writeObject(object);
        			base64 = Base64.encodeToString(byteStream.toByteArray(), Base64.DEFAULT);
        		} finally {
        			if (outputStream != null) {
        				outputStream.close();
        			}
        		}
    		} catch (IOException e) {
    			Log.e(TAG, String.format("setObject: Had an error writing %s", key), e);
    			return;
    		}
    	}
    	
		SharedPreferences.Editor editor = preferences.edit();
    	if (base64 != null && base64.length() > 0) {
            Log.d(TAG, String.format("setObject: saving key=%s", key));
    		editor.putString(key, base64);
    	} else {
            Log.d(TAG, String.format("setObject: key=%s is null, removing", key));
    		editor.remove(key);
    	}
		editor.commit();
    }
}
