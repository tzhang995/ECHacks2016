package com.example.zy.echacks2016;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity{

    public ArrayList<String> cityList;
    public ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityList = new ArrayList<>();
        cityList.add("PeterBourogoh");
        cityList.add("Toronto");
        cityList.add("Waterpo");
        mAdapter = new ArrayAdapter<String>(this,R.layout.city_listitem,cityList);
        ListView listView = (ListView) findViewById(R.id.city_list_view);
        listView.setAdapter(mAdapter);
    }

}
