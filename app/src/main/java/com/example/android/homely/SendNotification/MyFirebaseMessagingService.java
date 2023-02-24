package com.example.android.homely.SendNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.android.homely.Booking.MyBookingsActivity;
import com.example.android.homely.MainActivity;
import com.example.android.homely.Profile.MyTourActivity;
import com.example.android.homely.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public String title, message, id;
    public int uid;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title = remoteMessage.getData().get("Title");
        message = remoteMessage.getData().get("Message");
        id = getID();
        uid = new Random().nextInt();
        Intent myTour = new Intent(getApplicationContext(), MyTourActivity.class);
        Intent myBooking = new Intent(getApplicationContext(), MyBookingsActivity.class);
        Intent admin = new Intent(getApplicationContext(), MainActivity.class);

        myTour.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        myTour.setAction(Intent. ACTION_MAIN ) ;
        myTour.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;

        myBooking.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        myBooking.setAction(Intent. ACTION_MAIN ) ;
        myBooking.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;

        admin.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        admin.setAction(Intent. ACTION_MAIN ) ;
        admin.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;

        PendingIntent myTourP = PendingIntent.getActivity(getApplicationContext(), 0, myTour, 0);
        PendingIntent myBookingP = PendingIntent.getActivity(getApplicationContext(), 0, myBooking, 0);
        PendingIntent adminP = PendingIntent.getActivity(getApplicationContext(), 0, admin, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), id)
                .setSmallIcon(R.drawable.ic_outline_notifications_24)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (title.equals("New Deal")){
            builder.setContentIntent(adminP);
        }else if(title.equals("New Tour")){
            builder.setContentIntent(adminP);
        }else if (title.equals("Deal")){
            builder.setContentIntent(myBookingP);
        }else if(title.equals("Tour")){
            builder.setContentIntent(myTourP);
        }

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(id, id, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId(id);
        }

        notificationManager.notify(uid, builder.build());
    }

    private String getID() {
        DateFormat dateFormat = new SimpleDateFormat("yyddmm");
        Date date = new Date();
        String dt=String.valueOf(dateFormat.format(date));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("HHmmss");
        String tm= String.valueOf(time.format(new Date()));
        String id= dt+tm;
        System.out.println(id);
        return id;
    }
}
