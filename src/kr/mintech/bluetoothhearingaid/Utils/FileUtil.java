package kr.mintech.bluetoothhearingaid.utils;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
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
      return duration($context, $path, "mm:ss");
   }
   
   
   /**
    * 재생시간 가져오기
    * 
    * @param $context
    *           context
    * @param $path
    *           전체 경로<br>
    *           /storage/sdcard0/Sounds/voice_-1791181537.m4a
    * @param $format
    *           date format<br>
    *           default : "mm:ss"
    * @return
    */
   public static String duration(Context $context, String $path, String $format)
   {
      String result = DateFormatUtils.format(0, $format);
      
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
      player.reset();
      player.release();
      player = null;
      
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(duration);
      
      result = DateFormatUtils.format(calendar, $format);
      
      return result;
   }
   
   
   /**
    * 파일명 가져오기
    * 
    * @param $filename
    *           /storage/emulated/0/VoiceRecord/voice_20131118_113446.m4a
    * @return voice_20131118_113446.m4a
    */
   public static String filename(String $filename)
   {
      String result = "";
      
      if (TextUtils.isEmpty($filename))
         return result;
      else
      {
         File file = new File($filename);
         result = file.getName();
      }
      
      return result;
   }
   
   
   /**
    * 파일명 가져오기(확장자 제외)
    * 
    * @param $filename
    *           /storage/emulated/0/VoiceRecord/voice_20131118_113446.m4a
    * @return voice_20131118_113446
    */
   public static String filenameOnly(String $filename)
   {
      String result = "";
      
      String[] split = filename($filename).split("\\.");
      result = $filename;
      
      if (split.length > 0)
         result = split[0];
      
      return result;
   }
   
   
   /**
    * 마지막에 생성된 파일명 뒤의 숫자 만들기
    * 
    * @param $path
    *           /storage/emulated/0/VoiceRecord/
    * @return "00004"
    */
   public static String nextFilename(String $path)
   {
      String result = "00001";
      
      File path = new File($path);
      if (!path.exists())
         return result;
      
      try
      {
         File lastFile = path.listFiles()[path.listFiles().length - 1];
         String fileName = filenameOnly(lastFile.toString());
         int lastNum = Integer.parseInt(fileName.substring(fileName.length() - 5));
         lastNum += 1;
         result = StringUtils.leftPad(lastNum + "", 5, "0");
      }
      catch (Exception e)
      {
//         e.printStackTrace();
      }
      
      return result;
   }
}
