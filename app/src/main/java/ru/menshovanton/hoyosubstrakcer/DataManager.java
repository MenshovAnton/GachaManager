package ru.menshovanton.hoyosubstrakcer;

import android.content.Context;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataManager {
    private static final String FILE_NAME_1 = "dateData.json";
    private static final Gson gson = new Gson();

    static void Serialize(Context context, Date[] dataArray) {

        DataItems dataItems = new DataItems();
        dataItems.setDates(dataArray);
        String jsonString = gson.toJson(dataItems);

        try (FileOutputStream fileOutputStream =
                     context.openFileOutput(FILE_NAME_1, Context.MODE_PRIVATE))
        {
            fileOutputStream.write(jsonString.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Date[] Deserialize(Context context) {
        try (FileInputStream fileInputStream = context.openFileInput(FILE_NAME_1);
             InputStreamReader streamReader = new InputStreamReader(fileInputStream))
        {
            DataItems dataItems = gson.fromJson(streamReader, DataItems.class);
            return  dataItems.getDates();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    private static class DataItems {
        private Date[] dates;

        Date[] getDates() {
            return dates;
        }
        void setDates(Date[] dates) {
            this.dates = dates;
        }
    }
}
