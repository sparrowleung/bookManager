package com.example.administrator.myapplication.account;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.base.BaseActivity;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * Created by samsung on 2017/11/14.
 */

public class AccountActivity extends BaseActivity {

    private Toolbar mToolbar;
    private EventHandler _eventHandler;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        MobSDK.init(AccountActivity.this,"22547eb1b56cc","e4d211713a8d98952bbc877d7265e9ae");
        setContentView(R.layout.activity_account);

        upDateActionBar();

        mToolbar=(Toolbar) findViewById(R.id.toolbar_account);
        mToolbar.setTitle("账号管理");
        setSupportActionBar(mToolbar);
        ActionBar mActionBar=getSupportActionBar();
        if(mActionBar!=null){
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        AccountFragment accountFragment=new AccountFragment();
        attachFragmentAsSingle(accountFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();break;
        }
        return true;
    }
}
