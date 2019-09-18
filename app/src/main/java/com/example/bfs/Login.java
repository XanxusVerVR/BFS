package com.example.bfs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import javabean.member.LoginBean;
import pojo.IVolleyResponse;
import pojo.ServiceURL;
import pojo.VolleyService;

import static com.android.volley.VolleyLog.TAG;
import static pojo.OrderSend.sendOrderToHost;

public class Login extends Activity implements View.OnClickListener {

    private Button LoginButton, SignupButton;
    private String owneracc = "owner", ownerpass = "owner";
    private String input1,input2;
    private String url;
    private int mode = 0;//node 1為顧客登入，mode 2為老闆登入
    private IVolleyResponse resultCallback = null;
    private VolleyService mVolleyService;
    EditText account,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        LoginButton = (Button) findViewById(R.id.login);
        SignupButton = (Button) findViewById(R.id.signup);
        account = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        initVolleyCallback();
        mVolleyService = new VolleyService(resultCallback, this);

        SignupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Intent signup = new Intent();
                signup.setClass(Login.this, SignUp.class);
                startActivity(signup);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(Login.this,"登入中，請稍後", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(1000);//在這裡我讓線程進行耗時操作
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                input1 = account.getText().toString();
                input2 = password.getText().toString();
                if (input1.equals(owneracc) && input2.equals(ownerpass)) {
                    Intent Mainowner = new Intent();
                    Mainowner.setClass(Login.this, MainOwner.class);
                    mode = 1;
                    Toast.makeText(Login.this,"success", Toast.LENGTH_LONG).show();
                    startActivity(Mainowner);
                }else{
                    LoginBean loginBean = new LoginBean();    //包成json傳送至伺服器
                    loginBean.setStatus("signIN"); //登入或註冊
                    loginBean.setAccount(input1); //使用者輸入帳號
                    loginBean.setPassword(input2); //使用者輸入密碼
                    Gson gson = new Gson();
                    final String json = gson.toJson(loginBean);
                    url = ServiceURL.MEMBER;  //登入伺服器網址
                    mVolleyService.triggerStringRequest(json, url);//觸發Volley請求
                }
            }
        });
    }

    private void initVolleyCallback() {
        resultCallback = new IVolleyResponse() {
            @Override
            public void notifySuccess(String response) {
                if(response.trim().equals("success")){
                    Intent Maincustomer = new Intent();
                    Maincustomer.putExtra("ID",account.getText().toString());
                    Maincustomer.setClass(Login.this, MainCustomer.class);
                    startActivity(Maincustomer);
                } else {
                    Toast.makeText(Login.this,"帳號或密碼錯誤", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Toast.makeText(Login.this, "失敗", Toast.LENGTH_LONG).show();
                System.out.println(error);
                Log.d(TAG, "Volley JSON post:" + error);
                Log.d(TAG, "That didn't work!");
            }
        };
    }


    @Override
    public void onClick(View View) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //return true;
        //}

        return super.onOptionsItemSelected(item);
    }


}
