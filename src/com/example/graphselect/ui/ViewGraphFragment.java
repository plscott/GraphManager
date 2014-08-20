package com.example.graphselect.ui;

import org.joda.time.DateTime;

import com.example.graphselect.DataTask;
import com.example.graphselect.GraphContainer;
import com.example.graphselect.R;
import com.example.graphselect.ViewGraphActivity;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewGraphFragment extends Fragment {
	private static boolean _isLandscape;
	private static boolean _lookUp;
	private static GraphContainer _container;
	private GraphView _graph;
	private Button _exit;
	
	public static final ViewGraphFragment newInstance(GraphContainer container, boolean isLandscape, boolean lookUp) {
		ViewGraphFragment frag = new ViewGraphFragment();
		_container = container;
		_isLandscape = isLandscape;
		_lookUp = lookUp;
		return frag;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view;
		if (_isLandscape) {
			view = (ViewGroup) inflater.inflate(R.layout.view_graph_landscape, container, false);
		}
		else {
			view = (ViewGroup) inflater.inflate(R.layout.view_graph_portrait, container, false);

			_exit = (Button) view.findViewById(R.id.exit_popup);
			_exit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					((ViewGraphActivity) ViewGraphFragment.this.getActivity()).cancelTimer();
					ViewGraphFragment.this.getActivity().finish();
				}
			});
		}
		
		CheckBox box = (CheckBox) view.findViewById(R.id.refresh_checkbox);
		box.setChecked(((ViewGraphActivity) ViewGraphFragment.this.getActivity()).isChecked());
		box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				((ViewGraphActivity) ViewGraphFragment.this.getActivity()).setChecked(isChecked);
			}
		});
		
		_graph = this.createGraph(view);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.graph_holder);
		layout.addView(_graph);
		
		return view;
	}
	
	public void enableReturnButton(boolean enable) {
		if (!_isLandscape) {
			_exit.setEnabled(enable);
		}
	}
	
	public void disableRotation() {
		this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	public void loadData(GraphView graph) {
		DataTask myTask = new DataTask((ViewGraphActivity) this.getActivity(), graph, this);
		
		String influxUrl = this.getResources().getString(R.string.influx_url);
		String database = _container.getDatabase();
		String query = _container.getQuery();
		String user = _container.getUser();
		String password = _container.getPassword();
		
		myTask.execute(influxUrl, database, query, user, password);
	}
	
	public GraphView createGraph(View view) {
		GraphView graph;
		if (_container.isLine()) {
			graph = new LineGraphView(this.getActivity(), "");			
		}
		else {
			graph = new BarGraphView(this.getActivity(), "");
		}
		
		graph.setCustomLabelFormatter(new CustomLabelFormatter() {
			@Override
			public String formatLabel(double value, boolean isValueX) {
				if (isValueX) {
					DateTime timestamp = new DateTime((long) value);
					String month = timestamp.monthOfYear().getAsShortText();
					String day = timestamp.dayOfMonth().getAsShortText();
					String label = month + " " + day;
					return label;
				}
				return null;
			}
		});
		
		if (_lookUp) {
			this.loadData(graph);
		}
		else {
			GraphViewSeries series = ((ViewGraphActivity) this.getActivity()).getSeries();
			if (series != null) {
				graph.addSeries(series);
			}
		}
		
		graph.setHorizontalLabels(null);
		graph.setVerticalLabels(null);
		graph.getGraphViewStyle().setGridColor(Color.BLUE);
		graph.getGraphViewStyle().setTextSize(36);
		graph.getGraphViewStyle().setVerticalLabelsColor(Color.BLUE);
		graph.getGraphViewStyle().setHorizontalLabelsColor(Color.BLUE);
		graph.getGraphViewStyle().setNumHorizontalLabels(4);
		graph.getGraphViewStyle().setNumVerticalLabels(6);
		
		TextView titleView = (TextView) view.findViewById(R.id.graph_title);
		TextView subtitleView = (TextView) view.findViewById(R.id.graph_subtitle);
		TextView xView = (TextView) view.findViewById(R.id.x_axis);
		TextView yView = (TextView) view.findViewById(R.id.y_axis);
		
		String title = _container.getName();
		String subtitle = _container.getSubtitle();
		String x = _container.getX();
		String y = _container.getY();
		
		titleView.setText(title);
		subtitleView.setText(subtitle);
		xView.setText(x);
		yView.setText(y);
		yView.startAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.vertical_text));
		
		graph.setScalable(true);
		
		return graph;	
	}	
	
	public GraphView getGraph() {
		return _graph;
	}
}
