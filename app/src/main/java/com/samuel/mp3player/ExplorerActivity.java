package com.samuel.mp3player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by samuel on 06/08/15.
 */
public class ExplorerActivity extends Activity {
    Boolean connected;
    File folder;
    List<File> files;
    List<String> names;
    Mp3Service mp3Service;
    ListView listView;
    File heyWhatSUp;
    ArrayAdapter<String> arrayAdapter;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            connected = true;
            mp3Service = ((Mp3Service.MyBinder) service).getMyService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
            mp3Service = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);
        listView = (ListView) findViewById(R.id.listView);
        folder = new File(Environment.getExternalStorageDirectory().getPath() + "/Music");
        files = new ArrayList<>(Arrays.asList(folder.listFiles()));
        names = new ArrayList<>(files.size());
        for (File file :
                files) {
            names.add(file.getName());
        }
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, names);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                heyWhatSUp = files.get(position);
                if (heyWhatSUp.isDirectory()) {
                    changeFolder(heyWhatSUp.getAbsolutePath());
                }
                if (heyWhatSUp.getName().contains(".mp3")) {
                    mp3Service.changeMusic(heyWhatSUp.getPath(),heyWhatSUp.getName());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(this, Mp3Service.class);
        bindService(i, serviceConnection, BIND_AUTO_CREATE);
    }

    public void changeFolder(String path) {
        folder = new File(path);
        files = new ArrayList<>(Arrays.asList(folder.listFiles()));
        names = new ArrayList<>(files.size());
        for (File file :
                files) {
            names.add(file.getName());
        }
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, names);
    }
}
