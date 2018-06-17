package central;

import java.util.List;

public class ControlAirService implements Runnable {
	List<SeatStatusService> seats;
	public ControlAirService(List<SeatStatusService> seats) {
		this.seats = seats;
	}
	
	@Override
	public void run() {
	}
}
