package central;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ConnectClient {
	SocketChannel socketChannel;
	ExecutorService executorService;
	List<ConnectClient> connections;
	
	public ConnectClient(SocketChannel socketChannel, ExecutorService executorService, List<ConnectClient> connections) {
		this.socketChannel = socketChannel;
		this.executorService = executorService;
		this.connections = connections;
		receive();
	}
	
	void receive() {
		executorService.submit(()->{
			while(true) {
				try {
					ByteBuffer byteBuffer = ByteBuffer.allocate(100);
					int readByte = socketChannel.read(byteBuffer);
					if(readByte == -1) {
						throw new IOException();
					}
					
					String message = "[요청 받음 : " + socketChannel.getRemoteAddress() + "]";
					System.out.println(message);
					
					byteBuffer.flip();
					Charset charset = Charset.forName("UTF-8");
					String data = charset.decode(byteBuffer).toString();
					
				}catch(Exception e) {
					connections.remove(ConnectClient.this);
					socketChannel.close();
				}
			}
		});
	}
	
	void send(String data) {}
}
