package com.example.zy.echacks2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Tony on 2016-11-05.
 */

public class OrderBusTicketActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_bus);
        Intent intent = getIntent();

        TextView title = (TextView) findViewById(R.id.Title_Name);
        title.setText(intent.getStringExtra(Constants.CITY_NAME));
    }
}
