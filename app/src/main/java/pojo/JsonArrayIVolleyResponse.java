package pojo;

import com.android.volley.VolleyError;

import org.json.JSONArray;

/**
 * Created by surpr on 2017/12/27.
 */

public interface JsonArrayIVolleyResponse {
    public void notifySuccess(JSONArray response);//成功的回應
    public void notifyError(VolleyError error);//失敗的回應
}
