package local;

import java.util.List;

public class WatchDecibelService implements Runnable {
	List<SeatingPlace> seats;
	public WatchDecibelService(List<SeatingPlace> seats) {
		this.seats = seats;
	}
	
	@Override
	public void run() {
		//데시벨 감시
		System.out.println("데시벨 감지 시작");
	}
}
