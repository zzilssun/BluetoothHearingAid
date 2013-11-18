package kr.mintech.bluetoothhearingaid.activities;

import java.util.ArrayList;

import org.json.JSONArray;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.adapters.SMSAdapter;
import kr.mintech.bluetoothhearingaid.beans.Person;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
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
      
      String callReceiverJson = PreferenceUtil.callReceiver();
      Person person = new Person(callReceiverJson);
      
      _textCallReceiver = (TextView) findViewById(R.id.text_call_receiver);
      
      _textCallReceiver.setText(person.name + " " + person.phone);
      
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
      Person person = new Person("김태희", "01012345678");
      PreferenceUtil.putCallReceiver(person.jsonStr());
   }
   
   
   private void selectSMSReceiver()
   {
      ArrayList<Person> people = new ArrayList<Person>();
      people.add(new Person("김태희1", "01011111111"));
      people.add(new Person("김태희2", "01022222222"));
      people.add(new Person("김태희3", "01033333333"));
      people.add(new Person("김태희4", "01044444444"));
      people.add(new Person("김태희5", "01055555555"));
      people.add(new Person("김태희6", "01066666666"));
      
      JSONArray jsonArray = new JSONArray();
      for (Person person : people)
      {
         jsonArray.put(person.json());
      }
      Log.w("EmergencyCallActivity.java | selectSMSReceiver", "|" + jsonArray.toString() + "|");
      PreferenceUtil.putSMSReceiver(jsonArray.toString());
   }
   
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data)
   {
      super.onActivityResult(requestCode, resultCode, data);
   }
}
