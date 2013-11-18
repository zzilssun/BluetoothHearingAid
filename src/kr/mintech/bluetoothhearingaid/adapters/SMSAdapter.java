package kr.mintech.bluetoothhearingaid.adapters;

import java.util.ArrayList;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.beans.Person;
import kr.mintech.bluetoothhearingaid.utils.JsonUtil;
import kr.mintech.bluetoothhearingaid.utils.PreferenceUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SMSAdapter extends BaseAdapter
{
   private ArrayList<Person> _items = new ArrayList<Person>();
   private LayoutInflater _inflater;
   
   
   public SMSAdapter(Context $context)
   {
      super();
      _inflater = LayoutInflater.from($context);
      refreah();
   }
   
   
   public void refreah()
   {
      _items.clear();
      
      String jsonStr = PreferenceUtil.smsReceiver();
      ArrayList<String> jsonArray = JsonUtil.array(jsonStr);
      
      for (String json : jsonArray)
      {
         _items.add(new Person(json));
      }
      
      notifyDataSetChanged();
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
      
      return view;
   }
   
}
