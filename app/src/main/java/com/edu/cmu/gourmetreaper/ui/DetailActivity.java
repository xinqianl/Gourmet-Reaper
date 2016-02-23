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
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.cmu.gourmetreaper.R;
import com.edu.cmu.gourmetreaper.dblayout.CuisineDAO;
import com.edu.cmu.gourmetreaper.dblayout.CuisineReviewDAO;
import com.edu.cmu.gourmetreaper.dblayout.daoimpl.CuisineDAOImpl;
import com.edu.cmu.gourmetreaper.dblayout.daoimpl.CuisineReviewDAOImpl;
import com.edu.cmu.gourmetreaper.entities.Cuisine;
import com.edu.cmu.gourmetreaper.entities.CuisineReview;
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
import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends Activity {
    private CuisineDAO cd;
    private CuisineReviewDAO crd;
    private Cuisine c;
    private List<CuisineReview> crList;
    private TextView dishTitle, disText;
    private Button addButton;
    private ImageView dishImg1, dishImg2;
    private EditText dishComInput;
    private TextView dishComText1, dishComText2, reviewNum;
    private ImageView com1Star1, com1Star2, com1Star3, com1Star4, com1Star5;
    private ImageView com2Star1, com2Star2, com2Star3, com2Star4, com2Star5;
    private ImageView avgStar1, avgStar2, avgStar3, avgStar4, avgStar5;
    private ArrayList<String> chosenList;
    private CallbackManager callbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private ImageButton postButton;
    private ImageView selectButton, fromGallery;
    private RatingBar ratingBar;
    private TextView txtRatingValue;
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
        setContentView(R.layout.activity_detail);
        showDish();
        initReview();
        initFacebook();
        showReview();
        addListenerOnRatingBar();
    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                txtRatingValue.setText(String.valueOf(rating));
            }
        });
    }


    public void initFacebook() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken newToken) {
            }
        };
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
            }
        };
        facebookService = new FacebookService(mTokenTracker, mProfileTracker);
        this.startService(new Intent(this, FacebookService.class));

    }

    public void showDish() {
        dishTitle = (TextView) findViewById(R.id.dishTitle);
        disText = (TextView) findViewById(R.id.disText);
        dishImg1 = (ImageView) findViewById(R.id.dishImg1);
        cd = new CuisineDAOImpl(this);
        Bundle dishMsg = getIntent().getExtras();

        String dishName = dishMsg.getString("dishName");
        c = cd.getCuisineByName(dishName);
        dishTitle.setText(c.getCuisineName());
        disText.setText(c.getCuisineDescription());
        dishImg1.setImageBitmap(c.getImage());

        addButton = (Button) findViewById(R.id.addButton);
        chosenList = getIntent().getStringArrayListExtra("chosenList");
        if (chosenList != null && chosenList.size() > 0) {
            for (String dish : chosenList) {
                if (dish.equals(dishName)) {
                    addButton.setText("Added");
                    break;
                }
            }
        }

    }

    public void initReview() {
        crd = new CuisineReviewDAOImpl(this);
        crList = crd.getAllCuisineReviewWithCuisine(c.getCuisineID());
        dishComText1 = (TextView) findViewById(R.id.dishComText1);
        dishComText2 = (TextView) findViewById(R.id.dishComText2);
        com1Star1 = (ImageView) findViewById(R.id.com1Star1);
        com1Star2 = (ImageView) findViewById(R.id.com1Star2);
        com1Star3 = (ImageView) findViewById(R.id.com1Star3);
        com1Star4 = (ImageView) findViewById(R.id.com1Star4);
        com1Star5 = (ImageView) findViewById(R.id.com1Star5);
        com2Star1 = (ImageView) findViewById(R.id.com2Star1);
        com2Star2 = (ImageView) findViewById(R.id.com2Star2);
        com2Star3 = (ImageView) findViewById(R.id.com2Star3);
        com2Star4 = (ImageView) findViewById(R.id.com2Star4);
        com2Star5 = (ImageView) findViewById(R.id.com2Star5);
        avgStar1 = (ImageView) findViewById(R.id.avgStar1);
        avgStar2 = (ImageView) findViewById(R.id.avgStar2);
        avgStar3 = (ImageView) findViewById(R.id.avgStar3);
        avgStar4 = (ImageView) findViewById(R.id.avgStar4);
        avgStar5 = (ImageView) findViewById(R.id.avgStar5);

        reviewNum = (TextView) findViewById(R.id.reviewNum);
        dishComInput = (EditText) findViewById(R.id.dishComInput);

        postButton = (ImageButton) findViewById(R.id.fbShareButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePhotoToFacebook();
            }
        });

        fromGallery = (ImageView) findViewById(R.id.showImg);

        selectButton = (ImageView) findViewById(R.id.uploadImg);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    public void updateReviewNum() {
        reviewNum.setText(crList.size() + " Reviews");
    }

    public void updateReviewRating() {
        avgStar1.setVisibility(View.INVISIBLE);
        avgStar2.setVisibility(View.INVISIBLE);
        avgStar3.setVisibility(View.INVISIBLE);
        avgStar4.setVisibility(View.INVISIBLE);
        avgStar5.setVisibility(View.INVISIBLE);
        switch (getAvgReviewRating()) {
            case 1:
                avgStar1.setVisibility(View.VISIBLE);
                break;
            case 2:
                avgStar1.setVisibility(View.VISIBLE);
                avgStar2.setVisibility(View.VISIBLE);
                break;
            case 3:
                avgStar1.setVisibility(View.VISIBLE);
                avgStar2.setVisibility(View.VISIBLE);
                avgStar3.setVisibility(View.VISIBLE);
                break;
            case 4:
                avgStar1.setVisibility(View.VISIBLE);
                avgStar2.setVisibility(View.VISIBLE);
                avgStar3.setVisibility(View.VISIBLE);
                avgStar4.setVisibility(View.VISIBLE);
                break;
            case 5:
                avgStar1.setVisibility(View.VISIBLE);
                avgStar2.setVisibility(View.VISIBLE);
                avgStar3.setVisibility(View.VISIBLE);
                avgStar4.setVisibility(View.VISIBLE);
                avgStar5.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void addToOrder(View view) {
        if (addButton.getText().toString().equals("Add to order")) {
            addButton.setText("Added");
            Intent i = new Intent(this, OrderActivity.class);
            i.putExtra("singleDish", c.getCuisineName());
            startActivity(i);

        }
    }

    private void sharePhotoToFacebook() {
        if (Profile.getCurrentProfile() == null) {
            Toast.makeText(this, "You are not logged in. Please login before posting", Toast.LENGTH_LONG).show();
            return;
        }
        Bitmap bitmap = null;
        String postContent = "";
        try {
            bitmap = ((BitmapDrawable) fromGallery.getDrawable()).getBitmap();
            postContent = dishComInput.getText().toString();
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
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
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
        com1Star1.setVisibility(View.INVISIBLE);
        com1Star2.setVisibility(View.INVISIBLE);
        com1Star3.setVisibility(View.INVISIBLE);
        com1Star4.setVisibility(View.INVISIBLE);
        com1Star5.setVisibility(View.INVISIBLE);
        com2Star1.setVisibility(View.INVISIBLE);
        com2Star2.setVisibility(View.INVISIBLE);
        com2Star3.setVisibility(View.INVISIBLE);
        com2Star4.setVisibility(View.INVISIBLE);
        com2Star5.setVisibility(View.INVISIBLE);

        crList = crd.getAllCuisineReviewWithCuisine(c.getCuisineID());
        if (crList != null && crList.size() > 0) {
            if (crList.size() > 0 && crList.get(0) != null) {
                dishComText1.setText(crList.get(0).getComment());
                switch (crList.get(0).getRating()) {
                    case 1:
                        com1Star1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        com1Star1.setVisibility(View.VISIBLE);
                        com1Star2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        com1Star1.setVisibility(View.VISIBLE);
                        com1Star2.setVisibility(View.VISIBLE);
                        com1Star3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        com1Star1.setVisibility(View.VISIBLE);
                        com1Star2.setVisibility(View.VISIBLE);
                        com1Star3.setVisibility(View.VISIBLE);
                        com1Star4.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        com1Star1.setVisibility(View.VISIBLE);
                        com1Star2.setVisibility(View.VISIBLE);
                        com1Star3.setVisibility(View.VISIBLE);
                        com1Star4.setVisibility(View.VISIBLE);
                        com1Star5.setVisibility(View.VISIBLE);
                        break;
                }
            } else {
                dishComText1.setText("There is no comments yet");
            }
            if (crList.size() > 1 && crList.get(1) != null) {
                dishComText2.setText(crList.get(1).getComment());
                switch (crList.get(1).getRating()) {
                    case 1:
                        com2Star1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        com2Star1.setVisibility(View.VISIBLE);
                        com2Star2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        com2Star1.setVisibility(View.VISIBLE);
                        com2Star2.setVisibility(View.VISIBLE);
                        com2Star3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        com2Star1.setVisibility(View.VISIBLE);
                        com2Star2.setVisibility(View.VISIBLE);
                        com2Star3.setVisibility(View.VISIBLE);
                        com2Star4.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        com2Star1.setVisibility(View.VISIBLE);
                        com2Star2.setVisibility(View.VISIBLE);
                        com2Star3.setVisibility(View.VISIBLE);
                        com2Star4.setVisibility(View.VISIBLE);
                        com2Star5.setVisibility(View.VISIBLE);
                        break;
                }
            } else {
                dishComText2.setText("There is no comments yet");
            }
        }
        updateReviewRating();
        updateReviewNum();
    }

    public void submitReview(View view) {
        if (dishComInput.getText() == null || dishComInput.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please type in your comments", Toast.LENGTH_LONG).show();
            return;
        }
        CuisineReview cr = new CuisineReview();
        cr.setComment(dishComInput.getText().toString());
        if (txtRatingValue.getText() == null || txtRatingValue.getText().toString().length() == 0) {
            cr.setRating(0);
        } else {
            cr.setRating((int) Double.parseDouble(txtRatingValue.getText().toString()));
        }
        List<Bitmap> imgs = new ArrayList<>();
        fromGallery.buildDrawingCache();
        Bitmap bmap = fromGallery.getDrawingCache();
        imgs.add(bmap);
        cr.setImages(imgs);
        crd.insertCuisineReview(cr, c.getCuisineID());
        showReview();
    }

    private int getAvgReviewRating() {
        int totalRating = 0;
        List<CuisineReview> allList = crd.getAllCuisineReviewWithCuisine(c.getCuisineID());
        if (allList == null || allList.size() == 0) {
            return 0;
        }
        for (CuisineReview cuisineReview : allList) {
            totalRating += cuisineReview.getRating();
        }
        return totalRating / allList.size();
    }
}
