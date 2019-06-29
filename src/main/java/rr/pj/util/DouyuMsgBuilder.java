package rr.pj.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import rr.pj.bean.DouyuMessage;
import rr.pj.bean.Header;

import java.io.UnsupportedEncodingException;

public class DouyuMsgBuilder {

    /**
     * 登录请求消息: 该消息用于完成登陆授权<br/>
     * type@=loginreq/roomid@=58839/
     */
    public static byte[] build_loginreq(int roomid) {
        //
        String s = "type@=loginreq/roomid@={roomid}/".replaceAll("\\{roomid\\}",roomid+"");

//        String s = ("type@=loginreq/roomid@={roomid}/" +
//                "dfl@=sn@AA=105@ASss@AA=1/" +
//                "username@=visitor3515488/" +
//                "uid@=1396024416/" +
//                "ver@=20190530/" +
//                "aver@=218101901/" +
//                "ct@=0/")
//
//                .replaceAll("\\{roomid\\}",roomid+"");
        try {
            return s.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BinaryWebSocketFrame build_loginreq_frame(int roomid) {
        byte[] data = build_loginreq(roomid);

        ByteBuf buf = getByteBuf(data);
        return new BinaryWebSocketFrame(buf);
    }



    /**
     * 入组消息: 该消息用于完成加入房间分组<br/>
     * type@=joingroup/rid@=59872/gid@=-9999/
     */
    public static byte[] build_joingroup(int rid,int gid) {
        String s="type@=joingroup/rid@={rid}/gid@={gid}/";
        s = s.replaceAll("\\{rid\\}", rid+"");
        s = s.replaceAll("\\{gid\\}", gid+"");
        try {
            return s.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BinaryWebSocketFrame build_joingroup_frame(int rid,int gid) {
        byte[] data = build_joingroup(rid,gid);

        ByteBuf buf = getByteBuf(data);
        return new BinaryWebSocketFrame(buf);
    }

    /**
     * 该消息用于维持与后台间的心跳（新版）<br/>
     * type@=mrkl/
     */
    public static byte[] build_heartbeat() {
        String s="type@=mrkl/";
        try {
            return s.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BinaryWebSocketFrame build_heartbeat_frame() {
        byte[] data = build_heartbeat();

        ByteBuf buf = getByteBuf(data);
        return new BinaryWebSocketFrame(buf);
    }

    private static ByteBuf getByteBuf(byte[] data) {
        DouyuMessage douyumsg = new DouyuMessage();
        Header header = new Header();
        header.setLength(4 + 4 + data.length + 1);
        header.setMsgType(Header.MsgType.to_danmu_server_689);
        douyumsg.setHeader(header);
        douyumsg.setData(data);

        byte[] msg_bytes = DouyuMessage.convert(douyumsg);
        return Unpooled.copiedBuffer(msg_bytes);
    }


}
