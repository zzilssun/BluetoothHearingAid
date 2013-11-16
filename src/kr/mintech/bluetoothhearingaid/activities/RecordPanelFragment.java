package kr.mintech.bluetoothhearingaid.activities;

import kr.mintech.bluetoothhearingaid.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecordPanelFragment extends Fragment
{
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
      View view = inflater.inflate(R.layout.fragment_record_panel, container, false);
      return view;
   }
}
