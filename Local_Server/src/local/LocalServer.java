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
	public static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());;
	//좌석
	private List<SeatingPlace> seats = new ArrayList<>();
	//온도, 습도 -> 환경
	private Environment env = new Environment();
	
	//watchService, envService
	private WatchDecibelServiceByListener decibelService;
	private WatchEnvironmentService environmentService;
	
	private LocalServer() {
		seats.add(new SeatingPlace("1"));
		seats.add(new SeatingPlace("2"));
	} 
	
	public static LocalServer getInstance() {
		return local;
	}

	public void startLocal() {
		System.out.println("local 시작");
		//executorService -> watchService 등록
		executorService.submit(decibelService = new WatchDecibelServiceByListener(seats));
		executorService.submit(environmentService = new WatchEnvironmentService(env, serverSocketChannel));
	}
	
	public void stopLocal() {
		//centralServer에서 연결 끊기
		if(decibelService.gpio != null) {
			decibelService.gpio.removeAllListeners();
		}
		executorService.shutdownNow();
		
		System.out.println("local 종료");
	}
}
