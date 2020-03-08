package com.mingrisoft.mrshop.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 作者： LYJ
 * 功能： 文件操作类
 * 创建日期： 2017/5/11
 */

public class FileUtils {
    private static final String fileName = "MRKJ_Shop";
    private static final String prefix = "MRKJ_IMAGE_";
    private static final String suffix = ".jpg";

    /**
     * 创建File对象
     * @return
     */
    public static File getFileFolder(){
        File file = new File(Environment.getExternalStorageDirectory(),fileName);
        if (!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    /**
     * 保存文件到本地
     * @param bitmap
     * @param fileName
     * @return
     */
    public static String saveBitmapToLoad(Bitmap bitmap , String fileName){
        File file = new File(getFileFolder(),prefix+fileName+suffix);
        if (file.exists()){
            return file.getPath();
        }
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (null == out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getPath();
    }
}
