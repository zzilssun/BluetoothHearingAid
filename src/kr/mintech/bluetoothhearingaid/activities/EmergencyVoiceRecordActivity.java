package kr.mintech.bluetoothhearingaid.activities;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.bases.BaseVoiceRecordActivity;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import android.os.Bundle;

public class EmergencyVoiceRecordActivity extends BaseVoiceRecordActivity
{
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.act_voice_record1);
      
      _path = StringConst.EMERGENCY_PATH;
      begin();
   }
}
