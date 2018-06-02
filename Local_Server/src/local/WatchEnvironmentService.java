package local;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

public class WatchEnvironmentService implements Runnable {
	private Environment env;
	
	private static String line;
	private static String[] data;
	static float humidity=0f;
	static float temperature=0f;
	static String rootPath = System.getProperty("user.dir");
	static String filePath = rootPath + "/" + "dht.py";
	
    public WatchEnvironmentService(Environment env) {
		this.env = env;
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
				//System.out.println(filePath);
				System.out.println("outter if");
				if((line = bri.readLine()) != null) {
					//System.out.println("inner if");
					//System.out.println(line);
					
					if(!(line.contains("ERR_CRC") && !line.contains("ERR_FTR"))){
						data=line.split(", ");
						temperature=Float.parseFloat(data[0]);
						humidity=Float.parseFloat(data[1]);
						
						if (temperature >= env.PROPER_TEMPERATURE + 3) {
							env.setTemperature(temperature);
							env._temper_is_high_flag = true;
						} else if (temperature <= env.PROPER_TEMPERATURE - 3) {
							env.setTemperature(temperature);
						}
						
						if (humidity >= env.PROPER_HUMIDITY + 5) {
							env.setTemperature(humidity);
							env._hum_is_high_flag = true;
						} else if (humidity <= env.PROPER_HUMIDITY - 5) {
							env.setHumidity(humidity);
						}
						
						System.out.println("Temperature is : "+temperature+" 'C Humidity is :"+ humidity+" %RH");
					}
					else { 
						System.out.println("Data Error");
						flag = false;
					}
					
				}
				bri.close();
				p.waitFor();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
