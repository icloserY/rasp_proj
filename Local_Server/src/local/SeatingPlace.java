package local;

public class SeatingPlace {
	private static int seatCount = 1;
	private int _placeNumber;
	private String _gpioPinNumber;
	private boolean _noisy = false;
	
	public SeatingPlace(String gpioPinNumber) {
		_placeNumber = seatCount++;
		_gpioPinNumber = gpioPinNumber;
	}
	
	public int getPlaceNumber() {
		return _placeNumber;
	}

	public String getGpioPinNumber() {
		return _gpioPinNumber;
	}

	public boolean isNoisy() {
		return _noisy;
	}

	public void setNoisy(boolean noisy) {
		this._noisy = noisy;
	}
}
