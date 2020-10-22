package com.kanq.demo.controller;

import com.kanq.demo.utils.RedisUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用Netty客户端连接MQTT数据中转服务器
 *
 * @author yyc
 */
@Component
public class NioMqttClient {

    /**
     * 创建EventLoopGroup
     */
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    /**
     * 创建并配置Bootstrap
     */
    private final Bootstrap bootstrap = new Bootstrap();
    private Channel channel;
    boolean flag = true;

    @Value("${netServer.ip}")
    private String ip;
    @Value("${netServer.port}")
    private Integer port;

    private static final Logger LOGGER = LoggerFactory.getLogger(NioMqttClient.class);

    @Resource
    private RedisUtil redisCache;

    @PostConstruct
    public void init() {
        start();

        //通过ThreadPoolExecutor创建线程池
        new ThreadPoolExecutor(
                1, 1, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                new ThreadPoolExecutor.DiscardOldestPolicy())
         // 执行数据解析线程
        .execute(this::run);
    }

    /**
     * 启动客户端
     */
    public void start() {
        //配置
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                // channel的设置,关闭延迟发送
                .option(ChannelOption.TCP_NODELAY, true)
                //标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。
//                .option(ChannelOption.SO_BACKLOG, 1024)
                //启用心跳保活机制)
//                .option(ChannelOption.SO_KEEPALIVE, true)

                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
                        // 基于行的帧解码器
                        pipeline.addLast(new LineBasedFrameDecoder(1024 * 10));
                        // 字符串编解码器
                        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                        // 客户端的逻辑处理
                        pipeline.addLast(new NioMqttClientHandler());
                    }
                });
        //连接
        doConnect();

    }

    /**
     * 获取redis中的缓存数据并解析入库
     */
    private void run() {
        while (true) {
            try {
                List<String> mqttDataCache = redisCache.rightPopList("mqttDataCache");
                //缓存数据集为空,休眠1秒
                if (CollectionUtils.isEmpty(mqttDataCache)) {
                    Thread.sleep(1000);
                    continue;
                }
                for (String mess : mqttDataCache) {
                    LOGGER.info("获取redis中的数据格式为:[{}]", mess);
                }
            } catch (InterruptedException e) {
                LOGGER.error("线程休眠异常= {}", e.getMessage());
            } catch (Exception e) {
                LOGGER.error("数据解析异常= {}", e.getMessage());
            }
        }
    }
    /**
     * 连接服务端 and 重连
     */
    public void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture connect = bootstrap.connect(ip, port);
        //实现监听通道连接的方法
        connect.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                channel = channelFuture.channel();
                LOGGER.info("连接成功");
            } else {
                if (flag) {
                    LOGGER.info("每隔2s重连....");
                    channelFuture.channel().eventLoop().schedule(this::doConnect, 2, TimeUnit.SECONDS);
                }
            }
        });
    }

    /**
     * 重写SocketChannel的处理器
     */
    private class NioMqttClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            //通过ThreadPoolExecutor创建线程池
            new ThreadPoolExecutor(
                    1, 1, 10, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(1),
                    new ThreadPoolExecutor.DiscardOldestPolicy())
                    .execute(() -> {
                        // 心跳保活线程
                        while (true) {
                            LOGGER.info("==========开始发送心跳包==========");
                            try {
                                ctx.channel().writeAndFlush(Unpooled.copiedBuffer("FRONT GET/MQTT DATA/\r\n".getBytes()));
                                Thread.sleep(1000 * 59);
                            } catch (InterruptedException e) {
                                LOGGER.error(e.getMessage());
                            }
                        }
                    });
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            LOGGER.info("接收到MQTT服务端发来的消息:[{}]" ,msg);
            // 存入缓存队列
            redisCache.leftPushList("mqttDataCache", msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            LOGGER.error("Client,exceptionCaught: {}", cause.getMessage());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            LOGGER.warn("Client,channelInactive");
            doConnect();
        }

    }
}
