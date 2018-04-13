package io.github.ylbfdev.slideshowview.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @author YLBF
 * @version 版本号 1.0.0.0
 * <p>类说明 文件缓存工具类 缓存图片到SD卡缓存目录CacheDir</p>
 */
@SuppressLint("NewApi")
public class FileCache {
    private File cacheDir;

    public FileCache(Context context) {
        // 判断外存SD卡挂载状态，如果挂载正常，创建SD卡缓存文件夹
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(Environment.getExternalStorageDirectory()+"/ALWorkInfo/Cache/");// 输出结果：storage/emulated/0/ALWorkInfo/Cache
            //cacheDir = new File(context.getExternalCacheDir().getParent(),"Cache"); //storage/emulated/0/Android/data/com.hewaiming.ALWorkInfo/Cache
         
        } else {
            // SD卡挂载不正常，获取本地缓存文件夹（应用包所在目录）
            cacheDir = context.getCacheDir();
        }
        creatDir();
    }

    /**
     * 创建文件夹
     *
     * @return boolean  cacheDir.exists()
     */
    public boolean creatDir() {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
            return true;
        } else {
            return false;
        }
    }

    public File getFile(String url) {
        String fileName = String.valueOf(url.hashCode());
        creatDir();
        File file = new File(cacheDir, fileName);
        return file;
    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        for (File f : files)
            f.delete();
    }

    public String getCacheSize() {
        long size = 0;
        if (cacheDir.exists()) {
            File[] files = cacheDir.listFiles();
            for (File f : files) {
                size += f.length();
            }
        }
        String cacheSize = String.valueOf(size / 1024 / 1024) + "M";
        return cacheSize;
    }

}
