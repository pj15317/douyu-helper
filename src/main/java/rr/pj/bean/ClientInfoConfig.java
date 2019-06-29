package rr.pj.bean;

import io.netty.channel.Channel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: pj
 * @Date: 18/1/9 下午2:27
 */
@Component
@ConfigurationProperties(prefix = "tcp.client.config")
@Primary
public class ClientInfoConfig {
	private List<ClientInfo> client = new ArrayList<>();

	public List<ClientInfo> getClient() {
		return this.client;
	}

	public void setClient(List<ClientInfo> client) {
		this.client = client;
	}

	@Data
	public static class ClientInfo {

		private String romoteAddress;

		private int tcpPort;

		private String roomId;

		private boolean authPass;

		private Channel channel;
		
	}
}

