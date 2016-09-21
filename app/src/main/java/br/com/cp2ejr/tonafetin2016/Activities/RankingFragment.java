package br.com.cp2ejr.tonafetin2016.Activities;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.cp2ejr.tonafetin2016.Adapters.RankingAdapter;
import br.com.cp2ejr.tonafetin2016.Controls.HttpClientConnect;
import br.com.cp2ejr.tonafetin2016.Models.Project;
import br.com.cp2ejr.tonafetin2016.R;

/**
 * Created by Lara on 04/07/2016.
 */
public class RankingFragment extends Fragment {

    private View view;
    private ArrayList<Project> mProjects = new ArrayList<>();
    private RankingAdapter mRankingAdapter;
    private ListView mListView;
    private TextView tvInternetRanking;
    private Context mContext;
    private HttpClientConnect httpClientConnect = new HttpClientConnect();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        view = inflater.inflate(R.layout.ranking_layout, container, false);
        mContext = container.getContext();

        initComponents();

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if(checkConnection()) {
            tvInternetRanking.setVisibility(View.INVISIBLE);
            mListView.setVisibility(View.VISIBLE);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    httpClientConnect = new HttpClientConnect();

                    // Get JSON from WebServer.
                    String jsonString = httpClientConnect.Connect("http://www.cp2ejr.com.br/to_na_fetin/GetVotes.php", new ArrayList<NameValuePair>());

                    mProjects.clear();

                    // Parse JSON.
                    try {
                        JSONArray jsonArray = new JSONArray(jsonString);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Project newProject = new Project();

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            newProject.setId(Integer.parseInt(jsonObject.getString("idProject")));
                            newProject.setPosition(i);
                            newProject.setName(jsonObject.getString("name"));
                            newProject.setVotes(Integer.parseInt(jsonObject.getString("votes")));

                            mProjects.add(newProject);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ((MainActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRankingAdapter = new RankingAdapter(container.getContext(), mProjects);
                            mListView.setAdapter(mRankingAdapter);
                        }
                    });
                }
            }).start();


        } else {
            tvInternetRanking.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private void initComponents() {
        mListView = (ListView) view.findViewById(R.id.list_view_ranking);
        tvInternetRanking = (TextView) view.findViewById(R.id.tv_internet_ranking);
    }

    // Check internet connection.
    public  boolean checkConnection() {
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
