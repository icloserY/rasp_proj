package central;

import java.util.List;

import com.pi4j.gpio.extension.base.AdcGpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.spi.SpiChannel;

public class ControlAirService implements Runnable {
	List<SeatStatusService> seats;
	public ControlAirService(List<SeatStatusService> seats) {
		this.seats = seats;
	}
	GpioController gpio;
	
	@Override
	public void run() {
		//데시벨 감시
		System.out.println("데시벨 감지 시작");
		System.out.println("<--Pi4J--> MCP3008 ADC Example ... started.");
		try {
			gpio = GpioFactory.getInstance();
			
	        final AdcGpioProvider provider = new MCP3008GpioProvider(SpiChannel.CS0);
	
	        final GpioPinAnalogInput inputs[] = {
	                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH0, "MyAnalogInput-CH0")
	        };
	        
	        provider.setEventThreshold(10, inputs);
	        provider.setMonitorInterval(250);
	        
	        while(true) {
		        for(GpioPinAnalogInput input : inputs){
		            System.out.println("<VALUE> [" + input.getName() + "] : RAW VALUE = " + input.getValue());
		        }		       
		        Thread.sleep(500);
	        }
		} catch (Exception e) {
			System.out.println("watchService 오류 발생 다시 시작해 주세요.");
			e.getStackTrace();
		}
	}
}
