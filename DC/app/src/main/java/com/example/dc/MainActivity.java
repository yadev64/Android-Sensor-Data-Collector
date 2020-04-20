package com.example.dc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManagers;
    private Sensor senAccelerometor;
    private Sensor senGyroscope;
    private LocationManager locationManager;

    private long lastUpdate = 0;
    private long lastUpdate_gyro = 0;
    private float lastX=0, lastY=0,lastZ=0;
    private static final int SHAKE_THRESHOLD=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManagers = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometor = sensorManagers.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManagers.registerListener(this, senAccelerometor , SensorManager.SENSOR_DELAY_NORMAL);

        senGyroscope = sensorManagers.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManagers.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_NORMAL);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        loationGetter = locationManager.getAllProviders(Context.)


    }

    protected void onPause(){
        super.onPause();
        sensorManagers.unregisterListener(this);
    }

    protected void onResume(){
        super.onResume();
        sensorManagers.registerListener(this, senAccelerometor , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor mySensor = sensorEvent.sensor;
        Sensor GSensor = sensorEvent.sensor;

        if (GSensor.getType()==Sensor.TYPE_GYROSCOPE){
            float gx = sensorEvent.values[0];
            float gy = sensorEvent.values[1];
            float gz = sensorEvent.values[2];

            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastUpdate_gyro > 300)) {

                lastUpdate_gyro = currentTime;

                {
                    String sX = Float.toString(gx);
                    TextView text = (TextView) findViewById(R.id.gx);
                    text.setText(sX);

                    String sY = Float.toString(gy);
                    text = (TextView) findViewById(R.id.gy);
                    text.setText(sY);

                    String sZ = Float.toString(gz);
                    text = (TextView) findViewById(R.id.gz);
                    text.setText(sZ);
                }
            }
        }

        if(mySensor.getType()==Sensor.TYPE_ACCELEROMETER){
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastUpdate) > 300){
                long timeDiff = currentTime - lastUpdate;
                lastUpdate = currentTime;

                {
                    String sX = Float.toString(x);
                TextView text = (TextView) findViewById(R.id.ax);
                text.setText(sX);

                String sY = Float.toString(y);
                text = (TextView) findViewById(R.id.ay);
                text.setText(sY);

                String sZ = Float.toString(z);
                text = (TextView) findViewById(R.id.az);
                text.setText(sZ);
                }



//                float speed = (x + y + z - lastX - lastY - lastZ) / timeDiff * 10000;
//                if(speed > SHAKE_THRESHOLD){
//                    lastX = x;
//                    lastY = y;
//                    lastZ = z;
//
//                    updateContent();
//                }
            }
        }
    }

//    public void updateContent(){
//
//        String sX = Float.toString(lastX);
//        TextView text = (TextView) findViewById(R.id.ax);
//        text.setText(sX);
//
//        String sY = Float.toString(lastY);
//        text = (TextView) findViewById(R.id.ay);
//        text.setText(sY);
//
//        String sZ = Float.toString(lastZ);
//        text = (TextView) findViewById(R.id.az);
//        text.setText(sZ);
//    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
