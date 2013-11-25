package kr.mintech.bluetoothhearingaid.utils;

import java.util.Locale;

import android.content.Context;
import android.util.Log;

import com.lge.mtalk.sf.LGSFTextToSpeech;

public class LGTTS
{
   private static LGTTS _instance;
   private LGSFTextToSpeech _tts;
   private Context _context;
   private OnTTSCompleteListener _listener;
   
   
   public static synchronized LGTTS instance(Context $context)
   {
      if (_instance == null)
         _instance = new LGTTS($context);
      return _instance;
   }
   
   
   private LGTTS(Context $context)
   {
      _context = $context;
      init();
   }
   
   
   private void init()
   {
      _tts = new LGSFTextToSpeech(_context, onSpeakListener);
      
//		_tts.setApplication(LGSFTextToSpeech.MC_SMARTPHONE_DOWNLOAD_QVOICE_GAME);
      _tts.setLanguage(Locale.KOREA);
      _tts.setVoiceActor(LGSFTextToSpeech.VOICE_1_WOMAN_NORMAL);
   }
   
   
   public void setOnTTSCompleteListener(OnTTSCompleteListener $listener)
   {
      _listener = $listener;
   }
   
   
   /**
    * tts 말하기
    * 
    * @param $msg
    */
   public void speak(String $msg)
   {
      speak($msg, false);
   }
   
   
   /**
    * 말하기
    * 
    * @param $msg
    *           할 말
    * @param $isMan
    *           true=남자 목소리
    */
   public void speak(String $msg, boolean $isMan)
   {
      if ($isMan)
         _tts.setVoiceActor(LGSFTextToSpeech.VOICE_6_MAN_NORMAL);
      else
         _tts.setVoiceActor(LGSFTextToSpeech.VOICE_1_WOMAN_NORMAL);
      
      _tts.speak($msg);
   }
   
   
   /**
    * 말하기 중지
    */
   public void stop()
   {
      _tts.stop();
   }
   
   
   /**
    * 종료
    */
   public void shutdown()
   {
      _tts.shutdown();
      _instance = null;
   }
   
   
   /**
    * TTS가 말하고 있는 중인가
    * 
    * @return
    */
   public boolean isSpeaking()
   {
      return _tts.getPlayState() == LGSFTextToSpeech.PLAYSTATE_PLAYING;
   }
   
   private LGSFTextToSpeech.OnSpeakListener onSpeakListener = new LGSFTextToSpeech.OnSpeakListener()
   {
      @Override
      public void onInit(int value)
      {
         Log.i("TTS.java | onInit", "-- tts initialized -- ");
      }
      
      
      @Override
      public void onComplete(int value)
      {
         Log.i("TTS.java | onComplete", "TTS COMPLETE! : " + value + " == " + LGSFTextToSpeech.SUCCESS + "(" + LGSFTextToSpeech.getString(value)
               + ")");
         if (LGSFTextToSpeech.SUCCESS != value)
            return;
         
         if (_listener != null)
         {
            _listener.onComplete();
         }
      }
   };
   
   public interface OnTTSCompleteListener
   {
      public void onComplete();
   }
}
