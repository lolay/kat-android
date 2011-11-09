/*
 * Created by Lolay, Inc.
 * Copyright 2011 MyLife, Inc. All rights reserved.
 */
package com.lolay.android.util;

/**
 * An extension of {@link LolayPair} class that overrides toString() to return the second member of the Pair.
 * This class is intended to be used with {@link android.widget.Spinner} where {@code Pair.second} will be
 * used for spinner display value, and {@code Pair.first} will be used as the actual value to be saved.
 */
public class SpinnerPair<Object, String> extends LolayPair<Object, String>{
    public SpinnerPair(Object first, String second) {
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
