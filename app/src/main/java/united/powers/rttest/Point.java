package united.powers.rttest;

/**
 * Created by Disctanger on 3/24/2016.
 */
public class Point {
    double lattitude;
    double longtitude;
    double distance;
    String stopName;


    Point (){

    }

    Point (String value){
        stopName = value;
    }

    Point (String name, double vLattitude, double vLongtitude){
        stopName = name;
        lattitude = vLattitude;
        longtitude = vLongtitude;
    }

    Point (String name, double vDistance){
        distance = vDistance;
        stopName = name;
    }

    public void setDistance (double value){
        distance = value;
    }

    public void setName(String value){
        stopName = value;
    }

    public void setLattitude(double value){
        lattitude = value;
    }

    public void setLongtitude(double value){
        longtitude = value;
    }

    public String getName(){
        return stopName;
    }

    public double getLatitude(){
        return lattitude;
    }

    public double getLongtitude(){
        return longtitude;
    }

    public double getDistance(){
        return distance;
    }
}
