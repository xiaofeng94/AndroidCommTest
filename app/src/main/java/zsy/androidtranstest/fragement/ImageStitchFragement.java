package zsy.androidtranstest.fragement;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

import zsy.androidtranstest.R;

/**
 * Created by zsy on 16/6/16.
 */
//new fragement for image stitching
public class ImageStitchFragement extends Fragment {

    private SwipeMenuListView sMListView;
    private List<String> paths;
    private MyAdapter mAdapter;
    private Button addImage;
    private Button submit;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragement_image_stitch,container,false);

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
        paths.add("test");
        paths.add("test");
        paths.add("test");
        paths.add("test");


        mAdapter = new MyAdapter();
        sMListView = (SwipeMenuListView)view.findViewById(R.id.lv_iamges_stitch);
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

        addImage = (Button)view.findViewById(R.id.add_stitch);
        submit = (Button)view.findViewById(R.id.submit_stitch);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paths.add("test");
                mAdapter.notifyDataSetChanged();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"submit:"+paths.size(),Toast.LENGTH_SHORT).show();
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

    /**
     * px2dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    class MyAdapter extends BaseSwipListAdapter {

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
                convertView = View.inflate(getActivity(),R.layout.lv_item_stitch, null);
            }
            ImageView image = (ImageView)convertView.findViewById(R.id.iv_imag_stitch);

            return convertView;
        }


        @Override
        public boolean getSwipEnableByPosition(int position) {
//            if(position % 2 == 0){
//                return false;
//            }
            return true;
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

}
