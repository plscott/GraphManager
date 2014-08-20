package com.example.graphselect;

import java.util.ArrayList;

public class GraphList {
	private ArrayList<GraphContainer> _graphs;
	private String _name;
	private String _subtitle;
	
	public GraphList(String name, String subtitle) {
		_graphs = new ArrayList<GraphContainer>();
		_name = name;
		_subtitle = subtitle;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getSubtitle() {
		return _subtitle;
	}
	
	public ArrayList<GraphContainer> getList() {
		return _graphs;
	}
}