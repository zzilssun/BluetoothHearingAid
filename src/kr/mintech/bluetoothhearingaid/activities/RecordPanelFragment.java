package kr.mintech.bluetoothhearingaid.activities;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;

import org.apache.commons.lang3.time.DateFormatUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
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
   private boolean _isStoped = false;
   
   
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      
      _audioManager = (AudioManager) getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
      ContextUtil.CONTEXT = getActivity().getApplicationContext();
      
      IntentFilter filter = new IntentFilter();
      filter.addAction(StringConst.STOP_RECORING);
      getActivity().registerReceiver(stopRecordRecodingReceiver, filter);
      
      _startRecordingOnOpen = getArguments().getBoolean(StringConst.KEY_START_RECORDING_ON_OPEN);
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
      stopRecord();
      super.onDestroyView();
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
      if (_isStoped)
         return;
      
      _isStoped = true;
      
      if (!PreferenceUtil.isRecording())
         return;
      
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
         e.printStackTrace();
      }
      
      _btnRecordStop.setEnabled(false);
      _btnRecordStart.setEnabled(true);
      
      getFragmentManager().beginTransaction().remove(this).commit();
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
         stopRecord();
      }
   };
}
