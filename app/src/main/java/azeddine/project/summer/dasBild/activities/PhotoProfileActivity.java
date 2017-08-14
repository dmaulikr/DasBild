package azeddine.project.summer.dasBild.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import azeddine.project.summer.dasBild.R;
import azeddine.project.summer.dasBild.objectsUtils.Photo;

public class PhotoProfileActivity extends AppCompatActivity {
    private static final String TAG = "PhotoProfileActivity";
    public static final int  PHOTO_LOADER_ID = 3 ;

    private ImageView mPhotographerProfileImage;
    private TextView mPhotographerUsername;
    private ImageView mPhotoImageView;
    private Button mDonlowadButton;
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_photo_profile);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().setTitle(null);

        mPhotoImageView = (ImageView) findViewById(R.id.photo);
        mPhotographerProfileImage = (ImageView) findViewById(R.id.photograph_profile_image);
        mPhotographerUsername = (TextView) findViewById(R.id.photographer_name);

        photo = (Photo) getIntent().getSerializableExtra("Photo");

        Log.d(TAG, "onCreate: the full image is "+photo.getUnCroppedPhotoUrl());
        Glide.with(this)
                .load(photo.getUnCroppedPhotoUrl())
                .into(mPhotoImageView);


        Glide.with(this)
                .load(photo.getPhotographerImageUrl())
                .apply(new RequestOptions().circleCrop())
                .into(mPhotographerProfileImage);


        mPhotographerUsername.setText(photo.getPhotographerUsername());

    }

    @Override
    protected void onStart() {
        super.onStart();
    }




}
