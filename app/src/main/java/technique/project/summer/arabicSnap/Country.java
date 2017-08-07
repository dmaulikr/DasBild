package technique.project.summer.arabicSnap;

import java.util.ArrayList;

/**
 * Created by azeddine on 28/07/17.
 */

public class Country {
    private static final String FLAG_BASE_URL= "https://flagpedia.net/data/flags/normal/";
    private String name;
    private String capital;
    private String flagURL;
    private String code;
    private int photosNum = 0;
    private ArrayList<Photo> photoArrayList ;

    public Country(String name, String capital, String code) {
        this.name = name;
        this.capital = capital;
        this.code = code.toLowerCase();
        this.flagURL = FLAG_BASE_URL+this.code+".png";
    }

    public Country(String name, String capital, String code, int photosNum) {
        this.name = name;
        this.capital = capital;
        this.photosNum = photosNum;
        this.code = code.toLowerCase();
        this.flagURL = FLAG_BASE_URL+this.code+".png";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getFlagURL() {
        return flagURL;
    }

    public void setFlagURL(String flagURL) {
        this.flagURL = flagURL;
    }

    public int getPhotosNum() {
        return photosNum;
    }

    public void setPhotosNum(int photosNum) {
        this.photosNum = photosNum;
    }

    public ArrayList<Photo> getPhotoArrayList() {
        return photoArrayList;
    }

    public void setPhotoArrayList(ArrayList<Photo> photoArrayList) {
        this.photoArrayList = photoArrayList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
