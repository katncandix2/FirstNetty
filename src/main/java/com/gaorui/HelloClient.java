package com.gaorui;

import com.gaorui.pojo.Command;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by gaorui on 17/2/12.
 */
public class HelloClient {

    public static void main(String args[]) {
        // Client服务启动器
        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));
        // 设置一个处理服务端消息和各种消息事件的类(Handler)
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
//            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new ObjectEncoder(),
                       new  HelloClientHandler());
            }
        });
        // 连接到本地的8000端口的服务端
        bootstrap.connect(new InetSocketAddress(
                "127.0.0.1", 8000));
    }

    private static class HelloClientHandler extends SimpleChannelHandler {


        /**
         * 当绑定到服务端的时候触发，打印"Hello world, I'm client."
         */
        @Override
        public void channelConnected(ChannelHandlerContext ctx,
                                     ChannelStateEvent e) {

            sendObject(e.getChannel());
            System.out.println("Hello world, I'm client.");
        }

        private void sendObject(Channel channel) {
            Command command = new Command();
            command.setActionName("Hello action.");
            channel.write(command);
        }
    }
}
