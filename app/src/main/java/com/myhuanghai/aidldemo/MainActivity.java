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
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            mService = IRemoteService.Stub.asInterface(service);

            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }

            // As part of the sample, tell the user what happened.
            Toast.makeText(MainActivity.this, "服务器已连接",
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;

            // As part of the sample, tell the user what happened.
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
                   // Toast.makeText(MainActivity.this,"客户端："+value,Toast.LENGTH_SHORT).show();
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
