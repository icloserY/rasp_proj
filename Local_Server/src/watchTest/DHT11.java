package watchTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DHT11 {
	private static String line;
	private static String[] data;
	static int humidity=0;
	static int temperature=0;
	static String rootPath = System.getProperty("user.dir");
	static String filePath = rootPath + "/src/watchTest/" + "dht.py";
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Runtime rt= Runtime.getRuntime();
		String[] cmd = {"python", filePath};
		Process p=rt.exec(cmd);
		
		BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
		System.out.println("outter if");
		if((line = bri.readLine()) != null) {
			System.out.println("inner if");
			System.out.println(line);
			/*
			if(!(line.contains("ERR_CRC") || line.contains("ERR_FTR"))){
				data=line.split("ABC");
			    System.out.println(data[0]);
				temperature=Integer.parseInt(data[0]);
				humidity=Integer.parseInt(data[1]);
			}
			else { 
				System.out.println("Data Error");
			}
			*/
		}
		bri.close();
		p.waitFor();
		
      	System.out.println("Temperature is : "+temperature+" 'C Humidity is :"+ humidity+" %RH");
      	System.out.println("Done.");
	}
}