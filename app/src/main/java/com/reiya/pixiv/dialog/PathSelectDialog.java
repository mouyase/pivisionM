package com.reiya.pixiv.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.adapter.PathAdapter;

import java.io.File;

/**
 * Created by Administrator on 2015/12/31 0031.
 */
public class PathSelectDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_path, null);
        String oldPath = Environment.getExternalStorageDirectory() + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("path", "/MyPictures/");
        File path  = new File(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.key_path), oldPath));
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        editText.setText(path.getPath());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final PathAdapter adapter = new PathAdapter(getActivity(), path, PathAdapter.getFolders(path));
        adapter.setListener(new PathAdapter.OnSelectListener() {
            @Override
            public void onSelect(File file) {
                editText.setText(file.getPath());
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.up();
            }
        });
        builder.setTitle(getString(R.string.path_to_save_pic))
                .setView(view)
                .setPositiveButton(getString(R.string.positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString(getString(R.string.key_path), editText.getText().toString());
                        editor.apply();
                    }
                })
                .setNegativeButton(R.string.negative, null);
        return builder.create();
    }
}
