package com.example.administrator.myapplication.borrowbook;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Utils.BmobRequest;
import com.example.administrator.myapplication.Utils.onFindResultsListener;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.bmob.BookInformation;
import com.example.administrator.myapplication.recycleview.Category;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by samsung on 2017/11/30.
 */

public class BookDetailFragment extends BaseFragment {

    private View mRootView;
    private CardView mCardView;
    private String mBookName;
    private String mBookPress;
    private String mBookAuthor;
    private String mBookCategory;

    private TextView mName;
    private TextView mAuthor;
    private TextView mPress;
    private TextView mCategory;
    private TextView mState;
    private TextView mBorrowper;
    private ImageView mImageView;
    private Category _category;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstanceState){
        mRootView = layoutInflater.inflate(R.layout.fragment_bookdetail,container,false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage("Nothing but all");
        dialog.setTitle("Nothing");
        dialog.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();

        mBookName = getActivity().getIntent().getStringExtra("bookName");
        mBookAuthor = getActivity().getIntent().getStringExtra("bookAuthor");
        mBookPress = getActivity().getIntent().getStringExtra("bookPress");
        mBookCategory = getActivity().getIntent().getStringExtra("bookCategory");

        mCardView = (CardView) getActivity().findViewById(R.id.detail_cardview);
        mName = (TextView) getActivity().findViewById(R.id.detail_name);
        mAuthor = (TextView) getActivity().findViewById(R.id.detail_author);
        mPress = (TextView) getActivity().findViewById(R.id.detail_press);
        mCategory = (TextView) getActivity().findViewById(R.id.detail_category);
        mState = (TextView) getActivity().findViewById(R.id.detail_state);
        mBorrowper = (TextView) getActivity().findViewById(R.id.detail_borrowper);
        mImageView = (ImageView) getActivity().findViewById(R.id.detail_image);

        InitSharePreferences(mBookCategory);

        if(_set != null){
            _save.addAll(_set);
            for(int i = 0; i < _save.size(); i++) {
                _category = _gson.fromJson(_save.get(i), Category.class);
                if(_category.getName().equals(mBookName) && _category.getAuthor().equals(mBookAuthor)
                        && _category.getPress().equals(mBookPress)){
                    break;
                }
            }
            mName.setText(_category.getName());
            mAuthor.setText(_category.getAuthor());
            mPress.setText(_category.getPress());

            if(_category.getCategory().equals("literature")){
                mCategory.setText("文学");
            }else {
                mCategory.setText("技术");
            }

            mBorrowper.setText(_category.getBorrowper());

            if(_category.getState()) {
                mState.setText("可借阅");
            }else {
                mState.setText("已外借");
            }
            Glide.with(getContext()).load(_category.getPhoto().getFileUrl()).into(mImageView);
        }else {
            Bquery();
        }
    }

    public void Bquery(){

        HashMap<String , Object> mHashMap = new HashMap<String, Object>();
        if(!mBookName.equals(null)) {
            mHashMap.put("name", mBookName);
        }
        if(!mBookAuthor.equals(null)) {
            mHashMap.put("author", mBookAuthor);
        }
        if(!mBookPress.equals(null)) {
            mHashMap.put("press", mBookPress);
        }
        BmobRequest.findRequest("index", mHashMap, new onFindResultsListener<BookInformation>() {
            @Override
            public void onSuccess(List<BookInformation> list){
                BookInformation object = list.get(0);
                mName.setText(object.getName());

                mAuthor.setText(object.getAuthor());

                mPress.setText(object.getPress());

                if(object.getCategory().equals("literature")){
                    mCategory.setText("文学");
                }else {
                    mCategory.setText("技术");
                }

                mBorrowper.setText(object.getBorrowper());

                if(object.getState()) {
                    mState.setText("可借阅");
                }else {
                    mState.setText("已外借");
                }

                Glide.with(getContext()).load(object.getPhoto().getFileUrl()).into(mImageView);
            }

            @Override
            public void onFail(int errorCode, String errorMessage){

            }

            @Override
            public void onComplete(boolean normal){

            }
        });
    }

}
