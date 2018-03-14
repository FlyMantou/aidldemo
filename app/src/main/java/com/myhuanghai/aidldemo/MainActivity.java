package com.myhuanghai.aidldemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yulinkeji.doublebond.IRemoteService;
import com.yulinkeji.doublebond.IRemoteServiceCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    IRemoteService mService = null;

    private Button btn;
    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            mService = IRemoteService.Stub.asInterface(service);

            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {

            }

            Toast.makeText(MainActivity.this, "服务器已连接",
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            Toast.makeText(MainActivity.this, "服务器断开连接",
                    Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 远程回调接口实现
     */
    private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
        public void valueChanged(final String value) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("huanghai","键盘输入："+value);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.activity_main);
        btn = (Button)findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mService.onSendMsg("98 3C 64");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        bindService();
    }
    //绑定服务
    private void bindService(){
        Intent intent = new Intent();
        intent.setAction("com.yulinkeji.aidl");
        intent.setPackage("com.yulinkeji.doublebond");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbindService(mConnection);
    }


}
