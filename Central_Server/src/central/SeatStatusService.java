package central;

public class SeatStatusService {
	private static int seatCount = 1;
	private int _placeNumber;
	private int _gpioPinNumber;
	
	public SeatStatusService(int gpioPinNumber) {
		_placeNumber = seatCount++;
		_gpioPinNumber = gpioPinNumber;
	}
	
	public int getPlaceNumber() {
		return _placeNumber;
	}

	public int getGpioPinNumber() {
		return _gpioPinNumber;
	}
}
