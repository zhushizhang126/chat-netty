package com.imooc.netty;

import java.time.LocalDateTime;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
/*
 * 处理消息的handler
 * TextWebSocketFrame：在netty中，是用于websocket处理文本
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{

	private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,
			TextWebSocketFrame msg) throws Exception {
		// 获取客户端发送来的消息
		String content = msg.text();
		System.out.println("接收到的数据：" + content);
		for (Channel channel : clients) {
			channel.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息：]" + LocalDateTime.now() + "接收到消息,消息为："+content));
		}
		//clients.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息：]" + LocalDateTime.now() + "接收到消息,消息为："+content));
	}
	/**
	 * 当客户端连接服务端之后
	 * 获取客户端的channel放到group中去进行管理
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		clients.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		//client.remove(ctx.channel());
		System.out.println("客户端断开，channel对应的长id为："+ctx.channel().id().asLongText());
		System.out.println("客户端断开，channel对应的短id为："+ctx.channel().id().asShortText());
	}

}