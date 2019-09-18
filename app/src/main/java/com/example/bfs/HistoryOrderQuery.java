package com.example.bfs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import pojo.JsonArrayIVolleyResponse;
import pojo.JsonArrayVolleyService;
import pojo.ServiceURL;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by surpr on 2017/12/27.
 */

public class HistoryOrderQuery extends AppCompatActivity {

    private JsonArrayIVolleyResponse resultCallback = null;
    private JsonArrayVolleyService mVolleyService;
    private TextView tvBeginTime;
    private TextView tvEndTime;
    private Button btnBeginTime;
    private Button btnEndTime;
    private Button btnSend;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog1;
    private TimePickerDialog timePickerDialog1;
    private final GregorianCalendar calendar = new GregorianCalendar();
    private DateSelection dateSelection = new DateSelection();
    private String url;
    private ExpandableListView expandableListView;
    private ScrollView scrollView;
    private ExpandableListAdapter listAdapter;
    private ArrayList<String> userInfo = null;
    private ArrayList<ArrayList<String>> userMeal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);

        tvBeginTime = (TextView)findViewById(R.id.tv_beginTime);
        tvEndTime = (TextView)findViewById(R.id.tv_endTime);
        btnBeginTime = (Button)findViewById(R.id.btn_beginTime);
        btnEndTime = (Button)findViewById(R.id.btn_endTime);
        btnSend = (Button)findViewById(R.id.btn_send);
        expandableListView = (ExpandableListView) findViewById(R.id.exListSights);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        initVolleyCallback();
        mVolleyService = new JsonArrayVolleyService(resultCallback, this);


        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                tvBeginTime.setText(dateSelection.displayDay(year,monthOfYear,dayOfMonth));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                tvBeginTime.setText(tvBeginTime.getText()+" "+dateSelection.displayTime(hourOfDay,minute));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

        datePickerDialog1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                tvEndTime.setText(dateSelection.displayDay(year,monthOfYear,dayOfMonth));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog1 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                tvEndTime.setText(tvEndTime.getText()+" "+dateSelection.displayTime(hourOfDay,minute));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);


        btnBeginTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
                datePickerDialog.show();
            }
        });

        btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog1.show();
                datePickerDialog1.show();
            }
        });



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = ServiceURL.TIME_QUERY;  //登入伺服器網址
                String beginTime = tvBeginTime.getText().toString();
                String endTime = tvEndTime.getText().toString();

                JSONObject object = new JSONObject();
                try {
                    if(beginTime != null && endTime != null){
                        object.put("beginTime", beginTime);
                        object.put("endTime",endTime);

                    }else{
                        Toast.makeText(HistoryOrderQuery.this, "請選擇開始與結束時間", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String strObject = object.toString();
                mVolleyService.triggerStringRequest(strObject, url);//觸發Volley請求
            }
        });

    }

    private void newExpandableListAdapter() {
        listAdapter = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return userInfo.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return userMeal.get(groupPosition).size();
            }

            @Override
            public Object getGroup(int groupPostion) {
                return userInfo.get(groupPostion);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return userMeal.get(groupPosition).get(childPosition);
            }

            @Override
            public long getGroupId(int groupPositon) {
                return groupPositon;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
                TextView textView = getTextView();
                textView.setText(getGroup(groupPosition).toString());
                return textView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup viewGroup) {
                TextView textView = getTextView();
                textView.setText(getChild(groupPosition, childPosition).toString());
                textView.setBackgroundColor(Color.parseColor("#FFCCCCCC"));
                return textView;
            }

            @Override
            public boolean isChildSelectable(int i, int i1) {
                return true;
            }

            private TextView getTextView() {
                TextView textView = new TextView(HistoryOrderQuery.this);
                textView.setPadding(150, 0, 0, 0);
                return textView;
            }
        };
    }


    private void initVolleyCallback() {
        resultCallback = new JsonArrayIVolleyResponse() {
            @Override
            public void notifySuccess(JSONArray response) {
                JSONArray array = null;
                userInfo = new ArrayList<String>();
                userMeal = new ArrayList<ArrayList<String>>();
                try {
                    array = new JSONArray(response.toString());
                    for (int i = 0; i < array.length(); i++) {

                        ArrayList detial = new ArrayList<String>();
                        JSONObject jsonObject = array.getJSONObject(i);
                        int serialNumber = jsonObject.getInt("serialNumber");
                        int totalPrice = jsonObject.getInt("totalPrice");
                        String id = jsonObject.getString("id");
                        String time =jsonObject.getString("time");
                        String mealName =jsonObject.getString("mealName");
                        String mealQuantity =jsonObject.getString("mealQuantity");
                        userInfo.add(String.valueOf(" 序號: "+serialNumber)+"\n"+" 顧客ID: "+id+"\n"+" 完成時間: "+time);

                        String[] ArrayMealName = stringToArray(mealName);
                        String[] ArrayMealQuantity = stringToArray(mealQuantity);
                        for(int j =0;j<ArrayMealName.length;j++)
                        {
                            detial.add(" 餐點: "+ArrayMealName[j].trim()+" "+" 數量: "+ArrayMealQuantity[j].trim());
                        }
                        detial.add(" 總共價錢: "+String.valueOf(totalPrice));
                        userMeal.add(detial);
                    }

                    newExpandableListAdapter();
                    expandableListView.setAdapter(listAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Toast.makeText(HistoryOrderQuery.this, "失敗", Toast.LENGTH_LONG).show();
                System.out.println(error);
                Log.d(TAG, "Volley JSON post:" + error);
                Log.d(TAG, "That didn't work!");
            }
        };
    }

    public static String[] stringToArray(String str)
    {
        //去頭尾[]
        String arrstring = str.replaceFirst("\\[", "").replaceFirst("\\]", "");
        //這裡split時可能會有空白產生，如果不想要可以利用for loop trim掉
        //或使用時加trim
        return arrstring.split(",");
    }

}

