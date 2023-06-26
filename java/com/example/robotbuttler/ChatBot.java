package com.example.robotbuttler;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class ChatBot extends AppCompatActivity {
    Boolean cameraUsed = false;
    String textField;
    Thread runChat;
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        BluetoothConnection mBluetoothConnection = MainActivity.mBluetoothConnection;
        Button camera = findViewById(R.id.camera);
        Button send = findViewById(R.id.send);
        EditText textInput = findViewById(R.id.textInput);
        ListView chatBox = findViewById(R.id.listView);
        ImageView frame = findViewById(R.id.imageView);
        ArrayList<String> mssg = new ArrayList<String>();
        mssg.add("This is to communicate with the Robot, let me know what I can do to help.");
        ArrayAdapter mssgs = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mssg);
        chatBox.setAdapter(mssgs);


       runChat = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    runOnUiThread(new Runnable() // start actions in UI thread
                    {

                        @Override
                        public void run() {
                            while(true) {
                                try {
                                    mBluetoothConnection.write("readyForCommunications");
                                    Thread.sleep(3000);

                                    String difWord = MainActivity.mBluetoothConnection.read();
                                    if (!difWord.equals("NothingRightNow") && !difWord.equals("sendingPic")) {
                                        mssg.add(difWord);
                                        mssgs.notifyDataSetChanged();
                                    }else if(difWord.equals("sendingPic")){
                                        image = mBluetoothConnection.getPic();
                                        frame.setImageBitmap(image);
                                    }else{
                                        mssg.add("What can I help you with?");
                                        mssgs.notifyDataSetChanged();
                                        break;
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                } catch (InterruptedException e) {}
            }
        });

       runChat.start();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            cameraUsed = true;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textField = textInput.getText().toString();
                if(!cameraUsed && !textField.equals(""))
                {   mssg.add(textField);
                    mssgs.notifyDataSetChanged();
                    mssgs.notifyDataSetChanged();
                    mBluetoothConnection.write(textField);
                    textInput.clearAnimation();
                    textInput.setText("");
                    runChat = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                                runOnUiThread(new Runnable() // start actions in UI thread
                                {

                                    @Override
                                    public void run() {
                                        while(true) {
                                            try {
                                                Thread.sleep(5000);
                                                String difWord = MainActivity.mBluetoothConnection.read();
                                                if (!difWord.equals("NothingRightNow") ) {
                                                    mssg.add(difWord);
                                                    mssgs.notifyDataSetChanged();
                                                }else {
                                                    mssg.add("Sorry I don't think I understand, can you explain that again?");
                                                    mssgs.notifyDataSetChanged();
                                                    break;
                                                }
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                });
                            } catch (InterruptedException e) {}
                        }
                    });
                    runChat.start();
                }else if(cameraUsed){
                    //TODO: send pic
                }else{

                }
            }
        });
    }
}