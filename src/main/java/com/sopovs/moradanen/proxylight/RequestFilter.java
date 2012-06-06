package com.sopovs.moradanen.proxylight;

/**
 * Based on the code from http://code.google.com/p/proxoid/source/browse/trunk/#trunk%2Fproxylight
 * 
 */
public interface RequestFilter {

	/**
	 * Retourne true si le proxy ne doit meme pas repondre ...
	 */
	public boolean filter(Request request);
}