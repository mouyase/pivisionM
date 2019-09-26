package com.reiya.pixiv.history;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.adapter.ImageAdapter;
import com.reiya.pixiv.bean.Theme;
import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.db.RecordDAO;
import com.reiya.pixiv.dialog.ClearHistoryDialog;
import com.reiya.pixiv.util.Serializer;
import com.reiya.pixiv.view.WorkGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ImageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme());
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new WorkGridLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        List<Work> works = new ArrayList<>();
        List<String> record = new RecordDAO(this).getContent();
        for (String string : record) {
            Work work = (Work) Serializer.deserialize(string, Work.class);
            if (work != null) {
                works.add(work);
            }
        }
        adapter = new ImageAdapter(this, works);
//        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int id, List<Work> works1, int position) {
//                Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
//                intent.putExtra("id", id);
//                intent.putExtra("works", (ArrayList) works1);
//                intent.putExtra("position", position);
//                startActivity(intent);
//            }
//        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.clear_history).setIcon(R.drawable.ic_delete_white_24px).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                ClearHistoryDialog clearHistoryDialog = new ClearHistoryDialog();
                clearHistoryDialog.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clearItems();
                    }
                });
                clearHistoryDialog.show(getSupportFragmentManager(), "ClearHistory");
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
