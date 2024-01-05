package com.example.contoh;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class MyApplication extends Application {

    // Replace the below with your own ONESIGNAL_APP_ID
    private static final String ONESIGNAL_APP_ID = "ccf10458-33fd-485b-bee2-a45f8c816de2";
    private static MyApplication singleton;

    public MyApplication getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();

        OneSignal.addSubscriptionObserver(new OSSubscriptionObserver() {
            @Override
            public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
                if (!stateChanges.getFrom().isSubscribed() &&
                        stateChanges.getTo().isSubscribed()) {
                    stateChanges.getTo().getUserId();
                }
            }
        });

        OneSignal.setNotificationOpenedHandler(new OneSignal.OSNotificationOpenedHandler() {
            @Override
            public void notificationOpened(OSNotificationOpenedResult result) {
                try{
                    JSONObject data = result.getNotification().getAdditionalData();
                    String page_link =  data.getString("page_link");
                    if(page_link.equals("aktivitas")){
                        Intent intent = new Intent(singleton, Activity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        singleton.startActivity(intent);
                    }else if(page_link.equals("laporan_balita")){
                        String link_id =  data.getString("link_id");
                        Intent intent = new Intent(singleton, DetailPemeriksaanBalita.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle b = new Bundle();
                        b.putString("link_id", link_id);
                        intent.putExtras(b);
                        singleton.startActivity(intent);
                    }else if(page_link.equals("laporan_ibu_hamil")){
                        String link_id =  data.getString("link_id");
                        Intent intent = new Intent(singleton, DetailPemeriksaanIbuHamil.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle b = new Bundle();
                        b.putString("link_id", link_id);
                        intent.putExtras(b);
                        singleton.startActivity(intent);
                    }else if(page_link.equals("laporan_lansia")){
                        String link_id =  data.getString("link_id");
                        Intent intent = new Intent(singleton, DetailPemeriksaanLansia.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle b = new Bundle();
                        b.putString("link_id", link_id);
                        intent.putExtras(b);
                        singleton.startActivity(intent);
                    }else{
                        Intent intent = new Intent(singleton, Activity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        singleton.startActivity(intent);
                    }
                }catch (Exception e){
                    Intent intent = new Intent(singleton, Activity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    singleton.startActivity(intent);
                }
            }
        });
    }
}
