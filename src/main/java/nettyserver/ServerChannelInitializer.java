package nettyserver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import codec.LenPreMsgDecoder;
import codec.LenPreMsgEncoder;
import codec.MessageFilter;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import nettyclient.ClientHandler;
import nettyclient.ConnectionWatchDog;
import nettyclient.ReConnectionListener;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	private static ChannelHandler serverhandler = new ServerHandler();
    private MessageFilter messageFilter;
    private ServerConnectionHandler serverConnectionHandler;

    public ServerChannelInitializer(List<Integer> list) {
    	this.serverConnectionHandler = new ServerConnectionHandler();
    	this.messageFilter = new MessageFilter(list);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //
        pipeline.addLast(new IdleStateHandler(10,0,0,TimeUnit.SECONDS))
                .addLast(new LenPreMsgDecoder())
                .addLast(new LenPreMsgEncoder())
                .addLast(messageFilter)
                .addLast(serverConnectionHandler)
                
                .addLast(serverhandler);

    }
}
