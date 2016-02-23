package com.edu.cmu.gourmetreaper.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.cmu.gourmetreaper.R;
import com.edu.cmu.gourmetreaper.dblayout.CuisineDAO;
import com.edu.cmu.gourmetreaper.dblayout.RestaurantReviewDAO;
import com.edu.cmu.gourmetreaper.dblayout.daoimpl.CuisineDAOImpl;
import com.edu.cmu.gourmetreaper.dblayout.daoimpl.RestaurantReviewDAOImpl;
import com.edu.cmu.gourmetreaper.entities.Cuisine;
import com.edu.cmu.gourmetreaper.entities.RestaurantReview;
import com.edu.cmu.gourmetreaper.service.FacebookService;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Author: Qianwen Li
 * Team: Gourmet Reapers
 */
public class HomeActivity extends Activity {

    private final static String TAG = "DorisDebug";
    private RestaurantReviewDAO rrd;
    private CuisineDAO cd;

    private ImageView recImg1, recImg2, recImg3, popImg1, popImg2, popImg3;
    private TextView recText1, recText2, recText3, popText1, popText2, popText3, comText1;
    private EditText comInput;
    private RatingBar homeRatingBar;
    private TextView textRatingVale;
    private ImageView commStar1, commStar2, commStar3, commStar4, commStar5;

