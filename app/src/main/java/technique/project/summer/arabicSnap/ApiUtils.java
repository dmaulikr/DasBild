package technique.project.summer.arabicSnap;

import android.net.Uri;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by azeddine on 31/07/17.
 */

public class ApiUtils {
    public static String run(Uri url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url.toString())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
