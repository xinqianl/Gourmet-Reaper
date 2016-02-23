package com.edu.cmu.gourmetreaper.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.edu.cmu.gourmetreaper.dblayout.CuisineDAO;

import com.edu.cmu.gourmetreaper.R;
import com.edu.cmu.gourmetreaper.dblayout.daoimpl.CuisineDAOImpl;
import com.edu.cmu.gourmetreaper.entities.Cuisine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Doris on 8/3/15.
 */
class CustomAdapter extends ArrayAdapter<String> implements AdapterView.OnItemSelectedListener {
    private CuisineDAO cd;
    private LayoutInflater inflater;
    private float density = 2f;
    private ListView listView;

    public CustomAdapter(Context context, List<String> chosenList) {
        super(context, R.layout.custom_row, chosenList);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View workingView = myInflater.inflate(R.layout.custom_row, parent, false);
        cd = new CuisineDAOImpl(parent.getContext());
        String dishName = getItem(position);
        Cuisine c = cd.getCuisineByName(dishName);

        ImageView dishImg = (ImageView) workingView.findViewById(R.id.dishImg);
        TextView nameText = (TextView) workingView.findViewById(R.id.nameText);
        TextView descripText = (TextView) workingView.findViewById(R.id.descripText);
        TextView priceText = (TextView) workingView.findViewById(R.id.priceText);
        Spinner quanSpinner = (Spinner) workingView.findViewById(R.id.quanSpinner
        );

        dishImg.setImageBitmap(c.getImage());
        nameText.setText(c.getCuisineName());
        descripText.setText(c.getCuisineDescription());
        priceText.setText("$ " + c.getPrice());


        String[] quanArr = {"1", "2", "3", "4", "5", "6", "7", "8","9", "10"};
        ArrayList<String> quanList = new ArrayList<>(Arrays.asList(quanArr));
        ArrayAdapter<String> quanAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, quanList);
        quanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quanSpinner.setAdapter(quanAdapter);
        quanSpinner.setSelection(0);
        quanSpinner.setOnItemSelectedListener(this);


        // try delete view
        final CuisineHolder holder = getCuisineHolder(workingView);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mainView.getLayoutParams();
        params.rightMargin = 0;
        params.leftMargin = 0;
        holder.mainView.setLayoutParams(params);
        workingView.setOnTouchListener(new SwipeDetector(holder, position));

        return workingView;
    }

    private CuisineHolder getCuisineHolder(View workingView) {
        Object tag = workingView.getTag();
        CuisineHolder holder = null;

        if (tag == null || !(tag instanceof CuisineHolder)) {
            holder = new CuisineHolder();
            holder.mainView = (LinearLayout) workingView.findViewById(R.id.main_view);
            holder.deleteView = (RelativeLayout) workingView.findViewById(R.id.delete_view);

            /* initialize other views here */

            workingView.setTag(holder);
        } else {
            holder = (CuisineHolder) tag;
        }

        return holder;
    }

    public static class CuisineHolder {
        public LinearLayout mainView;
        public RelativeLayout deleteView;

        /* other views here */
    }

    class SwipeDetector implements View.OnTouchListener {

        private static final int MIN_DISTANCE = 300;
        private static final int MIN_LOCK_DISTANCE = 30; // disallow motion intercept
        private boolean motionInterceptDisallowed = false;
        private float downX, upX;
        private CuisineHolder holder;
        private int position;

        public SwipeDetector(CuisineHolder h, int pos) {
            holder = h;
            position = pos;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    return true; // allow other events like Click to be processed
                }

                case MotionEvent.ACTION_MOVE: {
                    upX = event.getX();
                    float deltaX = downX - upX;

                    if (Math.abs(deltaX) > MIN_LOCK_DISTANCE && listView != null && !motionInterceptDisallowed) {
                        listView.requestDisallowInterceptTouchEvent(true);
                        motionInterceptDisallowed = true;
                    }

                    if (deltaX > 0) {
                        holder.deleteView.setVisibility(View.GONE);

                    } else {
                        // if first swiped left and then swiped right
                        holder.deleteView.setVisibility(View.VISIBLE);
                    }

                    swipe(-(int) deltaX);
                    return true;
                }

                case MotionEvent.ACTION_UP:
                    upX = event.getX();
                    float deltaX = upX - downX;
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // left or right
                        swipeRemove();
                    } else {
                        swipe(0);
                    }

                    if (listView != null) {
                        listView.requestDisallowInterceptTouchEvent(false);
                        motionInterceptDisallowed = false;
                    }

                    holder.deleteView.setVisibility(View.VISIBLE);
                    return true;

                case MotionEvent.ACTION_CANCEL:
                    holder.deleteView.setVisibility(View.VISIBLE);
                    return false;
            }

            return true;
        }

        private void swipe(int distance) {
            View animationView = holder.mainView;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) animationView.getLayoutParams();
            params.rightMargin = -distance;
            params.leftMargin = distance;
            animationView.setLayoutParams(params);
        }

        private void swipeRemove() {
            remove(getItem(position));
            notifyDataSetChanged();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
