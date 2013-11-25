package kr.mintech.bluetoothhearingaid.consts;

import android.os.Environment;

public class StringConst
{
   private static final String BASE_PATH = Environment.getExternalStorageDirectory() + "/VoiceRecorder/BluetoothHearingAid";
   public static final String NORMAL_PATH = BASE_PATH + "/normal";
   public static final String EMERGENCY_PATH = BASE_PATH + "/emergency";
   
   public static final String ACTION_FORWORD_BROADCAST = "ACTION_FORWORD_BROADCAST";
   public static final String ACTION_STOP_RECORDING = "ACTION_STOP_RECORDING";
   public static final String ACTION_DROP_MODE_RECORD_END = "kr.mintech.bluetoothhearingaid.ACTION_DROP_MODE_RECORD_END";
   
   public static final String KEY_PATH = "KEY_PATH";
   public static final String KEY_CALL_SMS_TYPE = "KEY_CALL_SMS_TYPE";
   public static final String KEY_START_RECORDING_ON_OPEN = "KEY_START_RECORDING_ON_OPEN";
   public static final String KEY_TOGGLE_RECORD_STATE = "KEY_TOGGLE_RECORD_STATE";
   public static final String KEY_CODE = "KEY_CODE";
   public static final String KEY_RECORD_MODE = "KEY_RECORD_MODE"; // 일반적인 모드인지, 낙하 모드인지
}
