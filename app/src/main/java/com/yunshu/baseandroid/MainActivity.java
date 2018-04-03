package com.yunshu.baseandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.yunshu.baseandroid.activities.BaseActivity;
import com.yunshu.baseandroid.activities.ListActivity;
import com.yunshu.baseandroid.activities.TestActivity;
import com.yunshu.baseandroid.entity.Customer;
import com.yunshu.baseandroid.entity.CustomerMany;
import com.yunshu.baseandroid.entity.CustomerMany_;
import com.yunshu.baseandroid.entity.MySong;
import com.yunshu.baseandroid.entity.Order;
import com.yunshu.baseandroid.entity.OrderMany;
import com.yunshu.baseandroid.entity.Order_;
import com.yunshu.baseandroid.jpush.TagAliasUtil;
import com.yunshu.commonlib.okhttp.ResultDisposer;
import com.yunshu.commonlib.util.LogWrapper;
import com.yunshu.commonlib.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.objectbox.Box;
import io.objectbox.query.QueryBuilder;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class MainActivity extends BaseActivity {

    @BindView(R.id.edit_user_name)
    EditText mText1;
    @BindView(R.id.edit_user_pwd)
    EditText mText2;
    @BindView(R.id.login)
    Button mBtnLogin;
    @BindView(R.id.confirm)
    Button mBtnConfirm;
    @BindView(R.id.analytics_edit_text)
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*=========objectbox一对一=========*/
        Box<Order> orderBox = getBox(Order.class);
        Order order = new Order();
        order.setOrderName("测试order");
        Customer customer = new Customer();
        customer.setName("测试顾客");
        order.customer.setTarget(customer);
        long orderId = orderBox.put(order);
        LogWrapper.d(String.valueOf(orderId));
        QueryBuilder builder = orderBox.query();
        builder.equal(Order_.id, orderId);
        List<Order> orders = builder.build().find();
        Order orderResult = orders.get(0);
        LogWrapper.d(orderResult.getOrderName());
        // 这里只解除order和customer的关系，并不删除customer
        order.customer.setTarget(null);
        orderBox.put(order);
        /*=========objectbox一对一=========*/

        /*=========objectbox一对多=========*/
        Box<CustomerMany> orderManyBox = getBox(CustomerMany.class);
        CustomerMany customerMany = new CustomerMany();
        customerMany.setName("一对多测试客户-2");
        OrderMany orderMany = new OrderMany();
        orderMany.setOrderName("一对多测试-Order1111");
        OrderMany orderMany2 = new OrderMany();
        orderMany2.setOrderName("一对多测试-Order2222");
        customerMany.orders.add(orderMany);
        customerMany.orders.add(orderMany2);
        long orderManyId = orderManyBox.put(customerMany);
        QueryBuilder builder1 = orderManyBox.query();
        builder1.equal(CustomerMany_.id, orderManyId);
        CustomerMany a = (CustomerMany) builder1.build().findFirst();
        LogWrapper.d(a.getName() + "   " + a.orders.get(0).getOrderName());
        /*=========objectbox一对多=========*/

//        Box<MySong> songBox = getBox(MySong.class);
//        setSongData(songBox);
//        MySong song = songBox.get(5);
//        LogWrapper.d("name: "+ song.getName() + ", author: " + song.getAuthor());

        TagAliasUtil tagAliasUtil = new TagAliasUtil(this);
        tagAliasUtil.setNewAlias("tyrese");
//        mBtnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("test", "登录");
//                MobclickAgent.onEvent(MainActivity.this,"testcalculate");
//                MobclickAgent.onEvent(MainActivity.this," testcalculate", "testcalculatebutton");
//                login();
//                MobclickAgent.onEvent(MainActivity.this, "click");
//                MobclickAgent.onEvent(MainActivity.this, "click", "button");
//            }
//        });
//        mBtnLogin.setOnClickListener();

        try {
            ApplicationInfo appInfo = this.getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString("JPUSH_APPKEY");
            LogWrapper.d(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setSongData(Box<MySong> songBox) {
        ArrayList<MySong> data = new ArrayList<>();
        MySong song = null;
        song = new MySong(1, "改变自己", "王力宏", "2017-12-19");
        data.add(song);
        song = new MySong(2, "成都", "赵雷", "2017-12-19");
        data.add(song);
        song = new MySong(3, "情歌", "梁静茹", "2017-12-19");
        data.add(song);
        song = new MySong(4, "浮夸", "陈奕迅", "2017-12-19");
        data.add(song);
        song = new MySong(5, "鞋子特大号", "周杰伦", "2017-12-19");
        data.add(song);
        song = new MySong(6, "回到过去", "周杰伦", "2017-12-19");
        data.add(song);
        song = new MySong(7, "上海一九四三", "周杰伦", "2017-12-19");
        data.add(song);
        song = new MySong(8, "我不配", "周杰伦", "2017-12-19");
        data.add(song);
        songBox.put(data);
    }

    public void login() {

//        Intent intent = new Intent(this, ListActivity.class);
//        startActivity(intent);

//        String userName = mText1.getEditableText().toString().trim();
//        String password = mText2.getEditableText().toString().trim();
//        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
//            String url = "https://www.dj.scst.gov.cn:82/skjt/app/user/signin";
//            JSONObject params = new JSONObject();
//            try {
//                params.put("account", userName);
//                params.put("password", password);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return;
//            }
//            okHttpService.post(url, params.toString(), new OkHttpResultCallback() {
//                @Override
//                public void onFailure(IOException e) {
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onResponse(Response response) {
//                    ResponseBody body = response.body();
//
//                    try {
//                        String ddd = body.string();
//                        LogWrapper.d(ddd);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//        }

        String url = "https://www.dj.scst.gov.cn:82/skjt/app/news/1/10/3";
        for (int i = 0; i < 50; i ++) {
            okHttpService.get(url, new ResultDisposer() {
                @Override
                public void onException(Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void onFailure(String error) {
                    ToastUtil.textToast(MainActivity.this, error);
                }

                @Override
                public void onSuccess(String data) {
                    LogWrapper.d(data);
                }
            });
        }

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_PICK);
//        startActivityForResult(intent, REQUEST_CODE_FOR_IMAGE);


//        FileDownloader.getImpl().create("https://www.dj.scst.gov.cn:82/upload/file/f528214963724906261496328538323.jpg")
//                .setPath(Environment.getExternalStorageDirectory() + "/yunshuweilai/f528214963724906261496328538323.jpg")
//                .setListener(new FileDownloadListener() {
//                    @Override
//                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                    }
//
//                    @Override
//                    protected void started(BaseDownloadTask task) {
//                    }
//
//                    @Override
//                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
//                    }
//
//                    @Override
//                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                    }
//
//                    @Override
//                    protected void blockComplete(BaseDownloadTask task) {
//                    }
//
//                    @Override
//                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
//                    }
//
//                    @Override
//                    protected void completed(BaseDownloadTask task) {
//                    }
//
//                    @Override
//                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                    }
//
//                    @Override
//                    protected void error(BaseDownloadTask task, Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    protected void warn(BaseDownloadTask task) {
//                    }
//                }).start();

//        okHttpService.downloadFile("http://www.dj.scst.gov.cn:81/upload/file/f528214963724906261496328538323.jpg",
//                Environment.getExternalStorageDirectory() + "/yunshuweilai/f528214963724906261496328538323.jpg",
//                new FileDownloadListener() {
//                    @Override
//                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//
//                    }
//
//                    @Override
//                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//
//                    }
//
//                    @Override
//                    protected void completed(BaseDownloadTask task) {
//
//                    }
//
//                    @Override
//                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//
//                    }
//
//                    @Override
//                    protected void error(BaseDownloadTask task, Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    protected void warn(BaseDownloadTask task) {
//
//                    }
//                });

    }

    private final int REQUEST_CODE_FOR_IMAGE = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_IMAGE) {
                String url = "https://www.dj.scst.gov.cn:82/skjt/app/upload/headImage";
//                String url = "http://192.168.1.178:8080/test";
                String filePath = "";
                Cursor cursor = getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA}, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
//                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                    filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    LogWrapper.d(filePath);
                }
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                File file = new File(filePath);
                HashMap<String, Object> param = new HashMap<>();
                param.put("file", file);
                param.put("name", "testfff.png");
//                okHttpService.postFile(url, file, new OkHttpResultCallback() {
//                    @Override
//                    public void onFailure(IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Response response) {
//                        if (response.isSuccessful()) {
//                            ResponseBody body = response.body();
//                            try {
//                                String ddd = body.string();
//                                LogWrapper.d(ddd);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            LogWrapper.d("something wrong");
//                        }
//                    }
//                }, new ReqProgressCallBack() {
//                    @Override
//                    public void onProgress(long total, long current) {
//                        LogWrapper.d(String.valueOf(current));
//                    }
//                });
//                okHttpService.uploadMultipart(url, param, new OkHttpResultCallback() {
//                    @Override
//                    public void onFailure(IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(Response response) {
//                        if (response.isSuccessful()) {
//                            ResponseBody body = response.body();
//                            try {
//                                String ddd = body.string();
//                                LogWrapper.d(ddd);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            LogWrapper.d("something wrong");
//                        }
//                    }
//                });
            }
        }
    }
}
