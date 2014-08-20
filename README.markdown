GraphManager
================

This is an android app that is used as a tool for visualizing influxdb timeseries. The application retrieves InfluxDB data using an encoded HTTP Get call (using a database name, username, password and query for the data). The data is returned in the JSON form and is then processed to produce a graph with its timestamp along the x axis and the InfluxDB data point along the y axis. "Android Graphview" is used to model the data set, which is scrollable and scalable. The data automatically refreshes every 10 seconds after initially opened. 

The graphs themselves are managed by two pages: a list of "graphlists" each of which contain a list of graphs. The user can easily navigate between different graphlists and group datasets together in different ways. 

Tested primarily for Samgung Galaxy S3.
