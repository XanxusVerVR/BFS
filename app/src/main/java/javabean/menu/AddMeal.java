package javabean.menu;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.browse.MediaBrowser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.bfs.MainCustomer;
import com.example.bfs.MenuCustomer;
import com.example.bfs.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javabean.order.Meal;
import javabean.order.OrderBean;
import pojo.OrderSend;

import static pojo.OrderSend.sendOrderToHost;

/**
 * Created by 卓俊傑 on 2017/12/22.
 */

public class AddMeal extends BaseAdapter{
    private ArrayList<HashMap<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private ItemView itemView;

    private class ItemView {
        TextView ItemName;
        TextView count;
        Button plus,minus;
        TextView price,description,category;
    }

    public AddMeal(Context c, ArrayList<HashMap<String, Object>> appList, int resource, String[] from, int[] to) {
        mAppList = appList;
        mContext = c;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyString = new String[from.length];
        valueViewID = new int[to.length];
        mInflater = LayoutInflater.from(c);
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, valueViewID, 0, to.length);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return 0;
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return null;
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        //return 0;
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //return null;

        if (convertView != null) {
            itemView = (ItemView) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.additemadapter,null);
            itemView = new ItemView();
            itemView.ItemName = (TextView)convertView.findViewById(valueViewID[0]);
            itemView.minus = (Button)convertView.findViewById(valueViewID[1]);
            itemView.count = (TextView)convertView.findViewById(valueViewID[2]);
            itemView.plus = (Button)convertView.findViewById(valueViewID[3]);
            itemView.price = (TextView)convertView.findViewById(valueViewID[4]);
            convertView.setTag(itemView);
        }

        HashMap<String, Object> appInfo = mAppList.get(position);
        if (appInfo != null) {
            String name = (String) appInfo.get(keyString[0]);
            String count = (String) appInfo.get(keyString[2]);
            String price = (String) appInfo.get(keyString[4]);
            itemView.ItemName.setText(name);
            itemView.count.setText(count);
            itemView.price.setText(price+"元");
            itemView.minus.setOnClickListener(new ItemButton_Click(position));
            itemView.plus.setOnClickListener(new ItemButton_Click(position));
            itemView.ItemName.setOnClickListener(new ItemButton_Click(position));
            itemView.count.setOnClickListener(new ItemButton_Click(position));
            itemView.price.setOnClickListener(new ItemButton_Click(position));
        }

        return convertView;
    }

    class ItemButton_Click implements View.OnClickListener {
        private int position;

        ItemButton_Click(int pos) {
            position = pos;
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){

                case R.id.minus:
                    HashMap<String, Object> counterminus = mAppList.get(position);//取出listview中的hashmap
                    int counterm = Integer.parseInt(counterminus.get("count").toString());//計數器
                    if(counterm-1<0){
                        Toast.makeText(v.getContext(),"數量不可小於0", Toast.LENGTH_LONG).show();
                    }
                    else{
                        counterm = counterm-1;//減少動作
                        counterminus.put("count",String.valueOf(counterm));//變更hashmap的值
                        itemView.count.setText(counterminus.get("count").toString());//變更hashmap的值
                        notifyDataSetChanged();//刷新資料(重要)
                    }
                    break;
                case R.id.plus:
                    HashMap<String, Object> counterplus = mAppList.get(position);//取出listview中的hashmap
                    int counterp = Integer.parseInt(counterplus.get("count").toString());//計數器
                    counterp = counterp+1;//增加動作
                    counterplus.put("count",String.valueOf(counterp));//變更hashmap的值
                    itemView.count.setText(counterplus.get("count").toString());//將值打在手機畫面
                    notifyDataSetChanged();//刷新資料(重要 )
                    Log.d("測試","增加"+String.valueOf(position)+"值"+counterplus.get("count"));
                    break;
                case R.id.itemname:
                    Intent menucustomer = new Intent(mContext,MenuCustomer.class);
                    HashMap<String, Object> intent = mAppList.get(position);
                    String name = (String) intent.get(keyString[0]);
                    String price = (String) intent.get(keyString[4]);
                    String description = (String) intent.get(keyString[5]);
                    menucustomer.putExtra("name",name);
                    menucustomer.putExtra("price",price);
                    menucustomer.putExtra("description",description);
                    mContext.startActivity(menucustomer);
                    break;
                case R.id.itemprice:
                    Intent menucustomer2 = new Intent(mContext,MenuCustomer.class);
                    HashMap<String, Object> intent2 = mAppList.get(position);
                    String name2 = (String) intent2.get(keyString[0]);
                    String price2 = (String) intent2.get(keyString[4]);
                    String description2 = (String) intent2.get(keyString[5]);
                    menucustomer2.putExtra("name",name2);
                    menucustomer2.putExtra("price",price2);
                    menucustomer2.putExtra("description",description2);
                    mContext.startActivity(menucustomer2);
                    break;
            }
        }
    }

}