    private CallbackManager callbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private ImageButton postButton;
    private ImageView selectButton, fromGallery;
    private static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private FacebookService facebookService;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            postButton.setVisibility(View.VISIBLE);

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rrd = new RestaurantReviewDAOImpl(this);
        cd = new CuisineDAOImpl(this);
        initRecAndPop();
        initReview();
        initFacebook();
        addListenerOnRatingBar();
        showReview();
    }

    public void addListenerOnRatingBar() {
        homeRatingBar = (RatingBar) findViewById(R.id.homeRatingBar);
        textRatingVale = (TextView) findViewById(R.id.textRatingValue);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        homeRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                textRatingVale.setText(String.valueOf(rating));
            }
        });
    }


    private void initRecAndPop() {
        List<Cuisine> allList = cd.getAllCuisine();
        // recommendation
        recImg1 = (ImageView) findViewById(R.id.recImg1);
        recImg2 = (ImageView) findViewById(R.id.recImg2);
        recImg3 = (ImageView) findViewById(R.id.recImg3);
        recText1 = (TextView) findViewById(R.id.recText1);
        recText2 = (TextView) findViewById(R.id.recText2);
        recText3 = (TextView) findViewById(R.id.recText3);
        recImg1.setImageBitmap(null);
        recImg2.setImageBitmap(null);
        recImg3.setImageBitmap(null);

        recImg1.setImageBitmap(allList.get(0).getImage());
        recText1.setText(allList.get(0).getCuisineName());
        recImg2.setImageBitmap(allList.get(2).getImage());
        recText2.setText(allList.get(2).getCuisineName());
        recImg3.setImageBitmap(allList.get(6).getImage());
        recText3.setText(allList.get(6).getCuisineName());

        // Popular
        popImg1 = (ImageView) findViewById(R.id.popImg1);
        popImg2 = (ImageView) findViewById(R.id.popImg2);
        popImg3 = (ImageView) findViewById(R.id.popImg3);
        popText1 = (TextView) findViewById(R.id.popText1);
        popText2 = (TextView) findViewById(R.id.popText2);
        popText3 = (TextView) findViewById(R.id.popText3);
        popImg1.setImageBitmap(null);
        popImg2.setImageBitmap(null);
        popImg3.setImageBitmap(null);

        popImg1.setImageBitmap(allList.get(1).getImage());
        popText1.setText(allList.get(1).getCuisineName());
        popImg2.setImageBitmap(allList.get(4).getImage());
        popText2.setText(allList.get(4).getCuisineName());
        popImg3.setImageBitmap(allList.get(5).getImage());
        popText3.setText(allList.get(5).getCuisineName());

    }

    private void initReview() {
        comInput = (EditText) findViewById(R.id.comInput);
        comText1 = (TextView) findViewById(R.id.dishComText1);
        commStar1 = (ImageView) findViewById(R.id.commStar1);
        commStar2 = (ImageView) findViewById(R.id.commStar2);
        commStar3 = (ImageView) findViewById(R.id.commStar3);
        commStar4 = (ImageView) findViewById(R.id.commStar4);
        commStar5 = (ImageView) findViewById(R.id.commStar5);

        postButton = (ImageButton) findViewById(R.id.shareFBButton);
        fromGallery = (ImageView) findViewById(R.id.fromGalButton);
        fromGallery.setVisibility(View.INVISIBLE);
        selectButton = (ImageView) findViewById(R.id.selectButtonHome);
        selectButton.setVisibility(View.INVISIBLE);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    public void initFacebook() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken newToken) {

                if (newToken == null) {

                }
            }
        };
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

                if (newProfile == null) {

                }
            }
        };
        facebookService = new FacebookService(mTokenTracker, mProfileTracker);
        this.startService(new Intent(this, FacebookService.class));

    }

    public void submitRestReview(View view) {
        if (comInput.getText() == null || comInput.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please type in your comments", Toast.LENGTH_LONG).show();
            return;
        }
        RestaurantReview rr = new RestaurantReview();
        if (textRatingVale.getText() == null || textRatingVale.getText().toString().length() == 0) {
            rr.setRating(0);
        } else {
            rr.setRating((int) Double.parseDouble(textRatingVale.getText().toString()));
        }

        rr.setComment(comInput.getText().toString());
        rrd.insertRestaurantReview(rr);
        showReview();
    }

    public void shareFB(View view) {
        if (Profile.getCurrentProfile() == null) {
            Toast.makeText(this, "You are not logged in. Please login before posting", Toast.LENGTH_LONG).show();
            return;
        }
        Bitmap bitmap = null;
        String postContent = "";
        try {
            bitmap = ((BitmapDrawable) fromGallery.getDrawable()).getBitmap();
            postContent = comInput.getText().toString();
        } catch (Exception e) {
            Toast.makeText(this, "Please choose photo that you want to post.", Toast.LENGTH_LONG).show();
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
        Toast.makeText(this, "Thanks for using Lightening Order. Your post has been released.", Toast.LENGTH_LONG).show();
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
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = this.getContentResolver().query(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);
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
        Profile profile = Profile.getCurrentProfile();

    }

    @Override
    public void onStop() {
        super.onStop();
        this.stopService(new Intent(this, FacebookService.class));

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo with Camera", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public void showReview() {
        commStar1.setVisibility(View.INVISIBLE);
        commStar2.setVisibility(View.INVISIBLE);
        commStar3.setVisibility(View.INVISIBLE);
        commStar4.setVisibility(View.INVISIBLE);
        commStar5.setVisibility(View.INVISIBLE);
        if (rrd.getAllRestaurantReview() != null && rrd.getAllRestaurantReview().size() > 0) {
            RestaurantReview toprr = rrd.getRestaurantReviewByID(rrd.getAllRestaurantReview().size());
            if (toprr != null) {
                comText1.setText(toprr.getComment());
                // set stars
                switch (toprr.getRating()) {
                    case 1:
                        commStar1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        commStar1.setVisibility(View.VISIBLE);
                        commStar2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        commStar1.setVisibility(View.VISIBLE);
                        commStar2.setVisibility(View.VISIBLE);
                        commStar3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        commStar1.setVisibility(View.VISIBLE);
                        commStar2.setVisibility(View.VISIBLE);
                        commStar3.setVisibility(View.VISIBLE);
                        commStar4.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        commStar1.setVisibility(View.VISIBLE);
                        commStar2.setVisibility(View.VISIBLE);
                        commStar3.setVisibility(View.VISIBLE);
                        commStar4.setVisibility(View.VISIBLE);
                        commStar5.setVisibility(View.VISIBLE);
                        break;
                }
            }
        } else {
            comText1.setText("There is no comments yet");
        }

    }

    public void goToDetail(View view) {
        Intent i = new Intent(this, DetailActivity.class);
        String dishName = "";
        int id = view.getId();
        if (id == recImg1.getId()) {
            dishName = recText1.getText().toString();
        } else if (id == recImg2.getId()) {
            dishName = recText2.getText().toString();
        } else if (id == recImg3.getId()) {
            dishName = recText3.getText().toString();
        } else if (id == popImg1.getId()) {
            dishName = popText1.getText().toString();
        } else if (id == popImg2.getId()) {
            dishName = popText2.getText().toString();
        } else if (id == popImg3.getId()) {
            dishName = popText3.getText().toString();
        }
        i.putExtra("dishName", dishName);
        startActivity(i);
    }
}
