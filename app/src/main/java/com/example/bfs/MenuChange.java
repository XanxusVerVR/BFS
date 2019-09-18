package com.example.bfs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import pojo.ServiceURL;

public class MenuChange extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    Spinner mealcategory;
    String[] mealcategorylist = {"請選擇種類", "漢堡", "吐司", "蛋餅", "飲料","其他","套餐"};//Spinnert清單
    String mealtype;//Spinner清單類型代號
    int queryNumber = 2;//請求類型-新增
    EditText mealname, mealprice, mealdescription, mealimage;
    private static RequestQueue mQueue;//json包裝



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_menuchange);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mealcategory = (Spinner) findViewById(R.id.mealcategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mealcategorylist);
        mealcategory.setAdapter(adapter);
        mealcategory.setOnItemSelectedListener(this);
        mealname = (EditText) findViewById(R.id.mealname);
        mealprice = (EditText) findViewById(R.id.mealprice);
        mealdescription = (EditText) findViewById(R.id.mealdescription);
        mealimage = (EditText) findViewById(R.id.mealimage);
        Button insert = (Button) findViewById(R.id.insertbotton);
        insert.setOnClickListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                mealtype = "未選擇";//要求使用者選擇
                break;
            case 1:
                mealtype = "漢堡";//漢堡類
                break;
            case 2:
                mealtype = "吐司";//吐司類
                break;
            case 3:
                mealtype = "蛋餅";//蛋餅類
                break;
            case 4:
                mealtype = "飲料";//飲料類
                break;
            case 5:
                mealtype = "其他";//其他類
                break;
            case 6:
                mealtype = "套餐";//套餐
                break;
        }
    }




    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onClick(View View) {
        mQueue = Volley.newRequestQueue(this);
        if (View.getId() == R.id.insertbotton) {
            //空值判斷
            if(mealname.getText().toString().trim().equals("")){
                Toast.makeText(MenuChange.this,"餐點名稱不可為空", Toast.LENGTH_LONG).show();
            }
            else if(mealprice.getText().toString().trim().equals("")){
                Toast.makeText(MenuChange.this,"價格不可為空", Toast.LENGTH_LONG).show();
            }
            else if(mealdescription.getText().toString().trim().equals("")){
                Toast.makeText(MenuChange.this,"敘述不可為空", Toast.LENGTH_LONG).show();
            }
            else if(mealtype.trim().equals("未選擇")){
                Toast.makeText(MenuChange.this,"請選擇種類", Toast.LENGTH_LONG).show();
            }
            else{
                //宣告json物件，格式為object包住object
                JSONObject json_obj = new JSONObject();
                JSONObject json_obj_inside = new JSONObject();
                //json object內部
                try {
                    json_obj_inside.put("mealName",mealname.getText().toString());
                    json_obj_inside.put("mealPrice",Integer.parseInt(mealprice.getText().toString()));
                    json_obj_inside.put("mealDescription",mealdescription.getText().toString());
                    json_obj_inside.put("mealCategory", mealtype);
                    json_obj_inside.put("mealImage", mealimage.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //json object外部(物件包物件)
                try {
                    json_obj.put("queryNumber", Integer.parseInt("2"));
                    json_obj.put("queryAction", "insert");
                    json_obj.put("mealData", json_obj_inside);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        ServiceURL.MENU,
                        json_obj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("cylog", response.toString());
                                Toast.makeText(MenuChange.this,"新增成功",Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("err", error.toString());
                            }
                        }
                );
                mQueue.add(mJsonObjectRequest);
                //成功新增之後刷新回上一頁
                if(!MainOwner.hostactivity.isFinishing()){
                    MainOwner.hostactivity.finish();
                }
                Intent intent = new Intent(MenuChange.this, MainOwner.class);
                startActivity(intent);
                finish();
            }
        }
    }

}
