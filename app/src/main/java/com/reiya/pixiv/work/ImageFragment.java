package com.reiya.pixiv.work;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reiya.pixiv.adapter.CommentAdapter;
import com.reiya.pixiv.adapter.ImageAdapter;
import com.reiya.pixiv.base.BaseFragment;
import com.reiya.pixiv.bean.Comment;
import com.reiya.pixiv.bean.Tag;
import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.comment.CommentActivity;
import com.reiya.pixiv.dialog.BookmarkAddDialog;
import com.reiya.pixiv.dialog.MenuDialog;
import com.reiya.pixiv.gif.GifActivity;
import com.reiya.pixiv.grid.GridActivity;
import com.reiya.pixiv.image.ImageLoader;
import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;
import com.reiya.pixiv.profile.ProfileActivity;
import com.reiya.pixiv.search.SearchActivity;
import com.reiya.pixiv.util.ItemOperation;
import com.reiya.pixiv.util.UserOperation;
import com.reiya.pixiv.util.Value;
import com.reiya.pixiv.view.WorkGridLayoutManager;
import com.reiya.pixiv.zoom.ZoomActivity;

import java.util.ArrayList;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;
import rx.Subscriber;
import tech.yojigen.pivisionm.R;

/**
 * Created by Administrator on 2015/12/2 0002.
 */
public class ImageFragment extends BaseFragment<WorkPresenter> implements WorkContract.View, BookmarkAddDialog.OnBookmarkChangedCallback {
    private static final String PAGE = "mPage";
    //    private static final String ID = "id";
    private static final String WORK = "work";
    //    private static int mIndex = 0;
    private int mPage;
    //    private int id;
    private Work mWork;
    private boolean isCurrent = false;
    private WorkPresenter mPresenter;

    private boolean mHasRequestedExtra = false;
    private CheckBox mCbFav;
    private ImageView mCbSave;
    //    private Runnable onLongClick;
//    private OnItemLoadedListener onItemLoadedListener;
//    private String content = "";

    public static Fragment newInstance(int page, Work work) {
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        args.putParcelable(WORK, work);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(PAGE);
        mWork = getArguments().getParcelable(WORK);
        mPresenter = new WorkPresenter();
        mPresenter.setView(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_work, container, false);
        ScrollView scrollView = view.findViewById(R.id.scrollView);
        scrollView.setOnTouchListener((v, event) -> {
            if (!mHasRequestedExtra && v.getScrollY() > 0) {
                mHasRequestedExtra = true;
                NetworkRequest.getComment(mWork.getId())
                        .subscribe(new Subscriber<HttpService.CommentResponse>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(HttpService.CommentResponse commentResponse) {
                                List<Comment> comments = commentResponse.getComments();
                                LinearLayout layout = (LinearLayout) view.findViewById(R.id.commentLayout);
                                layout.removeAllViews();
//                                    int n = Math.min(comments.size(), 5);
//                                    for (int i = 0; i < n; i++) {
//                                        View item = inflater.inflate(R.layout.item_comment, layout, false);
//                                        Comment comment = comments.get(i);
//                                        ImageLoader.loadImage(getActivity(), comment.getUser().getMediumImageUrl())
//                                                .cacheResult()
//                                                .centerCrop()
//                                                .load((ImageView) item.findViewById(R.id.ivProfile));
//                                        ((TextView) item.findViewById(R.id.tvName)).setText(comment.getUser().getName());
//                                        ((TextView) item.findViewById(R.id.tvText)).setText(comment.getComment());
//                                        ((TextView) item.findViewById(R.id.tvTime)).setText(comment.getDate());
//                                        layout.addView(item);
//                                    }
                                RecyclerView recyclerView = new RecyclerView(getActivity());
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setNestedScrollingEnabled(false);
                                recyclerView.setAdapter(new CommentAdapter(getActivity(), comments.subList(0, Math.min(5, comments.size()))));
                                layout.addView(recyclerView);
                                if (comments.size() > 5) {
                                    View btnMore = inflater.inflate(R.layout.item_more, layout, false);
                                    btnMore.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), CommentActivity.class);
                                            intent.putExtra("id", mWork.getId());
                                            getActivity().startActivity(intent);
                                        }
                                    });
                                    layout.addView(btnMore);
                                }
                            }
                        });
                NetworkRequest.getRelatedWorks(mWork.getId())
                        .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("getRelatedWorks", "err:" + e.getLocalizedMessage());
                            }

                            @Override
                            public void onNext(HttpService.IllustListResponse illustListResponse) {
                                List<Work> works = illustListResponse.getWorks();
                                LinearLayout layout = (LinearLayout) view.findViewById(R.id.RelatedWorksLayout);
                                layout.removeViewAt(1);
                                RecyclerView recyclerView = new RecyclerView(getActivity());
                                recyclerView.setLayoutManager(new WorkGridLayoutManager(getActivity()));
                                recyclerView.setAdapter(new ImageAdapter(getActivity(), works));
                                layout.addView(recyclerView);
                            }
                        });
            }
            return false;
        });
        final ImageView iv = view.findViewById(R.id.iv);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv.getLayoutParams();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        params.height = display.getHeight() - statusBarHeight;
        iv.setLayoutParams(params);
        loadImage(iv);

        ImageLoader.loadImage(getActivity(), mWork.getUser().getMediumImageUrl())
                .fitCenter()
                .load(view.findViewById(R.id.ivProfile));
        ImageLoader.loadImage(getActivity(), mWork.getUser().getMediumImageUrl())
                .fitCenter()
                .load(view.findViewById(R.id.ivProfile2));
        ((TextView) view.findViewById(R.id.tvTitle)).setText(mWork.getTitle());
        ((TextView) view.findViewById(R.id.tvName)).setText(mWork.getUser().getName());
        ((TextView) view.findViewById(R.id.tvName2)).setText(mWork.getUser().getName());
        mCbFav = view.findViewById(R.id.btnFav);
        mCbSave = view.findViewById(R.id.btnSave);
        mCbSave.setOnClickListener(v -> ItemOperation.save(getActivity(), mWork));
        mCbFav.setChecked(mWork.isIsBookmarked());
        mCbFav.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mWork.isIsBookmarked() != isChecked) {
                mWork.setIsBookmarked(isChecked);
                if (isChecked) {
                    ItemOperation.addBookmark(getActivity(), mWork.getId(), "public");
//                    ItemOperation.addBookmark(getActivity(), mWork);
                    if (PreferenceManager.getDefaultSharedPreferences(getActivity())
                            .getBoolean(getString(R.string.key_show_toast_long_click_favorite), true)) {
                        Toast.makeText(getActivity(), R.string.toast_long_click_private, Toast.LENGTH_LONG).show();
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                                .putBoolean(getString(R.string.key_show_toast_long_click_favorite), false)
                                .apply();
                    }
                } else {
                    ItemOperation.removeBookmark(getActivity(), mWork.getId());
                }
            }
        });
        mCbFav.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BookmarkAddDialog dialog = new BookmarkAddDialog();
                dialog.setId(mWork.getId());
                dialog.setBookmarked(mCbFav.isChecked());
                dialog.setOnBookmarkChangedCallback(ImageFragment.this);
                dialog.show(getActivity().getSupportFragmentManager(), BookmarkAddDialog.class.getName());
