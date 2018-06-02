package central;

import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CentralServer {
	private static final CentralServer local = new CentralServer();
	//서버 소켓 채널
	private ServerSocketChannel serverSocketChannel;
	private ExecutorService executorService;
	//좌석
	private List<SeatStatusService> seats = new ArrayList<>();
	private ControlAirService controlAirService = new ControlAirService(seats);
	
	
	private CentralServer() {
		seats.add(new SeatStatusService(1));
		seats.add(new SeatStatusService(2));
	} 
	
	public static CentralServer getInstance() {
		return local;
	}
	
	public void startLocal() {
		System.out.println("central 시작");
		//centralServer에 소켓 연결
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
	public void stopLocal() {
		//centralServer에서 연결 끊기
		executorService.shutdownNow();
		
		System.out.println("local 종료");
	}
	
}
