package com.cairongcai.custdraglayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.cairongcai.custdraglayout.view.SlideMenu;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SlideMenu sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        sm = (SlideMenu) findViewById(R.id.sm);
        findViewById(R.id.ib_back).setOnClickListener(this);
    }
    public void onTabClick(View view)
    {
        Toast.makeText(getApplicationContext(),"展示具体内容",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        sm.switchState();
    }

}
