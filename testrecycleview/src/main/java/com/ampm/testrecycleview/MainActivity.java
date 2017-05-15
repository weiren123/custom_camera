package com.ampm.testrecycleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView test_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DefaultItemAnimator
        test_view = (RecyclerView) findViewById(R.id.test_view);
        test_view.setItemAnimator(new MyItemAnimator());
    }
}
