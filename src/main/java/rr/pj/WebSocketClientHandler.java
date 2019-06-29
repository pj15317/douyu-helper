package rr.pj;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import rr.pj.bean.DouyuMessage;
import rr.pj.util.DouyuMsgBuilder;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    private static int roomid = 5324559;


    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("WebSocket Client active");
        //
        handshaker.handshake(ctx.channel());


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("WebSocket Client disconnected!");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                System.out.println("WebSocket Client connected!");
                handshakeFuture.setSuccess();

                // 登录请求消息
                BinaryWebSocketFrame loginreq_msg_frame = DouyuMsgBuilder.build_loginreq_frame(roomid);

                ctx.writeAndFlush(loginreq_msg_frame);
            } catch (WebSocketHandshakeException e) {
                System.out.println("WebSocket Client failed to connect");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }


        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            System.out.println("WebSocket Client received message: " + textFrame.text());
        } else if (frame instanceof PongWebSocketFrame) {
            System.out.println("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("WebSocket Client received closing");
            ch.close();
        } else if (frame instanceof BinaryWebSocketFrame) {
            byte[] douyu_msg_byte = new byte[frame.content().readableBytes()];
            frame.content().readBytes(douyu_msg_byte);
            DouyuMessage douyu_msg = DouyuMessage.convert(douyu_msg_byte);
            System.err.println(douyu_msg.toString());
            process_douyumsg(ctx,douyu_msg);

        }



    }

    private void process_douyumsg(ChannelHandlerContext ctx,DouyuMessage douyu_msg) throws InterruptedException {
        JSONObject douyu_msg_data = DouyuMessage.getJSON(douyu_msg);
        System.err.println(douyu_msg_data.toJSONString());

        // loginres
        if (douyu_msg_data.getString("type").equals("loginres")) {
            System.err.println("receive loginres");

            BinaryWebSocketFrame joingroup_msg_frame = DouyuMsgBuilder.build_joingroup_frame(roomid, 1);

            ctx.writeAndFlush(joingroup_msg_frame).sync();

        }else if(douyu_msg_data.getString("type").equals("chatmsg")) {
            System.err.println("--------  chatmsg ------------");

            String txt = douyu_msg_data.getString("txt");
            String nn = douyu_msg_data.getString("nn");

            System.err.println(nn+": "+txt);
            System.err.println("--------  chatmsg ------------");
        }



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }

    private void send_heartbeat(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(DouyuMsgBuilder.build_heartbeat_frame());
    }



    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {

            IdleStateEvent userEvt = (IdleStateEvent) evt;
            if (userEvt.state().compareTo(IdleState.ALL_IDLE) == 0) {

            } else if (userEvt.state().compareTo(IdleState.READER_IDLE) == 0) {
                send_heartbeat(ctx);

            } else if (userEvt.state().compareTo(IdleState.WRITER_IDLE) == 0) {

            }

        }
    }
}