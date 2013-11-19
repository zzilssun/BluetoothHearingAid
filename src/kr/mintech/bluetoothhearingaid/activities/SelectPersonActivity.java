package kr.mintech.bluetoothhearingaid.activities;

import java.util.ArrayList;

import org.json.JSONArray;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.adapters.PeopleAdapter;
import kr.mintech.bluetoothhearingaid.beans.Person;
import kr.mintech.bluetoothhearingaid.consts.NumberConst;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

public class SelectPersonActivity extends Activity
{
   private PeopleAdapter _peopleAdapter;
   private int _type = NumberConst.TYPE_CALL;
   
   
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.act_select_person);
      
      _type = getIntent().getIntExtra(StringConst.KEY_CALL_SMS_TYPE, NumberConst.TYPE_CALL);
      
      _peopleAdapter = new PeopleAdapter(getApplicationContext(), _type);
      ListView listPeople = (ListView) findViewById(R.id.list_people);
      listPeople.setAdapter(_peopleAdapter);
      listPeople.setOnItemClickListener(onItemClickListener);
   }
   
   
   @Override
   public void onBackPressed()
   {
      checkResult();
      super.onBackPressed();
   }
   
   
   private void checkResult()
   {
      ArrayList<Person> data = _peopleAdapter.checkedPeople();
      
      JSONArray jsonArray = new JSONArray();
      for (Person person : data)
      {
         jsonArray.put(person.json());
      }
      
      if (_type == NumberConst.TYPE_CALL)
      {
         if (data.isEmpty())
            PreferenceUtil.putCallReceiver("");
         else
         {
            Person person = data.get(0);
            PreferenceUtil.putCallReceiver(person.jsonStr());
         }
      }
      else
         PreferenceUtil.putSMSReceiver(jsonArray.toString());
      
      setResult(RESULT_OK);
   }
   
   private OnItemClickListener onItemClickListener = new OnItemClickListener()
   {
      @Override
      public void onItemClick(AdapterView<?> arg0, View $view, int $position, long id)
      {
         CheckBox checkbox = (CheckBox) $view.findViewById(R.id.checkbox);
         _peopleAdapter.check($position, !checkbox.isChecked());
         checkbox.setChecked(!checkbox.isChecked());
         
         if (_type == NumberConst.TYPE_CALL)
         {
            checkResult();
            finish();
         }
      }
   };
}
