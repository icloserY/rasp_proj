package server_test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client_test {
	SocketChannel socketChannel;
	
	void startClient() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					socketChannel = SocketChannel.open();
					socketChannel.configureBlocking(true);
					socketChannel.connect(new InetSocketAddress("220.66.115.136", 5001));
					System.out.println("[연결 완료 : " + socketChannel.getRemoteAddress() + "]");
				} catch (Exception e) {
					// TODO: handle exception
					if(socketChannel.isOpen()) {stopClient();}
					return;
				}
				receive();
			}
		};
		thread.start();
	}
	
	void stopClient() {
		try {
			System.out.println("[연결 끊음]");
			if(socketChannel != null && socketChannel.isOpen()) {
				socketChannel.close();
			}
		} catch(IOException e) {}
	}
	
	void receive() {
		while(true) {
			try {
				ByteBuffer byteBuffer = ByteBuffer.allocate(100);
				
				int readByteCount = socketChannel.read(byteBuffer);
				
				if(readByteCount == -1) {
					throw new IOException();
				}
				
				byteBuffer.flip();
				Charset charset = Charset.forName("UTF-8");
				String data = charset.decode(byteBuffer).toString();
				
				System.out.println("[받기완료] : " + data);
			} catch(Exception e) {
				System.out.println("[서버 통신 안됨]");
				stopClient();
				break;
			}
		}
	}
	
	void send(String data) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					Charset charset = Charset.forName("UTF-8");
					ByteBuffer byteBuffer = charset.encode(data);
					socketChannel.write(byteBuffer);
					System.out.println("[보내기 완료]");
				} catch(Exception e) {
					System.out.println("[서버 통신 안됨]");
					stopClient();
				}
			}
		};
		thread.start();
	}
	
}
