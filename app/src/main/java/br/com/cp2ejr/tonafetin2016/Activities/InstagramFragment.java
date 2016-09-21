package br.com.cp2ejr.tonafetin2016.Activities;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

import br.com.cp2ejr.tonafetin2016.Adapters.PhotosAdapter;
import br.com.cp2ejr.tonafetin2016.Models.Photo;
import br.com.cp2ejr.tonafetin2016.R;

/**
 * Created by Lara on 04/08/2016.
 */
public class InstagramFragment extends Fragment {

    private View view;
    private ListView mListView;
    private TextView tvInternet;
    private ArrayList<Photo> mPhotoList;
    private PhotosAdapter mPhotoAdapter;
    private ProgressDialog progress;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
           return null;
        }

        view = inflater.inflate(R.layout.gallery_layout, container, false);
        mContext = container.getContext();


        initComponents();

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if(checkConnection()) {
            tvInternet.setVisibility(View.INVISIBLE);
            mListView.setVisibility(View.VISIBLE);

            progress 	= ProgressDialog.show(mContext, "", "Carregando ...", true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String jsonString = "";
                    String jsonStringInatel = "";
                    String jsonStringFetin2016 = "";
                    String jsonStringCP2eJr = "";

                    //#ToNaFetin
                    jsonString = getJsonFromInstagram("tonafetin");

                    //#Inatel
                    jsonStringInatel = getJsonFromInstagram("inatel");

                    //#Fetin2016
                    jsonStringFetin2016 = getJsonFromInstagram("fetin2016");

                    //#CP2eJr
                    jsonStringCP2eJr = getJsonFromInstagram("cp2ejr");

                    mPhotoList.clear();

                    addPhotoFromJson(jsonString);
                    addPhotoFromJson(jsonStringInatel);
                    addPhotoFromJson(jsonStringFetin2016);
                    addPhotoFromJson(jsonStringCP2eJr);

                    ((MainActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Collections.sort(mPhotoList);

                            mPhotoAdapter = new PhotosAdapter(container.getContext(), mPhotoList);
                            mListView.setAdapter(mPhotoAdapter);

                            progress.dismiss();
                        }
                    });
                }
            }).start();

        } else {
            tvInternet.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private void initComponents() {
        mListView 	= (ListView) view.findViewById(R.id.lv_photos_instagram);
        tvInternet  = (TextView) view.findViewById(R.id.tv_internet_instagram);

        mPhotoList = new ArrayList<>();
    }

    private void addPhotoFromJson(String jsonString) {
        // Parse JSON.
        try {
            JSONObject jsonObjectTotal = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObjectTotal.getJSONArray("data");

            // Get the last four photos from Instagram.
            for (int i = 0; i < 4; i++) {
                Photo newPhoto = new Photo();

                //GET PHOTO URL
                JSONObject jsonObjectImages = new JSONObject(jsonArray.getJSONObject(i).getString("images"));
                JSONObject jsonObjectPhoto = new JSONObject(jsonObjectImages.getString("standard_resolution"));
                String url = jsonObjectPhoto.getString("url");

                newPhoto.setUrlPhoto(url);

                //GET USER NAME
                JSONObject jsonObjectUser = new JSONObject(jsonArray.getJSONObject(i).getString("user"));
                String userName = jsonObjectUser.getString("username");

                newPhoto.setUserInstagram("@" + userName);

                //GET PHOTO LEGEND
                JSONObject jsonObjectCaption = new JSONObject(jsonArray.getJSONObject(i).getString("caption"));
                String photoLegend = jsonObjectCaption.getString("text");

                newPhoto.setLegendPhoto(photoLegend);

                //GET CREATED TIME
                String createdTime = jsonArray.getJSONObject(i).getString("created_time");

                newPhoto.setCreatedTime(Double.parseDouble(createdTime));

                //GET TAGS
                String tags = jsonArray.getJSONObject(i).getString("tags");

                newPhoto.setTags(tags);

                //GET ID
                JSONObject jsonObjectCaption2 = new JSONObject(jsonArray.getJSONObject(i).getString("caption"));
                String id = jsonObjectCaption2.getString("id");

                newPhoto.setId(id);

                mPhotoList.add(newPhoto);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String getJsonFromInstagram(String hashtag) {
        URL url = null;
        try {
            // URL Instagram API.
            url = new URL("https://api.instagram.com/v1/tags/"+hashtag+"/media/recent?access_token=469904489.5227b9e.703e794b515341798e0ec6879533e938&count=10");

            URLConnection tc = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));

            return in.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkConnection() {
        boolean connected;

        ConnectivityManager conectivtyManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }


}
