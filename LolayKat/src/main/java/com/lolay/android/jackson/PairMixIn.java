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
