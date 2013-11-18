package kr.mintech.bluetoothhearingaid.utils;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;

public class FileUtil
{
   /**
    * 재생 시간 가져오기
    * 
    * @param $context
    *           context
    * @param $path
    *           전체 경로<br>
    *           /storage/sdcard0/Sounds/voice_-1791181537.m4a
    * @return 00:00
    */
   public static String duration(Context $context, String $path)
   {
      String result = "00:00";
      
      MediaPlayer player = new MediaPlayer();
      
      try
      {
         player.setDataSource($path);
         player.prepare();
      }
      catch (Exception e)
      {
         
      }
      int duration = player.getDuration();
      player.release();
      
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(duration);
      
      result = DateFormatUtils.format(calendar, "mm:ss");
      
      return result;
   }
   
   
   /**
    * 파일명 가져오기
    * 
    * @param $path
    *           /storage/emulated/0/VoiceRecord/voice_20131118_113446.m4a
    * @return voice_20131118_113446.m4a
    */
   public static String filename(String $path)
   {
      String result = "";
      
      if (TextUtils.isEmpty($path))
         return result;
      else
      {
         File file = new File($path);
         result = file.getName();
      }
      
      return result;
   }
   
   
   /**
    * 파일명 가져오기(확장자 제외)
    * 
    * @param $path
    *           /storage/emulated/0/VoiceRecord/voice_20131118_113446.m4a
    * @return voice_20131118_113446
    */
   public static String filenameOnly(String $path)
   {
      String result = "";
      
      String[] split = filename($path).split("\\.");
      result = $path;
      
      if (split.length > 0)
         result = split[0];
      
      return result;
   }
}
