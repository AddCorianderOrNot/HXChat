package com.example.hxchat.service;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.hxchat.R;
import com.example.hxchat.app.util.CacheUtil;
import com.example.hxchat.app.util.Event;
import com.example.hxchat.data.model.bean.UserInfo;
import com.example.hxchat.data.packet.req.MessageReq;
import com.example.hxchat.data.packet.resp.MessageResp;
import com.example.hxchat.ui.activity.MainActivity;
import com.example.hxchat.util.Util;
import com.google.gson.Gson;
import com.king.easychat.netty.packet.Packet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;


public class JWebSocketClientService extends Service {
    public JWebSocketClient client;
    private JWebSocketClientBinder mBinder = new JWebSocketClientBinder();
    private final static int GRAY_SERVICE_ID = 1001;
    //灰色保活
    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
    PowerManager.WakeLock wakeLock;//锁屏唤醒
    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock()
    {
        if (null == wakeLock)
        {
            PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock)
            {
                wakeLock.acquire(10*60*1000L /*10 minutes*/);
            }
        }
    }

    //用于Activity和service通讯
    public class JWebSocketClientBinder extends Binder {
        public JWebSocketClientService getService() {
            return JWebSocketClientService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        Event.INSTANCE.registerEvent(this);
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //初始化websocket
        UserInfo user = CacheUtil.INSTANCE.getUser();
        String token = "default";
        if (user != null) {
            token = user.getToken();
        }
        Log.d("init", token);
        initSocketClient(token);
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
        NotificationChannel notificationChannel = null;

        //设置service为前台服务，提高优先级
        if (Build.VERSION.SDK_INT < 18) {
            //Android4.3以下 ，隐藏Notification上的图标
            startForeground(GRAY_SERVICE_ID, new Notification());
        } else if (Build.VERSION.SDK_INT > 18 && Build.VERSION.SDK_INT < 25) {
            //Android4.3 - Android7.0，隐藏Notification上的图标
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }else{
            //Android7.0以上app启动后通知栏会出现一条"正在运行"的通知
            String CHANNEL_ONE_ID = "CHANNEL_ONE_ID";
            String CHANNEL_ONE_NAME= "CHANNEL_ONE_ID";
            notificationChannel= new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, intent, 0);
            Notification notification= new Notification.Builder(this).setChannelId(CHANNEL_ONE_ID)
                    .setTicker("Nature")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("HXChat")
                    .setContentIntent(pendingIntent)
                    .setContentText("Running~")
                    .build();
            startForeground(GRAY_SERVICE_ID, notification);
        }

        acquireWakeLock();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        Event.INSTANCE.unregisterEvent(this);
        closeConnect();
        mHandler.removeCallbacks(heartBeatRunnable);
        super.onDestroy();
    }

    public JWebSocketClientService() {
    }


    /**
     * 初始化websocket连接
     */
    private void initSocketClient(String token) {
        token = "Bearer " + token;
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        URI uri = URI.create(Util.ws);
        Log.d("JWebSocketClientService", "访问网址:" + uri);
        client = new JWebSocketClient(uri, header) {
            @Override
            public void onMessage(String message) {
                Log.e("JWebSocketClientService", "收到的消息：" + message);
                Gson gson = new Gson();
                MessageResp messageResp = gson.fromJson(message, MessageResp.class);
                Event.INSTANCE.sendEvent(messageResp, false);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                Log.e("JWebSocketClientService", "websocket连接成功");
            }
        };
        connect();
    }

    /**
     * 连接websocket
     */
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 发送消息
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendMsg(MessageReq msg) {
        if (null != client) {
            Gson gson = new Gson();
            Log.e("JWebSocketClientService", "发送的消息：" + msg.toString());
            client.send(gson.toJson(msg));
        }
    }

    /**
     * 断开连接
     */
    private void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }


//    -----------------------------------消息通知--------------------------------------------------------

    /**
     * 检查锁屏状态，如果锁屏先点亮屏幕
     *
     * @param content
     */
    private void checkLockAndShowNotification(String content) {
        //管理锁屏的一个服务
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {//锁屏
            //获取电源管理器对象
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            if (!pm.isScreenOn()) {
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                wl.acquire(10*60*1000L /*10 minutes*/);  //点亮屏幕
                wl.release();  //任务结束后释放
            }
            sendNotification(content);
        } else {
            sendNotification(content);
        }
    }

    /**
     * 发送通知
     *
     * @param content
     */
    private void sendNotification(String content) {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                // 设置该通知优先级
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("服务器")
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                // 向通知添加声音、闪灯和振动效果
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .build();
        notifyManager.notify(1, notification);//id要保证唯一
    }


    //    -------------------------------------websocket心跳检测------------------------------------------------
    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("JWebSocketClientService", "心跳包检测websocket连接状态");
            if (client != null) {
                if (client.isClosed()) {
                    UserInfo user = CacheUtil.INSTANCE.getUser();
                    String token = "default";
                    if (user != null) {
                        token = user.getToken();
                    }
                    Log.d("rews", token);
                    initSocketClient(token);
//                    reconnectWs();
                }
            } else {
                //如果client已为空，重新初始化连接
                UserInfo user = CacheUtil.INSTANCE.getUser();
                String token = "default";
                if (user != null) {
                    token = user.getToken();
                }
                initSocketClient(token);
            }
            //每隔一定的时间，对长连接进行一次心跳检测
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    /**
     * 开启重连
     */
    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("JWebSocketClientService", "开启重连");
                    client.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
