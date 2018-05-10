package com.example.zhonghuazheng.mycolumnview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    DoubleColumnview doubleColumnview;
    Button btn;
    List<Databean> list = new ArrayList<>();
    Random rand;//随机数
    Databean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doubleColumnview = findViewById(R.id.double_columnview);
        btn=  findViewById(R.id.btn);
        btn.setOnClickListener(this);

        rand = new Random();
        doubleColumnview.setData(getList());
    }

    public List<Databean> getList(){
        list.clear();
        for (int i = 0;i < 8;i++) {
            bean = new Databean();
            bean.setTimecode_avg_result(String.valueOf(rand.nextInt(16) + 3));
            bean.setLow_value(String.valueOf(4));
            bean.setHigh_value(String.valueOf(10));
            list.add(bean);
        }
        return list;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                //点击事件中，调用动的方法
                doubleColumnview.setData(getList());
                break;
        }
    }
}
