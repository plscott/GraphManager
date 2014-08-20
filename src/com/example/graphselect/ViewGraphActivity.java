package com.example.graphselect;

import java.util.Timer;
import java.util.TimerTask;

import com.example.graphselect.ui.ViewGraphFragment;
import com.jjoe64.graphview.GraphViewSeries;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ViewGraphActivity extends FragmentActivity {
	private GraphContainer _container;
	private GraphViewSeries _series;
	private ViewGraphFragment _fragment;
	private DataTask _dataTask;
	private Timer _refreshTimer;
	private boolean _checked = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_sideways);
		
		Bundle bundle = getIntent().getExtras();
		String name = bundle.getString("name");
		String subtitle = bundle.getString("subtitle");
		String database = bundle.getString("database");
		String query = bundle.getString("query");
		String x = bundle.getString("x");
		String y = bundle.getString("y");
		String user = bundle.getString("user");
		String password = bundle.getString("password");
		boolean isLine = bundle.getBoolean("isLine");
		
		_container = new GraphContainer(name, subtitle);
		_container.setIsLine(isLine);
		_container.setDatabase(database);
		_container.setQuery(query);
		_container.setX(x);
		_container.setY(y);
		_container.setUser(user);
		_container.setPassword(password);
		
		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		_fragment = ViewGraphFragment.newInstance(_container, false, true);
		ft.replace(R.id.graph_view_activity, _fragment);
		
		ft.commit();
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		
		_refreshTimer = new Timer();
		_refreshTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				ViewGraphActivity.this.refresh();
			}
		}, 15*1000, 10*1000);
	}
	
	public void refresh() {
		if (_checked) {
			this.runOnUiThread(timerTick);
		}
	};
	
	public Runnable timerTick = new Runnable() {
		public void run() {
			_dataTask = new DataTask(ViewGraphActivity.this, _fragment.getGraph(), _fragment);
		
			String influxUrl = ViewGraphActivity.this.getResources().getString(R.string.influx_url);
			String database = _container.getDatabase();
			String query = _container.getQuery();
			String user = _container.getUser();
			String password = _container.getPassword();
			
			_dataTask.execute(influxUrl, database, query, user, password);
		}
	};
	
	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		
		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			_fragment = ViewGraphFragment.newInstance(_container, true, false);
			ft.replace(R.id.graph_view_activity, _fragment);
		}
		else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			_fragment = ViewGraphFragment.newInstance(_container, false, false);
			ft.replace(R.id.graph_view_activity, _fragment);
		}
		
		ft.commit();
	}
	
	@Override
	public void onBackPressed() {
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.cancelTimer();
		this.finish();
	}
	
	public void setChecked(boolean isChecked) {
		_checked = isChecked;
	}
	
	public boolean isChecked() {
		return _checked;
	}
	
	public void cancelTimer() {
		_refreshTimer.cancel();
	}
	public void setSeries(GraphViewSeries series) {
		_series = series;
	}
	
	public GraphViewSeries getSeries() {
		return _series;
	}
}