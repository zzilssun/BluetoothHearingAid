package kr.mintech.bluetoothhearingaid.services;

import kr.mintech.bluetoothhearingaid.consts.StringConst;
import kr.mintech.bluetoothhearingaid.receivers.BluetoothReceiver;
import kr.mintech.bluetoothhearingaid.utils.MusicController;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class MediaButtonMonitorService extends Service
{
   public static final String LAST_MEDIA_BUTTON_RECEIVER = "last_media_button_receiver";
   
   public SettingsObserver mSettingsObserver;
   public ComponentName mComponentName;
   public AudioManager mAudioManager;
   
   
   @Override
   public IBinder onBind(Intent arg0)
   {
      return null;
   }
   
   
   @Override
   public void onCreate()
   {
      super.onCreate();
      Log.w("MediaButtonMonitorService.java | onCreate", "|" + "onCreate()" + "|");
      mComponentName = new ComponentName(getPackageName(), BluetoothReceiver.class.getName());
      mSettingsObserver = new SettingsObserver();
      mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
   }
   
   
   @Override
   public int onStartCommand(Intent intent, int flags, int startId)
   {
      super.onStartCommand(intent, flags, startId);
      
      Log.w("MediaButtonMonitorService.java | onStartCommand", "|" + "onStartCommand(" + intent + ", " + flags + ", " + startId + "|");
      registerMediaButtonReceiver();
      return START_STICKY;
   }
   
   
   @Override
   public void onDestroy()
   {
      unregisterMediaButtonReceiver();
      super.onDestroy();
   }
   
   
   public void registerMediaButtonReceiver()
   {
      Log.w("MediaButtonMonitorService.java | registerMediaButtonReceiver", "|" + "registerMediaButtonReceiver()" + "|");
      mAudioManager.registerMediaButtonEventReceiver(mComponentName);
      
      IntentFilter forwardFilter = new IntentFilter();
      forwardFilter.addAction(StringConst.FORWORD_BROADCAST);
      registerReceiver(forwordReceiver, forwardFilter);
   }
   
   
   public void unregisterMediaButtonReceiver()
   {
      Log.w("MediaButtonMonitorService.java | onDestroy", "|" + "onDestroy() called. Unregistering media button receiver." + "|");
      mAudioManager.unregisterMediaButtonEventReceiver(mComponentName);
      
      try
      {
         unregisterReceiver(forwordReceiver);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   private BroadcastReceiver forwordReceiver = new BroadcastReceiver()
   {
      @Override
      public void onReceive(Context $context, Intent $intent)
      {
         unregisterMediaButtonReceiver();
         int keycode = $intent.getIntExtra(StringConst.KEY_CODE, -1);
         Log.w("forwordReceiver | onReceive", "|" + keycode + "|");
         
         MusicController mCtrl = new MusicController(MediaButtonMonitorService.this);
         mCtrl.putKeyCommand(keycode);
         
         //1.5 초 뒤에 
         registerMediaButtonReceiver();
      }
   };
   
   private class SettingsObserver extends ContentObserver
   {
      ContentResolver mContentResolver;
      MediaButtonMonitorService mMonitorService = MediaButtonMonitorService.this;
      private static final String MEDIA_BUTTON_RECEIVER = "media_button_receiver";
      
      
      SettingsObserver()
      {
         super(new Handler());
         mContentResolver = mMonitorService.getContentResolver();
         mContentResolver.registerContentObserver(Settings.System.getUriFor(MEDIA_BUTTON_RECEIVER), false, this);
      }
      
      
      @Override
      public void onChange(boolean selfChange)
      {
         super.onChange(selfChange);
         Log.w("MediaButtonMonitorService.java | onChange", "|" + "onChange(" + selfChange + ")" + "|");
         String receiverName = Settings.System.getString(mContentResolver, MEDIA_BUTTON_RECEIVER);
         Log.w("MediaButtonMonitorService.java | onChange", "|" + "MEDIA_BUTTON_RECEIVER changed to " + receiverName + "|");
         String flatten = mMonitorService.mComponentName.flattenToString();
         Log.w("MediaButtonMonitorService.java | onChange", "|" + receiverName.equals(flatten) + " : '" + receiverName + "' == '" + flatten);
         
         if (!selfChange && !receiverName.equals(flatten))
         {
            mMonitorService.registerMediaButtonReceiver();
         }
//         if (!selfChange && !receiverName.equals(flatten))
//         {
////            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mMonitorService.getApplicationContext());
////            preferences.edit().putString(LAST_MEDIA_BUTTON_RECEIVER, receiverName).commit();
////            Log.w("SettingsObserver", "Set LAST_MEDIA_BUTTON_RECEIVER to" + receiverName);
//         mMonitorService.registerMediaButtonReceiver();
//         }
      }
   }
}
