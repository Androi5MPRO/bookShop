package com.example.shopbansach.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.shopbansach.R;
import com.example.shopbansach.adapter.ThongbaoAdapter;
import com.example.shopbansach.model.ThongBao;

import java.util.ArrayList;

public class ThongBaoActivity extends AppCompatActivity {
    ListView lvtb;
    ArrayList<ThongBao> arrayListtb;
    ThongbaoAdapter adapterTb;
    Toolbar toolbartb;
    LinearLayout ln_home,ln_tk,ln_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);
        AnhXa();
        ActionToolbar();
        OnclickMenu();
        AddThongBao();
    }

    private void AddThongBao() {
        arrayListtb = new ArrayList<>();

        arrayListtb.add(new ThongBao("Khuyến mãi","Ngày 25/11/2021 sale 50% tất cả sản phẩm",R.drawable.icon_tags));
        arrayListtb.add(new ThongBao("Cập nhật bookshop","Vào ngày 26/11/2021 tiến hành cập nhật lại ứng dụng",R.drawable.icon_tags));
        arrayListtb.add(new ThongBao("Chia sẻ bookshop","Nhập thẻ giảm giá 50% khi chia sẽ ứng dụng",R.drawable.icon_tags));
        arrayListtb.add(new ThongBao("Đánh giá","Đánh giá áp nhận ngay 10% khi mua tất cả sản phẩm",R.drawable.icon_tags));
        arrayListtb.add(new ThongBao("Khảo sát","Khảo sát giúp cải thiện bookshop",R.drawable.icon_tags));
        adapterTb = new ThongbaoAdapter(this, R.layout.dong_thongbao, arrayListtb);
        lvtb.setAdapter(adapterTb);
    }

    private void OnclickMenu() {
        ln_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        ln_tk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TaiKhoanActivity.class);
                startActivity(intent);
            }
        });
        ln_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TimKiemActivity.class);
                startActivity(intent);
            }
        });

    }

    private void AnhXa() {
        lvtb = findViewById(R.id.listviewthongbao);
        toolbartb = findViewById(R.id.toolbarthongbao);
        ln_home = findViewById(R.id.ln_home);
        ln_tk = findViewById(R.id.ln_tk);
        ln_search = findViewById(R.id.ln_search);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbartb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbartb.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbartb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}