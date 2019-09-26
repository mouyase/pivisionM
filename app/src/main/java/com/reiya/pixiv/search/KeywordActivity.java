package com.reiya.pixiv.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.reiya.pixiv.adapter.SearchAdapter;
import com.reiya.pixiv.base.BaseApplication;

import java.util.Arrays;

import tech.yojigen.pivisionm.R;

public class KeywordActivity extends AppCompatActivity implements View.OnClickListener {
    private SearchAdapter mSearchAdapter;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_grey_24px);

        findViewById(R.id.btnSearch).setOnClickListener(this);
        ListView listView = (ListView) findViewById(R.id.listView);

        String[] history = BaseApplication.getHistory();
        mSearchAdapter = new SearchAdapter(this, Arrays.asList(history));
        mSearchAdapter.getFilter().filter("");
        listView.setAdapter(mSearchAdapter);
        mSearchAdapter.setOnTextSelected(new SearchAdapter.OnTextSelected() {
            @Override
            public void onTextSelected(String s) {
                search(s);
            }
        });

        mEditText = (EditText) findViewById(R.id.editText);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearchAdapter.getFilter().filter(s);
                mSearchAdapter.notifyDataSetChanged();
            }
        });
        String text = getIntent().getStringExtra("text");
        mEditText.setText(text);
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    trySearch();
                    return true;
                }
                return false;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mEditText, 0);
            }
        }, 500);
    }

    private void trySearch() {
        String s = mEditText.getText().toString();
        if (s.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.please_input_keyword, Toast.LENGTH_SHORT).show();
        } else {
            search(s);
        }
    }

    private void search(String s) {
        Intent intent = new Intent();
        intent.putExtra("text", s);
        setResult(SearchActivity.RESULT_KEYWORD, intent);
        finish();
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                trySearch();
                break;
        }
    }
}
