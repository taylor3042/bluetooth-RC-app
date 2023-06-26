package com.example.robotbuttler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class BluetoothConnection extends AppCompatActivity {
    final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private boolean isConnected;
    private BluetoothSocket mBluetoothSocket;

    @SuppressLint("MissingPermission")
    public boolean findDevice() {
        BluetoothSocket mBluetoothSocket = null;

        String MydeviceName = "sungod";
        Set<BluetoothDevice> bt = mBluetoothAdapter.getBondedDevices();
        mBluetoothAdapter.startDiscovery();
        int index = 0;
        if (bt.size() > 0) {

            for (BluetoothDevice device : bt) {
                if (MydeviceName.equals(device.getName())) {
                    Log.i("Device Name: ", "Found previous device connected!!!");
                    return tyConnect(device, 1);
                }
            }
        }
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothSocket mBluetoothSocket = null;
                String action = intent.getAction();
                Set<BluetoothDevice> bt = mBluetoothAdapter.getBondedDevices();
                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Inten
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView

                    if (MydeviceName.equals(device.getName())) {
                        Log.i("Device Name: ", "IT WORKS!!!");
                        isConnected = tyConnect(device, 1);

                    }
                }
            }

        };

        return false;
    }

    @SuppressLint("MissingPermission")
    public boolean tyConnect(BluetoothDevice device, int port) {
        mBluetoothSocket = null;
        try {
            device = mBluetoothAdapter.getRemoteDevice(device.getAddress());
            Method createRfcommSocket = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            mBluetoothSocket = (BluetoothSocket) createRfcommSocket.invoke(device, port);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            if (mBluetoothSocket.isConnected()) {
                isConnected = true;
                return true;
            } else {
                isConnected = false;
                return false;
            }

        } catch (NoSuchMethodException e) {

            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();

        } catch (InvocationTargetException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @SuppressLint("MissingPermission")
    public boolean isBluetoothOn() {
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
                return false;
        }else{
            return true;
        }
    }

    public String read() {

        try {

                byte[] buffer = new byte[1024];
                //int length = new DataInputStream(mBluetoothSocket.getInputStream()).read(buffer);
            InputStream inputStream = mBluetoothSocket.getInputStream();
            Log.i("Value: ", String.valueOf(inputStream.available()));
                if((inputStream.available()) == 0)
                {
                    return "NothingRightNow";
                }else {
                    String newWord = new String(buffer, 0, new DataInputStream(mBluetoothSocket.getInputStream()).read(buffer));
                    return newWord;

                }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void write(String word)
    {
        try {
            OutputStream output = mBluetoothSocket.getOutputStream();
            output.write(word.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getPic()
    {
        try {
            Bitmap decoded = BitmapFactory.decodeStream(mBluetoothSocket.getInputStream());
            return decoded;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
