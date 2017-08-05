package technique.project.summer.arabicSnap;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by azeddine on 31/07/17.
 */

public class CountryAlbumLoader extends AsyncTaskLoader<Object> {
    private static final String TAG = "CountryAlbumLoader";

    public static final String PHOTO_API_BASE_URL = "https://api.500px.com/v1/photos/search";
    public static final String API_CONSUMER_KEY ="8rESAvR28TpJTguNMbEabYUkDRBXK2ldh2H6Ypy0";
    public static final String API_LARGE_IMAGE_SIZE ="600";


    private String mCountryName;
    private int mAlbumPageNumber = 1;

    public CountryAlbumLoader(Context context,String countryName) {
        super(context);
        this.mCountryName =  countryName;
    }

    public CountryAlbumLoader(Context context,String countryName,int pageNumber) {
        super(context);
        mCountryName =  countryName;
        mAlbumPageNumber = pageNumber;
    }





    private Photo getPhotoInstance (JSONObject jsonObject) throws JSONException {

        String id =  jsonObject.getString("id");
        String url = jsonObject.getString("image_url");
        String description = jsonObject.getString("description");
        return new Photo(id,description,url);

    }
    @Override
    public ArrayList<Photo> loadInBackground() {
        Log.d(TAG, "loadInBackground: ");
        String responseBodyString;
        JSONArray photosJsonArray;
        ArrayList<Photo> photoArrayList = new ArrayList<>();
        Uri url = new Uri.Builder()
                .encodedPath(PHOTO_API_BASE_URL)
                .appendQueryParameter("term",mCountryName)
                .appendQueryParameter("image_size",API_LARGE_IMAGE_SIZE)
                .appendQueryParameter("page",""+mAlbumPageNumber)
                .appendQueryParameter("rpp","18")
                .appendQueryParameter("consumer_key",API_CONSUMER_KEY)
                .build();
        Log.d(TAG, "loadInBackground: the url is "+url);
        try{
              responseBodyString =  ApiUtils.run(url);
              photosJsonArray = new JSONObject(responseBodyString).getJSONArray("photos");
             for(int i=0;i<photosJsonArray.length();i++){
                 photoArrayList.add(getPhotoInstance(photosJsonArray.getJSONObject(i)));

             }

    } catch (IOException e) {
        e.printStackTrace();
    } finally {
      return photoArrayList;
    }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }


    public void forceLoad(int pageNumber) {
        super.forceLoad();
        mAlbumPageNumber = pageNumber;
        loadInBackground();
    }

}
