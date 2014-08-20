package com.example.graphselect.ui;

import java.util.ArrayList;

import com.example.graphselect.GraphContainer;
import com.example.graphselect.GraphList;
import com.example.graphselect.MainActivity;
import com.example.graphselect.R;
import com.example.graphselect.widget.GraphAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GraphsFragment extends Fragment {
	private GraphAdapter _adapter;
	private GraphList _graphList;
	private MainActivity _main;
	
	public GraphsFragment(MainActivity main, GraphList graphList) {
		super();
		_main = main;
		_adapter = new GraphAdapter(main, R.layout.row);
		_graphList = graphList;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup graphsView = (ViewGroup) inflater.inflate(R.layout.graphs, container, false);
		ListView graphs = (ListView) graphsView.findViewById(R.id.list_of_graphs);
		TextView tv = (TextView) graphsView.findViewById(R.id.empty_graphs_view);
		graphs.setEmptyView(tv);
		graphs.setAdapter(_adapter);
		
		ArrayList<GraphContainer> listOfGraphs = _graphList.getList();
		int count = listOfGraphs.size();
		for (int i = 0; i < count; i++) {
			_adapter.add(listOfGraphs.get(i));
		}
		
		graphs.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				_main.selectGraph(position);
			}
		});

		return graphsView;
	}
	
	public GraphAdapter getAdapter() {
		return _adapter;
	}
}