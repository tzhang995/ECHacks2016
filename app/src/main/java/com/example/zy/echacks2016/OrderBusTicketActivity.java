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
import android.widget.TextView;

/**
 * Created by Tony on 2016-11-05.
 */

public class OrderBusTicketActivity extends Activity {
    public ReceiveConfirmBroadcastReceiver receiver;

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, new IntentFilter(Constants.DIALOGKEY));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_bus);
        Intent intent = getIntent();
        receiver = new ReceiveConfirmBroadcastReceiver();
        TextView title = (TextView) findViewById(R.id.Title_Name);
        title.setText(intent.getStringExtra(Constants.CITY_NAME));
        Button singlePass = (Button) findViewById(R.id.day_pass);
        Button groupPass = (Button) findViewById(R.id.group_pass);
        singlePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmDialogBox fragment = new ConfirmDialogBox();
                fragment.show(getFragmentManager(), "string");
            }
        });
        groupPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmDialogBox fragment = new ConfirmDialogBox();
                fragment.show(getFragmentManager(), "string");
            }
        });
    }


    public class ReceiveConfirmBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent newIntent = new Intent(context, AcceptedPaymentActivity.class);
            startActivity(newIntent);
        }
    }
}
