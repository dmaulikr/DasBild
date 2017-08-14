package azeddine.project.summer.dasBild.loaders;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import azeddine.project.summer.dasBild.ApiUtils;
import azeddine.project.summer.dasBild.objectsUtils.Photo;

/**
 * Created by azeddine on 10/08/17.
 */

public class PhotoLoader extends android.support.v4.content.AsyncTaskLoader<Object>{
    private static final String TAG = "PhotoLoader";

    private String mPhotoId;
    private Photo photo;
    public PhotoLoader(Context context,String id) {
        super(context);
        mPhotoId = id;
    }

    @Override
    public Object loadInBackground() {
        Uri url = new Uri.Builder()
                .encodedPath(CountryAlbumLoader.PHOTO_API_BASE_URL)
                .appendPath(mPhotoId)
                .appendQueryParameter("image_size",CountryAlbumLoader.API_UNCROPPED_IMAGE_SIZE)
                .appendQueryParameter("consumer_key",CountryAlbumLoader.API_CONSUMER_KEY)
                .build();
        try {
            String photoJsonString = ApiUtils.run(url);
            JSONObject photoJsonObject = new JSONObject(photoJsonString).getJSONObject("photo");
            photo = new Photo();

            Log.d(TAG, "loadInBackground: "+photoJsonObject);

            photo.setUnCroppedPhotoUrl(photoJsonObject.getString("image_url"));
            photo.setPhotographerImageUrl(photoJsonObject.getJSONObject("user").getString("userpic_url"));
            photo.setPhotographerUsername(photoJsonObject.getJSONObject("user").getString("username"));


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            return photo;
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
