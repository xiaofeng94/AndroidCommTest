package zsy.androidtranstest.fragement;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import zsy.androidtranstest.MyApplication;
import zsy.androidtranstest.R;
import zsy.androidtranstest.service.ContactService;

/**
 * Created by zsy on 16/6/16.
 */
//new fragement for image stitching
public class ImageStitchFragement extends Fragment {

    private static final int TIME_OUT = 10 * 1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码

    private SwipeMenuListView sMListView;
    private List<String> paths;
    private MyAdapter mAdapter;
    private Button addImage;
    private Button submit;
    private MyApplication myApp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = (MyApplication)getActivity().getApplication();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragement_image_stitch, container, false);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
//                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        getActivity());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                openItem.setWidth(dp2px(90));
//                // set item title
//                openItem.setTitle("Open");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

//                 create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.icon_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        paths = new ArrayList<String>();
//        paths.add("test");
//        paths.add("test");
//        paths.add("test");
//        paths.add("test");


        mAdapter = new MyAdapter();
        sMListView = (SwipeMenuListView) view.findViewById(R.id.lv_iamges_stitch);
        sMListView.setMenuCreator(creator);
        sMListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        paths.remove(position);
                        mAdapter.notifyDataSetChanged();
                        // delete
                        break;
                    case 1:
                        // other
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        sMListView.setAdapter(mAdapter);

        addImage = (Button) view.findViewById(R.id.add_stitch);
        submit = (Button) view.findViewById(R.id.submit_stitch);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent local = new Intent();
                local.setType("image/*");
                local.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(local, 2);
//                paths.add("test");


            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "submit:" + paths.size(), Toast.LENGTH_SHORT).show();
