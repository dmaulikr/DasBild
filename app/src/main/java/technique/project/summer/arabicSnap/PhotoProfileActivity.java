package technique.project.summer.arabicSnap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import technique.project.summer.arabicSnap.R;

public class PhotoProfileActivity extends AppCompatActivity {
    private ImageView mPhotographerProfileImage;
    private TextView mPhotographerUsername;
    private ImageView mMainPhoto;
    private Button mDonlowadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_proile);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mMainPhoto = (ImageView) findViewById(R.id.photo);
        mDonlowadButton = (Button) findViewById(R.id.download_button);
        mPhotographerProfileImage = (ImageView) findViewById(R.id.photograph_profile_image);
        mPhotographerUsername = (TextView) findViewById(R.id.photographer_name);
    }
}
