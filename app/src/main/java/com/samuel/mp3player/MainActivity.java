package com.samuel.mp3player;

import android.graphics.Path;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    Button[] buttons;
    SeekBar bar;
    MediaPlayer player;
    EditText etUrl;
    Map<String,File> mpFTV;
    File f;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mpFTV = new HashMap<>();
        f =  new File(Environment.getExternalStorageDirectory().getPath()+"/Music");
        etUrl = (EditText) findViewById(R.id.editText);
        File[] files;
        files = f.listFiles();
        listView = (ListView)findViewById(R.id.listView);
        bar = (SeekBar) findViewById(R.id.seekBar);
        for (final File fi : files){
            if (fi.getName().contains(".mp3")) {
                mpFTV.put(fi.getName(), fi);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, mpFTV.keySet().toArray(new String[mpFTV.size()]));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                player.stop();
                player = MediaPlayer.create(getApplicationContext(), Uri.parse(mpFTV.get(listView.getItemAtPosition(position).toString()).getAbsolutePath()));
                chgMusic();
                etUrl.setText(listView.getItemAtPosition(position).toString());
            }
        });

        buttons = new Button[]{(Button) findViewById(R.id.stop), (Button) findViewById(R.id.play), (Button) findViewById(R.id.pause)};
        player = MediaPlayer.create(getApplicationContext(), Uri.parse(etUrl.getText().toString()));
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.seekTo(0);
                bar.setProgress(0);
                player.pause();
            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setMax(player.getDuration());
                player.start();
            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
            }
        });
        bar.postDelayed(new Runnable() {
            @Override
            public void run() {
                bar.setProgress(player.getCurrentPosition());
                bar.postDelayed(this, 100);
            }
        }, 100);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    player.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
                seekBar.setProgress(seekBar.getProgress());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void chgMusic(){
        this.bar.setMax(player.getDuration());
        this.player.seekTo(0);
        this.bar.setProgress(0);
        this.player.start();
    }
}
