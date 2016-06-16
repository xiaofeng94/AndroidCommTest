package zsy.androidtranstest.fragement;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import zsy.androidtranstest.R;

/**
 * Created by zsy on 16/6/16.
 */
public class NewOldImageFragment extends Fragment {
    private ImageView oldImage;
    private ImageView newImage;
    private String oldImagePath;
    private String newImagePath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_new_old, container, false);
        oldImage = (ImageView)view.findViewById(R.id.iv_old_image);
        newImage = (ImageView)view.findViewById(R.id.iv_new_image);

        oldImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choose picture
            }
        });
        newImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choose picture
            }
        });


        return view;
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

}