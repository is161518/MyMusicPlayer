package local.is161518_is161523.mymusicplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
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
import android.widget.SeekBar;
import android.widget.Toast;
import android.os.IBinder;

import java.io.File;
import java.io.IOException;

/*
SOURCES
http://blog.nkdroidsolutions.com/android-foreground-service-example-tutorial/
http://stackoverflow.com/questions/2468874/how-can-i-update-information-in-an-android-activity-from-a-background-service/2469646#2469646


Playing Music works

missing:
Ask User to grant access


 */

public class MainActivity extends AppCompatActivity{




    public static String TAG ="1234";
    private SeekBar sb_Status;
    private boolean runThread=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_play = (Button) findViewById(R.id.btn_play);
        Button btn_stop = (Button) findViewById(R.id.btn_stop);
        sb_Status = (SeekBar) findViewById(R.id.sb_Status);

        btn_play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                Log.i(TAG,"Start Intent");
                startPlayProgressUpdater();

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





    }

    ForegroundService mService;
    boolean mBound = false;

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to Your Service
        Intent intent = new Intent(this, ForegroundService.class);
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

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to the running Service, cast the IBinder and get instance
            ForegroundService.LocalBinder binder = (ForegroundService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    private final Handler handler = new Handler();

    public void startPlayProgressUpdater() {


        Runnable not = new Runnable() {
            public void run() {
                startPlayProgressUpdater();
                int progress = mService.getProgressPercentage();
                sb_Status.setProgress(progress);
                Log.i(TAG, "THREAD: " + Integer.toString(progress));
            }
        };
        handler.postDelayed(not,1000);
    }
/*
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

                int progress = mService.getProgressPercentage();
                Log.i(TAG, "THREAD: " + Integer.toString(progress));
                mUpdateTimeTask.run();
            handler.postDelayed(mUpdateTimeTask,1000);


        }
    };*/

}
