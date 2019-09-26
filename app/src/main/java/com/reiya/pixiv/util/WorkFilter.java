package com.reiya.pixiv.util;

import com.reiya.pixiv.bean.Tag;
import com.reiya.pixiv.bean.Work;

import java.util.List;

/**
 * Created by lenovo on 2016/5/29.
 */

public class WorkFilter {
    private String[] blacklist = {};

    public void setBlacklist(String[] blacklist) {
        this.blacklist = blacklist;
    }

    public List<Work> getFilteredList(List<Work> works) {
        for (int i = 0, l = works.size(); i < l; i++) {
            Work work = works.get(i);
            boolean filter = false;
            for (Tag tag : work.getTags()) {
                for (String word : blacklist) {
                    if (!word.equals("") && tag.getName().contains(word)) {
                        filter = true;
                        break;
                    }
                }
                if (filter) {
                    break;
                }
            }
            if (filter) {
                works.remove(i);
            }
        }
        return works;
    }
}
