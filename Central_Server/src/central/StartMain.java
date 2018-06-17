package central;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class StartMain {
	public static final int PITCHERN0303 = 1;
	
	public static void main(String[] args) {
		CentralServer central = CentralServer.getInstance();
		boolean serverStatus = false;
		boolean flag = true;
		boolean setFlag = true;
		boolean roomFlag = true;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));
		
		while(flag) {
			System.out.println("명령을 입력하세요.");
			System.out.println("1.start, 2.stop, 3.showConnection");
			System.out.println("4.setTempHum");
			String command = "NOTHING";
			String data = "";
			try {
				command = reader.readLine();
				if(!command.equals("NOTHING")) {
					if(command.equalsIgnoreCase("1")  && !serverStatus) {
						central.startCentral();
						serverStatus = true;
					} else if(command.equalsIgnoreCase("2") && serverStatus) {
						central.stopCentral();
						serverStatus = false;
					} else if(command.equalsIgnoreCase("3") && serverStatus) {
						System.out.println("서버가 이미 동작 중입니다.");
					} else if(command.equalsIgnoreCase("shw") && serverStatus) {
						central.showConnection();
					} else if(command.equalsIgnoreCase("4") && serverStatus) {
						while(setFlag) {
							System.out.println("1.PropTemp, 2.PropHum, 3.PropTemp,PropHum, 4.exit");
							command = reader.readLine();
							if(command.equalsIgnoreCase("1")) {
								System.out.println("적정온도를 입력하세요.(C)");
								command = reader.readLine();
								CentralServer.PROPER_TEMPERATURE = Float.parseFloat(command);
								data += "setPropTemp," + CentralServer.PROPER_TEMPERATURE;
								System.out.println(data);
								sendToLocal(central, data);
								data = "";
							} else if(command.equalsIgnoreCase("2")) {
								System.out.println("적정습도를 입력하세요.(%)");
								command = reader.readLine();
								CentralServer.PROPER_HUMIDITY = Float.parseFloat(command);
								data += "setPropHum," + CentralServer.PROPER_HUMIDITY;
								System.out.println(data);
								sendToLocal(central, data);
								data = "";
							} else if(command.equalsIgnoreCase("3")){
								System.out.println("적정온도,습도를 입력하세요.(C,%)");
								command = reader.readLine();
								String[] value = command.split(",");
								CentralServer.PROPER_TEMPERATURE = Float.parseFloat(value[0]);
								CentralServer.PROPER_HUMIDITY = Float.parseFloat(value[1]);
								data += "setPropTempHum," + command; 
								System.out.println(data);
								sendToLocal(central, data);
								data = "";
							} else if(command.equalsIgnoreCase("4")){
								setFlag = false;
							}
						}
					} else if(command.equalsIgnoreCase("set") && !serverStatus) {
						System.out.println("서버를 시작해 주세요.");
					}else {
						System.out.println("잘못된 명령어 입니다.");
					} 
				}
				setFlag = true;
				Thread.sleep(100);
			} catch (Exception e) {
				System.out.println("오류 발생 다시 시작해 주세요.");
				e.printStackTrace();
				flag = false;
			}
		}
	}
	
	static public void sendToLocal(CentralServer central, String data) {
		List<ConnectClient> clients = central.connections;
		for(ConnectClient client : clients) {
			client.send(data);
		}
	}
}
