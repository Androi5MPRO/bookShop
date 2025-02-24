package com.example.shopbansach.activity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopbansach.R;
import com.example.shopbansach.adapter.OderAdapter;
import com.example.shopbansach.adapter.ThongbaoAdapter;
import com.example.shopbansach.model.DonHang;
import com.example.shopbansach.model.ThongBao;
import com.example.shopbansach.model.emailLogin;
import com.example.shopbansach.util.CheckConnection;
import com.example.shopbansach.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonHangActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<DonHang> donHangArrayList;
    OderAdapter oderAdapter;
    Toolbar toolbardmm;
    String URL = Server.DuongdangetDonHang+"?email="+emailLogin.email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        AnhXa();
        ActionToolbar();
        GetDonHang(URL);
    }
    private void ActionToolbar() {
        setSupportActionBar(toolbardmm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbardmm.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbardmm.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void UpdateDonHangUS(String maDonHang){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdandeletedonhang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("success")){
                    CheckConnection.ShowToast_Short(getApplicationContext(),"Hủy thành công");
                    GetDonHang(URL);
                }else {
                    CheckConnection.ShowToast_Short(getApplicationContext(),"Update không thành công");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.ShowToast_Short(getApplicationContext(),"Update không thành công");
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maDonHang",String.valueOf(maDonHang));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GetDonHang(String url) {
            donHangArrayList.clear();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response){
                            for (int i = 0;i < response.length();i++){
                            String txtOrderName,txtTotal, txtQuantity, txtStatus;
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String maDH= "";
                                maDH = jsonObject.getString("maDonHang");
                                txtOrderName = jsonObject.getString("tenSanPham");
                                txtTotal   =(jsonObject.getString("tongtien"));
                                txtQuantity = (jsonObject.getString("soLuongSanPham"));
                                txtStatus = (jsonObject.getString("trangThai"));
                                donHangArrayList.add(new DonHang(maDH,txtOrderName,txtQuantity,txtStatus,txtTotal));
                                oderAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }}
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
            );
            requestQueue.add(jsonArrayRequest);
        }

    private void AnhXa() {
        toolbardmm = findViewById(R.id.toolbardmm);
        donHangArrayList = new ArrayList<>();
        oderAdapter = new OderAdapter (DonHangActivity.this,donHangArrayList);
        listView = findViewById(R.id.listViewOrder);
        listView.setAdapter(oderAdapter);
    }
}