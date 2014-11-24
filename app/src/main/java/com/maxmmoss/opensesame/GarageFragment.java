package com.maxmmoss.opensesame;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Max on 11/12/2014.
 */
public class GarageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout mSwipeRefresh;
    private boolean garageOpen;
    private boolean garageTransition;
    private Button garageUp;
    private Button garageDown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
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
        Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_LONG).show();
        mSwipeRefresh.setRefreshing(false);
    }
    public void closeGarage(){
        if(!garageOpen){
            Toast.makeText(getActivity(), "Garage is closed already", Toast.LENGTH_SHORT).show();
        } else if(garageOpen){
            garageOpen = false;
            Toast.makeText(getActivity(), "Garage is closing now...", Toast.LENGTH_SHORT).show();
        }
    }
    public void openGarage(){
        if(garageOpen){
            Toast.makeText(getActivity(), "Garage is open already", Toast.LENGTH_SHORT).show();
        } else if (!garageOpen){
            garageOpen = true;
            Toast.makeText(getActivity(), "Garage is opening now...", Toast.LENGTH_SHORT).show();
        }
    }
}
