package com.example.dc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManagers;
    private Sensor senAccelerometor;
    private Sensor senGyroscope;
    private LocationManager locationManager;
    private TextView GPSx;
    private TextView GPSy;
    private TextView GPS_loc;

    private long lastUpdate = 0;
    private long lastUpdate_gyro = 0;
    private float lastX = 0, lastY = 0, lastZ = 0;
    private static final int SHAKE_THRESHOLD = 1;
    private FusedLocationProviderClient MyFusedLocationClient;
    public int MYLOCATION;
    private int locationRequestCode=1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        sensorManagers = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometor = sensorManagers.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManagers.registerListener(this, senAccelerometor, SensorManager.SENSOR_DELAY_NORMAL);

        senGyroscope = sensorManagers.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManagers.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, locationRequestCode);
        }
        else {
            Toast.makeText(getApplicationContext(), "Location Permission Granted", Toast.LENGTH_SHORT);
        }

        Switch toggle = (Switch) findViewById(R.id.sw);
        toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                onResume();
            }
            else{
                onPause();
            }
        });

////        LocationListener locationListener = new
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MYLOCATION );
//            return;
//        }



        //NEW


//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
//
//                Toast.makeText(getApplicationContext(), "Error getting permissions", Toast.LENGTH_SHORT);
//            }else {
//                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MYLOCATION);
//            }
//
//
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, (LocationListener) this);


    }

//    protected void onToggle(){
//        ToggleButton toggle = (ToggleButton) findViewById(R.id.sw);
//        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    onResume();
//                }
//                else{
//                    onPause();
//                }
//            }
//        });
//    }

    protected void onPause(){
        super.onPause();
        sensorManagers.unregisterListener(this);
    }

    protected void onResume(){
        super.onResume();
        sensorManagers.registerListener(this, senAccelerometor , SensorManager.SENSOR_DELAY_NORMAL);
        sensorManagers.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

//    LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//
//            String longitude = "" + location.getLongitude();
//            TextView text = (TextView) findViewById(R.id.gpsx);
//            text.setText(longitude);
//
//            String latitude = "" + location.getLatitude();
//            text = (TextView) findViewById(R.id.gpsy);
//            text.setText(latitude);



//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Log.d("Latitude", "Status");
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            Log.d("Latitude", "Enable");
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Log.d("Latitude", "Disable");
//        }
//    };

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor mySensor = sensorEvent.sensor;
        Sensor GSensor = sensorEvent.sensor;

        MyFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if(location!=null){
                wayLatitude = location.getLatitude();
                wayLongitude = location.getLongitude();

                GPSx = (TextView) findViewById(R.id.gpsx);
                GPSx.setText("" + wayLatitude);

                GPSy = (TextView) findViewById(R.id.gpsy);
                GPSy.setText("" + wayLongitude);

                GPS_loc = (TextView) findViewById(R.id.city);
                GPS_loc.setText(String.format(Locale.US,"%s -- %s", wayLatitude, wayLongitude));
            }
        });

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


