package zsy.androidtranstest;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import zsy.androidtranstest.domain.Contact;
import zsy.androidtranstest.service.ContactService;
import zsy.androidtranstest.utils.MD5;

/**
 * 本地上传和调用系统拍照
 *
 * @author Administrator
 */

public class UpdateActivity extends Activity implements View.OnClickListener {

    //        private MyApplication application;
    String TAG = "PhotoActivity";
    private ImageView img;
    private EditText img_content;
    private Button nati;
    private Button pai;
    private Button submit;

    private EditText ipAddr;

    File cache; // 缓存文件

    //        @AbIocView(id = R.id.photo_full)
    LinearLayout photo_full;

    private static String srcPath;
    private static final int TIME_OUT = 10 * 1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码

    //        private String loginKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);

        cache = new File(Environment.getExternalStorageDirectory(), "cache"); // 实例化缓存文件

        initView();
    }

    private void initView() {
        ipAddr = (EditText) this.findViewById(R.id.update_ip);
        ipAddr.setText("192.168.1.103");
        img = (ImageView) findViewById(R.id.img);
        nati = (Button) findViewById(R.id.natives);
        pai = (Button) findViewById(R.id.pai);
        submit = (Button) findViewById(R.id.submit);
        img_content = (EditText) findViewById(R.id.img_content);

        nati.setOnClickListener(this);
        pai.setOnClickListener(this);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.natives:
                Intent local = new Intent();
                local.setType("image/*");
                local.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(local, 2);
                break;
            case R.id.pai:
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(it, 1);
                break;
            case R.id.submit:
                if (srcPath == null || srcPath == "") {
//                        showToast("文件不存在");
                    Log.e("err", "文件不存在");
                } else {
                    submitUploadFile();
                }
                break;
        }
    }

    /**
     * 拍照上传
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Bundle extras = data.getExtras();
                    Bitmap b = (Bitmap) extras.get("data");
                    img.setImageBitmap(b);
                    String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                    String fileNmae = Environment.getExternalStorageDirectory().toString() + File.separator + "dong/image/" + name + ".jpg";
                    srcPath = fileNmae;
                    System.out.println(srcPath + "----------保存路径1");
                    File myCaptureFile = new File(fileNmae);
                    try {
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            if (!myCaptureFile.getParentFile().exists()) {
                                myCaptureFile.getParentFile().mkdirs();
                            }
                            BufferedOutputStream bos;
                            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                            b.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                            bos.flush();
                            bos.close();
                        } else {
                            Log.e("err", "保存失败，SD卡无效");
//                                Toast toast= Toast.makeText(PhotoActivity.this, "保存失败，SD卡无效", Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Uri uri = data.getData();
                    img.setImageURI(uri);
                    ContentResolver cr = this.getContentResolver();
                    Cursor c = cr.query(uri, null, null, null, null);
                    c.moveToFirst();
                    //这是获取的图片保存在sdcard中的位置
                    srcPath = c.getString(c.getColumnIndex("_data"));
                    System.out.println(srcPath + "----------保存路径2");
                    break;
                default:
                    break;
            }
        }
//      n =1;
    }


    private void submitUploadFile() {
        final File file = new File(srcPath);
//            final String RequestURL=this.getResources().getString(R.string.update_addr);
        final String RequestURL = "http://" + ipAddr.getText() + ":8080/AndroidServerTest/UpdateServer";

        if (file == null || (!file.exists())) {
            return;
        }

        Log.i(TAG, "请求的URL=" + RequestURL);
        Log.i(TAG, "请求的fileName=" + file.getName());
        final Map<String, String> params = new HashMap<String, String>();
//            params.put("user_id", loginKey);
//            params.put("file_type", "1");
        params.put("content", img_content.getText().toString());
//            showProgressDialog();
        new Thread(new Runnable() { //开启线程上传文件
            @Override
            public void run() {
                uploadFile(file, RequestURL, params);
            }
        }).start();
    }

    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    private String uploadFile(File file, String RequestURL, Map<String, String> param) {
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        // 显示进度框
//      showProgressDialog();
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb;

                String params = "";
                if (param != null && param.size() > 0) {
                    Iterator<String> it = param.keySet().iterator();
                    while (it.hasNext()) {
//                            sb = null;
                        sb = new StringBuffer();
                        String key = it.next();
                        String value = param.get(key);
                        sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                        sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                        sb.append(value).append(LINE_END);
                        params = sb.toString();
                        Log.i(TAG, key + "=" + params + "##");
                        dos.write(params.getBytes());
//                          dos.flush();
                    }
                }
                sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"upfile\";filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: image/pjpeg; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);

                dos.flush();
                /**
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流
                 */

                int res = conn.getResponseCode();
                Log.v("conn.getResponseCode()", "res=========" + res);
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    final String path = sb1.toString();
                    result = path;
//                 // 移除进度框
//                      removeProgressDialog();
//                    Log.v("image result:", path);

                    //异步加载图片
                    asyncImageLoad(img, path);
//                    try {
//                        new Thread(new Runnable() {
//                            public void run() {
//                                try {
//                                    final Uri uri = getImageFromSever(path, cache);
//                                    img.setImageURI(uri);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
//                    img.setImageBitmap(null);
                   // finish();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Uri getImageFromSever(String path, File cacheDir) throws Exception{
        File localFile = new File(cacheDir, MD5.getMD5(path)+path.substring(path.lastIndexOf(".")));
        if(localFile.exists()){
           return Uri.fromFile(localFile);
        }else{
            HttpURLConnection conn = (HttpURLConnection) new URL(path).getContent();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            if(code == 200){
                FileOutputStream os = new FileOutputStream(localFile);
                InputStream inputStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while( (len = inputStream.read(buffer)) != -1){
                    os.write(buffer, 0, len);
                }
                inputStream.close();
                os.close();
                return Uri.fromFile(localFile);
            }
        }

        return null;
    }


    private void asyncImageLoad(ImageView imageView, String path) {
        AsyncImageTask asyncImageTask = new AsyncImageTask(imageView);
        asyncImageTask.execute(path);

    }
    /**
     * 使用AsyncTask异步加载图片
     *
     * @author Administrator
     *
     */
    private final class AsyncImageTask extends AsyncTask<String, Integer, Uri> {
        private ImageView imageView;

        public AsyncImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Uri doInBackground(String... params) {// 子线程中执行的
            try {
                return ContactService.getImage(params[0], cache);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Uri result) {// 运行在主线程
            if (result != null && imageView != null) {
                imageView.setImageURI(result);
                srcPath = result.getPath();
            }
        }
    }



    @Override
    protected void onDestroy() {
        // 删除缓存
        for (File file : cache.listFiles()) {
            file.delete();
        }
        cache.delete();
        super.onDestroy();
    }
}