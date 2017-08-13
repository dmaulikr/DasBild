package technique.project.summer.dasBild.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import technique.project.summer.dasBild.ApiUtils;
import technique.project.summer.dasBild.objectsUtils.Country;

/**
 * Created by azeddine on 29/07/17.
 */

public class CountriesListLoader extends AsyncTaskLoader<ArrayList<Country>>{
    private static final String TAG = "CountriesListLoader";
    private static final String REST_COUNTRY_API= "https://restcountries.eu/rest/v2";
    private static final String API_ENDPOINT= "regionalbloc";
    private static final String REGIONAL_BLOC= "al";

    public CountriesListLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Country> loadInBackground() {
        String responseBodyString;
        JSONArray countriesJsonArray;
        JSONObject countryJsonObject;
        ArrayList<Country> countryArrayList= new ArrayList<>();
        Log.d(TAG, "loadInBackground: ");

        Uri  url = new Uri.Builder()
                .encodedPath(REST_COUNTRY_API)
                .appendPath(API_ENDPOINT)
                .appendPath(REGIONAL_BLOC)
                .encodedQuery("fields=name;alpha2Code;alpha3Code")
                .build();

        try {
            responseBodyString =  ApiUtils.run(url);
            countriesJsonArray = new JSONArray(responseBodyString);

            for (int i=0;i<countriesJsonArray.length();i++){
                countryJsonObject = countriesJsonArray.getJSONObject(i);
                countryArrayList.add(getCountryInstance(countryJsonObject));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return countryArrayList;
        }

    }

    Country getCountryInstance(JSONObject jsonObject) throws JSONException {
        String name;
        String twoAlphaCode;
        String threeAlphaCode;

        name = jsonObject.getString("name");
        twoAlphaCode = jsonObject.getString("alpha2Code");
        threeAlphaCode = jsonObject.getString("alpha3Code");
        return  new Country(name,twoAlphaCode,threeAlphaCode);

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }


}
