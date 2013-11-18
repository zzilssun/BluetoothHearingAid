package kr.mintech.bluetoothhearingaid.activities;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.adapters.SMSAdapter;
import kr.mintech.bluetoothhearingaid.beans.Person;
import kr.mintech.bluetoothhearingaid.consts.NumberConst;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class EmergencyCallActivity extends Activity
{
   private TextView _textCallReceiver;
   private Button _btnSelectCall, _btnSelectSMS;
   private SMSAdapter _smsAdapter;
   
   
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.act_emergency_call);
      
      getActionBar().setDisplayHomeAsUpEnabled(true);
      
      ContextUtil.CONTEXT = getApplicationContext();
      
      _textCallReceiver = (TextView) findViewById(R.id.text_call_receiver);
      refreshCallReceiver();
      
      _btnSelectCall = (Button) findViewById(R.id.btn_select_call);
      _btnSelectCall.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            selectCallReceiver();
         }
      });
      
      _btnSelectSMS = (Button) findViewById(R.id.btn_select_sms);
      _btnSelectSMS.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            selectSMSReceiver();
         }
      });
      
      _smsAdapter = new SMSAdapter(getApplicationContext());
      ListView listSMS = (ListView) findViewById(R.id.list_sms);
      listSMS.setAdapter(_smsAdapter);
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
   
   
   private void selectCallReceiver()
   {
      Intent intent = new Intent(this, SelectPersonActivity.class);
      intent.putExtra(StringConst.KEY_CALL_SMS_TYPE, NumberConst.TYPE_CALL);
      startActivityForResult(intent, NumberConst.TYPE_CALL);
   }
   
   
   private void selectSMSReceiver()
   {
      Intent intent = new Intent(this, SelectPersonActivity.class);
      intent.putExtra(StringConst.KEY_CALL_SMS_TYPE, NumberConst.TYPE_SMS);
      startActivityForResult(intent, NumberConst.TYPE_SMS);
   }
   
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data)
   {
      super.onActivityResult(requestCode, resultCode, data);
      if (resultCode != RESULT_OK)
         return;
      
      refreshCallReceiver();
      _smsAdapter.refreah();
   }
   
   
   private void refreshCallReceiver()
   {
      String callReceiverJson = PreferenceUtil.callReceiver();
      Person person = new Person(callReceiverJson);
      _textCallReceiver.setText(person.name + " " + person.phone);
   }
}
