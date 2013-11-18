package kr.mintech.bluetoothhearingaid.utils;

import kr.mintech.bluetoothhearingaid.bases.BasePreferenceUtil;

public class PreferenceUtil extends BasePreferenceUtil
{
   private static final String RECORDING = "RECORDING";
   private static final String LAST_RECORDED_FILENAME = "LAST_RECORDED_FILENAME";
   
   
   /**
    * 현재 레코딩 중인가
    * 
    * @param $state
    *           true=레코딩 중
    */
   public static void putIsRecording(boolean $state)
   {
      put(RECORDING, $state);
   }
   
   
   /**
    * 현재 레코딩 중인가
    * 
    * @return true=레코딩 중
    */
   public static boolean isRecording()
   {
      return get(RECORDING, false);
   }
   
   
   /**
    * 마지막으로 녹음한 파일이름.<br>
    * 레코딩이 끝나고 파일이름을 바꾸기 위해 사용함
    * 
    * @param $filename
    *           파일 경로(/storage/emulated/0/VoiceRecord/voice_20131118_102600.m4a)
    */
   public static void putLastRecordedFilename(String $filename)
   {
      put(LAST_RECORDED_FILENAME, $filename);
   }
   
   
   /**
    * 마지막으로 녹음한 파일이름
    * 
    * @return 파일 경로(/storage/emulated/0/VoiceRecord/voice_20131118_102600.m4a)
    */
   public static String lastRecordedFilename()
   {
      return get(LAST_RECORDED_FILENAME);
   }
}
