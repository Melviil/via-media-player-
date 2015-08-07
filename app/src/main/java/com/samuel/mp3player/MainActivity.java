package com.samuel.mp3player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;


public class MainActivity extends Activity {
    Boolean connected;
    public static Button[] buttons;
    SeekBar bar;
    EditText etUrl;
    Mp3Service mp3Service;
    Button choose;
    String nameSong;

    //public static Button playB;  //define it globally

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
        setContentView(R.layout.activity_main);
        choose = (Button) findViewById(R.id.choose);
        etUrl = (EditText) findViewById(R.id.editText);
        bar = (SeekBar) findViewById(R.id.seekBar);
        //playB = (Button) findViewById(R.id.play);  //put it inside the oncreate of main activity
        buttons = new Button[]{(Button) findViewById(R.id.stop), (Button) findViewById(R.id.play), (Button) findViewById(R.id.pause)};
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp3Service.getMediaPLayer().seekTo(0);
                bar.setProgress(0);
                mp3Service.pause();
            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setMax(mp3Service.getMediaPLayer().getDuration());
                mp3Service.play();
            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp3Service.pause();
            }
        });
        bar.postDelayed(new Runnable() {
            @Override
            public void run() {
                bar.setProgress(mp3Service.getMediaPLayer().getCurrentPosition());
                bar.postDelayed(this, 100);
            }
        }, 100);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mp3Service.getMediaPLayer().seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp3Service.getMediaPLayer().seekTo(seekBar.getProgress());
                seekBar.setProgress(seekBar.getProgress());
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ExplorerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(this, Mp3Service.class);
        bindService(i, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        etUrl.setText(mp3Service.getNameSong());
        bar.setProgress(0);
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
}
