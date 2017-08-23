package azeddine.project.summer.dasBild.objectsUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by azeddine on 28/07/17.
 */

public class Country {
    private static final String FLAG_BASE_URL= "https://flagpedia.net/data/flags/normal/";

    private String name;
    private String capital;
    private String flagURL;
    private String twoAlphaCode;
    private String threeAlphaCode;
    private int photosNum = 0;
    private ArrayList<Photo> photoArrayList ;



    public Country(String name, String twoAlphaCode, String threeAlphaCode) {
        this.name = name.split("\\(")[0];
        this.twoAlphaCode = twoAlphaCode.toLowerCase();
        this.flagURL = FLAG_BASE_URL+ this.twoAlphaCode +".png";
        this.threeAlphaCode =threeAlphaCode;
    }
    public Country(String name){
        setName(name);
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

    public String getThreeAlphaCode() {
        return threeAlphaCode;
    }

    public void setThreeAlphaCode(String threeAlphaCode) {
        this.threeAlphaCode = threeAlphaCode;
    }

    public ArrayList<Photo> getPhotoArrayList() {
        return photoArrayList;
    }

    public void setPhotoArrayList(ArrayList<Photo> photoArrayList) {
        this.photoArrayList = photoArrayList;
    }

    public String getTwoAlphaCode() {
        return twoAlphaCode;
    }

    public void setTwoAlphaCode(String twoAlphaCode) {
        this.twoAlphaCode = twoAlphaCode;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Country){
            return this.getName().equalsIgnoreCase(((Country) obj).getName());
        }else return false;

    }
}
