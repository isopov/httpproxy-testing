package com.sopovs.moradanen;

import java.io.IOException;
import java.util.Collections;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.littleshoot.proxy.DefaultHttpProxyServer;
import org.littleshoot.proxy.HttpFilter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.HttpRequestFilter;

//TODO Proxy does not stop, see https://github.com/adamfisk/LittleProxy/issues/36
public class LittleProxyVsHttpClient3 {
	private static final int PROXY_PORT = 10200;

	public static void main(String[] args) throws HttpException, IOException {

		//Whithout proxy 
		HttpClient client = new HttpClient();
		client.getParams().setVersion(HttpVersion.HTTP_1_0);
		GetMethod getMethod = new GetMethod("http://google.com");
		//should print 200 - HTTP OK status
		if (200 == client.executeMethod(getMethod)) {
			System.out.println("OK without proxy");
		}
		//With proxy

		HttpProxyServer proxy = new DefaultHttpProxyServer(PROXY_PORT, new HttpRequestFilter() {

			@Override
			public void filter(HttpRequest httpRequest) {
				System.out.println("Request went through proxy");
			}
		}, Collections.<String, HttpFilter> emptyMap());

		proxy.start();
		System.out.println("proxy started");
		HostConfiguration config = client.getHostConfiguration();
		config.setProxy("localhost", PROXY_PORT);

		if (200 == client.executeMethod(getMethod)) {
			System.out.println("OK though proxy");
		}

		proxy.stop();
		System.out.println("proxy stopped");
	}
}
