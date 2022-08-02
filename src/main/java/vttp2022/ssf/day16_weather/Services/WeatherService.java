package vttp2022.ssf.day16_weather.Services;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssf.day16_weather.Models.Weather;
import vttp2022.ssf.day16_weather.Repository.WeatherRepository;

@Service
public class WeatherService {

    private static final String URL = "https://api.openweathermap.org/data/2.5/weather";

    @Value("${API_KEY}")
    private String key;

    @Autowired
    private WeatherRepository weatherRepo;

    // because the data is an array, we use a list
    public List<Weather> getWeather(String city) {

        // check whether we have the weather cahced
        Optional<String> opt = weatherRepo.get(city);
        System.out.printf(">>>> cache: %s", opt);
        
        String payload;

        //Check if the box is empty
        if (opt.isEmpty()) {

            System.out.println("Getting weather from OpenWeatherMap");

            String url = UriComponentsBuilder.fromUriString(URL)
                    .queryParam("q", city)
                    .queryParam("appid", key)
                    .toUriString();

            // Create the GET request, GET url
            RequestEntity<Void> req = RequestEntity.get(url).build();
            // ResponseEntity<String> resp;

            // Make the call to OpenWeatherMap
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> resp;
            
            try {
                //throws an exception if status codenot in between 200-399
                // expect response to come back
                resp = template.exchange(req, String.class);
                
            } catch (Exception ex) {
                System.err.printf("Error: %s\n", ex.getMessage());
                return Collections.emptyList();
            }

            // check status code - if not 200 it is an error
            // if (resp.getStatusCodeValue() != 200) {
            //     System.err.println("Error status code is not 200");
            //     return Collections.emptyList();
            // }

            // Get the payload and do something with it
            // String payload = resp.getBody();
            payload = resp.getBody();
            System.out.println("payload: " + payload);

            weatherRepo.save(city, payload);
        } else {
            //retrieve the value
            payload = opt.get();
            System.out.printf(">>>> cache: %s", payload);
        }

        // Convert payload to JsonObject

        // 1. Construct reader - Convert the String to the Reader
        Reader strReader = new StringReader(payload);
        // 2. Create a jsonreader from reader
        JsonReader jsonReader = Json.createReader(strReader);
        // 3. Read the payload as Json object
        JsonObject weatherResult = jsonReader.readObject();
        JsonArray cities = weatherResult.getJsonArray("weather");

        List<Weather> list = new LinkedList<>();
        // go thru the
        for (int i = 0; i < cities.size(); i++) {
            // weather[0]
            JsonObject w = cities.getJsonObject(i);
            list.add(Weather.create(w));
        }
        return list;

    }
}
