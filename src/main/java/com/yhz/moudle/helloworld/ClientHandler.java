package com.yhz.moudle.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Auther: yanghz
 * @Date: 2018/11/29 14:13
 * @Description:客户端事件处理器
 */
//标记该类的实例可以被多个 Channel 共享
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    //接受服务器传输回来的数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Client received: "+byteBuf.toString(CharsetUtil.UTF_8));
    }

    //连接成功通知，发送数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知 Channel是活跃的时候，发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!",CharsetUtil.UTF_8));
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty jacks!",CharsetUtil.UTF_8));
    }

    //发生异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
