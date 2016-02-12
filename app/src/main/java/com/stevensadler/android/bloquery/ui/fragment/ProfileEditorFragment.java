package com.stevensadler.android.bloquery.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.stevensadler.android.bloquery.R;
import com.stevensadler.android.bloquery.ui.BloqueryApplication;
import com.stevensadler.android.bloquery.utils.BitmapUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Steven on 1/24/2016.
 */
public class ProfileEditorFragment extends Fragment implements
        IDelegatingFragment,
        Observer,
        View.OnClickListener {

    public static interface Delegate extends IFragmentDelegate {
        public void onProfileSaveClicked(Bitmap bitmap, String description);
    }

    private static String TAG = ProfileEditorFragment.class.getSimpleName();

    private int RESULT_GALLERY_IMAGE = 1;
    private int RESULT_CAMERA_IMAGE = 2;

    private TextView mUserNameView;
    private ImageView mImageView;
    private Button mGalleryButton;
    private Button mCameraButton;
    private Button mSaveButton;
    private Button mExitButton;

    private Bitmap mBitmap;
    private EditText mDescriptionText;

    private WeakReference<Delegate> delegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        // TODO add retrieving image from profile object

        //this.delegate = new WeakReference<Delegate>((Delegate) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = layoutInflater.inflate(R.layout.fragment_profile_editor, viewGroup, false);
        mUserNameView = (TextView) view.findViewById(R.id.tv_fragment_profile_editor_user);
        mDescriptionText = (EditText) view.findViewById(R.id.et_fragment_profile_editor_description);
        mImageView = (ImageView) view.findViewById(R.id.iv_fragment_profile_editor_image);
        mGalleryButton = (Button) view.findViewById(R.id.b_fragment_profile_editor_load_gallery);
        mCameraButton = (Button) view.findViewById(R.id.b_fragment_profile_editor_load_camera);
        mSaveButton = (Button) view.findViewById(R.id.b_fragment_profile_editor_save);
        mExitButton = (Button) view.findViewById(R.id.b_fragment_profile_editor_exit);

        // TODO get name from DataSource instead of calling ParseUser
        mUserNameView.setText(ParseUser.getCurrentUser().getUsername());
        mGalleryButton.setOnClickListener(this);
        mCameraButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mExitButton.setOnClickListener(this);

//        ParseObject profile = BloqueryApplication.getSharedDataSource().getCurrentUserProfile();
//        mDescriptionText.setText(profile.getString("description"));
//
//        ParseFile imageFile = profile.getParseFile("imageFile");
//        if (imageFile != null) {
//            try {
//                byte[] imageBytes = imageFile.getData();
//                mBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                mImageView.setImageBitmap(mBitmap);
//            } catch (com.parse.ParseException e) {
//                e.printStackTrace();
//            }
//        }



//        mDescriptionText.setText(ParseUser.getCurrentUser().get("profileDescription").toString());
//        ImageFile imageFile = (ImageFile) ParseUser.getCurrentUser().getParseFile("profileImage");
//        if (imageFile != null) {
//            mBitmap = imageFile.getBitmap();
//        }

        mDescriptionText.setText(BloqueryApplication.getSharedDataSource().getCurrentUserProfileDescription());
        mBitmap = BloqueryApplication.getSharedDataSource().getCurrentUserProfileImage();
        if (mBitmap != null) {
            mImageView.setImageBitmap(mBitmap);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult    requestCode " + requestCode + "    resultCode " + resultCode);
        if (data != null) {
            if (requestCode == RESULT_GALLERY_IMAGE && resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                try {
                    // allow maximum 1000x1000 image so it remains well under the 10MB ParseFile limit
                    mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    mBitmap = BitmapUtils.scaleDownBitmap(mBitmap, 1000f, true);
                    mImageView.setImageBitmap(mBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESULT_CAMERA_IMAGE && resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult    camera image");

                mBitmap = (Bitmap) data.getExtras().get("data");
                mBitmap = BitmapUtils.scaleDownBitmap(mBitmap, 1000f, true);
                mImageView.setImageBitmap(mBitmap);
            }
        }
    }

    /*
     * Observer
     */
    @Override
    public void update(Observable observable, Object data) {
        Log.d(TAG, "update");
        // TODO does profile editor fragment really need to be an observer, since only the currentUser can change the data on this view?
    }

    /*
     * View.OnClickListener
     */
    @Override
    public void onClick(View view) {
        if (view == mGalleryButton) {
            Log.d(TAG, "onClick: mGalleryButton");

            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, RESULT_GALLERY_IMAGE);
        } else if (view == mCameraButton) {
            Log.d(TAG, "onClick: mCameraButton");

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, RESULT_CAMERA_IMAGE);
            }
        } else if (view == mSaveButton) {
            String description = mDescriptionText.getText().toString();
            Log.d(TAG, "onClick mSaveButton " + description);
            if (mBitmap == null) {
                // or if description is empty?
                Toast.makeText(getActivity().getApplicationContext(),
                        "Select a profile image before saving",
                        Toast.LENGTH_LONG).show();
            } else if (getDelegate() != null) {
                getDelegate().onProfileSaveClicked(mBitmap, description);
            }
        } else if (view == mExitButton) {
            Log.d(TAG, "onClick mExitButton");
            getFragmentManager().popBackStack();
        }
    }

    /*
     *
     */
    public Delegate getDelegate() {
        if (delegate == null) {
            return null;
        }
        return delegate.get();
    }

    /*
     * IDelegatingFragment
     */
    public void setDelegate(IFragmentDelegate iFragmentDelegate) {
        if (iFragmentDelegate != null) {
            Delegate delegate = (Delegate) iFragmentDelegate;
            if (delegate != null) {
                this.delegate = new WeakReference<Delegate>(delegate);
            }
        }
    }
}
