package central;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CentralServer {
	private static final CentralServer central = new CentralServer();
	//서버 소켓 채널
	private ServerSocketChannel serverSocketChannel;
	private SocketChannel librarySocketChannel;
	private ExecutorService executorService;
	//서버가 관리하는 클라이언트
	List<ConnectClient> connections = new Vector<>(); 
	//좌석
	private List<SeatStatusService> seats = new ArrayList<>();
	private ControlAirService controlAirService = new ControlAirService(seats);
	static float PROPER_TEMPERATURE = 23.0f;
	static float PROPER_HUMIDITY = 50.0f;
	
	private CentralServer() {
		seats.add(new SeatStatusService(1));
		seats.add(new SeatStatusService(2));
	} 
	
	public static CentralServer getInstance() {
		return central;
	}
	
	public void startCentral() {
		System.out.println("central 시작");
		//centralServer에 소켓 연결
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(true);
			serverSocketChannel.bind(new InetSocketAddress(5550));
			System.out.println("서버 대기중");
		} catch(Exception e) {
			if(serverSocketChannel.isOpen()) {
				e.printStackTrace();
				stopCentral();
			}
			return;
		}
		executorService.submit(()->{
			while(true) {
				try {
					System.out.println("연결 대기");
					SocketChannel socketChannel = serverSocketChannel.accept();
					String message = "[연결 수락 : " + socketChannel.getRemoteAddress() + "]";
					System.out.println(message);
					
					ConnectClient client = new ConnectClient(socketChannel, executorService, connections, central);
					connections.add(client);
					
					String value = "setEnv," + PROPER_TEMPERATURE + "," + PROPER_HUMIDITY;
					String libValue = "connLocal," + client.name;
					//local에 보내기
					client.send(value);
					//lib에 보내기
					send(libValue);
				}catch(Exception e) {
					if(serverSocketChannel.isOpen()) {stopCentral();}
					break;
				}
			}
		});
		//libraryMm에 소켓 연결
		try {
			librarySocketChannel = SocketChannel.open();
			librarySocketChannel.configureBlocking(true);
			librarySocketChannel.connect(new InetSocketAddress("220.66.115.136", 5001));
			
			String value = "setEnv," + PROPER_TEMPERATURE + "," + PROPER_HUMIDITY;
			send(value);
			receive();
		}catch(Exception e) {
			e.printStackTrace();
			if(librarySocketChannel.isOpen()) {try {
				librarySocketChannel.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
			}}
			return;
		}
	}
	
	public void stopCentral() {
		//centralServer에서 연결 끊기
		try {
			Iterator<ConnectClient> iterator = connections.iterator();
			while(iterator.hasNext()) {
				ConnectClient client = iterator.next();
				client.socketChannel.close();
				iterator.remove();
			}
			if(serverSocketChannel != null && serverSocketChannel.isOpen()) {
				serverSocketChannel.close();
			}
			if(executorService != null && !executorService.isShutdown()) {
				executorService.shutdown();
			}
		}catch(Exception e) {}
		System.out.println("central 종료");
	}
	
	public void showConnection() {
		if(!connections.isEmpty()) {
			System.out.print("연결 된 connections : ");
			System.out.println(connections.size());
		}
	}
	
	void receive() {
		executorService.submit(()->{
			while(true) {
				try {
					ByteBuffer byteBuffer = ByteBuffer.allocate(100);
					int readByte = librarySocketChannel.read(byteBuffer);
					if(readByte == -1) {
						throw new IOException();
					}
					
					byteBuffer.flip();
					Charset charset = Charset.forName("UTF-8");
					
					String data = charset.decode(byteBuffer).toString();
					String message = "[요청 받음 : " + librarySocketChannel.getRemoteAddress() + "] ->" + "";
					
					System.out.println(data);
					String[] value = data.split(",");
					if (value[0].equalsIgnoreCase("setEnv")) {
						PROPER_TEMPERATURE = Float.parseFloat(value[1]);
						PROPER_HUMIDITY = Float.parseFloat(value[2]);
					}
					System.out.println(PROPER_TEMPERATURE);
					System.out.println(PROPER_HUMIDITY);
					for (ConnectClient client : connections) {
						client.send(data);
					}
				}catch(Exception e) {
					e.printStackTrace();
					System.out.println("receive 오류");
					try {
						librarySocketChannel.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				}
			}
		});
	}
	
	void send(String data) {
		executorService.submit(()->{
			try {
				Charset charset = Charset.forName("UTF-8");
				ByteBuffer byteBuffer = charset.encode(data);
				this.librarySocketChannel.write(byteBuffer);
			}catch(Exception e) {
				e.printStackTrace();
			}
		});
	}
}
