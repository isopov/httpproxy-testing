package com.sopovs.moradanen;

import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpClientCodec;

//TODO Client not finished
public class LittleProxyVsNettyClient {
	public static void main(String[] args) {

		ClientBootstrap client = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		client.setPipelineFactory(new ChannelPipelineFactory() {

			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();

				pipeline.addLast("codec", new HttpClientCodec());

				pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));

				pipeline.addLast("handler", new SimpleChannelUpstreamHandler() {
					@Override
					public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
						e.getChannel().close().awaitUninterruptibly();
						System.out.println("Channel closed");
						super.messageReceived(ctx, e);
					}
				});
				return pipeline;
			}
		});

//		client.connect(remoteAddress)
	}
}
