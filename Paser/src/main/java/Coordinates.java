import lombok.ToString;
import org.jsoup.select.Elements;

import javax.xml.bind.Element;


//TODO Create class that represents coordinates
public class Coordinates {
    public double latitude;
    public double longitude;


    public Coordinates(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
