package com.example.shopbansach.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopbansach.R;
import com.example.shopbansach.adapter.TruyenTranhAdapter;
import com.example.shopbansach.model.Sanpham;
import com.example.shopbansach.util.CheckConnection;
import com.example.shopbansach.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TruyenTranhActivity extends AppCompatActivity {
    LinearLayout ln_home,ln_tk,ln_tb;
    Toolbar toolbartt;
    ListView lvtt;
    TruyenTranhAdapter truyenTranhAdapter;
    ArrayList<Sanpham> mangtt;
    int idtt = 0;
    int page = 1;
    View footerview;
    boolean isLoading = false;
    mHandler mHandler;
    boolean limitadata = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truyen_tranh);
        AnhXa();
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            GetIdLoaisp();
            ActionToolbar();
            GetData(page);
            LoadMoreData();
            ClickMenu();
        }
        else {
            CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối Internet");
        }

    }

    private void ClickMenu() {
        ln_home = findViewById(R.id.ln_tt_menu);
        ln_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        ln_tk = findViewById(R.id.ln_tt_tk);
        ln_tk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ln_tb = findViewById(R.id.ln_tt_tb);
        ln_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ThongBaoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), com.example.shopbansach.activity.Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadMoreData() {
        lvtt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),Chitietsanpham.class);
                intent.putExtra("thongtinsanpham",mangtt.get(i));
                startActivity(intent);
            }
        });

        lvtt.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int FirstItem, int VisibleItem, int TotalItem) {
                if (FirstItem + VisibleItem == TotalItem && TotalItem!= 0 && isLoading == false && limitadata == false){
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdantruyentranh+String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                lvtt.removeFooterView(footerview);
                int id=0;
                String Tentt = "";
                int Giatt = 0;
                String Hinhanhtt="";
                String Motatt="";
                int Idsptt = 0;
                if(response != null && response.length() != 2   ){
                    lvtt.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tentt = jsonObject.getString("tensp");
                            Giatt = jsonObject.getInt("giasp");
                            Hinhanhtt = jsonObject.getString("hinhanhsp");
                            Motatt = jsonObject.getString("motasp");
                            Idsptt = jsonObject.getInt("idsanpham");
                            mangtt.add(new Sanpham(id,Tentt,Giatt,Hinhanhtt,Motatt,Idsptt));
                            truyenTranhAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    limitadata = true;
                    lvtt.removeFooterView(footerview);
                    CheckConnection.ShowToast_Short(getApplicationContext(),"Đã hết dữ liệu");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String,String>();
                param.put("idSanPham",String.valueOf(idtt));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbartt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbartt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void GetIdLoaisp() {
        idtt = getIntent().getIntExtra("idloaisanpham",-1);
        Log.d("giatriloaisanpham",idtt+"");
    }

    private void AnhXa() {
        toolbartt = findViewById(R.id.toolbartruyentranh);
        lvtt = findViewById(R.id.listviewtruyentranh);
        mangtt= new ArrayList<>();
        truyenTranhAdapter = new TruyenTranhAdapter(getApplicationContext(),mangtt);
        lvtt.setAdapter(truyenTranhAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar,null);
        mHandler = new mHandler();
    }

    public class mHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvtt.addFooterView(footerview);
                    break;
                case 1:
                    GetData(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}