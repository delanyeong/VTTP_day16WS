package vttp2022.ssf.day16_weather.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.ssf.day16_weather.Models.Weather;
import vttp2022.ssf.day16_weather.Services.WeatherService;



@Controller
@RequestMapping (path={"/weather"})
public class WeatherController {

    @Autowired
    private WeatherService weatherSvc;
    
    @GetMapping
    public String getWeather (@RequestParam String city, Model model) {
        List<Weather> weather = weatherSvc.getWeather(city);
        model.addAttribute("weather", weather);
        model.addAttribute("city", city.toUpperCase());
        return "weather";
    }
    
}
