package com.example.graphselect;

public class GraphContainer {
	private String _name;
	private String _subtitle;
	private String _database;
	private String _query;
	private String _xAxis;
	private String _yAxis;
	private String _user;
	private String _password;
	private boolean _isLine;

	public GraphContainer(String name, String subtitle) {
		_name = name;
		_subtitle = subtitle;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getSubtitle() {
		return _subtitle;
	}
	
	public boolean isLine() {
		return _isLine;
	}
	
	public void setIsLine(boolean isLine) {
		_isLine = isLine;
	}
	
	public void setDatabase(String data) {
		_database = data;
	}
	
	public void setQuery(String query) {
		_query = query;
	}
	
	public String getDatabase() {
		return _database;
	}
	
	public String getQuery() {
		return _query;
	}
	
	public void setX(String x) {
		_xAxis = x;
	}
	
	public void setY(String y) {
		_yAxis = y;
	}
	
	public String getX() {
		return _xAxis;
	}
	
	public String getY() {
		return _yAxis;
	}
	
	public void setUser(String user) {
		_user = user;
	}
	
	public void setPassword(String pass) {
		_password = pass;
	}
	
	public String getUser() {
		return _user;
	}
	
	public String getPassword() {
		return _password;
	}
}