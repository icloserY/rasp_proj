package central;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ConnectClient {
	SocketChannel socketChannel;
	ExecutorService executorService;
	List<ConnectClient> connections;
	static int number = 1;
	String name = "Local_Server ";
	boolean aircon = false;
	boolean heater = false;
	
	public ConnectClient(SocketChannel socketChannel, ExecutorService executorService, List<ConnectClient> connections) {
		this.socketChannel = socketChannel;
		this.executorService = executorService;
		this.connections = connections;
		
		name += number++ + "번";
		receive();
	}
	
	void receive() {
		executorService.submit(()->{
			while(true) {
				try {
					ByteBuffer byteBuffer = ByteBuffer.allocate(100);
					int readByte = socketChannel.read(byteBuffer);
					if(readByte == -1) {
						throw new IOException();
					}
					
					byteBuffer.flip();
					Charset charset = Charset.forName("UTF-8");
					
					String data = charset.decode(byteBuffer).toString();
					String message = "[요청 받음 : " + socketChannel.getRemoteAddress() + "] ->" + "";
					
					System.out.print(message);
					System.out.println(data);
					
					if(data.equals("OVER_TEMPERATURE")) {
						this.aircon = true;
						aircon(aircon);
					}else if(data.equals("LOW_TEMPERATURE")) {
						this.heater = false;
						aircon(heater);
					}else if(data.equals("PROPER_TEMPERATURE")) {
						this.aircon = false;
						this.heater = false;
						aircon(aircon);
						heater(heater);
					}
					//가습기, 제습기 추가
					
				}catch(Exception e) {
					connections.remove(ConnectClient.this);
					socketChannel.close();
				}
			}
		});
	}
	
	void aircon(boolean aircon) {
		//에어콘 가동 or 중지
		Runtime rt= Runtime.getRuntime();
		String rootPath = System.getProperty("user.dir");
		String filePath;
		if(aircon) {
			filePath = rootPath + "/" + "airconOn.py";
			String[] cmd = {"python", filePath};
			Process p;
			try {
				p = rt.exec(cmd);
				p.waitFor();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			filePath = rootPath + "/" + "airconOFF.py";
			String[] cmd = {"python", filePath};
			Process p;
			try {
				p = rt.exec(cmd);
				p.waitFor();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void heater(boolean heater) {
		//히터 가동 or 중지
		Runtime rt= Runtime.getRuntime();
		String rootPath = System.getProperty("user.dir");
		String filePath;
		if(heater) {
			filePath = rootPath + "/" + "heaterOn.py";
			String[] cmd = {"python", filePath};
			Process p;
			try {
				p = rt.exec(cmd);
				p.waitFor();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			filePath = rootPath + "/" + "heaterOFF.py";
			String[] cmd = {"python", filePath};
			Process p;
			try {
				p = rt.exec(cmd);
				p.waitFor();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void send(String data) {
		executorService.submit(()->{
			try {
				Charset charset = Charset.forName("UTF-8");
				ByteBuffer byteBuffer = charset.encode(data);
				this.socketChannel.write(byteBuffer);
			}catch(Exception e) {
				e.printStackTrace();
			}
		});
	}
}
