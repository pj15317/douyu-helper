package rr.pj.bean;

import com.google.common.primitives.Bytes;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import rr.pj.util.BytesUtil;

@Data
@Getter
@Setter
public class Header {
    private int length;
    private MsgType msgType;
    private int encrypt;
    private int reserved;

    public enum MsgType {
        to_danmu_server_689,
        to_danmu_client_690,
    }

    public static byte[] convert(Header header,byte[] length) {
        if (header.getMsgType().equals(MsgType.to_danmu_server_689)) {
            return Bytes.concat(length, BytesUtil.intToBytesLittle(689));

        } else if (header.getMsgType().equals(MsgType.to_danmu_client_690)) {
            return Bytes.concat(length, BytesUtil.intToBytesLittle(690));
        }
        throw new RuntimeException("unknown msgType");
    }

    public static Header convert(byte[] header_bytes) {
        Header header = new Header();

        // length
        int length = BytesUtil.bytesToIntLittle(BytesUtil.splitByteByIndexAndLen(header_bytes, 0, 4),0);
        // msgType
        MsgType msgType=convertMsgType(BytesUtil.splitByteByIndexAndLen(header_bytes,4,4));
        // encrypt
        int encrypt=0;
        // reserved
        int reserved=0;

        header.setLength(length);
        header.setMsgType(msgType);
        header.setEncrypt(encrypt);
        header.setReserved(reserved);
        return header;
    }

    private static byte[] convertMsgType(MsgType msgType) {
        switch (msgType) {
            case to_danmu_server_689:
                return BytesUtil.intToBytesLittle(689);
            case to_danmu_client_690:
                return BytesUtil.intToBytesLittle(690);
            default:
                throw new RuntimeException("unknown msgType");
        }
    }

    private static MsgType convertMsgType(byte[] msgType_bytes) {

        int msgtype = BytesUtil.bytesToIntLittle(msgType_bytes,0);

        if (msgtype == 689) {
            return MsgType.to_danmu_client_690;
        } else if (msgtype == 690) {
            return MsgType.to_danmu_client_690;
        }
        throw new RuntimeException("unknown msgType");
    }
}
