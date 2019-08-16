package nettyclient;

import java.util.ArrayList;
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

public class ClientChannelInitiallizer extends ChannelInitializer<SocketChannel>{
	private static ChannelHandler clientHandler = new ClientHandler();
    private ConnectionWatchDog connectionWatchDog;
    private MessageFilter messageFilter;

    public ClientChannelInitiallizer(ReConnectionListener reConnectionListener,List<Integer> white_list) {
        this.connectionWatchDog = new ConnectionWatchDog(reConnectionListener);
        this.messageFilter = new MessageFilter(white_list);        
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //
        pipeline.addLast(new IdleStateHandler(0,5,0,TimeUnit.SECONDS))
                .addLast(new LenPreMsgDecoder())
                .addLast(new LenPreMsgEncoder())
                .addLast(messageFilter)
                .addLast(connectionWatchDog)
                
                .addLast(clientHandler);
    }
}
