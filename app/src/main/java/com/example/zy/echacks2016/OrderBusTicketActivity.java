package com.example.zy.echacks2016;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Tony on 2016-11-05.
 */

public class OrderBusTicketActivity extends Activity {
    public String place;
    public int index;
    public int[] mTransitLogo = {R.drawable.calgary,
                                R.drawable.calgary,
                                R.drawable.chicago,
                                R.drawable.calgary,
                                R.drawable.calgary,
                                R.drawable.peterborough,
                                R.drawable.calgary,
                                R.drawable.calgary,
                                R.drawable.calgary,
                                R.drawable.calgary,
                                R.drawable.calgary,
                                R.drawable.sf,
                                R.drawable.ttc,
                                R.drawable.nyc,
                                R.drawable.vancouver,
                                R.drawable.nyc,
                                R.drawable.grt,
                                R.drawable.grt};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_bus);
        Intent intent = getIntent();
        TextView title = (TextView) findViewById(R.id.Title_Name);
        place = intent.getStringExtra(Constants.CITY_NAME);
        title.setText(place);
        ImageView image = (ImageView) findViewById(R.id.transit_logo);
        index = intent.getIntExtra(Constants.CITY_INDEX,-1);
        image.setImageResource(mTransitLogo[index]);
        Button singlePass = (Button) findViewById(R.id.day_pass);
        Button groupPass = (Button) findViewById(R.id.group_pass);
        singlePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(view.getContext(), AcceptedPaymentActivity.class);
                newIntent.putExtra(Constants.CITY_NAME,place);
                newIntent.putExtra(Constants.CITY_INDEX, index);
                startActivity(newIntent);
            }
        });
        groupPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(view.getContext(), AcceptedPaymentActivity.class);
                newIntent.putExtra(Constants.CITY_NAME,place);
                newIntent.putExtra(Constants.CITY_INDEX, index);
                startActivity(newIntent);
            }
        });
    }
}
