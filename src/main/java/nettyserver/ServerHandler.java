package nettyserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.Header;
import protocol.LenPreMsg;
import protocol.RpcResponse;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<LenPreMsg> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);

    public ServerHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LenPreMsg msg) throws Exception {
    	LOGGER.info("server get msg:{}",msg);
    	RpcResponse rpcResponse = new RpcResponse("requestid", RpcResponse.RESOURCE, "errormsg", "content");
    	LenPreMsg lenPreMsg = new LenPreMsg(Header.rpc_response, 1, rpcResponse);
    	ctx.writeAndFlush(lenPreMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("api caught exception", cause);
        ctx.channel().close();
    }
}