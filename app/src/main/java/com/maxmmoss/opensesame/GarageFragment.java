package com.maxmmoss.opensesame;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by Max on 11/12/2014.
 */
public class GarageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefresh;
    private boolean garageOpen;
    private boolean garageTransition;
    private Button garageUp;
    private Button garageDown;
    private ImageView upHighlight;
    private ImageView downHighlight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        checkStatus(view); //when the view is created, it will check boolean garageOpen to highlight appropriate button

        //swipe to refresh
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiper);
        mSwipeRefresh.setOnRefreshListener(this);
        //end swipe to refresh

        garageUp = (Button) view.findViewById(R.id.up);
        garageDown = (Button) view.findViewById(R.id.down);

        garageUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGarage();
            }
        });
        garageDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeGarage();
            }
        });

        return view;
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(), "Refreshing...", Toast.LENGTH_SHORT).show();
        checkStatus(getView());
        mSwipeRefresh.setRefreshing(false);
    }

    public void closeGarage() {
        if (!garageOpen) {
            Toast.makeText(getActivity(), "Garage is closed already", Toast.LENGTH_SHORT).show();
        } else if (garageOpen) {
            updateWebpage task = new updateWebpage();
            task.execute(false);
            Toast.makeText(getActivity(), "Garage is closing now...", Toast.LENGTH_SHORT).show();
        }
        checkStatus(getView());
    }

    public void openGarage() {
        if (garageOpen) {
            Toast.makeText(getActivity(), "Garage is open already", Toast.LENGTH_SHORT).show();
        } else if (!garageOpen) {
            updateWebpage task = new updateWebpage();
            task.execute(true);
            Toast.makeText(getActivity(), "Garage is opening now...", Toast.LENGTH_SHORT).show();
        }
        checkStatus(getView());
    }

    public void checkStatus(View view) {
        readWebsite task = new readWebsite();
        task.execute();
    }

    public void updateUI(View view) {
        upHighlight = (ImageView) view.findViewById(R.id.upHighlight);
        downHighlight = (ImageView) view.findViewById(R.id.downHighlight);
        if (garageOpen) {
            upHighlight.setVisibility(View.VISIBLE);
            downHighlight.setVisibility(View.INVISIBLE);
        }
        if (!garageOpen) {
            downHighlight.setVisibility(View.VISIBLE);
            upHighlight.setVisibility(View.INVISIBLE);
        }
    }

    private class readWebsite extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            Document document = null;
            try {
                document = Jsoup.connect("http://maxmmoss.com/Opensesame/").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String status = document.select("#status").first().text();
            return status;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Open")) {
                garageOpen = true;
            } else if (result.equals("Closed")) {
                garageOpen = false;
            }
            updateUI(getView());

        }
    }

    public class updateWebpage extends AsyncTask<Boolean, Void, Void> {
        //none of this works
        @Override
        protected Void doInBackground(Boolean... params) {
            Document document = null;
            try {
                document = Jsoup.connect("http://maxmmoss.com/Opensesame/").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Element p = document.select("#status").first();
            if (params[0]) {
                p.text("Open");
            } else if (!params[0]) {
                p.text("Closed");
            }
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkStatus(getView());
    }
}
