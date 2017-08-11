package technique.project.summer.arabicSnap;

import java.io.Serializable;

/**
 * Created by azeddine on 28/07/17.
 */

public class Photo implements Serializable{
    private  String id;
    private String description;
    private String croppedPhotoUrl;
    private String unCroppedPhotoUrl;
    private String photographerUsername;
    private String photographerImageUrl;


    public Photo() {

    }
    public Photo(String ID, String url) {
        this.id = ID;
        this.croppedPhotoUrl = url;
    }

    public Photo(String ID, String description, String url) {
        this.id = ID;
        this.description = description;
        this.croppedPhotoUrl = url;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCroppedPhotoUrl() {
        return croppedPhotoUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUnCroppedPhotoUrl(String unCroppedPhotoUrl) {
        this.unCroppedPhotoUrl = unCroppedPhotoUrl;
    }

    public void setPhotographerUsername(String photographerUsername) {
        this.photographerUsername = photographerUsername;
    }

    public void setPhotographerImageUrl(String photographerImageUrl) {
        this.photographerImageUrl = photographerImageUrl;
    }

    public String getUnCroppedPhotoUrl() {
        return unCroppedPhotoUrl;
    }

    public String getPhotographerUsername() {
        return photographerUsername;
    }

    public String getPhotographerImageUrl() {
        return photographerImageUrl;
    }

    public void setCroppedPhotoUrl(String croppedPhotoUrl) {
        this.croppedPhotoUrl = croppedPhotoUrl;
    }
}
