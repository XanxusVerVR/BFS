package pojo;

import com.android.volley.VolleyError;

public interface IVolleyResponse {//用來給VolleyService物件傳回Server端回應的Interface

    public void notifySuccess(String response);//成功的回應
    public void notifyError(VolleyError error);//失敗的回應
}
