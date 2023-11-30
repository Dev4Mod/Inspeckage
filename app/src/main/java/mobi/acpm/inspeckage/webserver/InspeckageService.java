package mobi.acpm.inspeckage.webserver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by acpm on 17/11/15.
 */
public class InspeckageService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private WebServer ws;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String host = null;
        int port = 8008;
        // Let it continue running until it is stopped.
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    "1001",
                    "Running",
                    NotificationManager.IMPORTANCE_LOW
            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(this, "1001")
                    .setContentText("Service is running on port: " + port)
                    .setContentTitle("Service enabled");

            startForeground(1001, notification.build());
        }

        Context context = getApplicationContext();

        if (intent != null && intent.getExtras() != null) {
            host = intent.getStringExtra("host");
            port = intent.getIntExtra("port", 8008);
        }

        try {

            ws = new WebServer(host, port, context);


        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Service started on port " + port, Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ws!=null)
            ws.stop();

        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }
}
