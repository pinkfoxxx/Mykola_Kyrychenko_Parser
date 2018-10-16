/**
 * Created by Victor on 03.10.2018.
 */
import io.github.openunirest.http.HttpResponse;
import io.github.openunirest.http.JsonNode;
import io.github.openunirest.http.Unirest;
import lombok.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


@Getter
@Setter
@ToString
public class City {
    private String name;
    private String url;
    private String administrativeArea;
    private int numberOfCitizens;
    private String yearOfFound;
    private Coordinates coordinates; // Set this
    private JsonNode weather;
    private double area;

    private static final int INFO_SIZE = 6;

    public City() {};

    //<editor-fold desc="Getters and Setters">

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }


    public void setWeather(JsonNode weather) {
        this.weather = weather;
    }

    //</editor-fold>

    public static City parse(Element city) throws IOException {
        Elements info = city.select("td");
        if (info.size() == INFO_SIZE) {

            Element anchor = info.get(1).select("a").get(0);
            City myCity = new City();
            myCity.setName(anchor.attr("title"));
            myCity.setUrl(String.format("https://uk.wikipedia.org%s", anchor.attr("href")));

            //TODO  set all other attributes
            String urlCity = myCity.getUrl();
            Document docCity = Jsoup.connect(urlCity).get();
            Element cityElements=null;

            try{
                cityElements = docCity.getElementsByClass("geo").get(0);
            } catch (Exception e) {}

            String nohtml1 = String.valueOf(cityElements).replaceAll("<span class=\"geo\">","");
            String nohtml2 = nohtml1.replaceAll("</span>","");
            String nohtml3 = nohtml2.replaceAll(";","");
            String[] coordinates = nohtml3.split(" ");

            try {
                myCity.setCoordinates(new Coordinates(Double.valueOf(coordinates[0]), Double.valueOf(coordinates[1])));
            } catch (Exception e1){
                myCity.setCoordinates(new Coordinates(0, 0));
            }


            String cityCrd = myCity.getCoordinates().getLatitude()+","+myCity.getCoordinates().getLongitude();

            HttpResponse<JsonNode> getResponse = Unirest.get("http://api.apixu.com/v1/current.json?key=f6da3a783d34446f8f4120423180410&q="
                    +cityCrd).asJson();


            myCity.setWeather(getResponse.getBody());

            return myCity;
        }
        return null;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", administrativeArea='" + administrativeArea + '\'' +
                ", numberOfCitizens=" + numberOfCitizens +
                ", yearOfFound='" + yearOfFound + '\'' +
                ", coordinates=" + coordinates +
                ", \nweather=" + weather +
                '}';
    }

}