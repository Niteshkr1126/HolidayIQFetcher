package com.nk.travel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FetchHotelsUrlByCity {

	static void readCityUrl() {
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("./input/City_Hotels_URLs.txt"));
			String line = reader.readLine();
			while (line != null) {
				String[] lineArray = line.split(" : ");
				String city = lineArray[0];
				String cityUrl = lineArray[1];
				System.out.println(cityUrl);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		readCityUrl();
	}
}
