package com.example.graphselect.widget;

import java.util.ArrayList;

import com.example.graphselect.GraphContainer;
import com.example.graphselect.GraphList;
import com.example.graphselect.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GraphAdapter extends ArrayAdapter<GraphContainer> {
	private Context _context; 
	private ArrayList<ImageView> _forwards;
	private ArrayList<ImageView> _exits;
	private Button _addButton;
	private Button _editButton;
	private GraphList _graphList;
	private boolean _editMode;
	
	public GraphAdapter(Context context, int layoutResource) {
		super(context, layoutResource);
		_context = context;
		_forwards = new ArrayList<ImageView>();
		_exits = new ArrayList<ImageView>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.graph_row, parent, false);
		}
		
		TextView title = (TextView) rowView.findViewById(R.id.cell_title);
		TextView subtitle = (TextView) rowView.findViewById(R.id.subtitle);
		ImageView type = (ImageView) rowView.findViewById(R.id.graph_type);
		
		GraphContainer container = (GraphContainer) this.getItem(position);
		
		title.setText(container.getName());
		subtitle.setText(container.getSubtitle());
		
		if (container.isLine()) {
			type.setImageResource(R.drawable.line_type);
		}
		else {
			type.setImageResource(R.drawable.bar_type);
		}
		
		ImageView forward = (ImageView) rowView.findViewById(R.id.select_arrow);
		ImageView exit = (ImageView) rowView.findViewById(R.id.delete_item);
		
		if (_editMode) {
			forward.setVisibility(View.INVISIBLE);
			exit.setVisibility(View.VISIBLE);
		}
		else {
			forward.setVisibility(View.VISIBLE);
			exit.setVisibility(View.INVISIBLE);
		}
		
		_forwards.add(forward);
		_exits.add(exit);
		
		return rowView;
	}
	
	public void changeEditMode() {
		for (int i = 0; i < this.getCount(); i++) {			
			if (_editMode) {
				_exits.get(i).setVisibility(View.INVISIBLE);
				_forwards.get(i).setVisibility(View.VISIBLE);
				
				if (i == (this.getCount() - 1)) {
					_editMode = false;
				}
			}
			else {
				_forwards.get(i).setVisibility(View.INVISIBLE);
				_exits.get(i).setVisibility(View.VISIBLE);
				_exits.get(i).setOnClickListener(new MyClickListener(i));
				
				if (i == (this.getCount() - 1)) {
					_editMode = true;
				}
			}
		}
	}
	
	public void reset() {
		_forwards = new ArrayList<ImageView>();
		_exits = new ArrayList<ImageView>();
	}
	
	public void setButtons(Button add, Button edit) {
		_addButton = add;
		_editButton = edit;
	}
	
	public void setGraphList(GraphList list) {
		_graphList = list;
	}
	
	private class MyClickListener implements OnClickListener {
		private int _selected; 
		
		public MyClickListener(int selected) {
			_selected = selected;
		}
		
		@Override
		public void onClick(View view) {
			GraphAdapter.this.reset();
			GraphContainer container = GraphAdapter.this.getItem(_selected);
			GraphAdapter.this.remove(container);
			_graphList.getList().remove(container);
			
			if (GraphAdapter.this.getCount() == 0) {
				_editMode = false;
				_editButton.setSelected(false);
				_addButton.setEnabled(true);
			}
		}
	}
}