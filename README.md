智能双排键aidl远程接口服务示例
===========================
该示例程序用于测试与`智能双排键控制面板`程序提供的AIDL远程服务接口，该示例为安卓程序，使用`Android Studio3.0.1`开发。
****
| Company | [北京煜林教育科技有限公司](http://www.yulinkeji.com/.) |  
|---|---|  
| Author | 黄海 |  
| Email | 1165441461@qq.com |  

****

## 使用说明

    1.将aidl文件夹复制 到自己项目的main文件夹下  
    2.make project  
    3.根据文档中的调用示例进行接口注册  
    4.即可使用  
    
## 代码调用示例
```java
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
```