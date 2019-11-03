package com.reiya.pixiv.group;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(Color.BLACK);
        ViewPager viewPager = findViewById(R.id.viewPager);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);

        mWork = getIntent().getParcelableExtra("work");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        GroupPagerAdapter adapter = new GroupPagerAdapter(getSupportFragmentManager(), mWork.getPageCount());
        viewPager.setAdapter(adapter);

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

        }
    }

    class GroupPagerAdapter extends FragmentPagerAdapter {
        private final int count;

        GroupPagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.count = count;
        }


        @Override
        public Fragment getItem(int position) {
            return GroupFragment.newInstance(position, mWork.getImageUrl(2, position));
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
