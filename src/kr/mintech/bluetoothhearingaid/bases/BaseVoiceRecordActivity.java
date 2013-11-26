package kr.mintech.bluetoothhearingaid.bases;

import java.io.File;
import java.util.List;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.activities.PlayPanelFregment;
import kr.mintech.bluetoothhearingaid.activities.PlayPanelFregment.OnRemovePlayPanelCallback;
import kr.mintech.bluetoothhearingaid.activities.RecordPanelFragment;
import kr.mintech.bluetoothhearingaid.activities.RecordPanelFragment.RecordEndCallback;
import kr.mintech.bluetoothhearingaid.adapters.FilesAdapter;
import kr.mintech.bluetoothhearingaid.consts.NumberConst;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import kr.mintech.bluetoothhearingaid.utils.LGTTS;
import kr.mintech.bluetoothhearingaid.utils.LGTTS.OnTTSCompleteListener;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class BaseVoiceRecordActivity extends FragmentActivity
{
   protected String _path = StringConst.NORMAL_PATH;
   
   private FilesAdapter _filesAdapter;
   private ViewGroup _layoutPanel;
   
   private LGTTS _tts;
   
   
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.act_voice_record);
      
      getActionBar().setDisplayHomeAsUpEnabled(true);
      
      ContextUtil.CONTEXT = getApplicationContext();
      
      _layoutPanel = (ViewGroup) findViewById(R.id.layout_panel);
      
      _tts = LGTTS.instance(getApplicationContext());
      _tts.setOnTTSCompleteListener(onTTSCompleteListener);
      
      Log.i("BaseVoiceRecordActivity.java | onCreate", "|" + "onCreate" + "|");
   }
   
   
   @Override
   protected void onNewIntent(Intent $intent)
   {
      super.onNewIntent($intent);
      Log.i("BaseVoiceRecordActivity.java | onNewIntent", "|" + "onNewIntent" + "|");
      checkIntentAction($intent);
   }
   
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      getMenuInflater().inflate(R.menu.voice_record, menu);
      return true;
   }
   
   
   @Override
   public void onBackPressed()
   {
      if (_layoutPanel.getChildCount() > 0)
      {
         List<Fragment> fragments = getSupportFragmentManager().getFragments();
         
         if (fragments.size() > 0)
         {
            Fragment fragment = fragments.get(0);
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            _layoutPanel.removeAllViews();
         }
      }
      else
         super.onBackPressed();
   }
   
   
   protected void begin()
   {
      _filesAdapter = new FilesAdapter(getApplicationContext(), _path);
      ListView listFile = (ListView) findViewById(R.id.list_files);
      listFile.setAdapter(_filesAdapter);
      listFile.setOnItemClickListener(onFilecliClickListener);
      
      checkIntentAction(getIntent());
   }
   
   
   private void checkIntentAction(Intent $intent)
   {
      Log.i("BaseVoiceRecordActivity.java | checkIntentAction", "|" + _path + "|");
      // q보이스에서 '음성녹음'으로 넘어온거면 파일 재생하기
      if (MediaStore.Audio.Media.RECORD_SOUND_ACTION.equals($intent.getAction()))
      {
         String msg = getString(R.string.recorded_file_count, _filesAdapter.getCount());
         Log.i("BaseVoiceRecordActivity.java | checkIntentAction", "|" + msg + "|");
         LGTTS.instance(getApplicationContext()).speak(msg);
      }
      else
      {
         toggleRecordState($intent);
      }
   }
   
   
   private void toggleRecordState(Intent $intent)
   {
      String action = getPackageName() + "." + StringConst.KEY_TOGGLE_RECORD_STATE;
      Log.i("VoiceRecordActivity.java | toggleRecordState", "|" + action + "|" + $intent.getAction());
      if (!action.equals($intent.getAction()))
      {
         startRecord(false);
         return;
      }
      
      Log.i("MainActivity.java | toggleRecordState", "| recording? " + PreferenceUtil.isRecording() + "|");
      if (PreferenceUtil.isRecording())
      {
         Intent intent = new Intent(StringConst.ACTION_STOP_RECORDING);
         sendBroadcast(intent);
      }
      else
      {
         startRecord(true);
      }
   }
   
   
   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      switch (item.getItemId())
      {
         case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
            
         case R.id.action_record:
            startRecord(false);
            break;
      }
      return super.onOptionsItemSelected(item);
   }
   
   
   /**
    * 녹음 시작
    * 
    * @param $startRecordingImmediate
    *           녹음을 곧바로 시작할 것인가
    * @param $recordMode
    *           NumberConst.RECORD_MODE_NORMAL=일상적인 녹음
    */
   private void startRecord(boolean $startRecordingImmediate)
   {
      Log.i("VoiceRecordActivity.java | startRecord", "|" + "show record panel" + "|");
      
      int recordMode = StringConst.EMERGENCY_PATH.equals(_path) ? NumberConst.RECORD_MODE_DROP : NumberConst.RECORD_MODE_NORMAL;
      
      Bundle bundle = new Bundle();
      bundle.putBoolean(StringConst.KEY_START_RECORDING_ON_OPEN, $startRecordingImmediate);
      bundle.putInt(StringConst.KEY_RECORD_MODE, recordMode);
      bundle.putString(StringConst.KEY_PATH, _path);
      
      RecordPanelFragment panel = new RecordPanelFragment();
      panel.setOnRecordEndCallback(recordEndCallback);
      panel.setArguments(bundle);
      getSupportFragmentManager().beginTransaction().replace(_layoutPanel.getId(), panel).commit();
   }
   
   
   private void autoPlayNext()
   {
      Log.i("BaseVoiceRecordActivity.java | autoPlayNext", "|" + "======== auto play ============" + "|");
      File next = _filesAdapter.popForAutoPlay();
      if (next != null)
         play(next.toString(), true);
   }
   
   
   // 리스트에서 파일 하나 클릭
   private void play(String $fullpath)
   {
      play($fullpath, false);
   }
   
   
   // 리스트에서 파일 하나 클릭
   private void play(String $fullpath, boolean $autoPlayNext)
   {
      Log.i("VoiceRecordActivity.java | play", "|" + $fullpath + "|" + $autoPlayNext);
      
      Bundle bundle = new Bundle();
      bundle.putString(StringConst.KEY_PATH, $fullpath);
      
      PlayPanelFregment panel = new PlayPanelFregment();
      panel.setArguments(bundle);
      
      if ($autoPlayNext)
      {
         panel.setOnRemovePlayPanelCallback(onRemovePlayPanelCallback);
      }
      
      getSupportFragmentManager().beginTransaction().replace(_layoutPanel.getId(), panel).commit();
   }
   
   // 녹음 종료 후 파일 목록 다시 읽기
   private RecordEndCallback recordEndCallback = new RecordEndCallback()
   {
      @Override
      public void onRecordEnd(String $filename)
      {
         Runnable runn = new Runnable()
         {
            @Override
            public void run()
            {
               Log.i("VoiceRecordActivity.java | run", "|" + "refresh" + "|");
               _filesAdapter.refresh();
            }
         };
         Handler handler = new Handler();
         handler.postDelayed(runn, 1000);
      }
   };
   
   // 파일 클릭했을 때
   private OnItemClickListener onFilecliClickListener = new AdapterView.OnItemClickListener()
   {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id)
      {
         File file = (File) _filesAdapter.getItem(position);
         Log.i("VoiceRecordActivity.java | onItemClick", "|" + file.toString() + "|");
         play(file.toString());
      }
   };
   
   // x개의 파일이 있습니다 재생 후
   private OnTTSCompleteListener onTTSCompleteListener = new OnTTSCompleteListener()
   {
      @Override
      public void onComplete()
      {
         autoPlayNext();
      }
   };
   
   // 하나의 파일 자동 재생이 끝나면 다음 녹음파일 재생하기
   private OnRemovePlayPanelCallback onRemovePlayPanelCallback = new OnRemovePlayPanelCallback()
   {
      @Override
      public void onRemoved(String $file)
      {
         autoPlayNext();
      }
   };
}
