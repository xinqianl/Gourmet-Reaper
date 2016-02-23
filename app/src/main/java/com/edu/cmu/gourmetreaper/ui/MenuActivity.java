package com.edu.cmu.gourmetreaper.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.cmu.gourmetreaper.R;
import com.edu.cmu.gourmetreaper.dblayout.CuisineDAO;
import com.edu.cmu.gourmetreaper.dblayout.daoimpl.CuisineDAOImpl;
import com.edu.cmu.gourmetreaper.entities.Cuisine;

import java.util.ArrayList;
import java.util.List;


public class MenuActivity extends Activity {

    private CuisineDAO cd;
    private final static int APPETIZER = 1;
    private final static int ENTREES = 2;
    private final static int DESSERT = 3;
    private List<Cuisine> apzList;
    private List<Cuisine> entList;
    private List<Cuisine> desList;
    private ArrayList<String> chosenList;

    private ImageView img1, img2, img3, img4, img5, img6, img7;
    private TextView desText1, desText2, desText3, desText4, desText5, desText6, desText7;
    private TextView priceTitle1, priceTitle2, priceTitle3, priceTitle4, priceTitle5, priceTitle6, priceTitle7;
    private CheckBox check1, check2, check3, check4, check5, check6, check7;
    private String d1, d2, d3, d4, d5, d6, d7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        showMenu();
        initCB();
    }

    public void initCB() {
        chosenList = new ArrayList<>();
        check1 = (CheckBox) findViewById(R.id.check1);
        check2 = (CheckBox) findViewById(R.id.check2);
        check3 = (CheckBox) findViewById(R.id.check3);
        check4 = (CheckBox) findViewById(R.id.check4);
        check5 = (CheckBox) findViewById(R.id.check5);
        check6 = (CheckBox) findViewById(R.id.check6);
        check7 = (CheckBox) findViewById(R.id.check7);

        check1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    chosenList.add(d1);
                } else {
                    chosenList.remove(d1);
                }

            }
        });

        check2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    chosenList.add(d2);
                } else {
                    chosenList.remove(d2);
                }

            }
        });

        check3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    chosenList.add(d3);
                } else {
                    chosenList.remove(d3);
                }

            }
        });

        check4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    chosenList.add(d4);
                } else {
                    chosenList.remove(d4);
                }
            }
        });

        check5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    chosenList.add(d5);
                } else {
                    chosenList.remove(d5);
                }
            }
        });

        check6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    chosenList.add(d6);
                } else {
                    chosenList.remove(d6);
                }

            }
        });

        check7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    chosenList.add(d7);
                } else {
                    chosenList.remove(d7);
                }
            }
        });

    }

    public void showMenu() {
        cd = new CuisineDAOImpl(this);
        // getAllCuisineByCategoryID
        apzList = cd.getAllCuisineByCategoryID(APPETIZER);
        entList = cd.getAllCuisineByCategoryID(ENTREES);
        desList = cd.getAllCuisineByCategoryID(DESSERT);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);
        img6 = (ImageView) findViewById(R.id.img6);
        img7 = (ImageView) findViewById(R.id.img7);

        desText1 = (TextView) findViewById(R.id.desText1);
        desText2 = (TextView) findViewById(R.id.desText2);
        desText3 = (TextView) findViewById(R.id.desText3);
        desText4 = (TextView) findViewById(R.id.desText4);
        desText5 = (TextView) findViewById(R.id.desText5);
        desText6 = (TextView) findViewById(R.id.desText6);
        desText7 = (TextView) findViewById(R.id.desText7);

        priceTitle1 = (TextView) findViewById(R.id.priceTitle1);
        priceTitle2 = (TextView) findViewById(R.id.priceTitle2);
        priceTitle3 = (TextView) findViewById(R.id.priceTitle3);
        priceTitle4 = (TextView) findViewById(R.id.priceTitle4);
        priceTitle5 = (TextView) findViewById(R.id.priceTitle5);
        priceTitle6 = (TextView) findViewById(R.id.priceTitle6);
        priceTitle7 = (TextView) findViewById(R.id.priceTitle7);


        img1.setImageBitmap(apzList.get(0).getImage());
        desText1.setText(apzList.get(0).getCuisineName() + "\n" + apzList.get(0).getCuisineDescription());
        priceTitle1.setText("$ " + apzList.get(0).getPrice());
        img2.setImageBitmap(apzList.get(1).getImage());
        desText2.setText(apzList.get(1).getCuisineName() + "\n" + apzList.get(1).getCuisineDescription());
        priceTitle2.setText("$ " + apzList.get(1).getPrice());
        img3.setImageBitmap(apzList.get(2).getImage());
        desText3.setText(apzList.get(2).getCuisineName() + "\n" + apzList.get(2).getCuisineDescription());
        priceTitle3.setText("$ " + apzList.get(2).getPrice());

        img4.setImageBitmap(entList.get(0).getImage());
        desText4.setText(entList.get(0).getCuisineName() + "\n" + entList.get(0).getCuisineDescription());
        priceTitle4.setText("$ " + entList.get(0).getPrice());
        img5.setImageBitmap(entList.get(1).getImage());
        desText5.setText(entList.get(1).getCuisineName() + "\n" + entList.get(1).getCuisineDescription());
        priceTitle5.setText("$ " + entList.get(1).getPrice());
        img6.setImageBitmap(entList.get(2).getImage());
        desText6.setText(entList.get(2).getCuisineName() + "\n" + entList.get(2).getCuisineDescription());
        priceTitle6.setText("$ " + entList.get(2).getPrice());

        img7.setImageBitmap(desList.get(0).getImage());
        desText7.setText(desList.get(0).getCuisineName() + "\n" + desList.get(0).getCuisineDescription());
        priceTitle7.setText("$ " + desList.get(0).getPrice());

        d1 = apzList.get(0).getCuisineName();
        d2 = apzList.get(1).getCuisineName();
        d3 = apzList.get(2).getCuisineName();
        d4 = entList.get(0).getCuisineName();
        d5 = entList.get(1).getCuisineName();
        d6 = entList.get(2).getCuisineName();
        d7 = desList.get(0).getCuisineName();
    }

    public void addToOrder(View view) {
        Intent i = new Intent(this, OrderActivity.class);
        i.putStringArrayListExtra("chosenList", chosenList);
        startActivity(i);
    }

    public void goToDetail(View view) {
        Intent i = new Intent(this, DetailActivity.class);
        String dishName = "";
        int id = view.getId();
        if (id == img1.getId()) {
            dishName = d1;
        } else if (id == img2.getId()) {
            dishName = d2;
        } else if (id == img3.getId()) {
            dishName = d3;
        } else if (id == img4.getId()) {
            dishName = d4;
        } else if (id == img5.getId()) {
            dishName = d5;
        } else if (id == img6.getId()) {
            dishName = d6;
        } else if (id == img7.getId()) {
            dishName = d7;
        }
        i.putExtra("dishName", dishName);
        i.putStringArrayListExtra("chosenList", chosenList);
        startActivity(i);
    }

}
