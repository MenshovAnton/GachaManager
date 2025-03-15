package ru.menshovanton.hoyosubstrakcer;

import android.content.Context;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataManager {
    private static final String GENSHIN_TRACK_DATA_JSON = "genshinTrackData.json";
    private static final String HSR_TRACK_DATA_JSON = "hsrTrackData.json";
    private static final String ZZZ_TRACK_DATA_JSON = "zzzTrackData.json";
    private static final Gson gson = new Gson();

    static void Serialize(Context context, Date[] dataArray) {

        DataItems dataItems = new DataItems();
        dataItems.setDates(dataArray);
        String jsonString = gson.toJson(dataItems);

        switch (MainActivity.subType) {
            case 0:
                try (FileOutputStream fileOutputStream = context.openFileOutput(GENSHIN_TRACK_DATA_JSON, Context.MODE_PRIVATE))
                {   fileOutputStream.write(jsonString.getBytes());  }
                catch (Exception e)
                {   e.printStackTrace();    }
                break;
            case 1:
                try (FileOutputStream fileOutputStream = context.openFileOutput(HSR_TRACK_DATA_JSON, Context.MODE_PRIVATE))
                {   fileOutputStream.write(jsonString.getBytes());  }
                catch (Exception e)
                {   e.printStackTrace();    }
                break;
            case 2:
                try (FileOutputStream fileOutputStream = context.openFileOutput(ZZZ_TRACK_DATA_JSON, Context.MODE_PRIVATE))
                {   fileOutputStream.write(jsonString.getBytes());  }
                catch (Exception e)
                {   e.printStackTrace();    }
                break;
        }


    }

    static Date[] Deserialize(Context context) {
        switch (MainActivity.subType) {
            case 0:
                try (FileInputStream fileInputStream = context.openFileInput(GENSHIN_TRACK_DATA_JSON);
                     InputStreamReader streamReader = new InputStreamReader(fileInputStream))
                {
                    DataItems dataItems = gson.fromJson(streamReader, DataItems.class);
                    return  dataItems.getDates();
                }
                catch (IOException ex)
                {   ex.printStackTrace();   }
                break;
            case 1:
                try (FileInputStream fileInputStream = context.openFileInput(HSR_TRACK_DATA_JSON);
                     InputStreamReader streamReader = new InputStreamReader(fileInputStream))
                {
                    DataItems dataItems = gson.fromJson(streamReader, DataItems.class);
                    return  dataItems.getDates();
                }
                catch (IOException ex)
                {   ex.printStackTrace();   }
                break;
            case 2:
                try (FileInputStream fileInputStream = context.openFileInput(ZZZ_TRACK_DATA_JSON);
                     InputStreamReader streamReader = new InputStreamReader(fileInputStream))
                {
                    DataItems dataItems = gson.fromJson(streamReader, DataItems.class);
                    return  dataItems.getDates();
                }
                catch (IOException ex)
                {   ex.printStackTrace();   }
                break;
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
