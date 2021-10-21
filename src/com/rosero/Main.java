package com.rosero;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final String key = "Key";
    private final String baseUrl = "https://api.openweathermap.org/data/2.5/";
    private Gson gson;
    private List<WeathSummary> sumaries;
    private HTTPHelper httpHelper;

    public Main() {
        gson = new Gson();
        httpHelper = new HTTPHelper();
        sumaries = new ArrayList<WeathSummary>();
    }



    public void loadMultipleForecasts(List<String> cityList) {

        for (String city : cityList) {
            WeatherForecast forecast = loadWeatherForecast(city);
            WeathSummary summary = new WeathSummary(city, forecast);
            sumaries.add(summary);
        }
    }

    public void sortMaxWind() {

        sumaries.sort(new Comparator<WeathSummary>() {
            @Override
            public int compare(WeathSummary o1, WeathSummary o2) {

                return Float.compare(o1.getMaxWind(), o2.getMaxWind());
            }
        });
    }

    public void sortMaxTemp() {
        sumaries.sort((WeathSummary o1, WeathSummary o2) -> (int) (o2.getMaxTemp() - o1.getMaxTemp()));
    }

    public List<WeathSummary> getSumaries() {
        return sumaries;
    }



    public WeatherConditions loadCurrentWeather(String city) {

        String url = baseUrl + "weather" +
                "?q=" + city +
                "&units=imperial" +
                "&apiKey=" + key;
        String data = httpHelper.readHTTP(url);

        return gson.fromJson(data, WeatherConditions.class);
    }

    public WeatherForecast loadWeatherForecast(String city) {

        String url = baseUrl + "forecast" +
                "?q=" + city +
                "&units=imperial" +
                "&apiKey=" + key;
        String data = httpHelper.readHTTP(url);

        return gson.fromJson(data, WeatherForecast.class);
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        Main mgr = new Main();


        System.out.println("Enter city: ");
        String city = scanner.nextLine();
        WeatherConditions conditions = mgr.loadCurrentWeather(city);
        System.out.println(conditions);
        WeatherForecast forecast = mgr.loadWeatherForecast(city);
        System.out.println(forecast);


        ArrayList<String> cities = new ArrayList<String>();
        cities.add("Cali");
        cities.add("Lima");
        cities.add("Bogota");
        cities.add("Rexburg");
        cities.add("London");
        System.out.println("Unsorted Summaries:");
        mgr.loadMultipleForecasts(cities);
        for (WeathSummary summary : mgr.getSumaries()) {
            System.out.println(summary);
        }

        System.out.println("Sorted Max Temp:");
        mgr.sortMaxTemp();
        for (WeathSummary summary : mgr.getSumaries()) {
            System.out.println(summary);
        }

        System.out.println("Sorted Max Wind:");
        mgr.sortMaxWind();
        for (WeathSummary summary : mgr.getSumaries()) {
            System.out.println(summary);
        }

    }
}