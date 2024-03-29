package fr.umontpellier.etu.tp3_devmob;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileDownloadService extends IntentService {

    public FileDownloadService() {
        super("FileDownloadService");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String relativePath = "test.json";
        parseFile(relativePath);
    }


    private void parseFile(String relativePath) {
        // Obtain the absolute path to the external files directory
        File basePath = getExternalFilesDir(null);
        if (basePath != null) {
            // Correctly append the relative file path
            File file = new File(basePath, relativePath);
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] data = new byte[(int) file.length()];
                    fis.read(data);

                    String jsonString = new String(data, "UTF-8");
                    JSONObject jsonObject = new JSONObject(jsonString);

                    // Log and broadcast parsed JSON data
                    Log.d("FileDownloadService", "Parsed JSON data: " + jsonObject.toString());
                    Intent intent = new Intent("action.UPDATE_DATA");
                    intent.putExtra("surname", jsonObject.getString("surname"));
                    intent.putExtra("name", jsonObject.getString("name"));
                    intent.putExtra("birthdate", jsonObject.getString("birthdate"));
                    intent.putExtra("number", jsonObject.getString("number"));
                    intent.putExtra("mail", jsonObject.getString("mail"));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                } catch (IOException | JSONException e) {
                    Log.e("FileDownloadService", "Error reading or parsing file", e);
                }
            } else {
                Log.e("FileDownloadService", "File does not exist: " + file.getAbsolutePath());
            }
        } else {
            Log.e("FileDownloadService", "External files directory is not available.");
        }
    }
}
