package com.example.robotbuttler;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends AppCompatActivity {
    int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    Thread bluetoothThread;
    boolean locationService;
    Boolean bluetoothPermission;
    public static BluetoothConnection mBluetoothConnection = new BluetoothConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);
        Button reconnectBT = findViewById(R.id.button);
        ProgressBar locationProgress = findViewById(R.id.LocationLoading);
        ImageView locationCheck = findViewById(R.id.LocationCheck);
        ImageView bluetoothCheck = findViewById(R.id.BluetoothCheck);
        ProgressBar bluetoothProgress = findViewById(R.id.BluetoothLoading);
        ProgressBar findProgress = findViewById(R.id.FoundLoading);
        ImageView findCheck = findViewById(R.id.FoundCheck);
        checkLocationPermission();

        Handler handler = new Handler();
        bluetoothThread = new Thread()
        {
            public void run()
            {try {
                textView.setText("Connecting....");

                Thread.sleep(500);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mBluetoothConnection.findDevice())
                        {

                            try {
                                textView.setText("Connected");
                                findProgress.setVisibility(View.INVISIBLE);
                                findCheck.setImageResource(R.mipmap.check_mark_green);
                                Thread.sleep(400);
                                Intent startMainMenu = new Intent(MainActivity.this, MainMenu.class);
                                startActivity(startMainMenu);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }else{
                            textView.setText("Not Connected");
                            findProgress.setVisibility(View.INVISIBLE);
                            findCheck.setImageResource(R.mipmap.x_mark_red);
                        }
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            }
        };

        if(locationService)
        {
            locationProgress.setVisibility(View.INVISIBLE);
            locationCheck.setImageResource(R.mipmap.check_mark_green);

        }else{
            locationProgress.setVisibility(View.INVISIBLE);
            locationCheck.setImageResource(R.mipmap.x_mark_red);
        }

        if(mBluetoothConnection.isBluetoothOn())
        {
            bluetoothPermission = true;
            bluetoothProgress.setVisibility(View.INVISIBLE);
            bluetoothCheck.setImageResource(R.mipmap.check_mark_green);
            bluetoothThread.start();
        }else{
            Handler BTONhandler = new Handler();
            Thread BTON = new Thread()
            {
              public void run()
              {
                  try {
                      Thread.sleep(300);
                      bluetoothProgress.setVisibility(View.INVISIBLE);
                      bluetoothCheck.setImageResource(R.mipmap.x_mark_red);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }

                  BTONhandler.post(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              Thread.sleep(300);
                              bluetoothCheck.setVisibility(View.INVISIBLE);
                              bluetoothProgress.setVisibility(View.VISIBLE);
                              Thread.sleep(300);
                              if(mBluetoothConnection.isBluetoothOn())
                              {
                                  bluetoothPermission = true;
                                  bluetoothProgress.setVisibility(View.INVISIBLE);
                                  bluetoothCheck.setImageResource(R.mipmap.check_mark_green);
                                  bluetoothCheck.setVisibility(View.VISIBLE);
                                  bluetoothThread.start();
                              }else{
                                  bluetoothProgress.setVisibility(View.INVISIBLE);
                                  bluetoothCheck.setVisibility(View.VISIBLE);
                              }

                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                      }
                  });

              }

            };
            BTON.start();
        }


        reconnectBT.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                textView.setText("Connecting....");
                bluetoothCheck.setVisibility(View.INVISIBLE);
                bluetoothProgress.setVisibility(View.VISIBLE);
                locationCheck.setVisibility(View.INVISIBLE);
                locationProgress.setVisibility(View.VISIBLE);
                findProgress.setVisibility(View.VISIBLE);
                findCheck.setVisibility(View.INVISIBLE);

                bluetoothThread = new Thread()
                {
                    public void run()
                    {try {

                        Thread.sleep(500);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mBluetoothConnection.findDevice())
                                {

                                    try {
                                        textView.setText("Connected");
                                        bluetoothCheck.setVisibility(View.VISIBLE);
                                        bluetoothProgress.setVisibility(View.INVISIBLE);
                                        locationCheck.setVisibility(View.VISIBLE);
                                        locationProgress.setVisibility(View.INVISIBLE);
                                        findProgress.setVisibility(View.INVISIBLE);
                                        findCheck.setImageResource(R.mipmap.check_mark_green);
                                        findCheck.setVisibility(View.VISIBLE);
                                        Thread.sleep(300);
                                        Intent startMainMenu = new Intent(MainActivity.this, MainMenu.class);
                                        startActivity(startMainMenu);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    bluetoothCheck.setImageResource(R.mipmap.check_mark_green);
                                    bluetoothCheck.setVisibility(View.VISIBLE);
                                    bluetoothProgress.setVisibility(View.INVISIBLE);
                                    locationCheck.setImageResource(R.mipmap.check_mark_green);
                                    locationCheck.setVisibility(View.VISIBLE);
                                    locationProgress.setVisibility(View.INVISIBLE);
                                    findProgress.setVisibility(View.INVISIBLE);
                                    findCheck.setVisibility(View.VISIBLE);
                                    textView.setText("Not Connected");
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    }
                };
                checkLocationPermission();
                if(bluetoothPermission && locationService) {
                    bluetoothThread.interrupt();
                    bluetoothThread.start();
                }
            }
        });
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            locationService = false;
        }else{
            locationService = true;
        }




    }

}