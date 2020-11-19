package com.example.zomato;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    //String url ="";
ArrayList<String> saaa=new ArrayList<>();

    Button b;
    SearchView searchView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        listView =findViewById(R.id.list_view);
        searchView = findViewById(R.id.search);
        b = findViewById(R.id.button);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("asas", query);
                addrtolat(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence query = searchView.getQuery();
                Log.e("text0",query.toString());
                addrtolat(query.toString());

                Log.e("json", saaa.toString());

            }
        });
    }

    void addrtolat(String location) {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 3);
//Log.e("latlalaltatlatl",addresses);
            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                double lat = fetchedAddress.getLatitude();
                double lon = fetchedAddress.getLongitude();
                StringBuilder strAddress = new StringBuilder();
                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i));
                }
                String latlng = "lat=" + lat + "   long=" + lon;
                Log.e("adddressssssss", latlng);

                Log.e("adddressssssss", strAddress.toString());
                resdata(String.valueOf(lat),String.valueOf(lon));

            } else {

                Log.e("errrrorororooror", "Searching Current Address");
            }

        } catch (IOException e) {
            e.printStackTrace();

            Log.e("catchhhhh", "Could not get address..!");
        }
    }
    void resdata(String lat,String longi){
        try {
            URL url = new URL("https://developers.zomato.com/api/v2.1/search?entity_type=city&count=100&lat="+lat+"&lon="+longi+"&sort=real_distance&order=asc");
            URLConnection urlConnection = url.openConnection();

            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("user-key", "1b3c8b37ea96785391fa55c288ac385c");
            BufferedReader bf = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String a;
            int i;
            while ((a = bf.readLine()) != null) {

                JSONObject jsonObject = new JSONObject(a);
                JSONArray jsonArray = jsonObject.getJSONArray("restaurants");
                for (i = 0; i < jsonArray.length(); i++) {
                    JSONObject qo = jsonArray.getJSONObject(i);
                    JSONObject ja = qo.getJSONObject("restaurant");
                    String aas = ja.getString("name");

                   saaa.add(aas);

                    ArrayAdapter adapter=new ArrayAdapter(MainActivity.this,R.layout.list_item,saaa);
                    listView.setAdapter(adapter);


                }

            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


    }
}
