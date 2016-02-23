package com.edu.cmu.gourmetreaper.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TabHost;
import android.widget.Switch;
import android.widget.TextView;

import com.edu.cmu.gourmetreaper.R;
import com.edu.cmu.gourmetreaper.dblayout.CuisineCategoryDAO;
import com.edu.cmu.gourmetreaper.dblayout.CuisineDAO;
import com.edu.cmu.gourmetreaper.dblayout.daoimpl.CuisineCategoryDAOImpl;
import com.edu.cmu.gourmetreaper.dblayout.daoimpl.CuisineDAOImpl;
import com.edu.cmu.gourmetreaper.entities.Cuisine;
import com.edu.cmu.gourmetreaper.entities.CuisineCategory;
import com.edu.cmu.gourmetreaper.service.DBService;

public class MainActivity extends TabActivity {
    private TabHost tabHost;
    private CuisineCategoryDAO ccd;
    private CuisineDAO cd;
    private static Switch switch1;
    private static MediaPlayer song1;
    private DBService dbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ccd = new CuisineCategoryDAOImpl(this);
        cd = new CuisineDAOImpl(this);
        if (cd.getAllCuisine() == null || ccd.getAllCuisineCategory() == null) {
            initTable();
        }

        Resources resources = getResources();
        final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        // Home tab
        TabHost.TabSpec tabHome = tabHost
                .newTabSpec("Home")
                .setIndicator("Home", resources.getDrawable(R.drawable.icon_home_config))
                .setContent(new Intent().setClass(this, HomeActivity.class));

        // Menu tab
        TabHost.TabSpec tabMenu = tabHost
                .newTabSpec("Menu")
                .setIndicator("Menu", resources.getDrawable(R.drawable.icon_menu_config))
                .setContent(new Intent().setClass(this, MenuActivity.class));

        // Order tab
        TabHost.TabSpec tabOrder = tabHost
                .newTabSpec("Order")
                .setIndicator("Order", resources.getDrawable(R.drawable.icon_order_config))
                .setContent(new Intent().setClass(this, OrderActivity.class));

        // User tab
        TabHost.TabSpec tabUser = tabHost
                .newTabSpec("User")
                .setIndicator("User", resources.getDrawable(R.drawable.icon_user_config))
                .setContent(new Intent().setClass(this, ProfileActivity.class));

        // add all tabs
        tabHost.addTab(tabHome);
        tabHost.addTab(tabMenu);
        tabHost.addTab(tabOrder);
        tabHost.addTab(tabUser);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#EAEAEA")); // unselected
                    TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    tv.setTextColor(Color.parseColor("#787878"));
                }

                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FFBF00")); // selected
                TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                tv.setTextColor(Color.parseColor("#FFFFFF"));

            }
        });

        //set Windows tab as default (zero based)
        tabHost.setCurrentTab(0);
        for (int i = 1; i < tabHost.getTabWidget().getChildCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#EAEAEA")); // unselected
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#787878"));
        }
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FFBF00")); // selected
        TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#FFFFFF"));

        // play music
        song1 = MediaPlayer.create(this, R.raw.lost_stars);
        switchSong1();

       startService(new Intent(this, DBService.class));
    }

    private void initTable() {
        initCategory();
        initCuisine();
        initRReview();
        initCReview();

    }

    private void initCategory() {

        CuisineCategory cc1 = new CuisineCategory();
        cc1.setCategoryName("Appetizer");
        ccd.insertCuisineCategory(cc1);

        CuisineCategory cc2 = new CuisineCategory();
        cc2.setCategoryName("Entrees");
        ccd.insertCuisineCategory(cc2);

        CuisineCategory cc3 = new CuisineCategory();
        cc3.setCategoryName("Dessert");
        ccd.insertCuisineCategory(cc3);

    }

    private void initCuisine() {

        // appetizer
        Cuisine c1 = new Cuisine();
        c1.setCuisineName("Baked Grits");
        c1.setCuisineDescription("Buttery, cheese with shrimp or ham");
        c1.setPrice(11.99);
        c1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.dish_baked_grits));
        cd.insertCuisineWithCategory(c1, 1);

        Cuisine c2 = new Cuisine();
        c2.setCuisineName("Baby Bleu Salad");
        c2.setCuisineDescription("Mixed greens, blue cheese, oranges, strawberries");
        c2.setPrice(10.99);
        c2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.dish_baby_bleu));
        cd.insertCuisineWithCategory(c2, 1);

        Cuisine c3 = new Cuisine();
        c3.setCuisineName("Royal Red Shrimp");
        c3.setCuisineDescription("weet, salty, melt-in-your-mouth royal red shrimp");
        c3.setPrice(12.99);
        c3.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.dish_royal_red_shrimp));
        cd.insertCuisineWithCategory(c3, 1);

        // entrees
        Cuisine c4 = new Cuisine();
        c4.setCuisineName("Bouillabaisse");
        c4.setCuisineDescription("Scorpionfish, herbs spices");
        c4.setPrice(15.99);
        c4.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.dish_bouillabaisse));
        cd.insertCuisineWithCategory(c4, 2);

        Cuisine c5 = new Cuisine();
        c5.setCuisineName("Shrimp Grits");
        c5.setCuisineDescription("Shrimp and grits with cheddar cheese, chili peppers");
        c5.setPrice(20.99);
        c5.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.dish_shrimp_grits));
        cd.insertCuisineWithCategory(c5, 2);

        Cuisine c6 = new Cuisine();
        c6.setCuisineName("Athenian Grouper");
        c6.setCuisineDescription("Freshly caught fish, Greek flavor");
        c6.setPrice(18.99);
        c6.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.dish_athenian_grouper));
        cd.insertCuisineWithCategory(c6, 2);

        // dessert
        Cuisine c7 = new Cuisine();
        c7.setCuisineName("Black Bottom Pie");
        c7.setCuisineDescription("Dark-chocolate custard, chiffon, whipped cream");
        c7.setPrice(8.99);
        c7.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.dish_black_bottom_pie));
        cd.insertCuisineWithCategory(c7, 3);

    }

    private void initRReview() {
    }

    private void initCReview() {
    }

    private void switchSong1() {
        switch1 = (Switch) findViewById(R.id.switch1);
        //set the switch to ON
        switch1.setChecked(true);
        startSong1();
        //attach a listener to check for changes in state
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    startSong1();
                } else {
                    stopSong1();
                }
            }
        });
    }


    public void startSong1() {
        song1 = MediaPlayer.create(this, R.raw.lost_stars);
        song1.start();
    }

    public void stopSong1() {
        song1.stop();
        song1.release();
    }
    @Override
    public void onStop(){
        super.onStop();
        stopService(new Intent(this, DBService.class));
    }

}
