package com.example.kiit.remindmeplease;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

public class Notify extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        //Bundle events = getIntent().getExtras();
        Bundle b = intent.getExtras();
        String []message = b.getString("Event").split("#");
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic3);
        builder.setContentTitle("Here's your Reminder -->");
        builder.setContentText(message[0]);
        builder.setAutoCancel(true);
        notificationManager.notify(1234,builder.build());
        Handler h = new Handler();
        long delayInMilliseconds = 60000;
        h.postDelayed(new Runnable() {
            public void run() {
                //NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1234);
            }
        }, delayInMilliseconds);
        DatabaseHandler db = new DatabaseHandler(context,null,null,1);
        db.delEvents(message[1]);
        Intent i = new Intent(context,Main2Activity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("desc",message[0]);
        context.startActivity(i);
    }
}
