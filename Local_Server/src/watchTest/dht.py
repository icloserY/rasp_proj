import sys
sys.path.insert(0, '/lib')
import Adafruit_DHT
import time

sensor = Adafruit_DHT.DHT11
pin = 4

try:
	humidity, temperature = Adafruit_DHT.read_retry(sensor, pin)
except:
	print("ERR_FTR")

if humidity is not None and temperature is not None:
	print('Temp={0:0.1f}*C Humidity={1:0.1f}%'.format(temperature, humidity))
	time.sleep(3)
else:
	print("ERR_CRC")