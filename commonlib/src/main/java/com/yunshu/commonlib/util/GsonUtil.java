package com.yunshu.commonlib.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Tyrese on 2017/12/19.
 */

public class GsonUtil {

    private Gson gson;

    public GsonUtil() {
        gson = new Gson();
    }

    public <T> T fromJson(String result, Class<T> tClass) throws Exception {
        try {
            return gson.fromJson(result, tClass);
        } catch (Exception e) {
            throw new Exception("返回数据格式出错");
        }
    }

    public <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = gson.fromJson(json, type);

            ArrayList<T> arrayList = new ArrayList<>();
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(new Gson().fromJson(jsonObject, clazz));
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
