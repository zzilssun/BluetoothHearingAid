package kr.mintech.bluetoothhearingaid.activities;

import java.io.File;
import java.util.List;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.activities.RecordPanelFragment.RecordEndCallback;
import kr.mintech.bluetoothhearingaid.adapters.FilesAdapter;
import kr.mintech.bluetoothhearingaid.consts.NumberConst;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class VoiceRecordActivity extends FragmentActivity
{
   private FilesAdapter _filesAdapter;
   private ViewGroup _layoutPanel;
   
   
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.act_voice_record);
      
      getActionBar().setDisplayHomeAsUpEnabled(true);
      
      ContextUtil.CONTEXT = getApplicationContext();
      
      _layoutPanel = (ViewGroup) findViewById(R.id.layout_panel);
      
      _filesAdapter = new FilesAdapter(getApplicationContext());
      ListView listFile = (ListView) findViewById(R.id.list_files);
      listFile.setAdapter(_filesAdapter);
      listFile.setOnItemClickListener(onFilecliClickListener);
      
      toggleRecordState(getIntent());
   }
   
   
   @Override
   protected void onNewIntent(Intent $intent)
   {
      super.onNewIntent($intent);
      
      toggleRecordState($intent);
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
         clearFragment();
      else
         super.onBackPressed();
   }
   
   
   private void clearFragment()
   {
      List<Fragment> list = getSupportFragmentManager().getFragments();
      Fragment fragment = list.get(0);
      getSupportFragmentManager().beginTransaction().remove(fragment).commit();
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
         int recordMode = $intent.getIntExtra(StringConst.KEY_RECORD_MODE, NumberConst.RECORD_MODE_NORMAL);
         startRecord(true, recordMode);
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
    */
   private void startRecord(boolean $startRecordingImmediate)
   {
      startRecord($startRecordingImmediate, NumberConst.RECORD_MODE_NORMAL);
   }
   
   
   /**
    * 녹음 시작
    * 
    * @param $startRecordingImmediate
    *           녹음을 곧바로 시작할 것인가
    * @param $recordMode
    *           NumberConst.RECORD_MODE_NORMAL=일상적인 녹음
    */
   private void startRecord(boolean $startRecordingImmediate, int $recordMode)
   {
      Log.i("VoiceRecordActivity.java | startRecord", "|" + "show record panel" + "|");
      
      Bundle bundle = new Bundle();
      bundle.putBoolean(StringConst.KEY_START_RECORDING_ON_OPEN, $startRecordingImmediate);
      bundle.putInt(StringConst.KEY_RECORD_MODE, $recordMode);
      
      RecordPanelFragment panel = new RecordPanelFragment();
      panel.setOnRecordEndCallback(recordEndCallback);
      panel.setArguments(bundle);
      getSupportFragmentManager().beginTransaction().replace(_layoutPanel.getId(), panel).commit();
   }
   
   
   // 리스트에서 파일 하나 클릭
   private void play(String $fullpath)
   {
      Log.i("VoiceRecordActivity.java | play", "|" + $fullpath + "|");
      
      Bundle bundle = new Bundle();
      bundle.putString(StringConst.KEY_PATH, $fullpath);
      
      PlayPanelFregment panel = new PlayPanelFregment();
      panel.setArguments(bundle);
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
}
