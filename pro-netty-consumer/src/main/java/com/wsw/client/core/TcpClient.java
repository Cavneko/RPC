package com.wsw.client.core;

import com.alibaba.fastjson.JSONObject;
import com.wsw.client.handler.SimpleClientHandler;
import com.wsw.client.param.ClientRequest;
import com.wsw.client.param.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TcpClient {
    static final Bootstrap b = new Bootstrap();
    static ChannelFuture f = null;
    static{
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        b.group(workerGroup); // (2)
        b.channel(NioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new SimpleClientHandler());
                ch.pipeline().addLast(new StringEncoder());
            }
        });
        String host = "localhost";
        int port = 8080;
        try {
            f = b.connect(host, port).sync(); // (5)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //注意：1.每一个请求都是同一个连接，并发问题
    //发送数据
    public static Response send(ClientRequest request) {
        //System.out.println("TCP:sending request");
        f.channel().writeAndFlush(JSONObject.toJSONString(request));
        f.channel().writeAndFlush("\r\n");
        //System.out.println("TCP:WAITING FOR RESULT");
        DefaultFuture df = new DefaultFuture(request);
        return df.get();
    }
}
