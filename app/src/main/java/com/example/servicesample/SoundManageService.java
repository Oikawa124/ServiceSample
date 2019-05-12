package com.example.servicesample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

public class SoundManageService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // メディアプレイヤーフィールド
    private MediaPlayer _player;

    @Override
    public void onCreate() {
        _player = new MediaPlayer(); }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        // 音声ファイルのURI文字列を生成
        String mediaFIleUriStr = "android.resource://"
                + getPackageName() + "/"
                + R.raw.song_kei_harujion;

        // 音声ファイルをもとにURIオブジェクトを生成
        Uri mediaFIleUri = Uri.parse(mediaFIleUriStr);

        try {

            // メディアプレイヤーに音声ファイルを設定
            _player.setDataSource(SoundManageService.this, mediaFIleUri);

            // 非同期でのメディア再生準備完了した際のリスナを設定
            _player.setOnPreparedListener(new PlayerPreparedListener());

            // メディア再生が終了した際のリスナを設定
            _player.setOnCompletionListener(new PlayerCompletionListener());

            //非同期でメディア再生を準備。
            _player.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (_player.isPlaying()) {
            _player.stop();
        }

        _player.release();

        _player = null;
    }

    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();

            /* 再生開始通知の処理 */

            // Notificationを作成するBuilderクラスを生成
            NotificationCompat.Builder builder = new
                    NotificationCompat.Builder(
                    SoundManageService.this,
                    "soundmanagerservice_notification_channel"
            );

            // 通知エリアに表示されるアイコンの設定
            builder.setSmallIcon(android.R.drawable.ic_dialog_info);

            // 通知ドロワーでの表示タイトルを設定
            builder.setContentTitle(getString(R.string.msg_notification_titile_start));

            // 通知ドロワーでの表示メッセージを設定
            builder.setContentText(getString(R.string.msg_notification_text_start));

            // 起動先Activityクラスを指定したIntentオブジェクトを生成
            Intent intent = new Intent(
                    SoundManageService.this,
                    MainActivity.class
            );

            //起動先アクティビティに引き継ぎデータを格納
            intent.putExtra("fromNotification", true);

            // PendingIntenオブジェクトを取得
            PendingIntent stopServiceIntent =
                    PendingIntent.getActivity(
                            SoundManageService.this,
                            0,
                            intent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );

            // PendingIntentオブジェクトをビルダーに設定
            builder.setContentIntent(stopServiceIntent);

            // タップされた通知メッセージを自動的に消去するように設定。
            builder.setAutoCancel(true);

            // BuilderからNotificationオブジェクトを生成
            Notification notification = builder.build();

            // NotificationManagerオブジェクトを生成
            NotificationManager manager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);

            // 通知
            manager.notify(1, notification);

        }
    }

    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            // mp.stop();

            /* 再生終了通知の処理 */

            // Notificationを作成するBuilderクラスを生成
            NotificationCompat.Builder builder = new
                    NotificationCompat.Builder(
                    SoundManageService.this,
                    "soundmanagerservice_notification_channel"
            );

            // 通知エリアに表示されるアイコンの設定
            builder.setSmallIcon(android.R.drawable.ic_dialog_info);

            // 通知ドロワーでの表示タイトルを設定
            builder.setContentTitle(getString(R.string.msg_notification_titile_finish));

            // 通知ドロワーでの表示メッセージを設定
            builder.setContentText(getString(R.string.msg_notification_text_finish));

            // BuilderからNotificationオブジェクトを生成
            Notification notification = builder.build();

            // NotificationManagerオブジェクトを生成
            NotificationManager manager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);

            // 通知
            manager.notify(0, notification);

            stopSelf();
        }
    }
}
