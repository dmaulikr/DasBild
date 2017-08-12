package technique.project.summer.arabicSnap;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
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

    public static final String PHOTO_API_BASE_URL = "https://api.500px.com/v1/photos";
    public static final String API_CONSUMER_KEY ="8rESAvR28TpJTguNMbEabYUkDRBXK2ldh2H6Ypy0";
    public static final String API_LARGE_IMAGE_SIZE ="600";
    public static final String API_UNCROPPED_IMAGE_SIZE ="2048";
    public static final int DEFAULT_ALBUM_PAGE = 1;
    public static final int ALBUM_PAGE_IMAGE_NUM = 30;


    private String mCountryName;
    private int mAlbumPageNumber=DEFAULT_ALBUM_PAGE;
    ArrayList<Photo> mSavedPhotos = new ArrayList<>();

    public ArrayList<Photo> getSavedPhotos() {
      ArrayList<Photo> photos = mSavedPhotos;
      mSavedPhotos = null;
      if(photos == null) Log.d(TAG, "getSavedPhotos: It's null");
        return photos;
       }

    public void setSavedPhotos(ArrayList<Photo> savedPhotos) {
        this.mSavedPhotos = savedPhotos;
    }

    public CountryAlbumLoader(Context context, String countryName) {
        super(context);
        this.mCountryName =  countryName;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<Photo> loadInBackground() {
        Log.d(TAG, "loadInBackground: ");
        String responseBodyString;
        JSONArray photosJsonArray;
        ArrayList<Photo> photoArrayList = new ArrayList<>();

        Uri url = new Uri.Builder()
                .encodedPath(PHOTO_API_BASE_URL)
                .appendPath("search")
                .encodedQuery("image_size="+API_LARGE_IMAGE_SIZE+","+API_UNCROPPED_IMAGE_SIZE)
                .appendQueryParameter("term",mCountryName)
                .appendQueryParameter("page",""+mAlbumPageNumber)
                .appendQueryParameter("rpp",""+ALBUM_PAGE_IMAGE_NUM)
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
    public void deliverResult(Object data) {
        Log.d(TAG, "deliverResult: DATA SIZE "+((ArrayList<Photo>) data).size());
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        Log.d(TAG, "onReset: ");
    }


    public void forceLoad(int pageNumber) {
        Log.d(TAG, "forceLoad: ");
        mAlbumPageNumber = pageNumber;
        super.onForceLoad();
    }

    private Photo getPhotoInstance (JSONObject jsonObject) throws JSONException {
        Photo photo = new Photo();
        photo.setId(jsonObject.getString("id"));
        photo.setCroppedPhotoUrl(jsonObject.getJSONArray("images").getJSONObject(0).getString("url"));
        photo.setUnCroppedPhotoUrl(jsonObject.getJSONArray("images").getJSONObject(1).getString("url"));
        photo.setDescription(jsonObject.getString("description"));
        photo.setPhotographerImageUrl(jsonObject.getJSONObject("user").getString("userpic_url"));
        photo.setPhotographerUsername(jsonObject.getJSONObject("user").getString("fullname"));


        return photo;

    }




}
