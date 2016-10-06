package local.is161518_is161523.mymusicplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static String TAG ="1234";
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_play = (Button) findViewById(R.id.btn_play);
        Button btn_stop = (Button) findViewById(R.id.btn_stop);

        btn_play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                Log.i(TAG,"Start Intent");
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(MainActivity.this, ForegroundService.class);
                stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                startService(stopIntent);
            }
        });


    play();
    }

    public void play(){
        String PATH_TO_FILE = "/sdcard/Music/test.mp3";
  /*'      mediaPlayer = new  MediaPlayer();
        try {
            mediaPlayer.setDataSource(PATH_TO_FILE);
            mediaPlayer.prepare();
        }catch (IOException e){Log.i(TAG,"IOException"+e.toString());}
        mediaPlayer.start();
*/


        File file = new File(PATH_TO_FILE);
        if(file.exists())
        {
            Log.i(TAG,"File exist");
        }
        else
        {
            Log.i(TAG,"NO File");
        }

            MediaPlayer mPlayer = new MediaPlayer();
        Uri myUri = Uri.parse(PATH_TO_FILE);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try{
        mPlayer.setDataSource(getApplicationContext(), myUri);
        mPlayer.prepare();
        }catch (IOException e){Log.i(TAG,"IOException: "+e.toString());}
        mPlayer.start();
    }





}
