package com.example.administrator.myapplication.advice;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseActivity;

/**
 * Created by Administrator on 2017/10/30.
 */

public class BuyAdviceActivity extends BaseActivity {

    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_advice);

        upDateActionBar();

        mToolbar=(Toolbar) findViewById(R.id.toolbar_advice);
        mToolbar.setTitle("购买建议");
        setSupportActionBar(mToolbar);
        ActionBar mActionBar=getSupportActionBar();
        if(mActionBar!=null){
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        BuyAdviceFragment buyAdviceFragment=new BuyAdviceFragment();
        attachFragmentAsSingle(buyAdviceFragment);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:finish();break;
        }
        return true;
    }
}

