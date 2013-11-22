package kr.mintech.bluetoothhearingaid.activities;

import java.util.ArrayList;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.adapters.SMSAdapter;
import kr.mintech.bluetoothhearingaid.beans.Person;
import kr.mintech.bluetoothhearingaid.consts.NumberConst;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import kr.mintech.bluetoothhearingaid.utils.ContextUtil;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class EmergencyCallActivity extends Activity
{
   private static final String SMS_SENT = "SMS_SENT";
   private static final String SMS_DELIVERED = "SMS_DELIVERED";
   
   private TextView _textCallReceiver;
   private Button _btnSelectCall, _btnSelectSMS;
   private SMSAdapter _smsAdapter;
   
   
//   private LocationManager _locationManager;
//   private Location _currentLocation;
//   private String _mapLink;
//   private String _audioLink;
//   private UploadHelper _helper;
   
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
      
//      _locationManager = new LocationManager(getApplicationContext(), locationChangedListener);
      
      checkDropModeRecording(getIntent());
   }
   
   
   @Override
   protected void onNewIntent(Intent $intent)
   {
      super.onNewIntent($intent);
      checkDropModeRecording($intent);
   }
   
   
   @Override
   protected void onStop()
   {
      super.onStop();
//      _locationManager.disconnect();
      
      try
      {
         unregisterReceiver(sentReceiver);
         unregisterReceiver(deliveryReceiver);
      }
      catch (Exception e)
      {
//         e.printStackTrace();
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
      }
      return super.onOptionsItemSelected(item);
   }
   
   
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data)
   {
      super.onActivityResult(requestCode, resultCode, data);
      Log.i("EmergencyCallActivity.java | onActivityResult", "|" + resultCode + "|" + RESULT_OK);
      if (resultCode != RESULT_OK)
         return;
      
      refreshCallReceiver();
      _smsAdapter.refreah();
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
   
   
   private void refreshCallReceiver()
   {
      Person person = loadCallReceiver();
      if (person != null)
         _textCallReceiver.setText(person.name + " " + person.phone);
   }
   
   
   private Person loadCallReceiver()
   {
      String callReceiverJson = PreferenceUtil.callReceiver();
      if (TextUtils.isEmpty(callReceiverJson))
         return null;
      else
         return new Person(callReceiverJson);
   }
   
   
   private void checkDropModeRecording(Intent $intent)
   {
      String action = $intent.getAction();
      if (!StringConst.ACTION_DROP_MODE_RECORD_END.equals(action))
         return;
      
//      _locationManager.startLocationFind();
//   }
//   
//   
//   private void makeShortMapLink()
//   {
//      Log.i("EmergencyCallActivity.java | sendSMS", "|" + _currentLocation.getLatitude() + "," + _currentLocation.getLongitude());
//      
//      String location = _currentLocation.getLatitude() + "," + _currentLocation.getLongitude();
//      mapLinkShortenTask.execute(getString(R.string.map_link, location));
//   }
//   
//   
//   @Override
//   protected void uploadToGoogleDrive()
//   {
//      super.uploadToGoogleDrive();
//      
//      String filePath = PreferenceUtil.lastRecordedFileFullPath();
//      _helper = new UploadHelper(getApplicationContext(), credential, authIOExceptionCallback);
//      _helper.upload(filePath, onUploadEndCallback);
//   }
//   
//   
//   private void sendSMS()
//   {
      ArrayList<Person> people = _smsAdapter.list();
      if (people.isEmpty())
         return;
      
//         final String message = getString(R.string.sms_content, _mapLink, _audioLink);
      final String message = getString(R.string.sms_content_for_test, "http://goo.gl/o2vWtk");
      Log.i("EmergencyCallActivity.java | sendSMS", "|" + message + "|");
      
      final PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_SENT), 0);
      final PendingIntent deliveryPI = PendingIntent.getBroadcast(getApplicationContext(), 1, new Intent(SMS_DELIVERED), 1);
      
      registerReceiver(sentReceiver, new IntentFilter(SMS_SENT));
      registerReceiver(deliveryReceiver, new IntentFilter(SMS_DELIVERED));
      
      final SmsManager sms = SmsManager.getDefault();
      
      for (int i = 0; i < people.size(); i++)
      {
         final Person person = people.get(i);
         
         Runnable runn = new Runnable()
         {
            @Override
            public void run()
            {
               Log.i("EmergencyCallActivity.java | run", "|send sms : " + person.name + "|" + person.phone);
               sms.sendTextMessage(person.phone, null, message, sentPI, deliveryPI);
            }
         };
         Handler handler = new Handler();
         handler.postDelayed(runn, i * 5000);
      }
      
      startCall();
   }
   
   
   private void startCall()
   {
      Person person = loadCallReceiver();
      if (person == null)
         return;
      
      Log.i("EmergencyCallActivity.java | startCall", "|" + person.name + "|" + person.phone);
      
      Intent intent = new Intent(Intent.ACTION_CALL);
      intent.setData(Uri.parse("tel:" + person.phone));
      startActivity(intent);
   }
   
