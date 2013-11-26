package kr.mintech.bluetoothhearingaid.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.beans.FileViewHolder;
import kr.mintech.bluetoothhearingaid.utils.FileUtil;

import org.apache.commons.lang3.time.DateFormatUtils;

import android.content.Context;
import android.util.Log;
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
   private String _path;
   private int _lastAutoPlayPosition = -1;
   
   
   public FilesAdapter(Context $context, String $path)
   {
      super();
      _context = $context;
      _inflater = LayoutInflater.from($context);
      _path = $path;
      loadFileList();
   }
   
   
   private void loadFileList()
   {
      _items.clear();
      
      File path = new File(_path);
      if (!path.getParentFile().exists())
         path.getParentFile().mkdirs();
      
      if (!path.exists())
      {
         path.mkdirs();
         Log.i("FilesAdapter.java | loadFileList", "|" + _path + "|");
      }
      
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
   
   
   // 자동재생을 위한 다음에 자동재생할 파일 가져오기
   public File popForAutoPlay()
   {
      File result = null;
      
      if (getCount() == 0)
         return result;
      
      _lastAutoPlayPosition++;
      
      Log.i("FilesAdapter.java | popForAutoPlay", "|" + _lastAutoPlayPosition + "|" + getCount());
      if (_lastAutoPlayPosition < getCount())
         result = _items.get(_lastAutoPlayPosition);
      else
         _lastAutoPlayPosition = -1;
      
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
      FileViewHolder holder;
      
      if ($convertView == null)
      {
         $convertView = _inflater.inflate(R.layout.listitem_record_file, null);
         
         holder = new FileViewHolder();
         holder.name = (TextView) $convertView.findViewById(R.id.text_name);
         holder.alarm = (TextView) $convertView.findViewById(R.id.text_alarm);
         holder.createdAt = (TextView) $convertView.findViewById(R.id.text_created_at);
         holder.duration = (TextView) $convertView.findViewById(R.id.text_duration);
         
         $convertView.setTag(holder);
      }
      else
      {
         holder = (FileViewHolder) $convertView.getTag();
      }
      
      File file = _items.get($index);
      
      holder.name.setText(FileUtil.filenameOnly(file.getName()));
//      holder.alarm.setText();
      holder.createdAt.setText(DateFormatUtils.format(file.lastModified(), "yyyy-MM-dd HH:mm:ss"));
      holder.duration.setText(FileUtil.duration(_context, file.getAbsolutePath(), "m분 s초"));
      
      return $convertView;
   }
}
