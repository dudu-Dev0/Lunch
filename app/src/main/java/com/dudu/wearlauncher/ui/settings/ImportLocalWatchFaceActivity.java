package com.dudu.wearlauncher.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.core.net.UriKt;
import com.blankj.utilcode.util.*;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.model.WatchFaceInfo;
import com.dudu.wearlauncher.ui.BaseActivity;
import com.dudu.wearlauncher.utils.ILog;
import com.google.android.material.button.MaterialButton;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static com.dudu.wearlauncher.model.WatchFace.watchFaceFolder;

public class ImportLocalWatchFaceActivity extends BaseActivity {
    File tempWatchFace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_local_watchface);
        
        String zipPath = getRealPathFromURI(this,getIntent().getParcelableExtra("zip_uri"));

        ILog.w("zipPath:"+zipPath);
        Toast.makeText(this,zipPath,Toast.LENGTH_SHORT).show();

        try {
            if (zipPath == null||!new File(zipPath).exists()) {
                throw new IOException("无法获取文件");
            } else{
                try {
                    String md5 = EncryptUtils.encryptMD5File2String(new File(zipPath));
                    tempWatchFace = new File(getCacheDir()+"/"+md5);
                    ZipUtils.unzipFile(new File(zipPath),tempWatchFace);
                    JSONObject manifest = new JSONObject(FileIOUtils.readFile2String(tempWatchFace.getAbsolutePath()+"/manifest.json"));
                    WatchFaceInfo data = new WatchFaceInfo();
                    data.name = manifest.getString("name");
                    data.displayName = manifest.getString("displayName");
                    data.packageName = manifest.getString("packageName");
                    data.author = manifest.getString("author");
                    data.versionCode = manifest.getInt("versionCode");
                    ((TextView)findViewById(R.id.wf_name_txt)).setText(data.displayName);
                    ((TextView)findViewById(R.id.wf_author_txt)).setText(data.author);
                    ((TextView)findViewById(R.id.wf_version_txt)).setText(data.versionCode);
                    findViewById(R.id.apply_btn).setOnClickListener(v->{
                        v.setEnabled(false);
                        ((MaterialButton)v).setText("请稍候...");
                        FileUtils.copy(tempWatchFace,new File(watchFaceFolder));
                        FileUtils.rename(watchFaceFolder+"/"+md5,data.name);
                        Toast.makeText(this,"导入完成",Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } catch (IOException e) {
                    throw new RuntimeException("解压表盘失败");
                } catch (JSONException e){
                    throw new JSONException("未读取到表盘相关配置");
                }
            }
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            ILog.e(e.getMessage());
        }

    }
    /**
     * Uri2path
     */
    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
