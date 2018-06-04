package server_test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server_test {
	ExecutorService executorService;
	ServerSocketChannel serverSocketChannel;
	List<Client> connections = new Vector<Client>();
	
	void startServer() {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(true);
			serverSocketChannel.bind(new InetSocketAddress(5001));
		} catch(Exception e) {
			if(serverSocketChannel.isOpen()) { stopServer(); }
			return;
		}
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("[서버 시작]");
				while(true) {
					try {
						SocketChannel socketChannel = serverSocketChannel.accept();
						String message = "[연결 수락 :" + socketChannel.getRemoteAddress() +
												": " + Thread.currentThread().getName() + "]";
						System.out.println(message);
						
						Client client = new Client(socketChannel);
						connections.add(client);
						System.out.println("[연결 개수 :" + connections.size() + "]");
					} catch(Exception e) {
						if(serverSocketChannel.isOpen()) {stopServer();}
						break;
					}
				}
			}
		};
		executorService.submit(runnable);
	}
	void stopServer() {
		try {
			Iterator<Client> iterator = connections.iterator();
			while(iterator.hasNext()) {
				Client client = iterator.next();
				client.socketChannel.close();
				iterator.remove();
			}
			if(serverSocketChannel != null && serverSocketChannel.isOpen()) {
				serverSocketChannel.close();
			}
			if(executorService != null && executorService.isShutdown()) {
				executorService.shutdown();
			}
			System.out.println("[서버 멈춤]");
		} catch (Exception e) {}
	}
	
	class Client {
		SocketChannel socketChannel;
		public Client(SocketChannel socketChannel) {
			this.socketChannel = socketChannel;
			receive();
		}
		
		void receive() {} {
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(true) {
						try {
							ByteBuffer byteBuffer = ByteBuffer.allocate(100);
							
							int readByteCount = socketChannel.read(byteBuffer);
							
							if(readByteCount == -1) {
								throw new IOException();
							}
							
							String message = "[요청 처리 : " + socketChannel.getRemoteAddress() + 
													": " + Thread.currentThread().getName() + "]";
							System.out.println(message);
							
							byteBuffer.flip();
							Charset charset = Charset.forName("UTF-8");
							String data = charset.decode(byteBuffer).toString();
							
							for(Client client : connections) {
								client.send(data);
							}
						} catch(Exception e) {
							try {
								connections.remove(Client.this);
								String message = "[클라이언트 통신 안됨 : " +
													socketChannel.getRemoteAddress() + ": " +
													Thread.currentThread().getName() + "]";
								System.out.println(message);
								socketChannel.close();
							} catch (Exception e2) {
								break;
							}
						}
					}
				}
			};
			executorService.submit(runnable);
		}
		
		void send(String data) {
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Charset charset = Charset.forName("UTF-8");
						ByteBuffer byteBuffer = charset.encode(data);
						socketChannel.write(byteBuffer);
					} catch (Exception e) {
						// TODO: handle exception
						try {
							String message = "[클라이언트 통신 안됨 : " +
									socketChannel.getRemoteAddress() + ": " +
									Thread.currentThread().getName() + "]";
							System.out.println(message);
						} catch(IOException e2) {}
					}
				}
			};
			executorService.submit(runnable);
		}
		
	}
	
}
