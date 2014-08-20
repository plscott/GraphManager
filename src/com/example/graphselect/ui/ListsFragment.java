package com.example.graphselect.ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.example.graphselect.GraphList;
import com.example.graphselect.MainActivity;
import com.example.graphselect.R;
import com.example.graphselect.widget.GraphListAdapter;
import com.google.gson.Gson;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ListsFragment extends Fragment {
	final private GraphListAdapter _adapter;
	private MainActivity _main;
	
	public ListsFragment(MainActivity mainActivity) {
		super();
		_main = mainActivity;
		_adapter = new GraphListAdapter(_main, R.layout.row);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup listsView = (ViewGroup) inflater.inflate(R.layout.lists, container, false);
		ListView list = (ListView) listsView.findViewById(R.id.lists_of_lists);
		TextView tv = (TextView) listsView.findViewById(R.id.empty_view);
		list.setEmptyView(tv);
		list.setAdapter(_adapter);
		
		this.loadListData();
		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				_main.select(position);
			}
		});
		
		return listsView;
	}
	
	public GraphListAdapter getAdapter() {
		return _adapter;
	}
	
	public void loadListData() {
		SharedPreferences data = _main.getSharedPreferences("Data", 0);
		Set<String> objects = data.getStringSet("Lists", null);
		
		if (objects == null) {
			return;
		}
		
		Gson gson = new Gson();
		
		Iterator<String> iterator = objects.iterator();
		while (iterator.hasNext()) {
			String json = iterator.next();
			GraphList item = gson.fromJson(json, GraphList.class);
			_adapter.add(item);
		} 
	}
	
	public void saveListData() {
		Gson gson = new Gson();
		String json;
		Set<String> objects = new HashSet<String>();
		for (int i = 0; i < _adapter.getCount(); i++) {
			json = gson.toJson(_adapter.getItem(i));
			objects.add(json);
		}
		SharedPreferences data = _main.getSharedPreferences("Data", 0);
		SharedPreferences.Editor editor = data.edit();
		editor.putStringSet("Lists", objects);
		editor.commit(); 
	}
}