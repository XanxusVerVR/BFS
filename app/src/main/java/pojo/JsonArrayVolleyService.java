package pojo;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

/**
 * Created by surpr on 2017/12/27.
 */

public class JsonArrayVolleyService {

    private JsonArrayIVolleyResponse resultCallback = null;
    private Context mContext;

    public JsonArrayVolleyService(JsonArrayIVolleyResponse resultCallback, Context context) {
        this.resultCallback = resultCallback;
        mContext = context;
    }

    public void triggerStringRequest(final String json, String url) {//觸發Volley StringRequest物件
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonArrayRequest mJsonArrayRequest = new JsonArrayRequest(Request.Method.POST,
                url,
                json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (resultCallback != null)
                                resultCallback.notifySuccess(response);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (resultCallback != null)
                            resultCallback.notifyError(error);
                    }
                }
        );
        queue.add(mJsonArrayRequest);
    }

}
