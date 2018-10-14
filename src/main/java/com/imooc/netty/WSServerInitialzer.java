package com.imooc.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSServerInitialzer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline  pipeline = ch.pipeline();
		

		pipeline.addLast("HttpServerCodec", new HttpServerCodec());
		//对大数据里进行支持		
		pipeline.addLast(new ChunkedWriteHandler());
		//对HttpMessage进行聚合，聚合成FullHttpRequest ，几乎在netty的编程中都会使用到此hanler
		pipeline.addLast(new HttpObjectAggregator(1024*64));
		
		// ====================以上是用于支持http协议==============================
		
		/**
		 * websocket 服务器处理的协议，用于指定给客户端连接访问的路由
		 * 本handler会帮你处理一些繁重的十五
		 * 会帮你处理握手动作 ： handshaking
		 * 对于wensocket来讲都是用frames进行传输的，不同的数据类型对应的frame也不同
		 */
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
		
		pipeline.addLast("customerHandler", new ChatHandler());
	}

}
