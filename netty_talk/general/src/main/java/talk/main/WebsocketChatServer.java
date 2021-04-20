package talk.main;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import talk.handler.WebsocketChatServerInitializer;

/**
 * @Program: netty_talk
 * @Description
 * @Author XieFeng
 * @Create 2020-12-14-18-00
 **/


public class WebsocketChatServer {
    private int port;

    public WebsocketChatServer(int port) {
        this.port = port;
    }

    public void run() {
        var bossGroup = new NioEventLoopGroup();
        var workGroup = new NioEventLoopGroup();

        var serverBootStrap = new ServerBootstrap();
        serverBootStrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebsocketChatServerInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            var cf = serverBootStrap.bind(port).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

            System.out.println("WebsocketChatServer 关闭了");
        }
    }

    public static void main(String[] args) {
        new WebsocketChatServer(8080).run();
    }
}
