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
	//온도, 습도 -> 환경
	private Environment env = new Environment();
	
	//watchService, envService
	private WatchDecibelService decibelService;
	private WatchEnvironmentService environmentService;
	
	private LocalServer() {
		seats.add(new SeatingPlace(1));
		seats.add(new SeatingPlace(2));
	} 
	
	public static LocalServer getInstance() {
		return local;
	}
	
	public void startLocal() {
		System.out.println("local 시작");
		//centralServer에 소켓 연결
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		//executorService -> watchService 등록
		executorService.submit(decibelService = new WatchDecibelService(seats));
		executorService.submit(environmentService = new WatchEnvironmentService(env));
	}
	
	public void stopLocal() {
		//centralServer에서 연결 끊기
		executorService.shutdownNow();
		
		System.out.println("local 종료");
	}
	
}
