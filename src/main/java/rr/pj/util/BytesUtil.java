package rr.pj.util;

import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBufUtil;

import java.util.Arrays;

/**
 * @Author: pj
 * @Date: 17/12/23 下午2:21
 */

public class BytesUtil {
	public static void main(String[] args) {
		int i = 1234;
		byte[] a = intToBytesLittle(1234);
		byte[] b = int2Byte4(1234);
		System.err.println("a="+ ByteBufUtil.hexDump(a));
		System.err.println("b="+ ByteBufUtil.hexDump(b));


	}

	public static byte[] int2Byte(int x) {
		//
		return new byte[]{Ints.toByteArray(x)[3]};
	}

	public static byte[] int2Byte2(int x) {
		//
		return new byte[]{Ints.toByteArray(x)[2], Ints.toByteArray(x)[3]};
	}

	public static byte[] int2Byte3(int x) {
		//
		return new byte[]{Ints.toByteArray(x)[1],Ints.toByteArray(x)[2], Ints.toByteArray(x)[3]};
	}
	public static byte[] int2Byte4(int x) {
		//
		return new byte[]{Ints.toByteArray(x)[1],Ints.toByteArray(x)[1],Ints.toByteArray(x)[2], Ints.toByteArray(x)[3]};
	}


	public static byte[] splitByteByIndexAndLen(byte[] b, int fromIdx, int len) {
		//
		return Arrays.copyOfRange(b, fromIdx, fromIdx + len);
	}

	public static int bytes2int(byte[] b) {
		if (b.length==3){
			return Ints.fromBytes((byte)0,b[0],b[1],b[2]);
		} else if (b.length == 2) {
			return Ints.fromBytes((byte)0,(byte)0,b[0],b[1]);
		}else if(b.length==1){
			return Ints.fromBytes((byte)0,(byte)0,(byte)0,b[0]);
		}
		return Ints.fromByteArray(b);
	}

	public static int bytes2int(byte b) {
		//
		return b&0xff;
	}


	/**
	 * 以小端模式将int转成byte[]
	 *
	 * @param value
	 * @return
	 */
	public static byte[] intToBytesLittle(int value) {
		byte[] src = new byte[4];
		src[3] = (byte) ((value >> 24) & 0xFF);
		src[2] = (byte) ((value >> 16) & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}


	/**
	 * 以小端模式将byte[]转成int
	 */
	public static int bytesToIntLittle(byte[] src, int offset) {
		int value;
		value = ((src[offset] & 0xFF)
				| ((src[offset + 1] & 0xFF) << 8)
				| ((src[offset + 2] & 0xFF) << 16)
				| ((src[offset + 3] & 0xFF) << 24));
		return value;
	}

}