package com.lolay.android.security;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class OpenSSLSocketFactory implements SocketFactory {
	private SSLContext context = null;
	
	private SSLContext getContext() {
		if (context == null) {
			try {
		    	SSLContext context = SSLContext.getInstance("SSL");
	        	context.init(null, new TrustManager[] {new OpenX509TrustManager()}, null);
	        	this.context = context;
			} catch (NoSuchAlgorithmException e) {
//				throw new IOException(e);
			} catch (KeyManagementException e) {
//				throw new IOException(e);
			}
		}
		
		return this.context;
	}

	@Override
	public Socket createSocket() throws IOException {
		return getContext().getSocketFactory().createSocket();
	}

	@Override
	public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
		int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
		int soTimeout = HttpConnectionParams.getSoTimeout(params);

		InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
		SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());

		if ((localAddress != null) || (localPort > 0)) {
			if (localPort < 0) {
				localPort = 0;
			}
			InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
			sslsock.bind(isa);
        }

		sslsock.connect(remoteAddress, connTimeout);
		sslsock.setSoTimeout(soTimeout);
		return sslsock;
	}

	@Override
	public boolean isSecure(Socket sock) throws IllegalArgumentException {
		return true;
	}
	
	public static void openTrust(HttpClient client) {
		client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", new OpenSSLSocketFactory(), 443));
	}
}
