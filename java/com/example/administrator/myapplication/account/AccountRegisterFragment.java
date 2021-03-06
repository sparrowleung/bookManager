package com.example.administrator.myapplication.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.bmob.UserInformation;
import com.example.administrator.myapplication.base.BaseFragment;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by samsung on 2017/11/21.
 */

public class AccountRegisterFragment extends BaseFragment {

    private View _rootView;
    private EventHandler _eventHandler;
    private Boolean phoneVerify=false;

    private EditText mPhoneNum;
    private EditText mPassword;
    private EditText mVerifyCode;
    private EditText mName;
    private EditText mPart;
    private EditText mTeam;

    private String _phoneNum;
    private String _password;
    private String _verifyCode;
    private String _name;
    private String _part;
    private String _team;

    private Button mPhoneVer;
    private Button mRegister;


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(layoutInflater,container,saveInstanceState);
        _rootView=layoutInflater.inflate(R.layout.fragment_register,container,false);
        return _rootView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        mPhoneNum=(EditText) getActivity().findViewById(R.id.register_phone);
        mPassword=(EditText) getActivity().findViewById(R.id.register_password);
        mVerifyCode=(EditText) getActivity().findViewById(R.id.register_veifycode);
        mName=(EditText) getActivity().findViewById(R.id.register_name);
        mPart=(EditText) getActivity().findViewById(R.id.register_part);
        mTeam=(EditText) getActivity().findViewById(R.id.register_tg);

        InitializeSms();
        mPhoneVer=(Button) getActivity().findViewById(R.id.register_verb);
        mPhoneVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _phoneNum=mPhoneNum.getText().toString();
                SMSSDK.getVerificationCode("86",_phoneNum);
            }
        });


        mRegister=(Button) getActivity().findViewById(R.id.register_register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _verifyCode=mVerifyCode.getText().toString();
                SMSSDK.submitVerificationCode("86",_phoneNum,_verifyCode);
            }
        });
    }

    public void InitializeSms(){
        _eventHandler=new EventHandler(){
            @Override
            public void afterEvent(int event,int result,Object data){
                final Message _message=new Message();
                if(result == SMSSDK.RESULT_COMPLETE){
                    if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){

                        _message.what=1;
                        handler.sendMessage(_message);
                        Log.d("BACED",_phoneNum);

                    }else if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        phoneVerify=true;
                        _password=mPassword.getText().toString();
                        _name=mName.getText().toString();
                        _part=mPart.getText().toString();
                        _team=mTeam.getText().toString();
                        Log.d("BACED",_verifyCode+phoneVerify);

                        UserInformation userInformation = new UserInformation();
                        userInformation.setPart(_part);
                        userInformation.setTeamgroup(_team);
                        userInformation.setUsername(_name);
                        userInformation.setMobilePhoneNumber(_phoneNum);
                        userInformation.setMobilePhoneNumberVerified(phoneVerify);
                        userInformation.setPassword(_password);
                        userInformation.signUp(new SaveListener<UserInformation>() {
                                @Override
                                public void done(UserInformation object,BmobException e){
                                    if(e == null){
                                        _message.what=2;
                                        onDestroy();
                                    }else {
                                        _message.what=3;
                                    }
                                    handler.sendMessage(_message);
                                }
                            });
                    }else {
                        ((Throwable)data).printStackTrace();
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(_eventHandler);
    }


    public Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.d("BACED","SendSuccess");
                    Toast.makeText(getContext(), "发送验证码成功", Toast.LENGTH_SHORT).show();break;
                case 2:
                    Log.d("BACED","VerifySuccess");
                    Toast.makeText(getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent = getContext().getPackageManager()
                            .getLaunchIntentForPackage(getContext().getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 3:
                    Log.d("BACED","VerifyFail");
                    Toast.makeText(getContext(),"信息有误，注册失败",Toast.LENGTH_SHORT).show();break;
            }
            return false;
        }
    });

    @Override
    public void onDestroy(){
        super.onDestroy();
        SMSSDK.unregisterEventHandler(_eventHandler);
    }

}
