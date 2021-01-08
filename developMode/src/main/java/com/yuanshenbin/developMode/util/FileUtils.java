package com.yuanshenbin.developMode.util;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

/**
 * 文件工具包
 *
 * @author back
 * @version landingtech_v1
 */
public class FileUtils {

    /**
     * 判断手机是否有SD卡
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return !TextUtils.isEmpty(status) && status.equals(Environment.MEDIA_MOUNTED);
    }

    public static String getRootFilePath() {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
        } else {
            return Environment.getDataDirectory() + "/data/"; // filePath:
        }
    }

    /**
     * SD卡路径
     */
    public static String getSaveFilePath(Context context) {
        if (hasSDCard()) {
            return getRootFilePath() + context.getPackageName() + "/files/";
        } else {
            return getRootFilePath() + context.getPackageName() + "/files/";
        }
    }

    public static void shareFile(Context context, File file) {
        if (null != file && file.exists()) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_STREAM, FileProvider7.getUriForFile(context, file));
            share.setType(getMimeType(file.getAbsolutePath()));//此处可发送多种文件
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(share, "错误日志"));
        } else {
            Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
        }
    }

    // 根据文件后缀名获得对应的MIME类型。
    private static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }
}
