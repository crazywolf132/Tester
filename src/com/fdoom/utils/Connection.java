package com.fdoom.utils;

import java.io.*;
import java.net.*;
import java.util.*;


public class Connection {
	private URL url;
	private URLConnection urlcon;
	public Connection(URL url) {
		this.url = url;
	}
	
	public URLConnection createConnection() throws IOException {
		urlcon = url.openConnection();
		return urlcon;
	}
	
	public List<String> readURL() throws IOException {
		List<String> l = new ArrayList<String>();
		InputStreamReader inputStream = new InputStreamReader(
				urlcon.getInputStream());
		BufferedReader reader = new BufferedReader(inputStream);
		for(String s;(s = reader.readLine()) != null; ) {
			l.add(s);
		}
		inputStream.close();
		reader.close();
		return l;
	}
}
