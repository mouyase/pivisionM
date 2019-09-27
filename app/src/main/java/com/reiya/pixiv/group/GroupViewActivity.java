package com.reiya.pixiv.group;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.util.ItemOperation;

import tech.yojigen.pivisionm.R;

public class GroupViewActivity extends AppCompatActivity implements View.OnClickListener {
    private Work mWork;
    private int index = 0;
    private TextView tvPage;
    private View sheet;
    private boolean showSheet = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
//        tvPage = (TextView) findViewById(R.id.tvPage);
//        ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
//        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
//        TextView tvUsername = (TextView) findViewById(R.id.tvName);
//        sheet = findViewById(R.id.sheet);
//
//        findViewById(R.id.btnBrowser).setOnClickListener(this);
//        findViewById(R.id.btnFav).setOnClickListener(this);
//        findViewById(R.id.btnInfo).setOnClickListener(this);
//        findViewById(R.id.btnSave).setOnClickListener(this);
//        findViewById(R.id.btnTag).setOnClickListener(this);
//        findViewById(R.id.btnZoom).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);

        mWork = getIntent().getParcelableExtra("work");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String s = (position + 1) + "/" + mWork.getPageCount();
//                tvPage.setText(s);
                index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        GroupPagerAdapter adapter = new GroupPagerAdapter(getSupportFragmentManager(), mWork.getPageCount());
        viewPager.setAdapter(adapter);

        String s = "1/" + mWork.getPageCount();
//        tvPage.setText(s);
//
//        sheet.setVisibility(View.VISIBLE);
//
//        ImageLoader.loadImage(this, mWork.getImageUrl(2))
//                .cacheResult()
//                .fitCenter()
//                .load(ivProfile);
//
//        tvTitle.setText(mWork.getTitle());
//        tvUsername.setText(mWork.getUser().getName());

        index = getIntent().getIntExtra("index", 0);
        viewPager.setCurrentItem(index);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFav:
                ItemOperation.addBookmark(GroupViewActivity.this, mWork);
                break;
            case R.id.btnSave:
                ItemOperation.save(GroupViewActivity.this, mWork, index);
                break;
//            case R.id.btnInfo:
//                ItemOperation.showDetailDialog(GroupViewActivity.this, mWork);
//                break;
//            case R.id.btnTag:
//                ItemOperation.showTagsDialog(GroupViewActivity.this, mWork);
//                break;
//            case R.id.btnBrowser:
//                ItemOperation.browser(GroupViewActivity.this, mWork);
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
//                                ItemOperation.share(GroupViewActivity.this, mWork);
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
//                Intent intent = new Intent(getApplicationContext(), ZoomActivity.class);
//                intent.putExtra("url", mWork.getImageUrl(3, index));
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//                break;
        }
    }

    class GroupPagerAdapter extends FragmentPagerAdapter {
        //        private String url;
//        private String urlBig;
        private final int count;

        public GroupPagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.count = count;
//            this.url = url;
//            this.urlBig = urlBig;
        }

        @Override
        public Fragment getItem(int position) {
            GroupFragment fragment = (GroupFragment) GroupFragment.newInstance(position, mWork.getImageUrl(2, position));
//            fragment.setOnClick(new Runnable() {
//                @Override
//                public void run() {
//                    if (showSheet) {
//                        Animater.fadeOut(sheet);
//                        showSheet = false;
//                    } else {
//                        Animater.fadeIn(sheet);
//                        showSheet = true;
//                    }
//                }
//            });
            return fragment;
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
