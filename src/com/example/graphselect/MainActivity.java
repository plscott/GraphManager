package com.example.graphselect;

import com.example.graphselect.ui.GraphsFragment;
import com.example.graphselect.ui.GraphsPromptFragment;
import com.example.graphselect.ui.ListsFragment;
import com.example.graphselect.ui.PromptBarFragment;
import com.example.graphselect.widget.GraphAdapter;
import com.example.graphselect.widget.GraphListAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity {
	private PromptBarFragment _prompt;
	private ListsFragment _lists;
	private GraphsFragment _graphs;
	private GraphsPromptFragment _graphsPrompt;
	private boolean _isGraphs = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		_lists = new ListsFragment(this);
		GraphListAdapter adapter = _lists.getAdapter();
		ft.replace(R.id.list, _lists);
		
		_prompt = new PromptBarFragment(this);
		_prompt.setListAdapter(adapter);
		ft.replace(R.id.prompt_bar, _prompt);

		ft.commit();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		_lists.saveListData();
	}
	
	@Override
	public void onBackPressed() {
		if (_isGraphs) {
			this.changeToLists();
		}
		else {
			this.finish();
		}
	}
	
	public void select(int selected) {
		_prompt.selectItem(selected);
	}
	
	public void selectGraph(int selected) {
		_graphsPrompt.selectItem(selected);
	}

	public void changeToGraphs(GraphList graphList) {
		_isGraphs = true; 
		
		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
		
		_graphs = new GraphsFragment(this, graphList);
		GraphAdapter adapter = _graphs.getAdapter();
		ft.hide(_lists);
		ft.add(R.id.list, _graphs);
		
		_graphsPrompt = new GraphsPromptFragment(this, graphList);
		_graphsPrompt.setGraphAdapter(adapter);
		ft.hide(_prompt);
		ft.add(R.id.prompt_bar, _graphsPrompt);
		
		ft.commit();
	}
	
	public void changeToLists() {
		_isGraphs = false;
		
		GraphListAdapter adapter = _lists.getAdapter();
		adapter.updateCounts();
		_graphs.getAdapter().getCount();
		
		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		
		ft.remove(_graphs);
		ft.show(_lists);
		
		ft.remove(_graphsPrompt);
		ft.show(_prompt);

		ft.commit();
	}
}
