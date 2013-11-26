package kr.mintech.bluetoothhearingaid.activities;

import kr.mintech.bluetoothhearingaid.bases.BaseVoiceRecordActivity;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import android.os.Bundle;

public class VoiceRecordActivity extends BaseVoiceRecordActivity
{
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      
      _path = StringConst.NORMAL_PATH;
      begin();
   }
}
