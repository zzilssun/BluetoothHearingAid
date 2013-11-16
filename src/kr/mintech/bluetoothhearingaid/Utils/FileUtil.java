package kr.mintech.bluetoothhearingaid.Utils;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

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
    * @return
    */
   public static String duration(Context $context, String $path)
   {
      String result = "00:00";
      
      String[] cursorColumns = new String[] { MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION };
      
      String selection = MediaStore.Images.Media.DATA + "=? ";
      String[] selectionArgs = new String[] { $path };
      Cursor cursor = $context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursorColumns, selection, selectionArgs,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
      
      cursor.moveToFirst();
      
      int duration = 0;
      while (!cursor.isAfterLast())
      {
         int dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
         int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
         
         if ($path.equals(cursor.getString(dataColumn)))
         {
            duration = cursor.getInt(durationColumn);
            break;
         }
         cursor.moveToNext();
      }
      
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(duration);
      
      result = DateFormatUtils.format(calendar, "mm:ss");
      
      return result;
   }
}
