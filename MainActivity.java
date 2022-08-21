package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String Key = "a18b978603316d47c572d98d52a420f6";
    List<String> tempArray = new ArrayList<>();
    List<String> dateArray = new ArrayList<>();
    List<String> iconArray = new ArrayList<>();
    List<Bitmap> bitmapArray = new ArrayList<>();
    String cityName = null;

    public class DownloadJSON extends AsyncTask <String, Void, String> {
        String forecastJsonStr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TextView txtCity = (TextView) findViewById(R.id.txtCity);
            TextView txtDate = (TextView) findViewById(R.id.txtDate);
            TextView txtValue = (TextView) findViewById(R.id.txtValue);
            ImageView imgIcon = (ImageView) findViewById(R.id.imgIcon);

            try {
                if (forecastJsonStr != null) {
                    JSONObject full = new JSONObject(forecastJsonStr);
                    JSONObject citySegment = full.getJSONObject("city");
                    JSONArray mainArray = full.getJSONArray("list");
                    for (int i = 0; i < mainArray.length(); i++) {
                        JSONObject weather = mainArray.getJSONObject(i);
                        Date date = new Date((long) weather.getInt("dt")*1000);

                        if (weather != null) {
                            tempArray.add(weather.getJSONObject("temp").getString("day") + " °C");
                            dateArray.add(date.toString());
                            cityName = citySegment.getString("name");
                            JSONArray weatherArray = weather.getJSONArray("weather");
                            iconArray.add(weatherArray.getJSONObject(0).getString("icon"));
                            DownloadIcons downloadIcons = new DownloadIcons();
                            Bitmap bitmap = downloadIcons.execute(weatherArray.getJSONObject(0).getString("icon")).get();
                            bitmapArray.add(Bitmap.createScaledBitmap(bitmap, 500, 500, true));

                            if (i == 0) {
                                txtCity.setText(cityName);
                                txtValue.setText(weather.getJSONObject("temp").getString("day")+" °C");
                                txtDate.setText(date.toString());
                                imgIcon.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 500, true));
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            loadListView();
        }

        @Override
        protected String doInBackground(String... stringsArray) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String unit = "Metric";

            String city = null;
            if (city == null) {
                if (stringsArray.length == 0 || stringsArray[0] == null) {
                    city = "kandy";
                } else {
                    city = stringsArray[0];
                }
            }

            try {
                final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + "&cnt=7&appid=a18b978603316d47c572d98d52a420f6&units=" + unit;
                URL url = new URL(BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line1;
                while ((line1 = reader.readLine()) != null) {
                    buffer.append(line1 + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e("Hi", "Error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Hi", "Error closing stream", e);
                    }
                }
            }
            return forecastJsonStr;
        }
    }

    public class DownloadIcons extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap image = null;
            try {
                String iconId = null;
                if (strings.length != 0) {
                    iconId = strings[0];
                }
                String baseUrl = "https://openweathermap.org/img/wn/" + iconId +"@2x.png";
                URL url = new URL(baseUrl);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch(IOException e) {
                System.out.println(e);
            }
            return image;
        }
    }

    EditText edt;
    Button btn;
    RelativeLayout rlWeather,rlRt;

    public void Loading(View view) {
        EditText edt = (EditText) findViewById(R.id.edt);
        DownloadJSON downloadJSON = new DownloadJSON();
        downloadJSON.execute(edt.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        edt =  findViewById(R.id.edt);
        rlWeather =  findViewById(R.id.rlWeather);
        rlRt =  findViewById(R.id.rlRt);

        DownloadJSON downloadJSON = new DownloadJSON();
        downloadJSON.execute();
    }

    private void loadListView() {
        CustomListAdapter adapter = new CustomListAdapter(this, tempArray, dateArray, bitmapArray);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
}