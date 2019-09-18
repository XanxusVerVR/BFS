package com.example.bfs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import javabean.menu.Menu;
import pojo.ServiceURL;

public class MenuSearch extends AppCompatActivity implements View.OnClickListener {

    TextView name,price,description;
    String gname,gprice,gdescription,gnumber;
    Button delete;
    private static RequestQueue mQueue;//json包裝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menusearch);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        name = (TextView)findViewById(R.id.searchname);
        price = (TextView)findViewById(R.id.priceview);
        description = (TextView)findViewById(R.id.descriptionview);
        gname = getIntent().getStringExtra("name");
        name.setText(gname);
        gprice = getIntent().getStringExtra("price");
        price.setText(gprice);
        gdescription = getIntent().getStringExtra("description");
        description.setText(gdescription);

        gnumber = getIntent().getStringExtra("number");
        delete = (Button)findViewById(R.id.deletebutton);
        delete.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.deletebutton) {
            mQueue = Volley.newRequestQueue(this);

            JSONObject deleterequest = new JSONObject();
            JSONObject deleterequest_inside = new JSONObject();

            try {
                deleterequest_inside.put("mealNumber",Integer.parseInt(gnumber));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                deleterequest.put("queryNumber", Integer.parseInt("3"));
                deleterequest.put("queryAction", "delete");
                deleterequest.put("mealData",deleterequest_inside);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    ServiceURL.MENU,
                    deleterequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(MenuSearch.this,"刪除成功",Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("err", error.toString());
                            Toast.makeText(MenuSearch.this,"刪除失敗",Toast.LENGTH_LONG).show();
                        }
                    }
            );
            mQueue.add(mJsonObjectRequest);
        }
        if(!MainOwner.hostactivity.isFinishing()){
            MainOwner.hostactivity.finish();
        }
        Intent intent = new Intent(MenuSearch.this, MainOwner.class);
        startActivity(intent);
        finish();
    }

}
