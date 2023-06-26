package com.example.robotbuttler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.sql.Time;
import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    Intent extra = getIntent();
    static Time previousTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button chat = findViewById(R.id.chatPage);
        Button remote = findViewById(R.id.remote);
        Button help = findViewById(R.id.help);
        Button update = findViewById(R.id.update);
        ListView autoList = findViewById(R.id.listView);
        ArrayList<String> mssg = new ArrayList<String>();
        mssg.add("Hello, for more updates please check out the \"Updates\" button.");
        ArrayAdapter mssgs = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mssg);
        autoList.setAdapter(mssgs);
        (new Thread(new Runnable() {

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
                                        Thread.sleep(2000);
                                        String difWord = MainActivity.mBluetoothConnection.read();
                                        if (!difWord.equals("NothingRightNow") && !difWord.equals("sendingPic")) {
                                            mssg.add(difWord);
                                            mssgs.notifyDataSetChanged();
                                        } else {
                                            mssg.add("Stopping updates for now");
                                            mssgs.notifyDataSetChanged();
                                            break;
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        // ooops
                    }
            }
        })).start();
       chat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent ChatBot = new Intent(MainMenu.this, ChatBot.class);
               startActivity(ChatBot);
           }
       });
        remote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent remoteController = new Intent(MainMenu.this, RemoteController.class);
                startActivity(remoteController);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent help = new Intent(MainMenu.this, HelpPage.class);
                startActivity(help);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent update = new Intent(MainMenu.this, UpdatePage.class);
                startActivity(update);
            }
        });

    }
}