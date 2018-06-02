package local;

public class Environment {
	private float _temperature = 0;
	private float _humidity = 0;
	
	static final float PROPER_TEMPERATURE = 23;
	static final float PROPER_HUMIDITY = 50;
	
	public boolean _temper_is_high_flag = false;
	public boolean _hum_is_high_flag = false;
	
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
