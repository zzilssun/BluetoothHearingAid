package kr.mintech.bluetoothhearingaid.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.view.KeyEvent;

public class MusicController
{
   private Context _context;
   private AudioManager audio;
   
   
   public MusicController(Context $context)
   {
      super();
      _context = $context;
   }
   
   
   /**
    * 음악이 재생중인가
    * 
    * @return
    */
   public boolean isPlaying()
   {
      AudioManager kManager = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);
      return kManager.isMusicActive();
   }
   
   
   /*
    * Keycode 받아서 처리
    */
   public void putKeyCommand(int keycode)
   {
      putCommand(keycode);
   }
   
   
   /**
    * 재생 상태 토글
    */
   public void toggle()
   {
      putCommand(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
   }
   
   
   /**
    * 재생 시작
    */
   @TargetApi(Build.VERSION_CODES.HONEYCOMB)
   public void play()
   {
      putCommand(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
   }
   
   
   /**
    * 재생 중지
    */
   public void pause()
   {
      putCommand(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
   }
   
   
   /**
    * 정지
    */
   public void stop()
   {
      putCommand(KeyEvent.KEYCODE_MEDIA_STOP);
   }
   
   
   /**
    * 다음 노래
    */
   public void next()
   {
      putCommand(KeyEvent.KEYCODE_MEDIA_NEXT);
   }
   
   
   /**
    * 이전 노래
    */
   public void previous()
   {
      putCommand(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
   }
   
   
   public void clickVolumeDown()
   {
      audio = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);
      audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
   }
   
   
   public void clickVolumnUp()
   {
      audio = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);
      audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
   }
   
   
   //Put Command
   private void putCommand(int event)
   {
      try
      {
         IBinder iBinder = (IBinder) Class.forName("android.os.ServiceManager")
                                          .getDeclaredMethod("checkService", String.class)
                                          .invoke(null, Context.AUDIO_SERVICE);
         Object audioService = Class.forName("android.media.IAudioService$Stub")
                                    .getDeclaredMethod("asInterface", IBinder.class)
                                    .invoke(null, iBinder);
         
         KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, event);
         Class.forName("android.media.IAudioService").getDeclaredMethod("dispatchMediaKeyEvent", KeyEvent.class).invoke(audioService, keyEvent);
         
         KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, event);
         Class.forName("android.media.IAudioService").getDeclaredMethod("dispatchMediaKeyEvent", KeyEvent.class).invoke(audioService, keyEventUp);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}
