package nettyclient;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPipeline;

public class ReConnectionListener implements ChannelFutureListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReConnectionListener.class);
    private Connection connection;

    public ReConnectionListener(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        Channel channel = channelFuture.channel();
        if (channelFuture.isSuccess()) {
            LOGGER.info("重连接成功: {}", channel);
            //重新绑定channel
            connection.bind(channel);
        } else {
            connection.addRetryCount();
            if (connection.getCount() < Connection.DEFAULT_RECONNECT_TRY) {
                ChannelPipeline channelPipeline = channel.pipeline();
                channelPipeline.fireChannelInactive();
            } else {
                LOGGER.info("重连失败，且已经达到最大重试次数:{},不再进行重试!", Connection.DEFAULT_RECONNECT_TRY);
                connection.unbind();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
