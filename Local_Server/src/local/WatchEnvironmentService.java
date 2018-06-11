package local;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;

import local.Environment.Status;


public class WatchEnvironmentService implements Runnable {
	private Environment env;
	private SocketChannel localSocketChannel;
	private ExecutorService executorService;
	
	private static String line;
	private static String[] data;
	static String rootPath = System.getProperty("user.dir");
	static String filePath = rootPath + "/" + "dht.py";
	float temperature = 0.0f;
	float humidity = 0.0f;
	
    public WatchEnvironmentService(Environment env, SocketChannel localSocketChannel,
    								ExecutorService executorService) {
		this.env = env;
		this.localSocketChannel = localSocketChannel;
		this.executorService = executorService;
	}
	@Override
	public void run() {
		//온도, 습도 감시
		try {
			System.out.println("온도, 습도 감지 시작");
			
			boolean flag = true;
			Runtime rt= Runtime.getRuntime();
			String[] cmd = {"python", filePath};
			
			while(flag) {
				Process p=rt.exec(cmd);
				
				BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
				if((line = bri.readLine()) != null) {
					if(!line.contains("ERR_CRC") && !line.contains("ERR_FTR")){
						data=line.split(", ");
						System.out.println(line);
						env.setTemperature(temperature = Float.parseFloat(data[0]));
						env.setHumidity(humidity = Float.parseFloat(data[1]));
						
						//온도 감시
						if (temperature >= Environment.PROPER_TEMPERATURE + 3 && env.tempStatus == Status.PROPER_TEMPERATURE) {
							env.tempStatus = Status.OVER_TEMPERATURE;
							
							//통지 (에어컨 가동)
							System.out.println("에어콘 가동");
							send(env.tempStatus.name());
						}else if (temperature <= Environment.PROPER_TEMPERATURE - 3 && env.tempStatus == Status.PROPER_TEMPERATURE) {
							env.tempStatus = Status.LOW_TEMPERATURE;
								
							//통지 (히터 가동)
							System.out.println("히터 가동");
							send(env.tempStatus.name());
						}else if (temperature <= Environment.PROPER_TEMPERATURE + 1.5f && temperature > Environment.PROPER_TEMPERATURE - 1.5f
									&& (env.tempStatus == Status.OVER_TEMPERATURE || env.tempStatus == Status.LOW_TEMPERATURE)) {
							//통지 (에어컨 중지 또는 히터 중지)
							System.out.println("에어콘 중지, 히터 중지");
							send(env.tempStatus.name());
							env.tempStatus = Status.PROPER_TEMPERATURE;
						}
						
						//습도 감시
						if (humidity >= Environment.PROPER_HUMIDITY + 10 && env.humStatus == Status.PROPER_HUMIDITY) {
							env.humStatus = Status.OVER_HUMIDITY;
								
							//통지 (제습기 가동)
							System.out.println("제습기 가동");
							send(env.humStatus.name());
						}else if (humidity <= Environment.PROPER_HUMIDITY - 10 && env.humStatus == Status.PROPER_HUMIDITY) {
							env.humStatus = Status.LOW_HUMIDITY;
								
							//통지 (가습기 가동)
							System.out.println("가습기 가동");
							send(env.humStatus.name());
						}else if (humidity <= Environment.PROPER_HUMIDITY + 5 && humidity > Environment.PROPER_HUMIDITY - 5
									&& (env.humStatus == Status.OVER_HUMIDITY || env.humStatus == Status.LOW_HUMIDITY)) {
							//통지 (가습기 또는 제습기 중지)
							System.out.println("가습기 중지, 제습기 중지");
							send(env.humStatus.name());
							env.humStatus = Status.PROPER_HUMIDITY;
						}
						
						System.out.println("Temperature is : "+env.getTemperature()+ 
											" 'C Humidity is :"+ env.getHumidity()+" %RH");
					}
					else { 
						System.out.println("Data Error");
						flag = false;
					}
				}
				bri.close();
				p.waitFor();
				Thread.sleep(1000);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void send(String data) {
		executorService.submit(()->{
			try {
				Charset charset = Charset.forName("UTF-8");
				ByteBuffer byteBuffer = charset.encode(data);
				localSocketChannel.write(byteBuffer);
			}catch(Exception e) {
				System.out.println("서버 통신 불가");
			}
		});
	}
}
