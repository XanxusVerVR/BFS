package javabean.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bfs.MainCustomer;
import com.example.bfs.MenuChange;
import com.example.bfs.MenuCustomer;
import com.example.bfs.MenuSearch;
import com.example.bfs.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 卓俊傑 on 2018/1/6.
 */

public class HostMeal extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> mAppList;
    private LayoutInflater mInflater;
    private Context mContext;
    private String[] keyString;
    private int[] valueViewID;
    private ItemView itemView;

    private class ItemView {
        TextView ItemName;
        TextView price,description,category;
    }

    public HostMeal(Context c, ArrayList<HashMap<String, Object>> appList, int resource, String[] from, int[] to) {
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
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            itemView = (ItemView) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.hostmenuadapter,null);
            itemView = new HostMeal.ItemView();
            itemView.ItemName = (TextView)convertView.findViewById(valueViewID[0]);
            itemView.price= (TextView) convertView.findViewById(valueViewID[1]);
            convertView.setTag(itemView);
        }

        HashMap<String, Object> appInfo = mAppList.get(position);
        if (appInfo != null) {
            String name = (String) appInfo.get(keyString[0]);
            String price = (String) appInfo.get(keyString[4]);
            itemView.ItemName.setText(name);
            if(name.trim().equals("新增餐點")){
                itemView.price.setText("請從此處新增餐點");
            }
            else{
                itemView.price.setText(price+"元");
            }
            itemView.ItemName.setOnClickListener(new ItemButton_Click(position));
            itemView.price.setOnClickListener(new ItemButton_Click(position));
        }

        itemView.ItemName.setBackgroundColor(Color.WHITE);
        itemView.price.setBackgroundColor(Color.WHITE);
        itemView.ItemName.setTextColor(Color.BLACK);
        itemView.price.setTextColor(Color.BLACK);
        if(position == 0){
            itemView.ItemName.setBackgroundColor(Color.RED);
            itemView.price.setBackgroundColor(Color.RED);
            itemView.ItemName.setTextColor(Color.WHITE);
            itemView.price.setTextColor(Color.WHITE);
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
                case R.id.hostmeal:
                    Intent menuSearch = new Intent(mContext,MenuSearch.class);
                    Intent menuChange = new Intent(mContext,MenuChange.class);
                    HashMap<String, Object> intent = mAppList.get(position);
                    String name = (String) intent.get(keyString[0]);
                    String number = (String) intent.get(keyString[2]);
                    String price = (String) intent.get(keyString[4]);
                    String description = (String) intent.get(keyString[5]);
                    if(name.trim().equals("新增餐點")){
                        mContext.startActivity(menuChange);
                    }
                    else{
                        menuSearch.putExtra("name",name);
                        menuSearch.putExtra("number",number);
                        menuSearch.putExtra("price",price);
                        menuSearch.putExtra("description",description);
                        mContext.startActivity(menuSearch);
                    }
                    break;
                case R.id.hostprice:
                    Intent menuSearch2 = new Intent(mContext,MenuSearch.class);
                    Intent menuChange2 = new Intent(mContext,MenuChange.class);
                    HashMap<String, Object> intent2 = mAppList.get(position);
                    String name2 = (String) intent2.get(keyString[0]);
                    String number2 = (String) intent2.get(keyString[2]);
                    String price2 = (String) intent2.get(keyString[4]);
                    String description2 = (String) intent2.get(keyString[5]);
                    if(name2.trim().equals("新增餐點")){
                        mContext.startActivity(menuChange2);
                    }
                    else {
                        menuSearch2.putExtra("name", name2);
                        menuSearch2.putExtra("number",number2);
                        menuSearch2.putExtra("price", price2);
                        menuSearch2.putExtra("description", description2);
                        mContext.startActivity(menuSearch2);
                    }
                    break;
            }
        }
    }
}
