package com.reiya.pixiv.util;

import com.google.gson.Gson;

/**
 * Created by lenovo on 2016/4/4.
 */
public class Serializer {
    public static String serialize(Object object) {
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream os = new ObjectOutputStream(baos);
//            os.writeObject(object);
//            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//        try {
//            return LoganSquare.serialize(object);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return new Gson().toJson(object);
    }

    public static Object deserialize(String string, Class clazz) {
//        byte[] base64 = Base64.decode(string.getBytes(), Base64.DEFAULT);
//        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
//        try {
//            ObjectInputStream is = new ObjectInputStream(bais);
//            return is.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//        try {
//            return LoganSquare.parse(string, c);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return new Gson().fromJson(string, clazz);
    }
}
