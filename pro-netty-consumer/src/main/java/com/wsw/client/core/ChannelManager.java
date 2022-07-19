package com.wsw.client.core;

import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelFuture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ChannelManager {
    static CopyOnWriteArrayList<String> realServerPath = new CopyOnWriteArrayList<String>();
    static AtomicInteger position = new AtomicInteger();
    public static CopyOnWriteArrayList<ChannelFuture> channelFutures = new CopyOnWriteArrayList<>();


    public static void removeChannel(ChannelFuture channel) {
        channelFutures.remove(channel);
    }

    public static void add(ChannelFuture channel) {
        channelFutures.add(channel);
    }

    public static void clear() {
        channelFutures.clear();
    }

    public static ChannelFuture get(AtomicInteger i) {
        int size = channelFutures.size();
        ChannelFuture channel = null;
        if (i.get()> size) {
            channel = channelFutures.get(0);
            ChannelManager.position = new AtomicInteger(1);
        } else {
            channel = channelFutures.get(i.getAndIncrement());
        }

        if (!channel.channel().isActive()) {
            channelFutures.remove(channel);
            return get(position);
        }

        return channel;
    }
}
