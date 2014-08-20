package com.example.graphselect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import com.example.graphselect.ui.ViewGraphFragment;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;

public class DataTask extends AsyncTask<String, Object, String> {
	private ViewGraphFragment _frag;
	private ProgressDialog _dialog;
	private AlertDialog.Builder _builder;
	private GraphView _graph;
	private ViewGraphActivity _activity;
	private boolean _failed = false;
	
	public DataTask(ViewGraphActivity activity, GraphView graph, ViewGraphFragment frag) {
		super();
		_frag = frag;
		_graph = graph;
		_frag.getActivity();
		_activity = activity;
		_dialog = new ProgressDialog(activity);
		_builder = new AlertDialog.Builder(activity);
	}
	
	@Override
	protected void onPreExecute() {
		_dialog.setMessage("Loading Data");
		_dialog.show();
		_graph.setVisibility(View.GONE);
		_frag.enableReturnButton(false);
	}
	
	@Override
	protected String doInBackground(String... urls) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse;
		
		String url = null;
		try {
			for (int i = 0; i < urls.length; i++) {
				if (urls[i] == null) {
					throw new Exception("Error finding data from source");
				}
			}
			String influxUrl = urls[0];
			String database = urls[1];
			String query = urls[2];
			String user = urls[3];
			String password = urls[4];
			
			query = "select * from foo";
			user = "root";
			password = "3bJec/ZaW6);Ty";
			
			url = "{influxDB_url}/db/{database}/series?q={query}&u={user}&p={pass}";
			url = url.replace("{influxDB_url}", influxUrl);
			url = url.replace("{database}", URLEncoder.encode(database, "utf-8"));
			url = url.replace("{query}", URLEncoder.encode(query, "utf-8"));
			url = url.replace("{user}", URLEncoder.encode(user, "utf-8"));
			url = url.replace("{pass}", URLEncoder.encode(password, "utf-8"));
		} 
		catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
			_failed = true;
		}
		
		String responseString = null;
		try {
			httpResponse = httpClient.execute(new HttpGet(url));
			StatusLine statusLine = httpResponse.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				httpResponse.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			}
			else {
				httpResponse.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		}
		catch (IOException i) {
			i.printStackTrace();
			return null;
		}
		return responseString;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if ((_dialog.isShowing()) && (!_failed)) {
			_dialog.dismiss();
			_graph.setVisibility(View.VISIBLE);
		}
		else if ((_dialog.isShowing()) && (_failed)) {
			_dialog.dismiss();
			
			_builder.setTitle("Error Loading Data!");
			_builder.setMessage("Check the parameters used to build the graph and your network connection");
			_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			
			AlertDialog alert = _builder.create();
			alert.show();
			_frag.disableRotation();
		}
		
		_frag.enableReturnButton(true);

		try {
			JSONArray object = new JSONArray(result);
			JSONArray points = object.getJSONObject(0).getJSONArray("points");
			
			GraphViewData[] data = new GraphViewData[points.length()];
			for (int i = 0; i < (points.length()); i++) {
				JSONArray current = points.getJSONArray(i);
				
				Object timestamp = current.get(0);
				long time = Long.valueOf(timestamp.toString());
				
				Object value = current.get(2);
				long val = Long.valueOf(value.toString());

				int j = (points.length() - 1) - i;
				data[j] = new GraphViewData(time, val);
			}

			GraphViewSeries series = new GraphViewSeries(data);
			_graph.addSeries(series);
			_activity.setSeries(series);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
}