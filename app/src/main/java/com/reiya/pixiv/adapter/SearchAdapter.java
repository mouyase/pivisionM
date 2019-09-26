package com.reiya.pixiv.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.base.BaseApplication;
import com.reiya.pixiv.profile.ProfileActivity;
import com.reiya.pixiv.work.ViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/20 0020.
 */
public class SearchAdapter extends BaseAdapter implements Filterable {
    private final ArrayFilter filter = new ArrayFilter();
    private List<String> strings;
    private List<Item> items;
    private final Context context;
    private final Activity activity;
    private OnTextSelected onTextSelected;
    private boolean close = false;

    public SearchAdapter(Activity activity, List<String> list) {
        this.context = activity;
        this.activity = activity;
        this.strings = list;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : (close ? 0 : items.size());
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if(convertView == null){
            view = View.inflate(context, R.layout.item_search, null);
            holder = new ViewHolder();
            holder.tv1 = (TextView) view.findViewById(R.id.textView1);
            holder.tv2 = (TextView) view.findViewById(R.id.textView2);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        final Item item = items.get(position);

        holder.tv1.setText(item.text);
        String type = "";
        switch (item.type) {
            case 1:
                type = "作品id";
                break;
            case 2:
                type = "画师id";
                break;
            case -1:
                type = "清除搜索历史";
                break;
        }
        holder.tv2.setText(type);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (item.type) {
                    case -1:
                        strings = new ArrayList<>();
                        items = new ArrayList<>();
                        notifyDataSetChanged();
                        BaseApplication.writeHistory(new String[]{""});
                        break;
                    case 0:
                        if (onTextSelected != null) {
                            onTextSelected.onTextSelected(item.text);
                        }
                        break;
                    case 1:
                        intent = new Intent(context, ViewActivity.class);
                        intent.putExtra("id", Integer.parseInt(item.text));
                        context.startActivity(intent);
                        activity.finish();
                        break;
                    case 2:
                        intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra("id", Integer.parseInt(item.text));
                        context.startActivity(intent);
                        activity.finish();
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<String> unFiltered = new ArrayList<>(strings);
            if (constraint == null || constraint.length() == 0) {
                List<Item> newValues = new ArrayList<>();
                for (String text : unFiltered) {
                    if (text != null) {
                        Item item = new Item();
                        item.text = text;
                        item.type = 0;
                        newValues.add(item);
                    }
                }
                if (newValues.size() > 0) {
                    Item item = new Item();
                    item.text = "";
                    item.type = -1;
                    newValues.add(item);
                }
                results.values = newValues;
                results.count = newValues.size();
            } else {
                String s = constraint.toString().toLowerCase();
                List<Item> newValues = new ArrayList<>();
                try {
                    Integer.parseInt(s);
                    Item item1 = new Item();
                    item1.text = s;
                    item1.type = 1;
                    Item item2 = new Item();
                    item2.text = s;
                    item2.type = 2;
                    newValues.add(item1);
                    newValues.add(item2);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                for (String text : unFiltered) {
                    if (text != null) {
                        if (text.startsWith(s)) {
                            Item item = new Item();
                            item.text = text;
                            item.type = 0;
                            newValues.add(item);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items = (List<Item>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public void setStrings(List<String> list) {
        this.strings = list;
        notifyDataSetChanged();
    }

    static class Item {
        String text;
        int type;
    }

    static class ViewHolder{
        public TextView tv1;
        public TextView tv2;
    }

    public interface OnTextSelected {
        void onTextSelected(String s);
    }

    public void setOnTextSelected(OnTextSelected onTextSelected) {
        this.onTextSelected = onTextSelected;
    }

    public void open() {
        close = false;
        notifyDataSetChanged();
    }

    public void close() {
        close = true;
        notifyDataSetChanged();
    }
}
