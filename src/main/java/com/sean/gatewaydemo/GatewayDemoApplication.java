package com.sean.gatewaydemo;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayDemoApplication implements CommandLineRunner {
	private SocketIOServer server;
	public static void main(String[] args){
		SpringApplication.run(GatewayDemoApplication.class,args);
	}
	@Bean
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		config.setPort(8083);	//Netty-Socketio服务器端口
		this.server = new SocketIOServer(config);
		return server;
	}

	@Bean
	public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
		return new SpringAnnotationScanner(socketServer);
	}

	@Override
	public void run(String... args) throws Exception {
		server.start();
	}
}
