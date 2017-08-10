package technique.project.summer.arabicSnap;

import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PhotoProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {
    private static final String TAG = "PhotoProfileActivity";
    public static final int  PHOTO_LOADER_ID = 3 ;

    private ImageView mPhotographerProfileImage;
    private TextView mPhotographerUsername;
    private ImageView mPhotoImageView;
    private Button mDonlowadButton;
    private String mPhotoId;
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_photo_proile);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().setTitle(null);

        mPhotoImageView = (ImageView) findViewById(R.id.photo);
        mDonlowadButton = (Button) findViewById(R.id.download_button);
        mPhotographerProfileImage = (ImageView) findViewById(R.id.photograph_profile_image);
        mPhotographerUsername = (TextView) findViewById(R.id.photographer_name);

        mPhotoId = getIntent().getExtras().getString("ID");
        getSupportLoaderManager().initLoader(PHOTO_LOADER_ID,null,this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        switch (id){
            case PHOTO_LOADER_ID:
                return new PhotoLoader(this,mPhotoId);
            default:
                return null ;
        }
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        Log.d(TAG, "onLoadFinished: ");
        photo = (Photo) data;

        Log.d(TAG, "onLoadFinished: "+photo.getUnCroppedPhotoUrl());
        Glide.with(this)
                .load(photo.getUnCroppedPhotoUrl())
                .into(mPhotoImageView);

        Log.d(TAG, "onLoadFinished: "+photo.getPhotographerImageUrl());
        Glide.with(this)
                .load(photo.getPhotographerImageUrl())
                .apply(new RequestOptions().circleCrop())
                .into(mPhotographerProfileImage);
        Log.d(TAG, "onLoadFinished: "+photo.getPhotographerUsername());
        mPhotographerUsername.setText(photo.getPhotographerUsername());

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
