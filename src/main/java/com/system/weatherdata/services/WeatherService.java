package com.system.weatherdata.services;

import com.google.gson.Gson;
import com.system.pojo.UserRequest;
import com.system.pojo.weather.Weather;
import com.system.pojo.weather.WeatherAPI;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * https://api.meteostat.net/
 */
public class WeatherService {
    //https://api.meteostat.net/v1/history/daily?station=72502&start=2019-09-01&end=2019-09-30&key=GQ09iu1F
    private final String WEATHER_URL = "https://api.meteostat.net/v1/history/daily?";
    private final String API_KEY = "GQ09iu1F";
    private final Gson gson = new Gson();

    public void testWeather(UserRequest userRequest) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = createRequestURL(userRequest);
        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != 200) {
                throw new IOException("Exception: API response status code = " + responseCode);
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                WeatherAPI weatherApiResponse = gson.fromJson(result, WeatherAPI.class);
            }
        }
        httpClient.close();
    }

    /**
     * Creates request URL based on user data
     *
     * @param userRequest user request data
     * @return request URL
     */
    private String createRequestURL(UserRequest userRequest) {
        return WEATHER_URL + "station=" + userRequest.getStationId() + "&" + "start=" + userRequest.getStartDate() +
                "&" + "end=" + userRequest.getEndDate() + "&" + "key=" + API_KEY;
    }
}
