package vttp2022.ssf.day16_weather.Models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Weather {
    private String main;
    private String description;
    private String icon;
    

    public String getMain() {
        return this.main;
    }
    public void setMain(String main) {
        this.main = main;
    }


    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    //json to model
    public static Weather create(JsonObject jo) {
        Weather w = new Weather();
            w.setMain(jo.getString("main"));
            w.setDescription(jo.getString("description"));
            w.setIcon(jo.getString("icon"));
            return w;
    }

    //model to json
    public JsonObject toJson() {
        return Json.createObjectBuilder()
        .add("main",main)
        .add("description",description)
        .add("icon",icon)
        .build();
    }
}
