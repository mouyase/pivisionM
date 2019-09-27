package com.reiya.pixiv.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/23 0023.
 */
public class IO {
    private static final String imageCachePath = "/data/data/com.reiya.pixive/cache/image_manager_disk_cache/";
    private static final String gifCachePath = "/data/data/com.reiya.pixive/cache/gif_cache/";

    private static long getFolderSize(File f) {
        long size = 0;
        if (!f.exists()) {
            return 0;
        }
        if (f.isDirectory() && f.listFiles() != null) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static String getFormattedSize(double size) {
        if (size / 1024 < 1) {
            return String.format("%.2f", size) + "B";
        }
        size /= 1024;
        if (size / 1024 * 1024 < 1) {
            return String.format("%.2f", size) + "KB";
        }
        size /= 1024;
        if (size / 1024 < 1) {
            return String.format("%.2f", size) + "MB";
        }
        size /= 1024;
        return String.format("%.2f", size) + "GB";
    }

    public static String getImageCacheSize() {
        double size = getFolderSize(new File(imageCachePath)) + getFolderSize(new File(gifCachePath));
        return getFormattedSize(size);
    }

    public static double getImageCacheSizeMB() {
        return (double) (getFolderSize(new File(imageCachePath)) + getFolderSize(new File(gifCachePath))) / (1024 * 1024);
    }

    public static void save(File oldFile, File newFile) {
        try {
            int read;
            if (oldFile.exists()) { //文件存在时
                InputStream is = new FileInputStream(oldFile); //读入原文件
                FileOutputStream fos = new FileOutputStream(newFile);
                byte[] buffer = new byte[2048];
                while ((read = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, read);
                }
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getTemFile(File file) {
        try {
            int read;
            if (file.exists()) { //文件存在时
                InputStream is = new FileInputStream(file); //读入原文件
                FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/tem.jpg");
                byte[] buffer = new byte[2048];
                while ((read = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, read);
                }
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(Environment.getExternalStorageDirectory() + "/tem.jpg");
    }

    public static List<Bitmap> getBitmapList(int id) {
        List<Bitmap> bitmaps = new ArrayList<>();
        File path = new File(gifCachePath + id + "/");
        if (!path.exists()) {
            return bitmaps;
        }
        for (int i = 0; i < 50; i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(path + "/" + String.format("%06d", i) + ".jpg");
            if (bitmap != null) {
                bitmaps.add(bitmap);
            } else {
                return bitmaps;
            }
        }
        return bitmaps;
    }

    public static String getGifCachePath() {
        return gifCachePath;
    }

    public static void ClearGifCache() {
        deleteDirectory(gifCachePath);
    }

    private static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (File file : files) {
            if (file.isFile()) {
                //删除子文件
                flag = file.delete();
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }
}
