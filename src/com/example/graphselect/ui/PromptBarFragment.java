package com.example.graphselect.ui;

import com.example.graphselect.GraphList;
import com.example.graphselect.MainActivity;
import com.example.graphselect.R;
import com.example.graphselect.widget.GraphListAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

public class PromptBarFragment extends Fragment {
	private GraphListAdapter _adapter;

	private MainActivity _main;
	
	public PromptBarFragment(MainActivity main) {
		super();
		_main = main;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.prompt, container, false);
		this.assignListeners(view, inflater);
		return view;
	}
	
	public void assignListeners(final ViewGroup group, final LayoutInflater inflater) {
		final Button add = (Button) group.findViewById(R.id.add_button);
		final Button edit = (Button) group.findViewById(R.id.edit_button);
		
		OnClickListener addList = new OnClickListener() {
			@Override
			public void onClick(View view) {
				View popupView = inflater.inflate(R.layout.add_list_popup, group, false);
				final PopupWindow popup = new PopupWindow(popupView, (int) getResources().getDimension(R.dimen.popup_width),
															(int) getResources().getDimension(R.dimen.popup_height));
				popup.setFocusable(true);
				popup.showAtLocation(group, Gravity.CENTER, 0, 0);

				final EditText titleEntry = (EditText) popupView.findViewById(R.id.list_title);
				final EditText subtitleEntry = (EditText) popupView.findViewById(R.id.list_subtitle);

				Button exit = (Button) popupView.findViewById(R.id.exit_popup);
				exit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						popup.dismiss();
					}
				});
				
				Button  addList = (Button) popupView.findViewById(R.id.create_list);
				addList.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						String title = titleEntry.getText().toString();
						String subtitle = subtitleEntry.getText().toString();
						GraphList list = new GraphList(title, subtitle);
						_adapter.reset();
						_adapter.add(list);
						popup.dismiss();
						_main.changeToGraphs(list);
					}
				});
			}
		};
		
		OnClickListener editLists = new OnClickListener() {
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
		
		add.setOnClickListener(addList);
		edit.setOnClickListener(editLists);
		
		_adapter.setButtons(add, edit);
	}
	
	public void setListAdapter(GraphListAdapter adapter) {
		_adapter = adapter;
	}
	
	public void selectItem(int selected) {
		GraphList graphList = _adapter.getItem(selected);
		_main.changeToGraphs(graphList);
	}
}