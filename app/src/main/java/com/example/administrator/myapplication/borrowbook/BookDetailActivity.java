package com.example.administrator.myapplication.borrowbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Utils.BmobRequest;
import com.example.administrator.myapplication.Utils.onUpdateObjectListener;
import com.example.administrator.myapplication.account.AccountActivity;
import com.example.administrator.myapplication.base.BaseActivity;
import com.example.administrator.myapplication.bmob.BookInformation;
import com.example.administrator.myapplication.bmob.Summary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 37289 on 2017/11/22.
 */

public class BookDetailActivity extends BaseActivity{

    private Toolbar mToolbar;
    private Button mBorrow;
    private Button mBack;
    private BmobUser mUser;
    private Date mDate;
    private BookInformation mBookInf;

    private String mObjectId;
    private String mBorrowper;
    private String mCategory;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_bookdetail);

        upDateActionBar();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        mToolbar.setTitle("图书借阅");
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        if(mActionBar != null){
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        BookDetailFragment bookDetailFragment = new BookDetailFragment();
        attachFragmentAsSingle(bookDetailFragment);

        mUser = BmobUser.getCurrentUser();
        mDate = new Date(System.currentTimeMillis());
        mObjectId = getIntent().getStringExtra("objectId");
        mBorrowper = getIntent().getStringExtra("borrowper");
        mCategory = getIntent().getStringExtra("bookCategory");

        mBookInf = new BookInformation();

        mBorrow = (Button) findViewById(R.id.detail_borrow);
        mBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUser == null){
                    AccountLogin();
                }else {
                    if (mBorrowper.equals("")) {
                        mBookInf.setBorrowcount(mBookInf.getBorrowcount() + 1);
                        mBookInf.setBorrowper(mUser.getUsername());
                        mBookInf.setState(false);
                        mBookInf.setBorrowtime(mDate);
                        BmobRequest.updateObject(mBookInf, mObjectId, new onUpdateObjectListener() {
                            @Override
                            public void onSuccess(String objectId) {
                                SummaryChange(mCategory);
                                Toast.makeText(BookDetailActivity.this, "借阅成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFail(int errorCode, String errorMessage) {
                                Log.d(TAG, "error Message = " + errorMessage + ", error Code = " + errorCode);
                            }
                        });
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        Toast.makeText(BookDetailActivity.this, "书本已被借阅", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mBack = (Button) findViewById(R.id.detail_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUser == null){
                    AccountLogin();
                }else {
                    mBookInf.setBorrowper("");
                    mBookInf.setState(true);
                    mBookInf.setBorrowtime(new Date(System.currentTimeMillis()));
                    mBookInf.setBacktime(mDate);
                    BmobRequest.updateObject(mBookInf, mObjectId, new onUpdateObjectListener() {
                        @Override
                        public void onSuccess(String objectId) {
                            SummaryChange(mCategory);
                            Toast.makeText(BookDetailActivity.this, "归还成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail(int errorCode, String errorMessage) {
                            Log.d(TAG, "error Message = " + errorMessage + ", error Code = " + errorCode);
                        }
                    });
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    public void AccountLogin(){
        AlertDialog.Builder _builder = new AlertDialog.Builder(BookDetailActivity.this);
        _builder.setTitle("提示");
        _builder.setMessage("进行该操作需要登录账号，是否马上登录?");
        _builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent _intent = new Intent(BookDetailActivity.this, AccountActivity.class);
                startActivity(_intent);
            }
        });
        _builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        _builder.create();
        _builder.show();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:finish();break;
        }
        return true;
    }

    public void SummaryChange(String category){
        Summary _summary = new Summary();
        _summary.setChange(Double.toString(Math.random()));
        if(category.equals("TechnologyFragment")) {
            BmobRequest.updateObject(_summary, "iIqUZZZv", new onUpdateObjectListener() {
                @Override
                public void onSuccess(String objectId) {

                }

                @Override
                public void onFail(int errorCode, String errorMessage) {
                    Log.d(TAG, "error Message = " + errorMessage + ", error Code = " + errorCode);
                }
            });

        }else if(category.equals("LiteratureFragment")){
            BmobRequest.updateObject(_summary, "wmjW777E", new onUpdateObjectListener() {
                @Override
                public void onSuccess(String objectId) {

                }

                @Override
                public void onFail(int errorCode, String errorMessage) {
                    Log.d(TAG, "error Message = " + errorMessage + ", error Code = " + errorCode);
                }
            });
        }
    }
}
