package com.example.administrator.myapplication.category;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Utils.BmobRequest;
import com.example.administrator.myapplication.Utils.PreferenceKit;
import com.example.administrator.myapplication.Utils.onFindResultsListener;
import com.example.administrator.myapplication.base.BaseFragment;
import com.example.administrator.myapplication.bmob.BookInformation;
import com.example.administrator.myapplication.borrowbook.BookDetailActivity;
import com.example.administrator.myapplication.recycleview.Category;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by samsung on 2017/11/17.
 */

public class TechnologyFragment extends BaseFragment {

    private View mRootView;
    private String _TAG = TechnologyFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private List<Category> mList;
    private BookDetailListAdapter mCategoryAdapter;

    private List<String> mSave;
    private Set<String> mSet;
    private Gson mGson;
    private ProgressBar mProgressBar;
    private ComparatorImpl mComparator = new ComparatorImpl();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstanceState){
        mRootView = layoutInflater.inflate(R.layout.fragment_technology,container,false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.techno_recylcerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mList = new ArrayList<>();

        mSave = PreferenceKit.getPreference(getContext(), _TAG).getAll(_TAG);
        mGson = new Gson();
        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.techno_progressbar);

        if (NetworkAvailale(getContext())) {
            Bquery();
            mProgressBar.setVisibility(View.VISIBLE);
        }else {
            if (mSave != null) {
                Collections.sort(mSave,mComparator);
                Collections.reverse(mSave);
                for (int i = 0; i < mSave.size(); i++) {
                    mList.add(i, mGson.fromJson(mSave.get(i), Category.class));
                }
                mCategoryAdapter = new BookDetailListAdapter(getContext(),mList, _TAG);
                mRecyclerView.setAdapter(mCategoryAdapter);
            } else {
                Toast.makeText(getContext(), "暂无网络，请检查网络", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bquery();
                        }
                    }, 200);
                }
                break;
        }
    }

    public void Bquery(){
        if(mList.size() > 0){
            mList.clear();
        }

        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("category", "technology");
        BmobRequest.findRequest("-createdAt", 50, mHashMap, new onFindResultsListener<BookInformation>() {

            @Override
            public void onSuccess(List<BookInformation> object){
                mSave = new ArrayList<>(object.size());
                mSet = new TreeSet<>(mComparator);
                for(int i = 0; i < object.size(); i++){
                    Category a1 = new Category(object.get(i).getObjectId(), object.get(i).getCreatedAt(), object.get(i).getName()
                            , object.get(i).getAuthor(), object.get(i).getBorrowcount(), object.get(i).getPress(), object.get(i).getPrice(), object.get(i).getState(),
                            object.get(i).getCategory(), object.get(i).getBorrowper(), object.get(i).getPhoto(), object.get(i).getBorrowtime(), object.get(i).getBacktime());
                    mList.add(i, a1);
                    mSave.add(i, mGson.toJson(a1));
                }
                mSet.addAll(mSave);
                PreferenceKit.getPreference(getContext(), _TAG).put(_TAG, mSet);
            }

            @Override
            public void onFail(int errorCode, String errorMessage){

            }

            @Override
            public void onComplete(boolean normal){
                mProgressBar.setVisibility(View.GONE);
                mCategoryAdapter = new BookDetailListAdapter(getContext(),mList, _TAG);
                mRecyclerView.setAdapter(mCategoryAdapter);
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mCategoryAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

//    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
//
//        private List<Category> _list;
//
//        class ViewHolder extends RecyclerView.ViewHolder{
//            ImageView _image;
//            TextView _name;
//            TextView _author;
//            TextView _press;
//            TextView _status;
//            View _view;
//
//            public ViewHolder(View view){
//                super(view);
//                _image = (ImageView) view.findViewById(R.id.category_image);
//                _name = (TextView) view.findViewById(R.id.category_name);
//                _author = (TextView)view.findViewById(R.id.category_author);
//                _press = (TextView) view.findViewById(R.id.category_press);
//                _status = (TextView) view.findViewById(R.id.category_status);
//                _view = view;
//            }
//        }
//
//        public CategoryAdapter(List<Category> Categorylist){
//            _list = Categorylist;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup viewGroup,int type){
//            View view=LayoutInflater.from(getContext()).inflate(R.layout.recycler_category,viewGroup,false);
//            final ViewHolder viewHolder=new ViewHolder(view);
//            viewHolder._view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = viewHolder.getAdapterPosition();
//                    Category _category=_list.get(position);
//                    Intent _intent=new Intent(getActivity(), BookDetailActivity.class);
//                    _intent.putExtra("bookName", _category.getName());
//                    _intent.putExtra("bookAuthor", _category.getAuthor());
//                    _intent.putExtra("bookPress", _category.getPress());
//                    _intent.putExtra("bookCategory", _TAG);
//                    _intent.putExtra("objectId", _category.get_objectId());
//                    _intent.putExtra("borrowper", _category.getBorrowper());
//                    startActivityForResult(_intent,1);
//                }
//            });
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder viewHolder,int position){
//            Category _category = _list.get(position);
//            viewHolder._name.setText(_category.getName());
//            viewHolder._author.setText(_category.getAuthor());
//            viewHolder._press.setText(_category.getPress());
//            if(_category.getState()) {
//                viewHolder._status.setText("可    借");
//            }else {
//                viewHolder._status.setText("已借出");
//            }
//            Glide.with(getContext()).load(_category.getPhoto().getFileUrl()).into(viewHolder._image);
//        }
//
//        @Override
//        public int getItemCount(){
//            return _list.size();
//        }
//    }
}
