package com.example.servicesample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 通知チャンネルのID文字列を用意
        String id = "soundmanagerservice_notification_channel";

        // 通知チャンネル名をstring.xmlから取得
        String name = getString(R.string.notification_channel_name);

        // 通知チャンネルの重要度を標準に設定
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        // 通知チャンネルを生成
        NotificationChannel channel = new NotificationChannel(id, name, importance);

        // NotificationManagerオブジェクトを取得
        NotificationManager manager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE
        );

        // 通知チャンネルの設定
        manager.createNotificationChannel(channel);

        // Intentオブジェクトを取得
        Intent intent = getIntent();

        // 通知のタップから引き継ぎデータを取得
        boolean fromNotification = intent.getBooleanExtra(
                "fromNotification",
                false
        );

        // 引継ぎデータが存在、通知のタップから来た場合
        if (fromNotification) {
            Button btPlay = findViewById(R.id.btPlay);
            Button btStop = findViewById(R.id.btStop);

            btPlay.setEnabled(false);
            btStop.setEnabled(true);
        }


    }

    public void onPlayButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this,
                SoundManageService.class);

        // サービスを開始
        startService(intent);

        Button btPlay = findViewById(R.id.btPlay);
        Button btStop = findViewById(R.id.btStop);

        btPlay.setEnabled(false);
        btStop.setEnabled(true);
    }

    public void onStopButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this,
                SoundManageService.class);

        // サービスを停止
        stopService(intent);


        Button btPlay = findViewById(R.id.btPlay);
        Button btStop = findViewById(R.id.btStop);

        btPlay.setEnabled(true);
        btStop.setEnabled(false);
    }
}