//                if (!cbFav.isChecked()) {
//                    ItemOperation.addBookmark(getActivity(), mWork.getId(), "private");
//                    mWork.setIsBookmarked(true);
//                    cbFav.setChecked(true);
//                }
                return true;
            }
        });
        view.findViewById(R.id.relativeLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("id", mWork.getUser().getId());
                getActivity().startActivity(intent);
            }
        });

        View ivDyn = view.findViewById(R.id.ivDyn);
        if (mWork.isDynamic()) {
            ivDyn.setVisibility(View.VISIBLE);
        } else {
            ivDyn.setVisibility(View.GONE);
        }
        int page = mWork.getPageCount();
        String s = "";
//        ImageButton btnView = (ImageButton) view.findViewById(R.id.btnZoom);
        if (page > 1) {
            s = page + "P";
//            btnView.setImageResource(R.drawable.ic_layers_white_36px);
        } else {
//            btnView.setImageResource(R.drawable.ic_zoom_in_white_36px);
        }
        ((TextView) view.findViewById(R.id.tvPage)).setText(s);
        ((TextView) view.findViewById(R.id.tvCaption)).setText(Html.fromHtml(mWork.getCaption()));
        ((TextView) view.findViewById(R.id.tvInfo)).setText(mWork.getTime());

        TagGroup tagGroup = (TagGroup) view.findViewById(R.id.tagLayout);
        List<String> tags = new ArrayList<>();
        for (Tag tag : mWork.getTags()) {
            tags.add(tag.getName());
        }
        tagGroup.setTags(tags);
        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("tag", tag);
                getActivity().startActivity(intent);
            }
        });

        view.findViewById(R.id.userInfoLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("id", mWork.getUser().getId());
                getActivity().startActivity(intent);
            }
        });
        final CheckBox cbFollow = (CheckBox) view.findViewById(R.id.btnFollow);
        cbFollow.setChecked(mWork.getUser().isFollowed());
        cbFollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mWork.getUser().isFollowed() != isChecked) {
                    mWork.getUser().setFollowed(isChecked);
                    if (isChecked) {
                        UserOperation.favorite(getActivity(), mWork.getUser().getId(), "public");
//                    ItemOperation.addBookmark(getActivity(), mWork);
                        if (PreferenceManager.getDefaultSharedPreferences(getActivity())
                                .getBoolean(getString(R.string.key_show_toast_long_click_follow), true)) {
                            Toast.makeText(getActivity(), R.string.toast_long_click_private, Toast.LENGTH_LONG).show();
                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                                    .putBoolean(getString(R.string.key_show_toast_long_click_follow), false)
                                    .apply();
                        }
                    } else {
                        UserOperation.outFavorite(getActivity(), mWork.getUser().getId());
                    }
                }
            }
        });
        cbFollow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!cbFollow.isChecked()) {
                    UserOperation.favorite(getActivity(), mWork.getUser().getId(), "private");
                    mWork.getUser().setFollowed(true);
                    cbFollow.setChecked(true);
                }
                return true;
            }
        });

