package local;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.pi4j.gpio.extension.base.AdcGpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.spi.SpiChannel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WatchDecibelServiceByListener implements Runnable {
	private Label notice;
	List<SeatingPlace> seats;
	private Environment env;
	
	public WatchDecibelServiceByListener(Environment env, List<SeatingPlace> seats, Label notice) {
		this.seats = seats;
		this.notice = notice;
		this.env = env;
	}
	GpioController gpio;
	
	@Override
	public void run() {
		//데시벨 감시
		//System.out.println("데시벨 감지 시작");
		//System.out.println("<--Pi4J--> MCP3008 ADC Example ... started.");
		boolean flag = true;
		Environment env = Environment.getInstance();
		
		try {
			gpio = GpioFactory.getInstance();
			
	        final AdcGpioProvider provider = new MCP3008GpioProvider(SpiChannel.CS0);
	        
	        final GpioPinAnalogInput inputs[] = {
	                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH0, seats.get(0).getGpioPinNumber())
	        };
	        
	        provider.setEventThreshold(5, inputs);
	        provider.setMonitorInterval(250);
	        
	        GpioPinListenerAnalog listener = new GpioPinListenerAnalog()
	        {
	            @Override
	            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event)
	            {
	                double value = event.getValue();
	                int seatNum = Integer.parseInt(event.getPin().getName()) - 1;
	                if(value > 50) {
	                	seats.get(seatNum).setNoisy(true);
	                }
	            }
	        };
	        
	        gpio.addListener(listener, inputs);
	        boolean b_notice = false;
	        //경고 생성
			while(flag) {
				System.out.println(env.getHumidity());
				try {
					for(SeatingPlace seat : seats) {
						if(seat.isNoisy()) {
							//경고 띄우기(fx)
							Platform.runLater(() -> {
								try {
									notice.setText(seat.getGpioPinNumber() + "번 자리 조용히 하세요");
								} catch (Exception e){
									e.printStackTrace();
								}
							});
							//noisy to false
							seat.setNoisy(false);
							b_notice = true;
						}
						if(b_notice) {
							Thread.sleep(2000);
							Platform.runLater(() -> {
								try {
									notice.setText("도서관 환경관리 시스템");
								} catch (Exception e){
									e.printStackTrace();
								}
							});
							b_notice = false;
						}
					}
					Thread.sleep(500);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			flag = false;
			e.getStackTrace();
		}
	}
}
