package com.yhz.moudle.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Auther: yanghz
 * @Date: 2018/11/29 14:12
 * @Description:服务器端
 */
public class Server {

    private final int port;
    public Server(int port){
        this.port=port;
    }

    public static void main(String[] args) throws InterruptedException {
        if(args.length!=1){
            System.out.println("Usage:"+Server.class.getSimpleName()+"<port>");
        }
        //设置端口值（如果端口参数的格式不正确，则抛出一个NumberFormatException）
        //调用服务器的start方法
        new Server(2048).start();

    }

    public void start() throws InterruptedException {
        final ServerHandler serverHandler=new ServerHandler();
        //创建EventLoopGroup
        EventLoopGroup group=new NioEventLoopGroup();
        try{
            //创建ServerBootstrap
            ServerBootstrap b=new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)//指定所使用的NIO传输Channel
                    .localAddress(new InetSocketAddress(port))//使用指定的端口设置套接字地址
                    //添加一个ServerHandler到子Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //EchoServerHandler 被标注为@Shareable，所以我们可以总是使用同样的实例
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });
            //异步地绑定服务器；调用 sync()方法阻塞等待直到绑定完成
            ChannelFuture f=b.bind().sync();
            //获取 Channel 的CloseFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        }finally {
            //关闭 EventLoopGroup， 程直到它完成释放所有的资源
            group.shutdownGracefully().sync();
        }
    }
}
