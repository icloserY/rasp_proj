package local;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
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
			localSocketChannel.connect(new InetSocketAddress("192.168.0.51", 5550));
		}catch(Exception e) {
			e.printStackTrace();
			if(localSocketChannel.isOpen()) {stopLocal();}
			return;
		}
		receive();
		executorService.submit(environmentService = new WatchEnvironmentService(env, localSocketChannel, executorService, controller.getNotice_env(), controller.getTime_label(), controller.getTtle_label()));
		executorService.submit(decibelService = new WatchDecibelServiceByListener(seats, controller.getNotice_db()));
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
	
	public void receive() {
		executorService.submit(()->{
			while(true) {
				try {
					ByteBuffer byteBuffer = ByteBuffer.allocate(100);
					int readByte = localSocketChannel.read(byteBuffer);
					if(readByte == -1) {
						throw new IOException();
					}
					
					byteBuffer.flip();
					Charset charset = Charset.forName("UTF-8");
					
					String data = charset.decode(byteBuffer).toString();
					
					String[] value = data.split(",");
					if (value[0].equalsIgnoreCase("setPropTemp")) {
						Environment.PROPER_TEMPERATURE = Float.parseFloat(value[1]);
					} else if(value[0].equalsIgnoreCase("setPropHum")) {
						Environment.PROPER_HUMIDITY = Float.parseFloat(value[1]);
					} else if(value[0].equalsIgnoreCase("setPropTempHum")) {
						Environment.PROPER_TEMPERATURE = Float.parseFloat(value[1]);
						Environment.PROPER_HUMIDITY = Float.parseFloat(value[2]);
					}
				}catch(Exception e) {
					localSocketChannel.close();
				}
			}
		});
	}
}
