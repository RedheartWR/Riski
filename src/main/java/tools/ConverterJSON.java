package tools;

import com.google.gson.Gson;

public class ConverterJSON {

    public static String toJSON(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj).replace('\n', ' ');
    }
}
