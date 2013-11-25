package kr.mintech.bluetoothhearingaid;

import kr.mintech.bluetoothhearingaid.activities.EmergencyCallActivity;
import kr.mintech.bluetoothhearingaid.activities.VoiceRecordActivity;
import kr.mintech.bluetoothhearingaid.services.MediaButtonMonitorService;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity
{
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.act_main);
      
      Log.i("MainActivity.java | onCreate", "|" + "$$$$$$$$$$$$$$$$$$$$$$$$$$"+"|");
      
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
      
      Button btnEmergencyCall = (Button) findViewById(R.id.btn_emergency_call_config);
      btnEmergencyCall.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            showEmergencyCallAcitivity();
         }
      });
      
      // q보이스에서 '음성녹음'으로 들어온거면 파일 재생
      if (MediaStore.Audio.Media.RECORD_SOUND_ACTION.equals(getIntent().getAction()))
         showVoiceRecordAcitivityWithPlay();
   }
   
   @Override
   public void onConfigurationChanged(Configuration newConfig)
   {
//      super.onConfigurationChanged(newConfig);
   }
   
   
   private void showVoiceRecordAcitivity()
   {
      Intent intent = new Intent(this, VoiceRecordActivity.class);
      startActivity(intent);
   }
   
   
//   q보이스에서 '음성녹음'으로 들어온거면 파일 재생
   private void showVoiceRecordAcitivityWithPlay()
   {
      Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
      intent.setClass(getApplicationContext(), VoiceRecordActivity.class);
      startActivity(intent);
   }
   
   
   private void showEmergencyCallAcitivity()
   {
      Intent intent = new Intent(this, EmergencyCallActivity.class);
      startActivity(intent);
   }
}
