package com.reiya.pixiv.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.reiya.pixiv.base.BaseApplication;
import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.gif.GifActivity;
import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;
import com.reiya.pixiv.util.ItemOperation;
import com.reiya.pixiv.util.TempData;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import tech.yojigen.pivisionm.R;

public class ViewActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Work> mWorks;
    private int mIndex = 0;
    private TextView tvPage;
    private ImageView ivDyn;
    private ImageView ivProfile;
    private TextView tvTitle;
    private TextView tvUsername;
    private ImageButton btnView;
    private View sheet;
    private ViewPager viewPager;
    private boolean showSheet = true;
    private ImageView btnBack;
    private ImageView btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        tvPage = (TextView) findViewById(R.id.tvPage);
//        ivDyn = (ImageView) findViewById(R.id.ivDyn);
//        ivProfile = (ImageView) findViewById(R.id.ivProfile);
//        tvTitle = (TextView) findViewById(R.id.tvTitle);
//        tvUsername = (TextView) findViewById(R.id.tvName);
//        btnView = (ImageButton) findViewById(R.id.btnZoom);
//        sheet = findViewById(R.id.sheet);
//
//        findViewById(R.id.btnBrowser).setOnClickListener(this);
//        findViewById(R.id.btnFav).setOnClickListener(this);
//        findViewById(R.id.btnInfo).setOnClickListener(this);
//        findViewById(R.id.btnSave).setOnClickListener(this);
//        findViewById(R.id.btnTag).setOnClickListener(this);
//        btnView.setOnClickListener(this);
//        ivDyn.setOnClickListener(this);
//
//        setPresenter(new WorkPresenter());
//        mPresenter.setView(this);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnSave = (ImageView) findViewById(R.id.btnSave);

        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        ((BaseApplication) getApplication()).tryLogin(this, false, null);

        int id = getIntent().getIntExtra("id", 0);
        String data = getIntent().getDataString();
        if (data != null) {
            id = getId(data);
        }

//        mWorks = getIntent().getParcelableArrayListExtra("works");
        mWorks = (List<Work>) TempData.get("works");
        mIndex = getIntent().getIntExtra("position", 0);

        if (mWorks == null) {
            NetworkRequest.getWorkDetailWithBearer(id)
                    .subscribe(new Subscriber<HttpService.WorkResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            failToLoad();
                        }

                        @Override
                        public void onNext(HttpService.WorkResponse workResponse) {
                            showWork(workResponse.getWork());
                        }
                    });
        } else {
            show();
        }

    }

    private void show() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndex = position;
                showInfo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ImagePagerAdapter adapter = new ImagePagerAdapter(getSupportFragmentManager(), mWorks);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mIndex);

//        ivDyn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Work work = mWorks.get(mIndex);
//                Intent intent = new Intent(getApplicationContext(), GifActivity.class);
//                intent.putExtra("id", work.getId());
//                intent.putExtra("work", work);
//                startActivity(intent);
//            }
//        });
//        ivProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Work work = mWorks.get(mIndex);
//                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//                intent.putExtra("id", work.getUser().getId());
//                startActivity(intent);
//            }
//        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                sheet.setVisibility(View.VISIBLE);
//                showInfo(mIndex);
//            }
//        }, 100);

    }

    private void showInfo(int position) {
//        mIndex = position;
//        saveCache();
//        Work work = mWorks.get(mIndex);
//        ImageLoader.loadImage(this, work.getUser().getMediumImageUrl())
//                .cacheResult()
//                .fitCenter()
//                .load(ivProfile);
//        tvTitle.setText(work.getTitle());
//        tvUsername.setText(work.getUser().getName());
//        if (work.isDynamic()) {
//            ivDyn.setVisibility(View.VISIBLE);
//        } else {
//            ivDyn.setVisibility(View.GONE);
//        }
//        int page = work.getPageCount();
//        String s = "";
//        if (page > 1) {
//            s = page + "P";
//            btnView.setImageResource(R.drawable.ic_layers_white_36px);
//        } else {
//            btnView.setImageResource(R.drawable.ic_zoom_in_white_36px);
//        }
//        tvPage.setText(s);
    }
//
//    private void saveCache() {
//        Work work = mWorks.get(mIndex);
//        mPresenter.saveRecord(this, work);
//    }

    private int getId(String string) {
        String[] a = string.replaceAll("[^0-9]", "/").split("/");
        for (String s : a) {
            if (!s.equals("")) {
                return Integer.parseInt(s);
            }
        }
        return 0;
    }

    public void showWork(Work work) {
        if (work == null) {
            Toast.makeText(getApplicationContext(), R.string.invalid_object, Toast.LENGTH_SHORT).show();
        } else {
            mWorks = new ArrayList<>();
            mWorks.add(work);
            show();
        }
    }

    public void failToLoad() {
        Toast.makeText(getApplicationContext(), R.string.fail_to_load, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (mWorks == null) {
            return;
        }
        final Work work = mWorks.get(mIndex);
        switch (v.getId()) {
//            case R.id.btnFav:
//                ItemOperation.addBookmark(ViewActivity.this, work);
//                break;
//            case R.id.btnInfo:
//                ItemOperation.showDetailDialog(ViewActivity.this, work);
//                break;
//            case R.id.btnTag:
//                ItemOperation.showTagsDialog(ViewActivity.this, work);
//                break;
//            case R.id.btnBrowser:
//                ItemOperation.browser(ViewActivity.this, work);
//                PopupMenu popupMenu = new PopupMenu(this, v);
//                popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem i) {
//                        switch (i.getItemId()) {
//                            case R.id.open_in_browser:
//
//                                break;
//                            case R.id.share:
//                                ItemOperation.share(ViewActivity.this, work);
//                                break;
//                        }
//                        return false;
//                    }
//                });
//                popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//                    @Override
//                    public void onDismiss(PopupMenu menu) {
//
//                    }
//                });
//                popupMenu.show();
//                break;
//            case R.id.btnZoom:
//                if (work.isDynamic()) {
//                    Intent intent = new Intent(this, GifActivity.class);
//                    intent.putExtra("work", work);
//                    startActivity(intent);
//                } else if (work.getPageCount() == 1) {
//                    Intent intent = new Intent(getApplicationContext(), ZoomActivity.class);
//                    intent.putExtra("url", work.getImageUrl(3));
//                    startActivity(intent);
//                    overridePendingTransition(0, 0);
//                } else {
//                    Intent intent = new Intent(getApplicationContext(), GridActivity.class);
//                    intent.putExtra("work", work);
//                    startActivity(intent);
//                    overridePendingTransition(0, 0);
//                }
//                break;
            case R.id.ivDyn:
                Intent intent = new Intent(this, GifActivity.class);
                intent.putExtra("work", work);
                startActivity(intent);
                break;
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSave:
                ItemOperation.save(ViewActivity.this, work);
                break;
        }
    }

    public void showButtons() {
        btnBack.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
    }

    public void hideButton() {
        btnBack.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private final List<Work> mWorks;
        private final int count;

        public ImagePagerAdapter(FragmentManager fm, List<Work> works) {
            super(fm);
            this.mWorks = works;
            this.count = mWorks.size();
        }

        @Override
        public Fragment getItem(int position) {
            ImageFragment fragment = (ImageFragment) ImageFragment.newInstance(position, mWorks.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
