package com.my.study.spring_netty;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;

public class WebSocketServer implements IWebSocketServer {

	private ServerBootstrap bootstrap;
	
	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private boolean needSSL;
	
	
	public void init() {
		bootstrap = new ServerBootstrap()
				.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.SO_TIMEOUT, 60000)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						if (needSSL) {
							SSLEngine sslEngine = SSLContext.getDefault().createSSLEngine();
							pipeline.addLast("ssl", new SslHandler(sslEngine));
						}
						pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
						pipeline.addLast(new WebSocketRequestHandler());
					}
				});
	}

	public void startup() {
		init();
		try {
			ChannelFuture channelFuture = bootstrap.bind("127.0.0.1", 80).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	} 

	public void stop() {
		
	}

	public void destory() {
		
	}
	
	
	
	
}
