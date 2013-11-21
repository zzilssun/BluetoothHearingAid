package kr.mintech.bluetoothhearingaid.adapters;

import java.util.ArrayList;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.beans.Person;
import kr.mintech.bluetoothhearingaid.beans.PersonViewHolder;
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
   
   
   public PeopleAdapter(Context $context)
   {
      super();
      _inflater = LayoutInflater.from($context);
      _items.clear();
      
      Cursor cursor = $context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
      
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
   public View getView(int $index, View $convertView, ViewGroup arg2)
   {
      PersonViewHolder holder;
      
      if ($convertView == null)
      {
         $convertView = _inflater.inflate(R.layout.listitem_person, null);
         
         holder = new PersonViewHolder();
         holder.name = (TextView) $convertView.findViewById(R.id.text_name);
         holder.phone = (TextView) $convertView.findViewById(R.id.text_phone);
         holder.selected = (CheckBox) $convertView.findViewById(R.id.checkbox);
         holder.selected.setVisibility(View.VISIBLE);
         
         $convertView.setTag(holder);
      }
      else
      {
         holder = (PersonViewHolder) $convertView.getTag();
      }
      
      Person person = _items.get($index);
      
      holder.name.setText(person.name);
      holder.phone.setText(person.phone);
      holder.selected.setChecked(person.selected);
      
      return $convertView;
   }
   
}
