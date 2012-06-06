package com.sopovs.moradanen.proxylight;

import java.nio.channels.SocketChannel;

/**
 * Based on the code from http://code.google.com/p/proxoid/source/browse/trunk/#trunk%2Fproxylight
 * 
 */
public class Socket {

	public SocketChannel socket = null;
	public long created = System.currentTimeMillis();
	public long lastWrite = created;
	public long lastRead = created;
}