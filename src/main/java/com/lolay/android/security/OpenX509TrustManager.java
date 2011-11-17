/*
 * Created by Lolay, Inc.
 * Copyright 2011 Lolay, Inc. All rights reserved.
 */
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
