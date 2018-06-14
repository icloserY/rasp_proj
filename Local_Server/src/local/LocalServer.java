package local;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalServer {
	private static final LocalServer local = new LocalServer();
	//서버 소켓 채널
	private SocketChannel localSocketChannel;
	public static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());;
	//좌석
	private List<SeatingPlace> seats = new ArrayList<>();
	//온도, 습도 -> 환경
	private Environment env = Environment.getInstance();
	
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

	public void startLocal(Controller controller) {
		System.out.println("local 시작");
		//executorService -> watchService 등록
		try {
			localSocketChannel = SocketChannel.open();
			localSocketChannel.configureBlocking(true);
			localSocketChannel.connect(new InetSocketAddress("220.66.115.136", 5550));
			
		}catch(Exception e) {
			e.printStackTrace();
			if(localSocketChannel.isOpen()) {stopLocal();}
			return;
		}
		executorService.submit(environmentService = new WatchEnvironmentService(env, localSocketChannel, executorService));
		executorService.submit(decibelService = new WatchDecibelServiceByListener(env, seats, controller.getNotice(), controller.getEnv()));
	}
	
	public void stopLocal() {
		//centralServer에서 연결 끊기
		try {
			if(decibelService.gpio != null) {
				decibelService.gpio.removeAllListeners();
			}
			executorService.shutdownNow();
			if(localSocketChannel != null && localSocketChannel.isOpen()) {
				localSocketChannel.close();
			}
			System.out.println("local 종료");
		} catch(Exception e) {
			System.out.println("올바르게 종료하지 못했습니다.");
		}
	}
}
