package com.reiya.pixiv.group;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reiya.pixiv.image.ImageLoader;

/**
 * Created by Administrator on 2016/1/16 0016.
 */
public class GroupFragment extends Fragment {
    private static final String ARGS_PAGE = "args_page";
    private static final String URL = "url";
    private static final String URL_BIG = "url_big";
    private int page;
    private String url;
    private Runnable onClick;
//    private Runnable onLongClick;

    public static Fragment newInstance(int page, String url) {
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);
        args.putString(URL, url);
//        args.putString(URL_BIG, urlBig);
        GroupFragment fragment = new GroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARGS_PAGE);
        url = getArguments().getString(URL);
//        urlBig = getArguments().getString(URL_BIG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ImageView iv = new ImageView(getActivity().getApplicationContext());
        if (!url.equals("")) {
            ImageLoader.loadImage(getActivity(), url)
                    .fitCenter()
                    .thumbnail(0.1f)
                    .load(iv);
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick != null) {
                    onClick.run();
                }
            }
        });
//        iv.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (onLongClick != null) {
//                    onLongClick.run();
//                }
//                return true;
//            }
//        });
        return iv;
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

//    public void setOnLongClick(Runnable onLongClick) {
//        this.onLongClick = onLongClick;
//    }

}
