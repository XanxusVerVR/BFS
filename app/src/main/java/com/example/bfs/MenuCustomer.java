package com.example.bfs;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;

public class MenuCustomer extends AppCompatActivity {
    TextView name,price,description;
    String gname,gprice,gdescription,gnumber;
    Button delete;
    private static RequestQueue mQueue;//json包裝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menucustomer);
        name = (TextView)findViewById(R.id.searchname2);
        price = (TextView)findViewById(R.id.priceview2);
        description = (TextView)findViewById(R.id.descriptionview2);
        gname = getIntent().getStringExtra("name");
        name.setText(gname);
        gprice = getIntent().getStringExtra("price");
        price.setText(gprice);
        gdescription = getIntent().getStringExtra("description");
        description.setText(gdescription);

    }

}
