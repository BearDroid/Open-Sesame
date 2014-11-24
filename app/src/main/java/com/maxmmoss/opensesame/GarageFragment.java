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

import java.io.IOException;

/**
 * Created by Max on 11/12/2014.
 */
public class GarageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout mSwipeRefresh;
    private boolean garageOpen = false;
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
        Toast.makeText(getActivity(), "Refreshing...", Toast.LENGTH_LONG).show();
        checkStatus(getView());
        mSwipeRefresh.setRefreshing(false);
    }
    public void closeGarage(){
        if(!garageOpen){
            Toast.makeText(getActivity(), "Garage is closed already", Toast.LENGTH_SHORT).show();
        } else if(garageOpen){
            garageOpen = false;
            Toast.makeText(getActivity(), "Garage is closing now...", Toast.LENGTH_SHORT).show();
        }
        checkStatus(getView());
    }
    public void openGarage(){
        if(garageOpen){
            Toast.makeText(getActivity(), "Garage is open already", Toast.LENGTH_SHORT).show();
        } else if (!garageOpen){
            garageOpen = true;
            Toast.makeText(getActivity(), "Garage is opening now...", Toast.LENGTH_SHORT).show();
        }
        checkStatus(getView());
    }
    public void checkStatus(View view){
        readWebsite task = new readWebsite();
        task.execute(new String ("http://www.maxmmoss.com/Opensesame"));
        upHighlight = (ImageView) view.findViewById(R.id.upHighlight);
        downHighlight = (ImageView) view.findViewById(R.id.downHighlight);
        if(garageOpen){
            upHighlight.setVisibility(View.VISIBLE);
            downHighlight.setVisibility(View.INVISIBLE);
        }
        if (!garageOpen){
            downHighlight.setVisibility(View.VISIBLE);
            upHighlight.setVisibility(View.INVISIBLE);
        }
    }

    private class readWebsite extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
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

        }
    }
    /**
    private Class readWebsite extends AsyncTask<String, Void, Void>{
        protected String doInBackground
        /** manually done
        URL url = new URL("http://maxmmoss.com/Opensesame");
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            for (String line; (line = reader.readLine()) != null;) {
                builder.append(line.trim());
            }
        } finally {
            if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
        }
        String start = "<p id=\"status\">";
        String end = "</p>";
        String part = builder.substring(builder.indexOf(start) + start.length());
        String status = part.substring(0, part.indexOf(end));

        Document document = Jsoup.connect("http://maxmmoss.com/Opensesame/").get();
        String status = document.select("status").first().text();

        Boolean garageIsOpen = null;
        if (status == "Open"){
            garageIsOpen = true;
        } else if(status == "Closed"){
            garageIsOpen = false;
        }
        return garageIsOpen;

    }**/
}
