package nettyserver;

import java.util.List;

import javax.annotation.PostConstruct;

import static java.util.Arrays.asList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import protocol.Header;

@Component("server")
public class NettyServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

;

    /**
     * Netty 的连接线程池
     */
    private  EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private  List<Integer> whitelist = asList(Header.heart_beat,Header.reg_addservice,Header.reg_discover,Header.rpc_request);
    /**
     * Netty 的Task执行线程池
     */
    private  EventLoopGroup workerGroup = new NioEventLoopGroup(4);
    
    private  ServerBootstrap serverBootstrap = new ServerBootstrap()
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childHandler(new ServerChannelInitializer(whitelist));
 
    @PostConstruct
    public void init(){
    	try {
            //绑定对应ip和端口，同步等待成功
            ChannelFuture future = serverBootstrap.bind(9800).sync();
            LOGGER.info("server 已启动，端口：{}", 9800);
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException i) {
            LOGGER.error("server 出现异常，端口：{}, cause:", 9800, i.getMessage());
        } finally {
            //优雅退出，释放 NIO 线程组
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    
}
