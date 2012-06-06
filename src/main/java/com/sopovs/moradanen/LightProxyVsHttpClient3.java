package com.sopovs.moradanen;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.GetMethod;

import com.sopovs.moradanen.proxylight.ProxyLight;
import com.sopovs.moradanen.proxylight.Request;
import com.sopovs.moradanen.proxylight.RequestFilter;

public class LightProxyVsHttpClient3 {
	private static final int PROXY_PORT = 10200;
	private static final String FAKE_ADDRESS = "local-google";
	private static final String REAL_ADDRESS = "www.google.com.ua";

	public static void main(String[] args) throws Exception {

		HttpClient client = new HttpClient();
		client.getParams().setVersion(HttpVersion.HTTP_1_0);
		GetMethod getMethod = new GetMethod("http://" + FAKE_ADDRESS);

		ProxyLight proxyLight = new ProxyLight();
		proxyLight.setPort(PROXY_PORT);
		proxyLight.getRequestFilters().add(new RequestFilter() {

			@Override
			public boolean filter(Request request) {
				request.setHost(request.getHost().replace(FAKE_ADDRESS, REAL_ADDRESS));
				request.setStatusline(request.getStatusline().replace(FAKE_ADDRESS, REAL_ADDRESS));
				request.getHeaders().put("Host", request.getHeaders().get("Host").replace(FAKE_ADDRESS, REAL_ADDRESS));
				return false;
			}
		});
		proxyLight.start();
		System.out.println("proxy started");
		//Start method returns before proxy really starts
		Thread.sleep(100L);

		HostConfiguration config = client.getHostConfiguration();
		config.setProxy("localhost", PROXY_PORT);

		if (200 == client.executeMethod(getMethod)) {
			System.out.println("OK though proxy");
		}

		proxyLight.stop();
		System.out.println("proxy stopped");
	}
}
