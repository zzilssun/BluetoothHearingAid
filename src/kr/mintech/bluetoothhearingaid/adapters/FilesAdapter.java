package kr.mintech.bluetoothhearingaid.adapters;

import java.io.File;
import java.util.ArrayList;

import kr.mintech.bluetoothhearingaid.R;
import kr.mintech.bluetoothhearingaid.Utils.FileUtil;

import org.apache.commons.lang3.time.DateFormatUtils;

import android.content.Context;
import android.os.Environment;
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
      
      File path = new File(Environment.getExternalStorageDirectory() + "/Sounds");
      if (!path.exists())
         path.mkdirs();
      
      for (File file : path.listFiles())
      {
         Log.w("FilesAdapter.java | loadFileList", "|" + file.getName() + "|");
         _items.add(file);
      }
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
   public View getView(int $index, View $view, ViewGroup arg2)
   {
      View view = _inflater.inflate(R.layout.listitem_record_file, null);
      
      File file = _items.get($index);
      
      TextView title = (TextView) view.findViewById(R.id.text_name);
      String name = file.getName().split("\\.")[0];
      title.setText(name);
      
      TextView createdAt = (TextView) view.findViewById(R.id.text_created_at);
      createdAt.setText(DateFormatUtils.format(file.lastModified(), "yyyy/MM/dd HH:mm:ss"));
      
      TextView duration = (TextView) view.findViewById(R.id.text_duration);
      duration.setText(FileUtil.duration(_context, file.getAbsolutePath()));
      
      return view;
   }
}
