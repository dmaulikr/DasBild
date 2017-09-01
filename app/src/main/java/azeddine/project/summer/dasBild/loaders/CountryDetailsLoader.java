package azeddine.project.summer.dasBild.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import azeddine.project.summer.dasBild.ApiUtils;

/**
 * Created by azeddine on 23/08/17.
 */
/*
* https://en.wikipedia.org/w/api.php?action=query&prop=extracts&exintro=&explaintext=&titles=State of Palestine&exsentences=4&format=json
* */
public class CountryDetailsLoader extends AsyncTaskLoader<String>{
    private static final String TAG = "CountryDetailsLoader";
    private static final String WIKI_API_BASE_URL = "https://en.wikipedia.org/w/api.php";
    public static final int WIKI_API_SENTENCES_NUBER =4;
    private String mCountryName;
    public CountryDetailsLoader(Context context,String name) {
        super(context);
        mCountryName = name;
    }

    @Override
    public String loadInBackground() {
        String responseString;
        JSONObject responseObject;
        String detailsText = null;
        Uri url = new Uri.Builder()
                .encodedPath(WIKI_API_BASE_URL)
                .appendQueryParameter("action","query")
                .appendQueryParameter("prop","extracts")
                .appendQueryParameter("exintro","")
                .appendQueryParameter("explaintext","")
                .appendQueryParameter("titles", mCountryName)
                .appendQueryParameter("exsentences",""+WIKI_API_SENTENCES_NUBER)
                .appendQueryParameter("format","json")
                .build();
        Log.d(TAG, "loadInBackground: wiki url= "+url);

        try {
          responseString =   ApiUtils.run(url);
          responseObject = new JSONObject(responseString).getJSONObject("query").getJSONObject("pages");
          Iterator<String> stringIterator = responseObject.keys();

          if (stringIterator.hasNext()){
              String id = stringIterator.next();
              detailsText = responseObject.getJSONObject(id).getString("extract");
              detailsText = detailsText.replaceAll("\\(.*?\\) ?", "");
          }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detailsText;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
