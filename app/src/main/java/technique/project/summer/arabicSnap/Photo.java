package technique.project.summer.arabicSnap;

import java.io.Serializable;

/**
 * Created by azeddine on 28/07/17.
 */

public class Photo implements Serializable{
    private final String id;
    private String description;
    private String url;

    public Photo(String ID, String url) {
        this.id = ID;
        this.url = url;
    }

    public Photo(String ID, String description, String url) {
        this.id = ID;
        this.description = description;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
