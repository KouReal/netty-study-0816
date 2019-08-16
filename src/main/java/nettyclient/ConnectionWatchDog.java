package nettyclient;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import protocol.Header;
import protocol.LenPreMsg;

@ChannelHandler.Sharable
public class ConnectionWatchDog extends SimpleChannelInboundHandler<LenPreMsg> implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionWatchDog.class);
    private ReConnectionListener listener;

    public ConnectionWatchDog(ReConnectionListener reConnectionListener) {
        this.listener = reConnectionListener;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LenPreMsg msg) throws Exception {
    	if(msg.getProtocol() == Header.heart_beat){
    		LOGGER.info("收到心跳:{}",msg);
    	}else{
    		ctx.fireChannelRead(msg);
    	}
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	LOGGER.info("the channel:{} is active",ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //线程开启定时任务，准备尝试重连
    	LOGGER.info("the channel:{} is inactive",ctx.channel());
        ctx.channel().eventLoop().schedule(this, 3L, TimeUnit.SECONDS);
        ctx.fireChannelInactive();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state()==IdleState.WRITER_IDLE){
            	LenPreMsg msg = new LenPreMsg(Header.heart_beat, 0, null);
                ctx.writeAndFlush(msg);
                LOGGER.info("发送心跳包，msg：{}", msg);
            }
            
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void run() {
        Connection connection = listener.getConnection();
        Bootstrap bootstrap = connection.getBootstrap();
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Connection.DEFAULT_CONNECT_TIMEOUT);
        ChannelFuture future = bootstrap.connect(connection.getTargetIP(), connection.getTargetPort());
        //不能在EventLoop中进行同步调用，这样会导致调用线程即EventLoop阻塞
        future.addListener(listener);
    }

}
