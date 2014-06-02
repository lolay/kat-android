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

import android.R;
import android.content.Context;
import android.graphics.Typeface;
import android.text.*;
import android.text.style.CharacterStyle;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;

/**
 * Adapted from http://www.androidengineer.com/2010/08/easy-method-for-formatting-android.html.
 */
public class StyleableSpannableStringBuilder extends SpannableStringBuilder {

	public static StyleSpan styleSpanBold () {
		return new StyleSpan(Typeface.BOLD);
	}

	public static RelativeSizeSpan relativeSizeSpan75Percent() {
		return new RelativeSizeSpan(0.75f);
	}

	public static RelativeSizeSpan relativeSizeSpan(float proportion) {
		return new RelativeSizeSpan(proportion);
	}

	public static TextAppearanceSpan textAppearanceSpanSmall(Context context) {
		return new TextAppearanceSpan(context, R.style.TextAppearance_Small);
	}

	public StyleableSpannableStringBuilder append(CharSequence text) {
		super.append(text);
		return this;
	}

	public StyleableSpannableStringBuilder append(CharSequence text, int start, int end) {
		super.append(text, start, end);
		return this;
	}

	@Override
	public StyleableSpannableStringBuilder append(char text) {
		super.append(text);
		return this;
	}

	public StyleableSpannableStringBuilder appendWithStyle(CharSequence text, CharacterStyle... styles) {
        super.append(text);
        int startPos = length() - text.length();
        for (CharacterStyle style : styles) {
            setSpan(style, startPos, length(), 0);
        }
        return this;
    }

    public StyleableSpannableStringBuilder appendBold(CharSequence text) {
        return appendWithStyle(text, styleSpanBold());
    }

    /**
     * Find each span surrounded by {@code token}, make them a span and apply supplied {@code styles} to them.  This is not yet
     * working as expected.  The latter span always remove the earlier span for whatever reason.
     * @param text
     * @param token
     * @param styles
     * @return
     */
    public static CharSequence setSpanBetweenTokens(CharSequence text, String token, CharacterStyle... styles)
    {
        // Start and end refer to the points where the span will apply
        int tokenLen = token.length();

        int start = text.toString().indexOf(token) + tokenLen;
        int end = text.toString().indexOf(token, start);

        if (start > -1 && end > -1)
        {
            // Copy the spannable string to a mutable spannable string
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            for (CharacterStyle style : styles) {
                ssb.setSpan(style, start, end, 0);
            }

            // Delete the tokens before and after the span
            ssb.delete(end, end + tokenLen);
            ssb.delete(start - tokenLen, start);

            text = ssb;
        }
        return text;
    }

    /**
     * For a given CharSequence {@code original}, find the first occurrence of {@code substring}, and apply suplied
     * {@code styles} to it to make it into a styled span.
     *
     * @param original
     * @param substring
     * @param styles
     * @return
     */
    public static CharSequence applyStyleToSubstring(CharSequence original, String substring, CharacterStyle... styles) {
        int i = TextUtils.indexOf(original, substring);
        int length = substring.length();
        SpannableString spannableString = new SpannableString(original);
        if (i > -1) {
            for (CharacterStyle style : styles) {
                spannableString.setSpan(style, i, i + length, 0);
            }
        }
        return spannableString;

    }

	/**
	 * Where a string resource has both styled span, and formats, e.g.,
	 * {@coee &lt;b&gt;Your Name&lt;/b&gt; %1$s }, this method returns a {@link android.text.Spanned} that is formatted with
	 * {@code String.format()} and preserves the styled span by calling {@code Html.fromHtml()}
	 * @param formatWithEscapedHtmlTag
	 * @param arguments
	 * @return
	 */
	public static Spanned formatSpanned(String formatWithEscapedHtmlTag, Object... arguments) {
		Object[] encodedArguments = new Object[arguments.length];
		int i = 0;
		for (Object argument : arguments) {
			if (argument instanceof String) {
				encodedArguments[i++] = TextUtils.htmlEncode((String) argument);
			}
			else {
				encodedArguments[i++] = argument;
			}
		}
		String simpleResult=String.format(formatWithEscapedHtmlTag, encodedArguments);
		Spanned spanned = Html.fromHtml(simpleResult);
		return spanned;
	}

}
