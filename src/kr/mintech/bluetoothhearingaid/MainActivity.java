package kr.mintech.bluetoothhearingaid;

import kr.mintech.bluetoothhearingaid.activities.EmergencyCallActivity;
import kr.mintech.bluetoothhearingaid.activities.VoiceRecordActivity;
import kr.mintech.bluetoothhearingaid.services.MediaButtonMonitorService;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity
{
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.act_main);
      
      ContextUtil.CONTEXT = getApplicationContext();
      
      Intent serviceIntent = new Intent(getApplicationContext(), MediaButtonMonitorService.class);
      startService(serviceIntent);
      
      Button btnVoiceRecord = (Button) findViewById(R.id.btn_voice_record);
      btnVoiceRecord.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            showVoiceRecordAcitivity();
         }
      });
      
      Button btnEmergencyCall = (Button) findViewById(R.id.btn_emergency_call);
      btnEmergencyCall.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            showEmergencyCallAcitivity();
         }
      });
   }
   
   
   private void showVoiceRecordAcitivity()
   {
      Intent intent = new Intent(this, VoiceRecordActivity.class);
      startActivity(intent);
   }
   
   
   private void showEmergencyCallAcitivity()
   {
      Intent intent = new Intent(this, EmergencyCallActivity.class);
      startActivity(intent);
   }
}