//                Log.v("imageViews", mAdapter.imageViews.size();
                for (int i = 0; i < paths.size();++i){
                    Log.v("last_path", paths.get(i) + "----------保存路径2");
                }
            }
        });

        return view;
    }

    /**
     * dp2px
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    class MyAdapter extends BaseSwipListAdapter {

//        public List<ImageView> imageViews;
//
//        public MyAdapter() {
//            super();
//            imageViews = new ArrayList<>();
//        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public String getItem(int position) {
            return paths.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.lv_item_stitch, null);
            }
            ImageView image = (ImageView) convertView.findViewById(R.id.iv_imag_stitch);
//            if (imageViews.size() < position + 1) {
//                imageViews.add(image);
//            }
            image.setImageURI(Uri.parse(paths.get(position)));

            return convertView;
        }


        @Override
        public boolean getSwipEnableByPosition(int position) {
//            if(position % 2 == 0){
//                return false;
//            }
            return true;
        }

        @Override
        public void notifyDataSetChanged (){
            super.notifyDataSetChanged();
//            Log.v("imageViews", mAdapter.imageViews.size() + "----------xxx2");
        }
    }

    @Override
    public void onResume() {
        //...更新View
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
//                case 1:
//                    Bundle extras = data.getExtras();
//                    Bitmap b = (Bitmap) extras.get("data");
//                    img.setImageBitmap(b);
//                    String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
//                    String fileNmae = Environment.getExternalStorageDirectory().toString() + File.separator + "dong/image/" + name + ".jpg";
//                    srcPath = fileNmae;
//                    System.out.println(srcPath + "----------保存路径1");
//                    File myCaptureFile = new File(fileNmae);
//                    try {
//                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                            if (!myCaptureFile.getParentFile().exists()) {
//                                myCaptureFile.getParentFile().mkdirs();
//                            }
//                            BufferedOutputStream bos;
//                            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
//                            b.compress(Bitmap.CompressFormat.JPEG, 80, bos);
//                            bos.flush();
//                            bos.close();
//                        } else {
//                            Log.e("err", "保存失败，SD卡无效");
////                                Toast toast= Toast.makeText(PhotoActivity.this, "保存失败，SD卡无效", Toast.LENGTH_SHORT);
////                                toast.setGravity(Gravity.CENTER, 0, 0);
////                                toast.show();
//                        }
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    break;
                case 2:
                    Uri uri = data.getData();
                    ContentResolver cr = getActivity().getContentResolver();
                    Cursor c = cr.query(uri, null, null, null, null);
                    c.moveToFirst();
                    //这是获取的图片保存在sdcard中的位置
                    paths.add(c.getString(c.getColumnIndex("_data")));
                    Log.v("last_path", paths.get(paths.size() - 1) + "----------保存路径2");
                    mAdapter.notifyDataSetChanged();

//                    mAdapter.imageViews.get(mAdapter.imageViews.size()-1).setImageURI(uri);
//                    Log.v("imageViews", mAdapter.imageViews.size() + "----------保存路径2");
                    break;
                default:
                    break;
//            }
            }
        }
    }

    private void submitUploadFile() {

        final String RequestURL = "http://" + myApp.serverIP + ":8080/AndroidServerTest/UpdateServer";
        final Map<String, String> params = new HashMap<String, String>();
        final List<File> updateFileList = new ArrayList<>();
//        final File file = new File(paths.get(0));
//
//            if (file == null || (!file.exists())) {
//                return;
//            }
//
////            Log.i(TAG, "请求的URL=" + RequestURL);
////            Log.i(TAG, "请求的fileName=" + file.getName());
//            final Map<String, String> params = new HashMap<String, String>();
////            params.put("user_id", loginKey);
////            params.put("file_type", "1");
////            params.put("content", img_content.getText().toString());
////            showProgressDialog();
//            new Thread(new Runnable() { //开启线程上传文件
//                @Override
//                public void run() {
//                    uploadFile(file, RequestURL, params);
//                }
//            }).start();


        for(int i = 0;i < paths.size();++i) {
            File file = new File(paths.get(i));
            if (file == null || (!file.exists())) {
                continue;
            }
            updateFileList.add(file);
        }
        new Thread(new Runnable() { //开启线程上传文件
            @Override
            public void run() {
                uploadFiles(updateFileList, RequestURL, params);
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
//                        Log.i(TAG, key + "=" + params + "##");
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
//                    result = path;
//                 // 移除进度框
//                      removeProgressDialog();
                    Log.v("image result:", path);

                    //异步加载图片
//                    asyncImageLoad(img, path);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * android上传文件到服务器
     *
     * @param fileList       需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    private String uploadFiles(List<File> fileList, String RequestURL, Map<String, String> param) {
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
//                        Log.i(TAG, key + "=" + params + "##");
                    dos.write(params.getBytes());
//                          dos.flush();
                }
            }
            sb = new StringBuffer();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
//            sb.append(LINE_END);
            dos.write(sb.toString().getBytes());

            for(int i = 0;i < fileList.size();++i){
                dos.write(LINE_END.getBytes());

                sb = new StringBuffer();
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"upfile\";filename=\"" + fileList.get(i).getName() + "\"" + LINE_END);
                sb.append("Content-Type: image/pjpeg; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(fileList.get(i));
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY).getBytes();
                dos.write(end_data);
                dos.flush();
            }
            dos.write(PREFIX.getBytes());
            dos.write(LINE_END.getBytes());
            dos.flush();

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
//                    result = path;
//                 // 移除进度框
//                      removeProgressDialog();
                Log.v("image result:", path);
            }

//            if (fileList != null) {
//                /**
//                 * 当文件不为空，把文件包装并且上传
//                 */
//                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
//                StringBuffer sb;
//
//                String params = "";
//                if (param != null && param.size() > 0) {
//                    Iterator<String> it = param.keySet().iterator();
//                    while (it.hasNext()) {
////                            sb = null;
//                        sb = new StringBuffer();
//                        String key = it.next();
//                        String value = param.get(key);
//                        sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
//                        sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
//                        sb.append(value).append(LINE_END);
//                        params = sb.toString();
////                        Log.i(TAG, key + "=" + params + "##");
//                        dos.write(params.getBytes());
////                          dos.flush();
//                    }
//                }
//                sb = new StringBuffer();
//                sb.append(PREFIX);
//                sb.append(BOUNDARY);
//                sb.append(LINE_END);
//                /**
//                 * 这里重点注意：
//                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
//                 * filename是文件的名字，包含后缀名的   比如:abc.png
//                 */
//                sb.append("Content-Disposition: form-data; name=\"upfile\";filename=\"" + file.getName() + "\"" + LINE_END);
//                sb.append("Content-Type: image/pjpeg; charset=" + CHARSET + LINE_END);
//                sb.append(LINE_END);
//                dos.write(sb.toString().getBytes());
//                InputStream is = new FileInputStream(file);
//                byte[] bytes = new byte[1024];
//                int len = 0;
//                while ((len = is.read(bytes)) != -1) {
//                    dos.write(bytes, 0, len);
//                }
//                is.close();
//                dos.write(LINE_END.getBytes());
//                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
//                dos.write(end_data);
//
//                dos.flush();
//                /**
//                 * 获取响应码  200=成功
//                 * 当响应成功，获取响应的流
//                 */
//
//                int res = conn.getResponseCode();
//                Log.v("conn.getResponseCode()", "res=========" + res);
//                if (res == 200) {
//                    InputStream input = conn.getInputStream();
//                    StringBuffer sb1 = new StringBuffer();
//                    int ss;
//                    while ((ss = input.read()) != -1) {
//                        sb1.append((char) ss);
//                    }
//                    final String path = sb1.toString();
////                    result = path;
////                 // 移除进度框
////                      removeProgressDialog();
//                    Log.v("image result:", path);
//
//                    //异步加载图片
////                    asyncImageLoad(img, path);
//                }
//            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_left) {
//            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
//            return true;
//        }
//        if (id == R.id.action_right) {
//            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


