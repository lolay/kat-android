/*
 * Created by Lolay, Inc.
 * Copyright 2011 MyLife, Inc. All rights reserved.
 */
package com.lolay.android.jackson;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * A Jackson MixIn annotation class for serializing/de-serializing android.util.Pair class.
 * @param <K>
 * @param <V>
 */
public abstract class PairMixIn<K, V> {
    @JsonCreator PairMixIn(@JsonProperty("first") K first, @JsonProperty("second") V second) { }
    @JsonProperty K first;
    @JsonProperty V second;
}
