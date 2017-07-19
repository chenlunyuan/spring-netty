package com.my.study.spring_netty;

import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class WebSocketRequestHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

	private ISessionService sessionService;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
		String websocketContent = msg.content().toString(Charset.forName("UTF-8"));
		
		WebSocketRequest webSocketRequest = JSON.parseObject(websocketContent, WebSocketRequest.class);
		
	}

}
