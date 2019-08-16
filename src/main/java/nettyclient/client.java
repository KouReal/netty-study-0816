package nettyclient;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import protocol.Header;
import protocol.LenPreMsg;

import static java.util.Arrays.asList;

@Component("client")
public class client{
	private static final Logger LOGGER = LoggerFactory.getLogger(client.class);
    /**
     * writeAndFlush（）实际是提交一个task到EventLoopGroup，所以channel是可复用的
     */
    private  List<Integer> protocol_whitelist = asList(Header.reg_discover,Header.rpc_response);
    /**
     * 配置客户端 NIO 线程组
     */
    private  EventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
    /**
     * 创建并初始化 Netty 客户端 Bootstrap 对象
     */
    private  Connection connection;
    private  Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
            //禁用nagle算法
            .option(ChannelOption.TCP_NODELAY, true);

    


    @PostConstruct
    private void init() {
        connection = new Connection("127.0.0.1",9800, bootstrap);
        ReConnectionListener reConnectionListener = new ReConnectionListener(connection);
        bootstrap.handler(new ClientChannelInitiallizer(reConnectionListener,protocol_whitelist ));
        ChannelFuture future = bootstrap.connect("127.0.0.1",9800);
		connection.bind(future.channel());
        future.awaitUninterruptibly();
		try {
			future.sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    public  void sendtoserver(LenPreMsg msg) throws Exception {

        Channel channel = getChannel();
        channel.writeAndFlush(msg);
        LOGGER.info("client send msg:{} with channel:{}",msg,channel);
 
        
    }

    public  Channel getChannel() throws Exception {
        return connection.get();
    }

}