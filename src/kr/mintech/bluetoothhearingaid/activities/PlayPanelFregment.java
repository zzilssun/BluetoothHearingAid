package kr.mintech.bluetoothhearingaid.activities;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import kr.mintech.bluetoothhearingaid.utils.FileUtil;

import org.apache.commons.lang3.time.DateFormatUtils;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PlayPanelFregment extends Fragment
{
   private Button _btnPlay, _btnStop;
   private TextView _textCurrentPosition, _textTotalDuration;
   private TextView _textTitle;
   private SeekBar _seekbar;
   private String _fullPath;
   private MediaPlayer _player;
   private Calendar _calendar;
   
   
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      ContextUtil.CONTEXT = getActivity().getApplicationContext();
      
      _calendar = Calendar.getInstance();
      
      _fullPath = getArguments().getString(StringConst.KEY_PATH);
      Log.w("PlayPanelFregment.java | onCreate", "|" + _fullPath + "|");
   }
   
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
      View view = inflater.inflate(R.layout.fragment_play_panel, container, false);
      
      _textTitle = (TextView) view.findViewById(R.id.text_name);
      _textTitle.setText(FileUtil.filenameOnly(_fullPath));
      
      _textCurrentPosition = (TextView) view.findViewById(R.id.text_current_position);
      _textTotalDuration = (TextView) view.findViewById(R.id.text_total_duration);
      
      _seekbar = (SeekBar) view.findViewById(R.id.progress_play);
      
      _seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
      {
         @Override
         public void onStopTrackingTouch(SeekBar seekBar)
         {
            
         }
         
         
         @Override
         public void onStartTrackingTouch(SeekBar seekBar)
         {
            
         }
         
         
         @Override
         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
         {
            _calendar.setTimeInMillis(progress);
            _textCurrentPosition.setText(DateFormatUtils.format(_calendar, "mm:ss"));
         }
      });
      
      _btnPlay = (Button) view.findViewById(R.id.btn_play_start);
      _btnPlay.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View arg0)
         {
            play();
         }
      });
      
      _btnStop = (Button) view.findViewById(R.id.btn_play_stop);
      _btnStop.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            stop();
         }
      });
      
      play();
      
      return view;
   }
   
   
   @Override
   public void onDestroyView()
   {
      stop();
      super.onDestroyView();
   }
   
   
   private void play()
   {
      _player = null;
      _player = new MediaPlayer();
      _player.setOnPreparedListener(preparedListener);
      _player.setOnCompletionListener(completionListener);
      
      ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
      service.scheduleWithFixedDelay(new Runnable()
      {
         @Override
         public void run()
         {
            _seekbar.setProgress(_player.getCurrentPosition());
         }
      }, 100, 100, TimeUnit.MILLISECONDS);
      
      try
      {
         _player.setDataSource(_fullPath);
         _player.prepare();
      }
      catch (Exception e)
      {
         Toast.makeText(getActivity(), "Can't play", Toast.LENGTH_SHORT).show();
         e.printStackTrace();
      }
   }
   
   
   /**
    * 재생 중지
    */
   public void stop()
   {
      _btnPlay.setEnabled(true);
      _btnStop.setEnabled(false);
      
      try
      {
         _player.stop();
      }
      catch (Exception e)
      {
      }
      
      try
      {
         _player.reset();
      }
      catch (Exception e)
      {
      }
      
      try
      {
         _player.release();
      }
      catch (Exception e)
      {
      }
      
      _player = null;
      
      getFragmentManager().beginTransaction().remove(this).commit();
   }
   
   private OnPreparedListener preparedListener = new OnPreparedListener()
   {
      @Override
      public void onPrepared(MediaPlayer mp)
      {
         _seekbar.setMax(mp.getDuration());
         _calendar.setTimeInMillis(mp.getDuration());
         _textTotalDuration.setText(DateFormatUtils.format(_calendar, "mm:ss"));
         mp.start();
         
         _btnPlay.setEnabled(false);
         _btnStop.setEnabled(true);
      }
   };
   
   private OnCompletionListener completionListener = new OnCompletionListener()
   {
      @Override
      public void onCompletion(MediaPlayer mp)
      {
         stop();
      }
   };
   
}
