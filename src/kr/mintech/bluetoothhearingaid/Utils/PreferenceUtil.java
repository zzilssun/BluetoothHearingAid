package kr.mintech.bluetoothhearingaid.utils;

import kr.mintech.bluetoothhearingaid.bases.BasePreferenceUtil;

public class PreferenceUtil extends BasePreferenceUtil
{
   private static final String RECORDING = "RECORDING";
   private static final String LAST_RECORDED_FILE_PATH = "LAST_RECORDED_FILE_PATH";
   private static final String CALL_RECEIVER = "CALL_RECEIVER";
   private static final String SMS_RECEIVER = "SMS_RECEIVER";
   private static final String LAST_VOLUME = "LAST_VOLUME";
   
   
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
    * @param $path
    *           파일 경로(/storage/emulated/0/VoiceRecord/voice_20131118_102600.m4a)
    */
   public static void putLastRecordedFileFullPath(String $path)
   {
      put(LAST_RECORDED_FILE_PATH, $path);
   }
   
   
   /**
    * 마지막으로 녹음한 파일이름
    * 
    * @return 파일 경로(/storage/emulated/0/VoiceRecord/voice_20131118_102600.m4a)
    */
   public static String lastRecordedFileFullPath()
   {
      return get(LAST_RECORDED_FILE_PATH);
   }
   
   
   /**
    * 전화 받을 사람의 json 문자열 저장
    * 
    * @param $jsonStr
    *           {"phone":"1234","name":"asdf"}
    */
   public static void putCallReceiver(String $jsonStr)
   {
      put(CALL_RECEIVER, $jsonStr);
   }
   
   
   /**
    * 전화받을 사람의 json 문자열
    * 
    * @return {"phone":"1234","name":"asdf"}
    */
   public static String callReceiver()
   {
      return get(CALL_RECEIVER);
   }
   
   
   /**
    * 문자 받을 사람들의 json 문자열 저장
    * 
    * @param $jsonStr
    *           [{"phone":"1234","name":"asdf"},
    *           {"phone":"12345","name":"asdf1"}]
    */
   public static void putSMSReceiver(String $jsonStr)
   {
      put(SMS_RECEIVER, $jsonStr);
   }
   
   
   /**
    * 전화받을 사람들의 json 문자열
    * 
    * @return [{"phone":"1234","name":"asdf"}, {"phone":"12345","name":"asdf1"}]
    */
   public static String smsReceiver()
   {
      return get(SMS_RECEIVER);
   }
   
   
   /**
    * 마지막 음량 저장
    * 
    * @param $volume
    */
   public static void putLastVolume(int $volume)
   {
      put(LAST_VOLUME, $volume);
   }
   
   
   /**
    * 마지막 음량
    * 
    * @return
    */
   public static int lastVolume()
   {
      return get(LAST_VOLUME, 100);
   }
   
}
