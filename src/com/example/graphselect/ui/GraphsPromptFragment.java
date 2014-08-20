package com.example.graphselect.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.graphselect.GraphContainer;
import com.example.graphselect.GraphList;
import com.example.graphselect.MainActivity;
import com.example.graphselect.R;
import com.example.graphselect.ViewGraphActivity;
import com.example.graphselect.widget.GraphAdapter;

public class GraphsPromptFragment extends Fragment {
	private MainActivity _main;
	private GraphAdapter _adapter;
	private GraphList _graphList;
	
	public GraphsPromptFragment(MainActivity main, GraphList graphList) {
		super();
		_main = main;
		_graphList = graphList;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.graphs_prompt, container, false);
		this.assignListeners(view, inflater);
		return view;
	}
	
	public void assignListeners(final ViewGroup group, final LayoutInflater inflater) {
		final Button add = (Button) group.findViewById(R.id.add_graph_button);
		final Button edit = (Button) group.findViewById(R.id.edit_button);
		TextView lists = (TextView) group.findViewById(R.id.lists_back_text);
		ImageView back = (ImageView) group.findViewById(R.id.lists_back_icon);
		
		OnClickListener addGraph = new OnClickListener() {
			@Override
			public void onClick(View view) {
				View popupView = inflater.inflate(R.layout.add_graph_popup, group, false);
				final PopupWindow popup = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				popup.setFocusable(true);
				popup.showAtLocation(group, Gravity.NO_GRAVITY, 0, 0);
				
				final EditText titleEntry = (EditText) popupView.findViewById(R.id.graph_title);
				final EditText subtitleEntry = (EditText) popupView.findViewById(R.id.graph_subtitle);
				final EditText databaseEntry = (EditText) popupView.findViewById(R.id.graph_data);
				final EditText queryEntry = (EditText) popupView.findViewById(R.id.query_text);
				final EditText xEntry = (EditText) popupView.findViewById(R.id.enter_x);
				final EditText yEntry = (EditText) popupView.findViewById(R.id.enter_y);
				
				final RadioGroup radio = (RadioGroup) popupView.findViewById(R.id.graph_type_group);
				
				
				SharedPreferences sharedPreferences = _main.getSharedPreferences("user", Context.MODE_PRIVATE);
		        final boolean firstRun;
		        final String savedUser;
		        final String savedPassword; 
		        if (sharedPreferences.getString("username", null) == null) {
		        	firstRun = true;
		        	savedUser = null;
		        	savedPassword = null;
		        }
		        else {
		        	firstRun = false;
		        	savedUser = sharedPreferences.getString("username", null);
		        	savedPassword = sharedPreferences.getString("password", null);
		        }
				
				final CheckBox check = (CheckBox) popupView.findViewById(R.id.check_user);
				if (firstRun) { 
					check.setChecked(false);
					check.setVisibility(View.INVISIBLE);
				}
				
				Button exit = (Button) popupView.findViewById(R.id.exit_popup);
				exit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						popup.dismiss();
					}
				});
				
				Button addGraph = (Button) popupView.findViewById(R.id.create_graph);
				addGraph.setOnClickListener(new OnClickListener() {
					private String _username;
					private String _password;
					
					@Override
					public void onClick(View view) {
						if ((firstRun) || (!check.isChecked())) {
							this.promptDialog();
						}
						else {
							this.showGraph();
						}
					}
					
					public void promptDialog() {
						AlertDialog.Builder builder = new AlertDialog.Builder(_main);
						final AlertDialog enterInfo = builder.create();
						View dialog = _main.getLayoutInflater().inflate(R.layout.user_password_dialog, null);
						enterInfo.setView(dialog);
						
						final EditText userEntry = (EditText) dialog.findViewById(R.id.username);
						final EditText passwordEntry = (EditText) dialog.findViewById(R.id.pass_text);
						
						Button save = (Button) dialog.findViewById(R.id.exit_user_password);
						save.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View view) { 
								_username = userEntry.getText().toString();
								_password = passwordEntry.getText().toString();
								enterInfo.dismiss();
								showGraph();
							}
						});
						
						enterInfo.show();
					}
					
					public void showGraph() {
						String title = titleEntry.getText().toString();
						String subtitle = subtitleEntry.getText().toString();
						GraphContainer container = new GraphContainer(title, subtitle);
						
						String database = databaseEntry.getText().toString();
						String query = queryEntry.getText().toString();
						String xText = xEntry.getText().toString();
						String yText = yEntry.getText().toString();
						container.setDatabase(database);
						container.setQuery(query);
						container.setX(xText);
						container.setY(yText);
						
						if ((firstRun) || (!check.isChecked())) {
							SharedPreferences prefs = _main.getSharedPreferences("user", Context.MODE_PRIVATE);
							prefs.edit().putString("username", _username).apply();
							prefs.edit().putString("password", _password).apply();

							container.setUser(_username);
							container.setPassword(_password);
						}
						else {
							container.setUser(savedUser);
							container.setPassword(savedPassword);
						}
						
						if (radio.getCheckedRadioButtonId() == R.id.line_graph) {
							container.setIsLine(true);
						}
						else {
							container.setIsLine(false);
						}
						
						int count = _adapter.getCount();
						_adapter.add(container);
						_adapter.reset();
						ArrayList<GraphContainer> listOfGraphs = _graphList.getList();
						listOfGraphs.add(container);
						popup.dismiss();
						GraphsPromptFragment.this.selectItem(count);
					}
				});
			}
		};
		
		OnClickListener backLists = new OnClickListener() {
			@Override
			public void onClick(View view) {
				_main.changeToLists();
			}
		};
		
		
		OnClickListener editGraphs = new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (edit.isSelected()) {
					edit.setSelected(false);
					add.setEnabled(true);
				}
				else {
					edit.setSelected(true);
					add.setEnabled(false);
				}
				_adapter.changeEditMode();
			}
		};
		
		add.setOnClickListener(addGraph);
		edit.setOnClickListener(editGraphs);
		lists.setOnClickListener(backLists);
		back.setOnClickListener(backLists);
		
		_adapter.setButtons(add, edit);
		_adapter.setGraphList(_graphList);
	}
	
	public void setGraphAdapter(GraphAdapter adapter) {
		_adapter = adapter;
	}
	
	public void selectItem(int selected) {
		GraphContainer container = _adapter.getItem(selected);
		String name = container.getName();
		String subtitle = container.getSubtitle();
		String database = container.getDatabase();
		String query = container.getQuery();
		String x = container.getX();
		String y = container.getY();
		String user = container.getUser();
		String password = container.getPassword();
		boolean isLine = container.isLine();
		
		Intent viewGraph = new Intent(_main, ViewGraphActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("name", name);
		bundle.putString("subtitle", subtitle);
		bundle.putString("database", database);
		bundle.putString("query", query);
		bundle.putString("x", x);
		bundle.putString("y", y);
		bundle.putString("user", user);
		bundle.putString("password", password);
		bundle.putBoolean("isLine", isLine);
		viewGraph.putExtras(bundle);
		startActivity(viewGraph);
	}
}