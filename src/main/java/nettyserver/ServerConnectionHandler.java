package nettyserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import protocol.Header;
import protocol.LenPreMsg;

@ChannelHandler.Sharable
public class ServerConnectionHandler extends SimpleChannelInboundHandler<LenPreMsg> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnectionHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LenPreMsg msg) throws Exception {
        //Header header = message.getHeader();
        //若是心跳请求则直接返回，否则交给下一handler处理
    	if(msg.getProtocol() == Header.heart_beat){
    		LOGGER.info("收到心跳:{}",msg);
    	}else{
    		ctx.fireChannelRead(msg);
    	}
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state()==IdleState.READER_IDLE){
            	LOGGER.info("not receive heartbeat timeout, will close link");
                ctx.close();
            }
            
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}