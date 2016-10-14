package local.is161518_is161523.mymusicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;



/*
AUTHOR: is161518/ is161523
AUTHOR-NAME: KRAUS Armin / PLANK Andreas

DESCRIPTION:
Music Player using Foreground Service

SOURCES:
http://blog.nkdroidsolutions.com/android-foreground-service-example-tutorial/
http://stackoverflow.com/questions/2468874/how-can-i-update-information-in-an-android-activity-from-a-background-service/2469646#2469646

INTERNAL:
currently missing:
Ask User to grant rights for access to Storage [READ_EXTERNAL_STORAGE]
*/

public class MainActivity extends AppCompatActivity{

    //VAR
    public static String TAG ="1234";
    private SeekBar sb_Status;
    ForegroundService mService;
    //Serviced binded or not
    boolean mBound = false;
    //handler is used for waiting in thread
    private final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn_play = (Button) findViewById(R.id.btn_play);
        Button btn_stop = (Button) findViewById(R.id.btn_stop);
        sb_Status = (SeekBar) findViewById(R.id.sb_Status);

        //Button Listener
        btn_play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                Log.i(TAG,"Start Intent");
                startSeekBarUpdater();

            }
        });

        //Button Listener
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(MainActivity.this, ForegroundService.class);
                stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                startService(stopIntent);
                Log.i(TAG,"Stop Intent");
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ForegroundService.class);
        /*
        Bind to ForegroundService
        onServiceConnected() will be called from android when connection should be established
        */
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /* Required Interface used by bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            // We've bound to the running Service, cast the IBinder and get instance
            ForegroundService.LocalBinder binder = (ForegroundService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    //Updates SeekBar Status
    public void startSeekBarUpdater() {

        Runnable not = new Runnable() {
            public void run() {
                //Update SeekBar as long as music is playing
                if(mService.getMediaPlayerPlaying()) {
                    startSeekBarUpdater();
                    int progress = mService.getProgressPercentage();
                    sb_Status.setProgress(progress);
                    Log.i(TAG, "THREAD: " + Integer.toString(progress));
                }
                else {
                    sb_Status.setProgress(0);
            }
            }
        };
        handler.postDelayed(not,1000);
    }

}//Class MainActivity End
