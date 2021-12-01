package com.uniphore.ri.main.e2e;

public class HostAndPort {
	
	

	public HostAndPort(String host, String port) {
		super();
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String host;
	public String port;
}
