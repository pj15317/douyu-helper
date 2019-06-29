package rr.pj.bean;

import com.alibaba.fastjson.JSONObject;
import com.google.common.primitives.Bytes;
import lombok.Data;
import rr.pj.util.BytesUtil;

/**
 * 斗鱼弹幕服务器第三方接入协议v1.6.2.pdf
 */
@Data
public class DouyuMessage {
    private int length;

    private Header header;
    private byte[] data;
    private int tail=0;



    public static DouyuMessage convert(byte[] msg_bytes) {
        DouyuMessage douyuMessage = new DouyuMessage();
        // length
        int length = msg_bytes.length;
        // header
        Header header = Header.convert(BytesUtil.splitByteByIndexAndLen(msg_bytes, 4, 8));

        douyuMessage.setLength(length);
        douyuMessage.setHeader(header);
        douyuMessage.setData(BytesUtil.splitByteByIndexAndLen(msg_bytes,12,length-12-1));
        douyuMessage.setTail(0);

        return douyuMessage;
    }

    public static byte[] convert(DouyuMessage msg) {
        byte[] data=msg.getData();
        byte[] length = BytesUtil.intToBytesLittle(4+4+data.length+1);
        byte[] header=Header.convert(msg.getHeader(),length);
        byte[] tail = new byte[]{0};

        return Bytes.concat(length,header,data,tail);
    }

    public static JSONObject getJSON(DouyuMessage douyu_msg) {
        String dy_resp = new String(douyu_msg.getData());

        String[] dy_resp_split = dy_resp.split("/");
        String kv[] = null;
        try {
            JSONObject jo = new JSONObject();
            for (int i = 0; i <dy_resp_split.length ; i++) {

                kv = dy_resp_split[i].split("@=");
                if (kv.length == 2) {
                    jo.put(kv[0],kv[1]);
                }


            }
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}


