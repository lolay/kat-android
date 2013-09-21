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
package com.lolay.android.security;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

import com.lolay.android.log.LolayLog;

public class OpenX509TrustManager implements X509TrustManager {
	private static final String TAG = LolayLog.buildTag(OpenX509TrustManager.class);

    public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
    	// Assume everything is trusted
    }

    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
    	// Assume everything is trusted
    }

    public X509Certificate[] getAcceptedIssuers() {
    	return null;
    }
    
    public static void openTrust() {
        LolayLog.w(TAG, "openTrust", "THIS IS AN OPEN TRUST MANAGER FOR DEBUGGING ONLY!");
        try {
        	SSLContext context = SSLContext.getInstance("SSL");
        	context.init(null, new TrustManager[] {new OpenX509TrustManager()}, null);
        	HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        	HttpsURLConnection.setDefaultHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    	} catch (Exception e) {
    		LolayLog.e(TAG, "openTrust", "Could not open the trust", e);
    	}
    }
}
