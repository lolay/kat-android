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

/**
 * An extension of {@link LolayPair} class that overrides toString() to return the second member of the Pair.
 * This class is intended to be used with {@link android.widget.Spinner} where {@code Pair.second} will be
 * used for spinner display value, and {@code Pair.first} will be used as the actual value to be saved.
 */
public class SpinnerPair<F> extends LolayPair<F, String>{
	private static final long serialVersionUID = 1L;

	public SpinnerPair(F first, String second) {
        super(first, second);
    }

    @Override
    public java.lang.String toString() {
    	if (second != null) {
    		return second.toString();
    	}
    	return null;
    }
}
