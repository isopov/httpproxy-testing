package com.sopovs.moradanen;

import java.util.Collections;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.littleshoot.proxy.DefaultHttpProxyServer;
import org.littleshoot.proxy.HttpFilter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.HttpRequestFilter;

//TODO Proxy does not stop, see https://github.com/adamfisk/LittleProxy/issues/36
public class LittleProxyVsHttpClient4 {
	private static final int PROXY_PORT = 10200;

	public static void main(String args[]) throws Exception {
		HttpClient client = new DefaultHttpClient();

		HttpGet get = new HttpGet("http://www.google.com.ua");
		HttpResponse responce = client.execute(get);
		if (200 == responce.getStatusLine().getStatusCode()) {
			System.out.println("OK without proxy");
		}
		EntityUtils.consume(responce.getEntity());

		HttpProxyServer proxy = new DefaultHttpProxyServer(PROXY_PORT, new HttpRequestFilter() {

			@Override
			public void filter(HttpRequest httpRequest) {
				System.out.println("Request went through proxy");
			}
		}, Collections.<String, HttpFilter> emptyMap());

		proxy.start();
		System.out.println("proxy started");
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("localhost", PROXY_PORT));
		responce = client.execute(get);
		if (200 == responce.getStatusLine().getStatusCode()) {
			System.out.println("OK with proxy");
		}
		EntityUtils.consume(responce.getEntity());

		proxy.stop();
		System.out.println("proxy stopped");

		Thread.sleep(1000);

	}
}
