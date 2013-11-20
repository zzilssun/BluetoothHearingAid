package kr.mintech.bluetoothhearingaid.receivers;

import kr.mintech.bluetoothhearingaid.activities.EmergencyCallActivity;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DropModeRecordEndReceiver extends BroadcastReceiver
{
   @Override
   public void onReceive(Context $context, Intent $intent)
   {
      Intent intent = new Intent(StringConst.ACTION_DROP_MODE_RECORD_END);
      intent.setClass($context, EmergencyCallActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      $context.startActivity(intent);
   }
}
