package technique.project.summer.arabicSnap;

/**
 * Created by azeddine on 28/07/17.
 */

public class Photo {
    private final String  ID;
    private String description;
    private String url;

    public Photo(String ID, String url) {
        this.ID = ID;
        this.url = url;
    }

    public Photo(String ID, String description, String url) {
        this.ID = ID;
        this.description = description;
        this.url = url;
    }

    public String getID() {
        return ID;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
