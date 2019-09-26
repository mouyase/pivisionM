package com.reiya.pixiv.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/3/19.
 */
public class Task implements Parcelable {
    public final Work work;
    public final int index;

    public Task(Work work, int index) {
        this.work = work;
        this.index = index;
    }

    protected Task(Parcel in) {
        work = in.readParcelable(Work.class.getClassLoader());
        index = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(work, flags);
        dest.writeInt(index);
    }

    public boolean equals(Task task) {
        return work.getId() == task.work.getId() && index == task.index;
    }
}
