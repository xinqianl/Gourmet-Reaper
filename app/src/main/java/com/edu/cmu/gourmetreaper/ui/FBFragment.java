package com.edu.cmu.gourmetreaper.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.cmu.gourmetreaper.R;
import com.edu.cmu.gourmetreaper.service.FacebookService;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import android.database.Cursor;
import android.provider.MediaStore.MediaColumns;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class FBFragment extends Fragment {

    private TextView mTextDetails ;
    private CallbackManager callbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private Button postButton;
    private ImageView selectButton;
    private ImageView fromGallery;
    private LoginButton loginButton;
    private ProfilePictureView profilePictureView;
    private static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private FacebookService facebookService;
    private FacebookCallback<LoginResult > mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();

            displayWelcomeMessage(profile);

            profilePictureView.setVisibility(View.VISIBLE);
            mTextDetails.setVisibility(View.VISIBLE);
            postButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTextDetails.setVisibility(View.INVISIBLE);
                    profilePictureView.setVisibility(View.INVISIBLE);
                }
            });

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    public FBFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager=CallbackManager.Factory.create();
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken newToken) {

                if (newToken == null) {


                    mTextDetails.setVisibility(View.INVISIBLE);
                    profilePictureView.setVisibility(View.INVISIBLE);

                }
            }
        };
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

                displayWelcomeMessage(newProfile);
                if (newProfile == null) {


                    mTextDetails.setVisibility(View.INVISIBLE);
                    profilePictureView.setVisibility(View.INVISIBLE);
                }
            }
        };
        facebookService = new FacebookService(mTokenTracker, mProfileTracker);
        getActivity().startService(new Intent(getActivity(), FacebookService.class));
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        LoginManager.getInstance().logInWithPublishPermissions(
                this,
                Arrays.asList("publish_actions"));
        loginButton.registerCallback(callbackManager, mCallback);

        mTextDetails = (TextView) view.findViewById(R.id.textView);
        postButton = (Button) view.findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePhotoToFacebook();
            }
        });
        postButton.setVisibility(View.GONE);
        profilePictureView = (ProfilePictureView) view.findViewById(R.id.profilePicture);

        fromGallery = (ImageView) view.findViewById(R.id.from_gallery);

        selectButton = (ImageView) view.findViewById(R.id.select_button);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fb, container, false);
    }

    private void displayWelcomeMessage(Profile profile){
        if(profile!=null){
            mTextDetails.setText("Hello,  " + profile.getName());
            try {
                profilePictureView.setProfileId(profile.getId());
            } catch (Exception e) {
                Log.e("BUG", e.toString());
                e.printStackTrace();
            }
            postButton.setVisibility(View.VISIBLE);
            profilePictureView.setVisibility(View.VISIBLE);
            mTextDetails.setVisibility(View.VISIBLE);
        }
    }

    private void sharePhotoToFacebook() {
        if (Profile.getCurrentProfile() == null) {
            Toast.makeText(getActivity(), "You are not logged in. Please login before posting", Toast.LENGTH_LONG).show();
            return;
        }
        Bitmap bitmap = null;
        EditText editText;
        String postContent = "";
        try {
            bitmap = ((BitmapDrawable) fromGallery.getDrawable()).getBitmap();
            editText = (EditText) getView().findViewById(R.id.input);
            postContent = editText.getText().toString();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Please choose photo that you want to post.", Toast.LENGTH_LONG).show();
            return;
        }
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .setCaption(postContent)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);
        Toast.makeText(getActivity(), "Thanks for using Lightening Order. Your post has been released.", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fos;
        try {
            destination.createNewFile();
            fos = new FileOutputStream(destination);
            fos.write(bytes.toByteArray());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fromGallery.setImageBitmap(bm);
    }


    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaColumns.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, projection, null, null,
                null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(columnIndex);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        fromGallery.setImageBitmap(bm);
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile=Profile.getCurrentProfile();

        displayWelcomeMessage(profile);

    }
    @Override
    public void onStop(){
        super.onStop();
        getActivity().stopService(new Intent(getActivity(), FacebookService.class));

    }
    private void selectImage() {
        final CharSequence[] items = {"Take Photo with Camera", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo that You Want to Post");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo with Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

}
