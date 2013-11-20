package kr.mintech.bluetoothhearingaid.receivers;

import kr.mintech.bluetoothhearingaid.activities.VoiceRecordActivity;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class BluetoothReceiver extends BroadcastReceiver
{
   @Override
   public void onReceive(final Context $context, Intent $intent)
   {
      ContextUtil.CONTEXT = $context;
      Log.i("BluetoothReceiver.java | onReceive", "|" + $intent.getAction() + "|");
      
      String action = $intent.getAction();
      if (Intent.ACTION_MEDIA_BUTTON.equals(action))
      {
         KeyEvent keyEvent = (KeyEvent) $intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
         int currentKey = keyEvent.getKeyCode();
         
         Log.i("BluetoothReceiver.java | onReceive", "|" + $intent.getAction() + " | " + keyEvent.getAction());
         
         if (KeyEvent.ACTION_UP == keyEvent.getAction())
         {
            Log.i("BluetoothReceiver.java | onReceive", "|" + currentKey + "|");
            
            if (currentKey == KeyEvent.KEYCODE_MEDIA_PREVIOUS || currentKey == KeyEvent.KEYCODE_MEDIA_NEXT)
            {
               Intent intent = new Intent($context.getPackageName() + "." + StringConst.KEY_TOGGLE_RECORD_STATE);
               intent.putExtra(StringConst.KEY_RECORD_MODE, currentKey);// PREVIOUS=nomal, NEXT=drop
               intent.setClass($context, VoiceRecordActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               $context.startActivity(intent);
            }
            else
            {
               Log.i("BluetoothReceiver.java | onReceive", "|" + "forword" + "|");
               
               Intent intent = new Intent(StringConst.ACTION_FORWORD_BROADCAST);
               intent.putExtra(StringConst.KEY_CODE, currentKey);
               $context.sendBroadcast(intent);
            }
         }
      }
      else if ("android.media.VOLUME_CHANGED_ACTION".equals(action))
      {
         int volume = (Integer) $intent.getExtras().get("android.media.EXTRA_VOLUME_STREAM_VALUE");
         int lastVolume = PreferenceUtil.lastVolume();
         
         PreferenceUtil.putLastVolume(volume);
         
         if (volume < lastVolume)
         {
            Intent intent = new Intent(StringConst.ACTION_STOP_RECORDING);
            $context.sendBroadcast(intent);
         }
      }
   }
}
