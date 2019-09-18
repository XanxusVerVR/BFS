package com.example.bfs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import javabean.member.SignUpBean;
import pojo.IVolleyResponse;
import pojo.ServiceURL;
import pojo.VolleyService;

import static com.android.volley.VolleyLog.TAG;

public class SignUp extends AppCompatActivity {
    private String url;
    private IVolleyResponse resultCallback = null;
    private VolleyService mVolleyService;
    private EditText email,account,password,passwordCheck,age;
    private String inputemail,inputaccount,inputpassword,inputpasswordCheck,inputage,inputgender = "";
    int intage;
    private RadioButton male,female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email = (EditText)findViewById(R.id.email2);
        account = (EditText)findViewById(R.id.account);
        password = (EditText)findViewById(R.id.password2);
        passwordCheck = (EditText)findViewById(R.id.passwordcheck);
        age = (EditText)findViewById(R.id.old);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        inputpasswordCheck = passwordCheck.getText().toString();
        inputage = age.getText().toString();


        initVolleyCallback();
        mVolleyService = new VolleyService(resultCallback, this);
        Button sendInfo = (Button) findViewById(R.id.signup2);
        sendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                url = ServiceURL.MEMBER;  //登入伺服器網址 json介面格式那邊有寫
                if("".equals(email.getText().toString().trim())){
                    Toast.makeText(SignUp.this,"信箱不可為空", Toast.LENGTH_LONG).show();
                }
                else if("".equals(account.getText().toString().trim())){
                    Toast.makeText(SignUp.this,"帳號不可為空", Toast.LENGTH_LONG).show();
                }
                else if("".equals(password.getText().toString().trim())){
                    Toast.makeText(SignUp.this,"密碼不可為空", Toast.LENGTH_LONG).show();
                }
                else if("".equals(age.getText().toString().trim())){
                    Toast.makeText(SignUp.this,"年齡不可為空", Toast.LENGTH_LONG).show();
                }
                else if("".trim().equals(inputgender.trim())){
                    Toast.makeText(SignUp.this,"請勾選性別", Toast.LENGTH_LONG).show();
                }
                else {
                    SignUpBean signUpBean = new SignUpBean();    //包成json傳送至伺服器
                    signUpBean.setStatus("signUP"); //登入或註冊  這一格不用改
                    signUpBean.setAccount(account.getText().toString()); //使用者註冊帳號  (字串) 抓取activity的每一格使用者填的資料 就ok了  記得要判斷使用者填資訊不能為空喔
                    signUpBean.setPassword(password.getText().toString()); //使用者註冊密碼 (字串)
                    signUpBean.setGender(inputgender); //使用者性別 (字串) (man or woman)
                    signUpBean.setAge(Integer.parseInt(age.getText().toString())); //使用者年齡  (整數)
                    signUpBean.setEmail(email.getText().toString()); //使用者信箱 (字串)
                    Gson gson = new Gson();
                    final String json = gson.toJson(signUpBean);
                    mVolleyService.triggerStringRequest(json, url);//觸發Volley請求
                    Log.d("test","性別"+inputgender);
                }

            }

        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.male:
                if (checked)
                    inputgender = "男性";
                break;
            case R.id.female:
                if (checked)
                    inputgender = "女性";
                break;
        }
    }

    private void initVolleyCallback() {
        resultCallback = new IVolleyResponse() {
            @Override
            public void notifySuccess(String response) {
                Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
                Log.d("test",response);
                Log.d(TAG, "Volley JSON post:" + response);
            }

            @Override
            public void notifyError(VolleyError error) {
                Toast.makeText(SignUp.this, "失敗", Toast.LENGTH_LONG).show();
                System.out.println(error);
                Log.d(TAG, "Volley JSON post:" + error);
                Log.d(TAG, "That didn't work!");
            }
        };
    }

}
