package com.yhz.moudle.helloworld;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Auther: yanghz
 * @Date: 2018/11/29 14:12
 * @Description:客户端
 */
public class Client {
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group=new NioEventLoopGroup();
        try {
            //创建Bootstrap
            Bootstrap b=new Bootstrap();
            b.group(group)
                    //指定 EventLoopGroup 以处理客户端事件；需要适用于 NIO 的实现适用于 NIO 传输的Channel 类型
                    .channel(NioSocketChannel.class)
                    //设置服务器的InetSocketAddress
                    .remoteAddress(new InetSocketAddress(host,port))
                    //在创建Channel时，向 ChannelPipeline中添加一个 ClientHandler 实例
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });
            //连接到远程节点， 阻塞等待直到连接完成
            ChannelFuture f=b.connect().sync();
            //阻塞， 直到Channel 关闭
            f.channel().closeFuture().sync();
        }finally {
            //关闭线程池并且释放所有的资源
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Client("127.0.0.1",2048).start();
    }
}
