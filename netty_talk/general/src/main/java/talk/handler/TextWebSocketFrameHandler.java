package talk.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Program: netty_talk
 * @Description
 * @Author XieFeng
 * @Create 2020-12-14-17-29
 **/


public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        var incoming = ctx.channel();
        group.forEach(ch -> {
            if (ch != incoming) {
                ch.writeAndFlush(new TextWebSocketFrame("[" + incoming.remoteAddress() + "]:" + msg.text()));
            } else {
                ch.writeAndFlush(new TextWebSocketFrame("[you]:" + msg.text()));
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        var incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress() + "异常");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        var incoming = ctx.channel();
        System.out.println(incoming.remoteAddress() + "在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        var incoming = ctx.channel();
        System.out.println(incoming.remoteAddress() + "掉线");
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        var incoming = ctx.channel();
        group.add(incoming);
        group.forEach(ch -> {
            if (ch != incoming) {
                ch.writeAndFlush(new TextWebSocketFrame(incoming.remoteAddress() + ":加入聊天"));
            }
        });
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        var incoming = ctx.channel();
        group.remove(incoming);
        group.forEach((ch -> {
            if (ch != incoming) {
                ch.writeAndFlush(new TextWebSocketFrame(incoming.remoteAddress() + ":退出聊天"));
            }
        }));
    }
}
