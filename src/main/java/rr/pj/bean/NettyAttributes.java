package rr.pj.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties(prefix = "netty.attributes")
@Data
@Primary
public class NettyAttributes {

    private int maxFrameLength;
    private int lengthFieldOffset;
    private int lengthFieldLength;
    private int lengthFieldEndOffset;
    private int lengthAdjustment;
    private int initialBytesToStrip;
    private boolean failFast;

    private long readerIdleTime;
    private long writerIdleTime;
    private long allIdleTime;

}
