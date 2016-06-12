package com.soffice.clickandpay.NetWork;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Utilty.Display;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by sys2025 on 12/9/15.
 */
public class JsonRequester {
    public static final String TAG = JsonRequester.class.getSimpleName();
    Context con;
    TaskListner listen = null;

    public JsonRequester(TaskListner con1) {
//        con=con1;
        listen = con1;
    }

    //    public void setTaskListner(TaskListner listner){
//
//    }
    public void JsonObjectRequester(String url, JSONObject jsonobject, int type, String _className, String _methodName) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(type,
                url, jsonobject, new OnResponseObjectlistner(_className,_methodName), new OnerrorListner(_className,_methodName));
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(6000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ClickandPay.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void JsonArrayRequester(String url, String _className, String _methodName) {
        JsonArrayRequest arrayrequest = new JsonArrayRequest(url, new OnResponseArraylistner(_className,_methodName), new OnerrorListner(_className,_methodName));
        arrayrequest.setRetryPolicy(new DefaultRetryPolicy(6000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ClickandPay.getInstance().addToRequestQueue(arrayrequest);

    }

    public void StringRequester(String url, int type, String _className, String _methodName) {
        StringRequest stringrequest = new StringRequest(type, url, new OnResponseStringlistner(_className,_methodName), new OnerrorListner(_className,_methodName));
        ClickandPay.getInstance().addToRequestQueue(stringrequest);
    }



    public void StringRequesterFormValues(String url, int type, String _className, String _methodName, final Map<String, String> param,String TAG) {

        StringRequest stringrequest = new StringRequest(Request.Method.POST,url, new OnResponseStringlistner(_className, _methodName), new OnerrorListner(_className, _methodName) ){

            @Override
            protected Map<String, String> getParams() {
                try
                {
                    return param;
                }catch (Exception e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        stringrequest.setRetryPolicy(new DefaultRetryPolicy(6000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ClickandPay.getInstance().addToRequestQueue(stringrequest,TAG);


//        StringRequest stringrequest = new StringRequest(type,
//                url,new OnResponseStringlistner(_className,_methodName), new OnerrorListner(_className,_methodName) {
//
//            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
//                Map<String, String> params = param;
//                return params;
//            };
//        });
    }


    class OnResponseObjectlistner implements Response.Listener<JSONObject> {
        String className;
        String methodName;
        public  OnResponseObjectlistner(String _className, String _methodName){
            className = _className;
            methodName = _methodName;
        }
        @Override
        public void onResponse(JSONObject jsonObject) {
            listen.onTaskfinished(jsonObject.toString(), 05, className, methodName);
        }
    }

    class OnerrorListner implements Response.ErrorListener {
        String className;
        String methodName;
        public  OnerrorListner(String _className, String _methodName){
            className = _className;
            methodName = _methodName;
        }
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            listen.onTaskfinished(volleyError.getMessage(), 00, className, methodName);
        }
    }

    public class OnResponseArraylistner implements Response.Listener<JSONArray> {
        String className;
        String methodName;
        public  OnResponseArraylistner(String _className, String _methodName){
            className = _className;
            methodName = _methodName;
        }
        @Override
        public void onResponse(JSONArray jsonArray) {
            listen.onTaskfinished(jsonArray.toString(), 05, className, methodName);
        }
    }

    public class OnResponseStringlistner implements Response.Listener<String> {
        String className;
        String methodName;
        public  OnResponseStringlistner(String _className, String _methodName){
            className = _className;
            methodName = _methodName;
        }
        @Override
        public void onResponse(String s) {
            listen.onTaskfinished(s, 05, className, methodName);
        }
    }
}
