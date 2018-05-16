package local;

import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalServer {
	private static final LocalServer local = new LocalServer();
	//서버 소켓 채널
	private ServerSocketChannel serverSocketChannel;
	private ExecutorService executorService;
	//좌석
	private List<SeatingPlace> seats = new ArrayList<>();
	private LocalServer() {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		seats.add(new SeatingPlace(1));
		seats.add(new SeatingPlace(2));
	}
	//온도, 습도 -> 환경
	private Environment env = new Environment(); 
	
	public static LocalServer getInstance() {
		return local;
	}
	public void startLocal() {
		System.out.println("local 시작");
		//centralServer에 소켓 연결
		
		//executorService -> watchService 등록
		executorService.submit(new WatchDecibelService(seats));
		executorService.submit(new WatchEnvironmentService(env));
	}
	public void stopLocal() {
		//centralServer에서 연결 끊기
		executorService.shutdown();
		System.out.println("local 종료");
	}
	
}
