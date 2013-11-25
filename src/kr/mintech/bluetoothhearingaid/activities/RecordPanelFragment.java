package kr.mintech.bluetoothhearingaid.activities;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.consts.NumberConst;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import kr.mintech.bluetoothhearingaid.utils.FileUtil;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;

public class RecordPanelFragment extends Fragment
{
   private ImageButton _btnRecordStart, _btnRecordStop;
   private Chronometer _chronometer;
   private AudioManager _audioManager;
   private MediaRecorder _recorder;
   private RecordEndCallback _recordEndCallback;
   private boolean _startRecordingOnOpen = false;
   private int _recordMode = NumberConst.RECORD_MODE_NORMAL;
   private boolean _isStoped = false;
   private String _path = StringConst.NORMAL_PATH;
   
   
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      
      _audioManager = (AudioManager) getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
      ContextUtil.CONTEXT = getActivity().getApplicationContext();
      
      IntentFilter filter = new IntentFilter();
      filter.addAction(StringConst.ACTION_STOP_RECORDING);
      getActivity().registerReceiver(stopRecordRecodingReceiver, filter);
      
      _startRecordingOnOpen = getArguments().getBoolean(StringConst.KEY_START_RECORDING_ON_OPEN);
      _recordMode = getArguments().getInt(StringConst.KEY_RECORD_MODE);
      _path = getArguments().getString(StringConst.KEY_PATH);
   }
   
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
      View view = inflater.inflate(R.layout.fragment_record_panel, container, false);
      
      _chronometer = (Chronometer) view.findViewById(R.id.chronometer);
      
      _btnRecordStart = (ImageButton) view.findViewById(R.id.btn_record_start);
      _btnRecordStart.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            startRecord();
         }
      });
      
      _btnRecordStop = (ImageButton) view.findViewById(R.id.btn_record_stop);
      _btnRecordStop.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            stopRecord();
         }
      });
      
      if (_startRecordingOnOpen)
         startRecord();
      
      return view;
   }
   
   
   @Override
   public void onDestroyView()
   {
      getActivity().unregisterReceiver(stopRecordRecodingReceiver);
      PreferenceUtil.putIsRecording(false);
      stopRecord();
      super.onDestroyView();
   }
   
   
   /**
    * 녹음 시작
    */
   public void startRecord()
   {
      File path = new File(_path);
      if (!path.exists())
         path.mkdirs();
      
//      Calendar calendar = Calendar.getInstance();
//      String currentDateStr = DateFormatUtils.format(calendar, "yyyyMMdd_HHmmss");
      
      String prefix = _recordMode == NumberConst.RECORD_MODE_NORMAL ? "메모 " : "응급 ";
      File file = new File(path + File.separator + prefix + FileUtil.nextFilename(_path) + ".m4a");
      
      try
      {
         file.createNewFile();
      }
      catch (IOException e)
      {
         e.printStackTrace();
         return;
      }
      
      _btnRecordStop.setEnabled(true);
      _btnRecordStart.setEnabled(false);
      Log.i("RecordPanelFragment.java | startRecord", "|" + file.toString() + "|");
      PreferenceUtil.putLastRecordedFileFullPath(file.toString());
      
      _audioManager.startBluetoothSco();
      
      try
      {
         _recorder = new MediaRecorder();
         _recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
         _recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
         _recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
         _recorder.setOutputFile(file.toString());
         _recorder.prepare();
         _recorder.start();
         PreferenceUtil.putIsRecording(true);
         _chronometer.setBase(SystemClock.elapsedRealtime());
         _chronometer.start();
         
         if (_recordMode == NumberConst.RECORD_MODE_DROP)
            autoStopWhenDropMode();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   
   // 추락 모드에서는 자동으로 15초 후에 종료
   private void autoStopWhenDropMode()
   {
      PreferenceUtil.putIsDropModeRecording(true);
      Runnable runn = new Runnable()
      {
         @Override
         public void run()
         {
            if (PreferenceUtil.isDropModeRecording())
               stopDropModeRecord(false);
         }
      };
      
      Handler handler = new Handler();
      handler.postDelayed(runn, 15000);
   }
   
   
   /**
    * 녹음 종료
    */
   public void stopRecord()
   {
      if (_isStoped)
         return;
      
      _isStoped = true;
      
      if (!PreferenceUtil.isRecording())
         return;
      if (PreferenceUtil.isDropModeRecording())
         stopDropModeRecord(true);
      else
         stopNomalModeRecord();
   }
   
   
   private void stopNomalModeRecord()
   {
      Log.i("RecordPanelFragment.java | stopRecord", "|" + "stop" + "|" + PreferenceUtil.lastRecordedFileFullPath());
      _chronometer.stop();
      
      try
      {
         _audioManager.stopBluetoothSco();
         _recorder.stop();
         _recorder.reset();
         _recorder.release();
         _recorder = null;
         
         PreferenceUtil.putIsRecording(false);
         String filename = PreferenceUtil.lastRecordedFileFullPath();
         _recordEndCallback.onRecordEnd(filename);
      }
      catch (Exception e)
      {
//         e.printStackTrace();
      }
      
      _btnRecordStop.setEnabled(false);
      _btnRecordStart.setEnabled(true);
      
      try
      {
         getFragmentManager().beginTransaction().remove(this).commit();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      
      // XXX: 10이상 녹음하면 강제로 일정을 등록해버린다.
      try
      {
         int second = Integer.parseInt(_chronometer.getText().toString().split(":")[1]);
         if (second > 10)
         {
            addScheduleToCalendar();
         }
      }
      catch (Exception e)
      {
//         e.printStackTrace();
      }
   }
   
   
   /**
    * 낙하 모드 녹음 중지
    * 
    * @param $stopByUser
    *           사용자가 중단시켰는가
    */
   private void stopDropModeRecord(boolean $stopByUser)
   {
      stopNomalModeRecord();
      PreferenceUtil.putIsDropModeRecording(false);
      
      Log.i("RecordPanelFragment.java | stopDropModeRecord", "| drop mode stop by user? " + $stopByUser + "|");
      if ($stopByUser)
         return;
      
      // 사용자가 중단 시키지 않았을 때는 sms 보낸 후 전화 걸기
      Intent intent = new Intent(StringConst.ACTION_DROP_MODE_RECORD_END);
      getActivity().sendBroadcast(intent);
   }
   
   
   private void addScheduleToCalendar()
   {
      String projection[] = { "_id" };
      Uri calendars = Uri.parse("content://com.android.calendar/calendars");
      
      ContentResolver cr = getActivity().getContentResolver();
      Cursor managedCursor = cr.query(calendars, projection, null, null, null);
      managedCursor.moveToFirst();
      String calID = managedCursor.getString(0);
      managedCursor.close();
      
      long startMillis = 0;
      
      Calendar beginTime = Calendar.getInstance();
      long tomorrow = DateUtils.addDays(beginTime.getTime(), +1).getTime();
      int year = Integer.parseInt(DateFormatUtils.format(tomorrow, "yyyy"));
      int month = Integer.parseInt(DateFormatUtils.format(tomorrow, "MM")) - 1;
      int day = Integer.parseInt(DateFormatUtils.format(tomorrow, "dd"));
      int hour = Integer.parseInt(DateFormatUtils.format(tomorrow, "HH"));
      int minute = Integer.parseInt(DateFormatUtils.format(tomorrow, "mm"));
      
      beginTime.set(year, month, day, hour, minute);
      startMillis = beginTime.getTimeInMillis();
      Log.i("MainActivity.java | onCreate", "|" + DateFormatUtils.format(startMillis, "yyyy-MM-dd HH:mm:ss") + "|" + startMillis);
      
      // Insert Event
      ContentValues values = new ContentValues();
      TimeZone timeZone = TimeZone.getDefault();
      values.put(CalendarContract.Events.DTSTART, startMillis);
      values.put(CalendarContract.Events.DTEND, startMillis);
      values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
      values.put(CalendarContract.Events.TITLE, "테스트 일정");
      values.put(CalendarContract.Events.CALENDAR_ID, calID);
      Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
      uri.getLastPathSegment();
   }
   
   public interface RecordEndCallback
   {
      public void onRecordEnd(String $filename);
   }
   
   
   public void setOnRecordEndCallback(RecordEndCallback $callback)
   {
      _recordEndCallback = $callback;
   }
   
   private BroadcastReceiver stopRecordRecodingReceiver = new BroadcastReceiver()
   {
      @Override
      public void onReceive(Context context, Intent intent)
      {
         if (!PreferenceUtil.isRecording())
            return;
         
         stopRecord();
      }
   };
}
