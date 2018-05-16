package local;

public class SeatingPlace {
	private static int seatCount = 1;
	private int _placeNumber;
	private int _gpioPinNumber;
	
	public SeatingPlace(int gpioPinNumber) {
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
