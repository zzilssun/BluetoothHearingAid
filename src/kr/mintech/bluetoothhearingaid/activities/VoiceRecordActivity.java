package kr.mintech.bluetoothhearingaid.activities;

import kr.mintech.bluetoothhearingaid.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class VoiceRecordActivity extends FragmentActivity
{
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.act_voice_record);
      
      setupActionBar();
   }
   
   
   /**
    * Set up the {@link android.app.ActionBar}.
    */
   private void setupActionBar()
   {
      getActionBar().setDisplayHomeAsUpEnabled(true);
   }
   
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      getMenuInflater().inflate(R.menu.voice_record, menu);
      return true;
   }
   
   
   @Override
   public boolean onMenuItemSelected(int featureId, MenuItem item)
   {
      startRecord();
      return super.onMenuItemSelected(featureId, item);
   }
   
   
   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      switch (item.getItemId())
      {
         case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
      }
      return super.onOptionsItemSelected(item);
   }
   
   
   private void startRecord()
   {
      Log.w("VoiceRecordActivity.java | startRecord", "|" + "start record" + "|");
   }
   
}
