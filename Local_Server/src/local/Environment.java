package local;

public class Environment {
	public enum Status {
		OVER_TEMPERATURE, OVER_HUMIDITY, 
		LOW_TEMPERATURE, LOW_HUMIDITY, 
		PROPER_TEMPERATURE, PROPER_HUMIDITY;
	}
	
	public Status tempStatus = Status.PROPER_TEMPERATURE;
	public Status humStatus = Status.PROPER_HUMIDITY;
	
	private float _temperature = 0f;
	private float _humidity = 0f;
	
	static final float PROPER_TEMPERATURE = 23;
	static final float PROPER_HUMIDITY = 50;
	
	public float getTemperature() {
		return _temperature;
	}
	public void setTemperature(float temperature) {
		this._temperature = temperature;
	}
	public float getHumidity() {
		return _humidity;
	}
	public void setHumidity(float humidity) {
		this._humidity = humidity;
	}
	
}
