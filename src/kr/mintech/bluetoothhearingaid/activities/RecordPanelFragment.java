package kr.mintech.bluetoothhearingaid.activities;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;

import org.apache.commons.lang3.time.DateFormatUtils;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

public class RecordPanelFragment extends Fragment
{
   private Button _btnRecordStart, _btnRecordStop;
   private Chronometer _chronometer;
   private AudioManager _audioManager;
   private MediaRecorder _recorder;
   private RecordEndCallback _recordEndCallback;
   
   
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      _audioManager = (AudioManager) getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
      ContextUtil.CONTEXT = getActivity().getApplicationContext();
   }
   
   
   @Override
   public void onDestroyView()
   {
      stopRecord();
      super.onDestroyView();
   }
   
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
      View view = inflater.inflate(R.layout.fragment_record_panel, container, false);
      
      _chronometer = (Chronometer) view.findViewById(R.id.chronometer);
      
      _btnRecordStart = (Button) view.findViewById(R.id.btn_record_start);
      _btnRecordStart.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            startRecord();
         }
      });
      
      _btnRecordStop = (Button) view.findViewById(R.id.btn_record_stop);
      _btnRecordStop.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            stopRecord();
         }
      });
      
      return view;
   }
   
   
   /**
    * 녹음 시작
    */
   public void startRecord()
   {
      File path = new File(StringConst.PATH);
      if (!path.exists())
         path.mkdirs();
      
      Calendar calendar = Calendar.getInstance();
      String currentDateStr = DateFormatUtils.format(calendar, "yyyyMMdd_HHmmss");
      File file = new File(path + File.separator + "voice_" + currentDateStr + ".m4a");
      
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
      Log.w("RecordPanelFragment.java | startRecord", "|" + file.toString() + "|");
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
         
         _chronometer.setBase(SystemClock.elapsedRealtime());
         _chronometer.start();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   
   /**
    * 녹음 종료
    */
   public void stopRecord()
   {
      Log.w("RecordPanelFragment.java | stopRecord", "|" + "stop" + "|" + PreferenceUtil.lastRecordedFileFullPath());
      _chronometer.stop();
      
      try
      {
         _audioManager.stopBluetoothSco();
         _recorder.stop();
         _recorder.release();
         
         getActivity().getApplicationContext().sendBroadcast(
               new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + new File(StringConst.PATH))));
         String filename = PreferenceUtil.lastRecordedFileFullPath();
         _recordEndCallback.onRecordEnd(filename);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      
      _btnRecordStop.setEnabled(false);
      _btnRecordStart.setEnabled(true);
   }
   
   public interface RecordEndCallback
   {
      public void onRecordEnd(String $filename);
   }
   
   
   public void setOnRecordEndCallback(RecordEndCallback $callback)
   {
      _recordEndCallback = $callback;
   }
}
