package kr.mintech.bluetoothhearingaid.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.beans.FileViewHolder;
import kr.mintech.bluetoothhearingaid.consts.StringConst;
import kr.mintech.bluetoothhearingaid.utils.FileUtil;

import org.apache.commons.lang3.time.DateFormatUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FilesAdapter extends BaseAdapter
{
   private ArrayList<File> _items = new ArrayList<File>();
   private Context _context;
   private LayoutInflater _inflater;
   
   
   public FilesAdapter(Context $context)
   {
      super();
      _context = $context;
      _inflater = LayoutInflater.from($context);
      loadFileList();
   }
   
   
   private void loadFileList()
   {
      _items.clear();
      
      File path = new File(StringConst.PATH);
      if (!path.exists())
         path.mkdirs();
      
      for (File file : path.listFiles())
      {
         _items.add(file);
      }
      
      Collections.reverse(_items);
      
      notifyDataSetChanged();
   }
   
   
   public void refresh()
   {
      loadFileList();
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
      FileViewHolder holder;
      
      if ($convertView == null)
      {
         $convertView = _inflater.inflate(R.layout.listitem_record_file, null);
         
         holder = new FileViewHolder();
         holder.title = (TextView) $convertView.findViewById(R.id.text_name);
         holder.createdAt = (TextView) $convertView.findViewById(R.id.text_created_at);
         holder.duration = (TextView) $convertView.findViewById(R.id.text_duration);
         
         $convertView.setTag(holder);
      }
      else
      {
         holder = (FileViewHolder) $convertView.getTag();
      }
      
      File file = _items.get($index);
      
      holder.title.setText(FileUtil.filenameOnly(file.getName()));
      holder.createdAt.setText(DateFormatUtils.format(file.lastModified(), "yyyy/MM/dd HH:mm:ss"));
      holder.duration.setText(FileUtil.duration(_context, file.getAbsolutePath()));
      
      return $convertView;
   }
}
