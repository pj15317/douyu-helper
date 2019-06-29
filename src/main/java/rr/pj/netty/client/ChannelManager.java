package rr.pj.netty.client;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;

public class ChannelManager {
    // all channel group
    @Getter
    private static final ChannelGroup client_channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    // all server group
    @Getter
    private static final ChannelGroup rpc_channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


}

