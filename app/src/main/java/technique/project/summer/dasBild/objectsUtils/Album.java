package technique.project.summer.dasBild.objectsUtils;

import java.util.ArrayList;

/**
 * Created by azeddine on 13/08/17.
 */

public class Album {

    private ArrayList<Photo> photos = new ArrayList<>();
    private String category;
    private int totalPages ;
    private int totalItems;

    public Album(){

    }

    public Album(ArrayList<Photo> photos, String category, int totalPages, int totalItems) {
        this.category = category;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public void addPhotos(ArrayList<Photo> photos){
        this.photos.addAll(photos);
    }
    public void addPhoto(Photo photo){
        this.photos.add(photo);
    }
    public void removePhoto(int index){
        if(index< photos.size()) photos.remove(index);
    }
    public Photo getPhotoByIndex(int index) {
        if (photos == null) return null; else return photos.get(index);
    }
}
