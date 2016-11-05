package com.example.zy.echacks2016;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity{

    public ArrayList<String> cityList;
    public ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityList = new ArrayList<>();
        cityList.add("Boston");
        cityList.add("Calgary");
        cityList.add("Chicago");
        cityList.add("Edmonton");
        cityList.add("Hamilton");
        cityList.add("Peterborough");
        cityList.add("Houston");
        cityList.add("Kingston");
        cityList.add("Los Angeles");
        cityList.add("Montreal");
        cityList.add("Ottawa");
        cityList.add("San Francisco");
        cityList.add("Toronto");
        cityList.add("New York");
        cityList.add("Vancouver");
        cityList.add("Washington D.C.");
        cityList.add("Waterloo");
        mAdapter = new ArrayAdapter<String>(this, R.layout.city_listitem, cityList);
        ListView listView = (ListView) findViewById(R.id.city_list_view);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), OrderBusTicketActivity.class);
                intent.putExtra(Constants.CITY_NAME,cityList.get(i));
                intent.putExtra(Constants.CITY_INDEX, i);
                startActivity(intent);
            }
        });

    }

}
