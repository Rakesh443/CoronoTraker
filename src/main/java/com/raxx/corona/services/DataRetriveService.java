package com.raxx.corona.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.raxx.corona.models.Location;

@Service
public class DataRetriveService {
	
	private static String DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/01-01-2021.csv";
	private List<Location> loactions= new ArrayList<>();
	
	public List<Location> getLoactions() {
		return loactions;
	}

	//@Scheduled(cron = "* * * * * *")
	@PostConstruct
	public void fetchData() throws IOException, InterruptedException {
		
		
		
		System.out.println("started");
		HttpClient client = HttpClient.newHttpClient();
		
		HttpRequest request = (HttpRequest) HttpRequest.newBuilder().uri(URI.create(DATA_URL)).build();
		
		HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
		List<Location> tempList  = new ArrayList();
		
		
		StringReader reader = new StringReader(httpResponse.body());
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
		for (CSVRecord record : records) {
			Location loc = new Location();
			
			if(record.get("Country_Region")!="") {
				loc.setCountry(record.get("Country_Region"));
				if(record.get("Province_State")=="")
					loc.setState("NA");
				else
					loc.setState(record.get("Province_State"));
				
				
				if(record.get("Active")=="")
					loc.setTotalCases(0);
				else
					loc.setTotalCases(Integer.parseInt(record.get("Active")));
				tempList.add(loc);
			}

		    System.out.println(loc.getCountry());
		}
		
		this.loactions=tempList;
	}
	
	

}
