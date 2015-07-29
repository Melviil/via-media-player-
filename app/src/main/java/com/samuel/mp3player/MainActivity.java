package com.samuel.mp3player;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;


public class MainActivity extends ActionBarActivity {
    Button[] buttons;
    SeekBar bar;
    MediaPlayer player;
    EditText etUrl;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        i = 0;
        etUrl = (EditText) findViewById(R.id.editText);
        buttons = new Button[]{(Button) findViewById(R.id.stop), (Button) findViewById(R.id.play), (Button) findViewById(R.id.pause)};
        bar = (SeekBar) findViewById(R.id.seekBar);
        player = MediaPlayer.create(getApplicationContext(), Uri.parse(etUrl.getText().toString()));
        buttons[0].setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              player.seekTo(0);
                                              bar.setProgress(0);
                                              player.pause();
                                          }
                                      }
        );
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
            }
        });
        bar.setMax(player.getDuration());
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
}
