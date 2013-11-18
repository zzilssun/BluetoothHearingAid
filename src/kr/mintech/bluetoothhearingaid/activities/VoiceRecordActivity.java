package kr.mintech.bluetoothhearingaid.activities;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.activities.RecordPanelFragment.RecordEndCallback;
import kr.mintech.bluetoothhearingaid.adapters.FilesAdapter;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
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
         _layoutPanel.removeAllViews();
      else
         super.onBackPressed();
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
            startRecord();
            break;
      }
      return super.onOptionsItemSelected(item);
   }
   
   
   private void startRecord()
   {
      Log.w("VoiceRecordActivity.java | startRecord", "|" + "start record" + "|");
      
      RecordPanelFragment panel = new RecordPanelFragment();
      panel.setOnRecordEndCallback(recordEndCallback);
      getSupportFragmentManager().beginTransaction().replace(R.id.layout_panel, panel).commit();
   }
   
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
               _filesAdapter.refresh();
            }
         };
         Handler handler = new Handler();
         handler.postDelayed(runn, 1000);
      }
   };
}
