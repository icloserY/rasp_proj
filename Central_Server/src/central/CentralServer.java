package central;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
	private ExecutorService executorService;
	//서버가 관리하는 클라이언트
	List<ConnectClient> connections = new Vector<>(); 
	//좌석
	private List<SeatStatusService> seats = new ArrayList<>();
	private ControlAirService controlAirService = new ControlAirService(seats);
	
	
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
			serverSocketChannel.bind(new InetSocketAddress(5001));
		} catch(Exception e) {
			if(serverSocketChannel.isOpen()) {
				stopCentral();
			}
			return;
		}
		executorService.submit(()->{
			while(true) {
				try {
					SocketChannel socketChannel = serverSocketChannel.accept();
					String message = "[연결 수락 : " + socketChannel.getRemoteAddress() + "]";
					System.out.println(message);
					
					ConnectClient client = new ConnectClient(socketChannel, executorService, connections);
					connections.add(client);
				}catch(Exception e) {
					if(serverSocketChannel.isOpen()) {stopCentral();}
					return;
				}
			}
		});
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
		if(connections != null) {
			System.out.println("연결 된 connections");
			for(ConnectClient connectClient : connections) {
				System.out.println(connectClient.name);
			}
		}
	}
}
