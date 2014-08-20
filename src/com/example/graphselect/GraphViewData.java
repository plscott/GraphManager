package com.example.graphselect;

import com.jjoe64.graphview.GraphViewDataInterface;

public class GraphViewData implements GraphViewDataInterface {
	private double _x;
	private double _y;
	
	public GraphViewData(double x, double y) {
		_x = x;
		_y = y;
	}
	
	@Override
	public double getX() {
		return _x;
	}

	@Override
	public double getY() {
		return _y;
	}
}