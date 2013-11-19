package kr.mintech.bluetoothhearingaid.consts;

import android.os.Environment;

public class StringConst
{
   public static final String PATH = Environment.getExternalStorageDirectory() + "/VoiceRecorder/BluetoothHearingAid";
   public static final String KEY_PATH = "KEY_PATH";
   public static final String KEY_CALL_SMS_TYPE = "KEY_CALL_SMS_TYPE";
   public static final String KEY_START_RECORDING_ON_OPEN = "KEY_START_RECORDING_ON_OPEN";
   
   // 녹음
   public static final String KEY_TOGGLE_RECORD_STATE = "KEY_TOGGLE_RECORD_STATE";
   
   public static final String FORWORD_BROADCAST = "FORWORD_BROADCAST";
   public static final String STOP_RECORING = "STOP_RECORING";
   public static final String KEY_CODE = "key_code";
}
