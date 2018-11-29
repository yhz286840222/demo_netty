package com.yhz.moudle.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Auther: yanghz
 * @Date: 2018/11/29 14:13
 * @Description:服务器端事件处理器
 */
//标识一个ChannelHandler可以被多个Channel共享
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    //数据处理
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        //输出接受到客户端的消息
        String requestData=in.toString(CharsetUtil.UTF_8);
        System.out.println("Server received:"+requestData);
        String responseData="Server response:"+requestData;
        //将服务器响应消息写给发送者，而不冲刷出站消息
        ctx.write(Unpooled.copiedBuffer(responseData.getBytes()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将未决消息冲刷到远程节点，并且关闭该Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常栈跟踪
        cause.printStackTrace();
        //关闭Channel
        ctx.close();
    }
}
