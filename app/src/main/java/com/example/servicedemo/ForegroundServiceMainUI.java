package com.example.servicedemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class ForegroundServiceMainUI extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceMainUIChannel";

    /* onCreate()
    The system invokes this method to perform one-time setup procedures when the service is initially created
     (before it calls either onStartCommand() or onBind()). If the service is already running, this method is
      not called.
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /*
    onStartCommand()
    The system invokes this method by calling startForeground() when another component (such as an activity)
    requests that the service be started. When this method executes, the service is started and can run
    in the background indefinitely. If you implement this, it is your responsibility to stop the service
    when its work is complete by calling stopSelf() or stopService(). If you only want to provide binding,
    you don't need to implement this method.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        //set up the notification (compulsory)
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service Demo")
                .setContentText("In FG Service main UI Thread")
                .setSmallIcon(R.drawable.ic_check_circle_24px)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
        // Stop the service using the startId, so that we don't stop
        // the service in the middle of handling another job
        stopSelf(startId);
        return START_NOT_STICKY;
        /*
        /////START_NOT_STICKY
        If the system kills the service after onStartCommand() returns, do not recreate the service unless there are pending
        intents to deliver. This is the safest option to avoid running your service when not necessary and when your application
        can simply restart any unfinished jobs.
        /////START_STICKY
        If the system kills the service after onStartCommand() returns, recreate the service and call onStartCommand(), but
        do not redeliver the last intent. Instead, the system calls onStartCommand() with a null intent unless there are pending
        intents to start the service. In that case, those intents are delivered.
        This is suitable for media players (or similar services) that are not executing commands but are running indefinitely
        and waiting for a job.
        /////START_REDELIVER_INTENT
        If the system kills the service after onStartCommand() returns, recreate the service and call onStartCommand() with the
        last intent that was delivered to the service. Any pending intents are delivered in turn. This is suitable for services
        that are actively performing a job that should be immediately resumed, such as downloading a file.
         */
    }

    /*
    onBind()
    The system invokes this method by calling bindService() when another component wants to bind with the
    service (such as to perform RPC). In your implementation of this method, you must provide an interface
    that clients use to communicate with the service by returning an IBinder. You must always implement this
    method; however, if you don't want to allow binding, you should return null.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    onDestroy()
    The system invokes this method when the service is no longer used and is being destroyed. Your service
    should implement this to clean up any resources such as threads, registered listeners, or receivers.
    This is the last call that the service receives.
    */
    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
