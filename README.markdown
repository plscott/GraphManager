GraphManager
================

This is an android app that is used as a tool for visualizing influxdb timeseries. The application retrieves InfluxDB data using an encoded HTTP Get call (using a database name, username, password and query for the data). The data is returned in the JSON form and is then processed to produce a graph with its timestamp along the x axis and the InfluxDB data point along the y axis. "Android Graphview" is used to model the data set, which is scrollable and scalable. The data automatically refreshes every 10 seconds after initially opened. 

The graphs themselves are managed by two pages: a list of "graphlists" each of which contain a list of graphs. The user can easily navigate between different graphlists and group datasets together in different ways. 

Tested primarily for Samgung Galaxy S3.

<img src="https://cloud.githubusercontent.com/assets/7840727/3986690/31e80dbe-289d-11e4-9058-2feb994812e0.png" height="280" /> &nbsp;
<img src="https://cloud.githubusercontent.com/assets/7840727/3986686/31723d46-289d-11e4-8dfe-e611092714d0.png" height="280" /> nbsp;
<img src="https://cloud.githubusercontent.com/assets/7840727/3986689/31e6a500-289d-11e4-9ea7-cf5de4685a4f.png" height="280" /> nbsp;
<img src="https://cloud.githubusercontent.com/assets/7840727/3986687/3190e214-289d-11e4-9c68-d15d0c19c91a.png" height="280" /> nbsp;
<img src="https://cloud.githubusercontent.com/assets/7840727/3986688/31aac36e-289d-11e4-89aa-e4ecd26d9959.png" height="280" />
