package com.edu.cmu.gourmetreaper.ui;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.cmu.gourmetreaper.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class OrderActivity extends Activity {
    private TextView orderTitle, totalPriceTitle, noOrderTitle;
    private static ArrayList<String> chosenList = new ArrayList<>();
    ;
    private ListAdapter myAdapter;
    private ListAdapter listAdapter;
    private ListView myListView;
    private static SharedPreferences mSettings;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mSettings = getApplicationContext().getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
        if (mSettings.getStringSet("orderSet", null) != null) {
            chosenList = new ArrayList<>(mSettings.getStringSet("orderSet", null));
        }
        showDate();
        showChosenList();
    }

    public void showChosenList() {
        if ((chosenList == null || chosenList.size() == 0) && getIntent().getStringArrayListExtra("chosenList") == null) {
            noOrderTitle = (TextView) findViewById(R.id.noOrderTitle);
            noOrderTitle.setText("You have no order yet today!");
            return;
        } else if (getIntent().getStringArrayListExtra("chosenList") != null) {
            chosenList = getIntent().getStringArrayListExtra("chosenList");
            editor.remove("orderSet");
            editor.apply();
            editor.putStringSet("orderSet", new HashSet<>(chosenList));
            editor.apply();
        }
        myListView = (ListView) findViewById(R.id.myListView);

        myAdapter = new CustomAdapter(this, chosenList);
        myListView.setAdapter(myAdapter);

    }

    public void showDate() {
        orderTitle = (TextView) findViewById(R.id.orderTitle);
        Date curDate = new Date( );
        SimpleDateFormat ft =
                new SimpleDateFormat ("MM/dd/yyyy");
        orderTitle.setText("Your order on " + ft.format(curDate));

    }

    private double sumUp() {
        double total = 0;
        for (int i = 0; i < myListView.getAdapter().getCount(); i++) {
            View eachView = myListView.getAdapter().getView(i, null, myListView);
            TextView priceText = (TextView) eachView.findViewById(R.id.priceText);
            Spinner quanSpinner = (Spinner) eachView.findViewById(R.id.quanSpinner);
            total += Double.parseDouble(priceText.getText().toString().substring(2)) * Integer.parseInt(quanSpinner.getSelectedItem().toString());

        }
        return total;

    }

    public void checkout(View view) {
        totalPriceTitle = (TextView) findViewById(R.id.totalPriceTitle);
        totalPriceTitle.setText("Total Price: $ " + String.format("%1$,.2f", sumUp()));

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://10.0.0.22:8080/AndroidWeb/browse.do?price=" + String.format("%1$,.2f", sumUp())));
            startActivity(browserIntent);
        } else {
            totalPriceTitle.setText("No network connection available.");
        }
    }


}