//   private OnLocationChangedListener locationChangedListener = new OnLocationChangedListener()
//   {
//      @Override
//      public void onLocationChanged(Location $location)
//      {
//         if ($location != null)
//         {
//            _currentLocation = $location;
//            _locationManager.disconnect();
//            makeShortMapLink();
//         }
//      }
//   };
   
   private BroadcastReceiver sentReceiver = new BroadcastReceiver()
   {
      @Override
      public void onReceive(Context context, Intent intent)
      {
         switch (getResultCode())
         {
            case RESULT_OK:
               Log.w("MainActivity.java | sentReceiver", "|" + "sms sent" + "|");
               break;
            
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
               Log.w("MainActivity.java | sentReceiver", "|" + "SmsManager.RESULT_ERROR_GENERIC_FAILURE:" + "|");
               break;
            
            case SmsManager.RESULT_ERROR_NO_SERVICE:
               Log.w("MainActivity.java | sentReceiver", "|" + "SmsManager.RESULT_ERROR_NO_SERVICE" + "|");
               break;
            
            case SmsManager.RESULT_ERROR_NULL_PDU:
               Log.w("MainActivity.java | sentReceiver", "|" + "SmsManager.RESULT_ERROR_NULL_PDU" + "|");
               break;
            
            case SmsManager.RESULT_ERROR_RADIO_OFF:
               Log.w("MainActivity.java | sentReceiver", "|" + "SmsManager.RESULT_ERROR_RADIO_OFF:" + "|");
               break;
            
            default:
               Log.w("MainActivity.java | sentReceiver", "| =====" + getResultCode() + "|");
               break;
         }
      }
   };
   
   private BroadcastReceiver deliveryReceiver = new BroadcastReceiver()
   {
      @Override
      public void onReceive(Context context, Intent intent)
      {
         switch (getResultCode())
         {
            case RESULT_OK:
               Log.w("MainActivity.java | deliveryReceiver", "|" + "RESULT_OK" + "|");
               break;
            
            case RESULT_CANCELED:
               Log.w("MainActivity.java | deliveryReceiver", "|" + "RESULT_CANCELED:" + "|");
               break;
            
            default:
               Log.w("MainActivity.java | deliveryReceiver", "| +++++++" + getResultCode() + "|");
               break;
         }
      }
   };
   
//   private AsyncTask<String, Integer, String> mapLinkShortenTask = new AsyncTask<String, Integer, String>()
//   {
//      @Override
//      protected String doInBackground(String... params)
//      {
//         return UrlShortenUtil.shorten(params[0]);
//      }
//      
//      
//      protected void onPostExecute(String result)
//      {
//         _mapLink = result;
//         Log.i("EmergencyCallActivity.java | map link task", "|" + _mapLink + "|");
//         uploadToGoogleDrive();
//      };
//      
//   };
   
//   private OnUploadEndCallback onUploadEndCallback = new OnUploadEndCallback()
//   {
//      @Override
//      public void onUploaded(String $url)
//      {
//         _audioLink = $url;
//         Log.i("EmergencyCallActivity.java | upload task", "|" + _audioLink + "|");
//         sendSMS();
//      }
//      
//      
//      @Override
//      public void onUploadFail()
//      {
//         
//      }
//   };
   
}
