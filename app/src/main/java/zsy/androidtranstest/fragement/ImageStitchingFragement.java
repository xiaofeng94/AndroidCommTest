package zsy.androidtranstest.fragement;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Inflater;

import zsy.androidtranstest.R;
import zsy.androidtranstest.view.SildeCutListView;

/**
 * Created by zsy on 16/6/15.
 */
public class ImageStitchingFragement extends Fragment {

    private Button submit;
    private Button addIamge;
    private SildeCutListView imagesListView;
    List<String> paths;
    private MyAdapter adapter;

    static final int ANIMATION_DURATION = 200;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragement_image_stitching,container,false);
        submit = (Button)view.findViewById(R.id.submit_stitching);
        addIamge = (Button)view.findViewById(R.id.add_stitching);
        imagesListView = (SildeCutListView)view.findViewById(R.id.lv_iamges);
        paths = new ArrayList<>();

        addIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"paths size:" + paths.size(),Toast.LENGTH_SHORT);
                Log.v("paths size:",""+paths.size());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "submit images!", Toast.LENGTH_SHORT);
            }
        });

        paths.add("test");
        paths.add("test");
        paths.add("test");
        paths.add("test");
        paths.add("test");
        paths.add("test");

        adapter = new MyAdapter(getActivity(),R.layout.lv_item_stitching,R.id.tv1,paths);
        imagesListView.setAdapter(adapter);
        imagesListView.setRemoveListener(new SildeCutListView.RemoveListener() {
            @Override
            public void removeItem(SildeCutListView.RemoveDirection direction, int position) {
//                deleteCell(adapter.getItem(position));

                View itemView = imagesListView.getClickView();
                deleteCell(itemView, position);

//                adapter.remove(adapter.getItem(position));
                switch (direction) {
                    case RIGHT:
                        Toast.makeText(getActivity(), "向右删除  " + position, Toast.LENGTH_SHORT).show();
                        break;
                    case LEFT:
                        Toast.makeText(getActivity(), "向左删除  " + position, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }

            }
        });

        return view;
    }

    private void deleteCell(final View v, final int index) {
        if (v != null) {
            Animation.AnimationListener al = new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {
                    paths.remove(index);


//                    ((MyAdapter)adapter).getPathsList().remove(index);
//                    Log.v("getPathsSize", ((MyAdapter) adapter).getPathsList().size() + "");

//                    ViewHolder vh = (ViewHolder) v.getTag();
//                    vh.needInflate = true;
                    Log.v("RemovePath",""+paths.size());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            };

            collapse(v, al);
            Log.v("Remove", "" + paths.size());
        }
    }

    private void collapse(final View v, Animation.AnimationListener al) {
        final int initialHeight = v.getMeasuredHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                }
                else {
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (al!=null) {
            anim.setAnimationListener(al);
        }
        anim.setDuration(ANIMATION_DURATION);
        v.startAnimation(anim);
    }

    public class MyAdapter extends ArrayAdapter<String> {
        private int mResourceId;
        private List<Integer> paths = new ArrayList<>();

        public List<Integer> getPathsList(){
            return paths;
        }
        public MyAdapter(Context context, int resourceId,
                                  int textViewResourceId, List<String>  words) {
            super(context, resourceId, textViewResourceId, words);
            this.mResourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = ImageStitchingFragement.this.getActivity().getLayoutInflater();
            if(view == null) {
                view = inflater.inflate(mResourceId, null);
            }
            ImageView image = (ImageView)view.findViewById(R.id.iv_imag_stitching);

            return view;
        }
    }
}
