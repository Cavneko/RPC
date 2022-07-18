package com.wsw.client.core;

import com.alibaba.fastjson.JSONObject;
import com.wsw.client.constant.Constants;
import com.wsw.client.handler.SimpleClientHandler;
import com.wsw.client.param.ClientRequest;
import com.wsw.client.param.Response;
import com.wsw.client.zk.ZookeeperFactory;
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
import org.apache.curator.framework.CuratorFramework;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        CuratorFramework client = ZookeeperFactory.create();
        String host = "localhost";
        int port = 8080;
        try {
            List<String> serverPaths = client.getChildren().forPath(Constants.SERVER_PATH);
            Set<String> realServerPath = new HashSet<String>();
            for (String serverPath : serverPaths) {
                realServerPath.add(serverPath.split("#")[0]);
            }
            if (realServerPath.size() > 0) {
                host = realServerPath.toArray()[0].toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


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
