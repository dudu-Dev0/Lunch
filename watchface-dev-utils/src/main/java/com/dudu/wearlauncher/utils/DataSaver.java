package com.dudu.wearlauncher.utils;

import com.blankj.utilcode.util.FileIOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.dudu.wearlauncher.model.WatchFace.watchFaceFolder;

public class DataSaver {
    private String watchfaceName;
    private File dataJsonFile;
    private JSONObject dataJson = new JSONObject();

    public DataSaver(String watchfaceName) {
        this.watchfaceName = watchfaceName;
        dataJsonFile = new File(watchFaceFolder + "/" + watchfaceName + "/data.json");
        new Thread(() -> {
            if (!dataJsonFile.exists()) FileIOUtils.writeFileFromString(dataJsonFile, new JSONObject().toString());
            try {
                dataJson = new JSONObject(FileIOUtils.readFile2String(dataJsonFile));
            } catch (JSONException e) {
                ILog.e("are u sure data.json is A JSON FILE???");
            }
        }).start();
    }

    public void put(String key, String value) {
        try {
            dataJson.put(key, value);
        } catch (JSONException e) {
            ILog.e("are u sure data.json is A JSON FILE???");
        }
    }

    public void put(String key, int value) {
        try {
            dataJson.put(key, value);
        } catch (JSONException e) {
            ILog.e("are u sure data.json is A JSON FILE???");
        }
    }

    public void put(String key, double value) {
        try {
            dataJson.put(key, value);
        } catch (JSONException e) {
            ILog.e(e.getMessage());
        }
    }

    public void put(String key, long value) {
        try {
            dataJson.put(key, value);
        } catch (JSONException e) {
            ILog.e(e.getMessage());
        }
    }

    public String get(String key, String defaultValue) {
        try {
            return dataJson.getString(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public int get(String key, int defaultValue) {
        try {
            return dataJson.getInt(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public long get(String key, long defaultValue) {
        try {
            return dataJson.getLong(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public double get(String key, double defaultValue) {
        try {
            return dataJson.getDouble(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    public void apply() {
        new Thread(() -> {
            FileIOUtils.writeFileFromString(dataJsonFile, dataJson.toString());
        }).start();
    }
}