//        content = new RecordDAO(getActivity()).getContent(id);
//        if (work == null) {
//            request(iv, true);
//        } else {
//            loadImage(work, iv);
////            Log.e("cache", "mPage:" + mPage + "   index:" + index);
//            if (mPage == index) {
////                Log.e("cache", "update");
//                new RecordDAO(getContext()).updateRecord(work.getId(), System.currentTimeMillis());
//            }
//        }


        return view;
    }

//    private void request(final ImageView iv, final boolean retry) {
//        HttpClient.requestWithBearer(String.format(Value.URL_ILLUST_DETAIL, id), new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                if (getActivity() == null) {
//                    return;
//                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getContext(), R.string.fail_to_load, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                if (getActivity() == null) {
//                    return;
//                }
//                content = response.body().string();
//                work = JsonReader.getItem(content);
//                if (work == null) {
//                    if (retry) {
//                        request(iv, false);
//                    } else {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getContext(), R.string.invalid_object, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                    return;
//                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadImage(work, iv);
//                    }
//                });
////                Log.e("cache", "mPage:" + mPage + "   index:" + index);
//                if (mPage == index) {
////                    Log.e("cache", "add");
//                    new RecordDAO(getContext()).addRecord(work.getId(), content, System.currentTimeMillis());
//                }
//            }
//        });
//    }

    private void loadImage(ImageView iv) {
//        if (onItemLoadedListener != null) {
//            onItemLoadedListener.onItemLoaded(item, mPage);
//        }
        String url = mWork.getImageUrl(2);
        if (!url.equals("")) {
            ImageLoader.loadImage(getActivity(), url)
                    .fitCenter()
                    .thumbnail(0.1f)
                    .load(iv);
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWork.isDynamic()) {
                    Intent intent = new Intent(getActivity(), GifActivity.class);
                    intent.putExtra("work", mWork);
                    startActivity(intent);
                } else if (mWork.getPageCount() == 1) {
                    Intent intent = new Intent(getActivity(), ZoomActivity.class);
                    intent.putExtra("url", mWork.getImageUrl(3));
                    startActivity(intent);
                    getActivity().overridePendingTransition(0, 0);
                } else {
                    Intent intent = new Intent(getActivity(), GridActivity.class);
                    intent.putExtra("work", mWork);
                    startActivity(intent);
                    getActivity().overridePendingTransition(0, 0);
                }
            }
        });
        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MenuDialog menuDialog = new MenuDialog();
                menuDialog.setListener(new String[]{"复制id", "分享"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                int id = mWork.getId();
                                ClipboardManager cb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData data = ClipData.newPlainText("id", String.valueOf(id));
                                cb.setPrimaryClip(data);
                                Toast.makeText(getActivity(), getString(R.string.clip_info_id), Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                String text = mWork.getTitle() + " / " + mWork.getUser().getName() + " #pixiv " + Value.URL_ILLUST_PAGE + mWork.getId();
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                                sendIntent.setType("text/plain");
                                startActivity(sendIntent);
                                break;
                        }
                    }
                });
                menuDialog.show(getFragmentManager(), "menu");
                return true;
            }
        });
    }

    public int getPage() {
        return mPage;
    }
//
//    public void setOnLongClick(Runnable onLongClick) {
//        this.onLongClick = onLongClick;
//    }

//    public void setOnItemLoadedListener(OnItemLoadedListener onItemLoadedListener) {
//        this.onItemLoadedListener = onItemLoadedListener;
//    }
//
//    public interface OnItemLoadedListener {
//        void onItemLoaded(IllustItem item, int index);
//    }
//

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isCurrent = isVisibleToUser;
        if (mWork != null) {
            if (isCurrent) {
                mPresenter.saveRecord(getActivity(), mWork);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isCurrent) {
            mPresenter.saveRecord(getActivity(), mWork);
        }
    }

    @Override
    public void onChanged(boolean isBookmarked) {
        mWork.setIsBookmarked(isBookmarked);
        mCbFav.setChecked(isBookmarked);
    }
}
