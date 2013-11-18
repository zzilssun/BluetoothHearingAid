package kr.mintech.bluetoothhearingaid.adapters;

import java.util.ArrayList;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.beans.Person;
import kr.mintech.bluetoothhearingaid.consts.NumberConst;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;

import org.json.JSONArray;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class PeopleAdapter extends BaseAdapter
{
   private ArrayList<Person> _items = new ArrayList<Person>();
   private LayoutInflater _inflater;
   private int _type = NumberConst.TYPE_CALL;
   
   
   public PeopleAdapter(Context $context, int $type)
   {
      super();
      _inflater = LayoutInflater.from($context);
      _items.clear();
      _type = $type;
      
      Cursor cursor = $context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
      
      cursor.moveToFirst();
      while (!cursor.isAfterLast())
      {
         int phone_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
         int name_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
         String phone = cursor.getString(phone_idx);
         String name = cursor.getString(name_idx);
         
         _items.add(new Person(name, phone));
         
         cursor.moveToNext();
      }
      
      notifyDataSetChanged();
   }
   
   
   public void check(int $position, boolean $check)
   {
      Person person = _items.get($position);
      person.selected = $check;
      _items.set($position, person);
   }
   
   
   public ArrayList<Person> checkedPeople()
   {
      ArrayList<Person> result = new ArrayList<Person>();
      
      for (Person person : _items)
      {
         if (person.selected)
            result.add(person);
      }
      
      JSONArray jsonArray = new JSONArray();
      for (Person person : result)
      {
         jsonArray.put(person.json());
      }
      
      if (_type == NumberConst.TYPE_CALL)
      {
         Person person = result.get(0);
         PreferenceUtil.putCallReceiver(person.jsonStr());
      }
      else
         PreferenceUtil.putSMSReceiver(jsonArray.toString());
      
      return result;
   }
   
   
   @Override
   public int getCount()
   {
      return _items.size();
   }
   
   
   @Override
   public Object getItem(int $index)
   {
      return _items.get($index);
   }
   
   
   @Override
   public long getItemId(int $index)
   {
      return $index;
   }
   
   
   @Override
   public View getView(int $index, View arg1, ViewGroup arg2)
   {
      View view = _inflater.inflate(R.layout.listitem_person, null);
      
      Person person = _items.get($index);
      
      TextView textName = (TextView) view.findViewById(R.id.text_name);
      textName.setText(person.name);
      
      TextView textPhone = (TextView) view.findViewById(R.id.text_phone);
      textPhone.setText(person.phone);
      
      CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);
      checkbox.setChecked(person.selected);
      
      return view;
   }
   
}
