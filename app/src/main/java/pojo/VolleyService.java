package pojo;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class VolleyService {

    private IVolleyResponse resultCallback = null;
    private Context mContext;

    public VolleyService(IVolleyResponse resultCallback, Context context) {
        this.resultCallback = resultCallback;
        mContext = context;
    }

    public void triggerStringRequest(final String json, String url) {//觸發Volley StringRequest物件
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {   //volley傳送出json 回傳也必須json 所以要覆寫裡面方法才能輸出json 傳入string
            @Override
            public void onResponse(String response) { //當請求成功，伺服器回傳的訊息
                if (resultCallback != null)
                    resultCallback.notifySuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {//當請求失敗，伺服器回傳的訊息
                if (resultCallback != null)
                    resultCallback.notifyError(error);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return json.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(request);
    }

}
