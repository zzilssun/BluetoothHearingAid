package kr.mintech.bluetoothhearingaid.activities;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.adapters.FilesAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class VoiceRecordActivity extends FragmentActivity
{
   private FilesAdapter _filesAdapter;
   
   
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.act_voice_record);
      
      getActionBar().setDisplayHomeAsUpEnabled(true);
      
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
      getSupportFragmentManager().beginTransaction().replace(R.id.layout_panel, panel).commit();
   }
   
   
   private void play()
   {
      
   }
   
}
