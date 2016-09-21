package br.com.cp2ejr.tonafetin2016.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.cp2ejr.tonafetin2016.Controls.HttpClientConnect;
import br.com.cp2ejr.tonafetin2016.Models.Project;
import br.com.cp2ejr.tonafetin2016.Models.User;
import br.com.cp2ejr.tonafetin2016.R;

/**
 * Created by Lara on 11/09/2016.
 */
public class ActivityVote extends Activity {

    private ImageView ivVote;
    private EditText etSearch;
    private ImageView ivSearch;

    public ProgressDialog mProgressDialog;
    public RadioGroup mRadioGroup;
    public ArrayList<Project>  mProjects;       // List with all projects.
    public ArrayList<Project> mProjectsSearch; // List with projects returns by the search.
    public User user = new User();
    public String arrayPopulated = "";          // Save with which ArrayList the RadioGroup was populated.
    public HttpClientConnect httpClientConnect = new HttpClientConnect(); // Class to connect with the WebServer.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        initComponents();

        // Get the User that was passed by Intent.
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // This URL will return the JSON code.
        String jsonString = httpClientConnect.Connect("http://www.cp2ejr.com.br/to_na_fetin/GetVotes.php", new ArrayList<NameValuePair>());

        mProjects.clear();

        // Parse JSON.
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for(int i = 0; i < jsonArray.length(); i++) {
                Project newProject = new Project();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                newProject.setPosition(i);
                newProject.setId(Integer.parseInt(jsonObject.getString("idProject")));
                newProject.setName(jsonObject.getString("name"));
                newProject.setVotes(Integer.parseInt(jsonObject.getString("votes")));

                mProjects.add(newProject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
            The RadioGroup is populated with Projects, and can be all projects or projects returned by the search.
            Then the String "arrayPopulated" save with which the RadioGroup was populated: mProjects -> All projects / mProjectsSearch -> Projects returned by the search.
        */
        populateRadioGroup(mProjects);
        arrayPopulated = "mProjects";

        ivVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkConnection()) {

                    int radioButtonID = mRadioGroup.getCheckedRadioButtonId();
                    View radioButton = mRadioGroup.findViewById(radioButtonID);
                    int index = mRadioGroup.indexOfChild(radioButton);

                    // index == -1 it was not selected any item of RadioGroup.
                    if (index != -1) {

                        mProgressDialog = ProgressDialog.show(ActivityVote.this, "", "Computando voto...", true);

                        if (android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }

                        int position = -1;

                        if(arrayPopulated.equals("mProjects")) {
                            position = mProjects.get(index).getId();
                        } else {
                            position = mProjectsSearch.get(index).getId();
                        }

                        // Create JSON String with the data to pass to WebServer.
                        String json_str = "{\"idUserFacebook\":" + user.getId() + ", \"idProject\":\" " + position + "\"}";

                        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

                        params.add(new BasicNameValuePair("json_str", json_str));

                        httpClientConnect.Connect("http://www.cp2ejr.com.br/to_na_fetin/InsertVote.php", params);

                        StartActivityMain();
                    } else {
                        Toast.makeText(ActivityVote.this, "Você não selecionou nenhum projeto.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ActivityVote.this, "Favor verificar a conexão com a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProject();
            }
        });
    }

    private void searchProject() {
        mProjectsSearch.clear();

        String projectName = etSearch.getText().toString().toLowerCase();

        //Blank seach populated RadioGroup with all projects.
        if(projectName.equals("")) {
            populateRadioGroup(mProjects);
            arrayPopulated = "mProjects";
        } else {
            for (Project project : mProjects) {
                if (project.getName().toLowerCase().contains(projectName)) {
                    mProjectsSearch.add(project);
                }
            }

            if(mProjectsSearch.size() == 0) {
                Toast.makeText(ActivityVote.this, "Nenhum projeto encontrado.", Toast.LENGTH_LONG).show();
            } else {
                populateRadioGroup(mProjectsSearch);
                arrayPopulated = "mProjectsSearch";
            }
        }
    }

    private void populateRadioGroup(ArrayList<Project> mProjects) {
        mRadioGroup.removeAllViews();

        for(int i=0; i < mProjects.size(); i++) {
            //Create a RadioButton with project data to add in RadioGroup.
            RadioButton rbVote = new RadioButton(this);

            rbVote.setText(mProjects.get(i).getName());
            rbVote.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            rbVote.setGravity(Gravity.CENTER_HORIZONTAL);

            mRadioGroup.addView(rbVote);
        }
    }

    private void initComponents() {
        mRadioGroup     = (RadioGroup) findViewById(R.id.rg_vote);
        ivVote          = (ImageView) findViewById(R.id.iv_icon_vote);
        ivSearch        = (ImageView) findViewById(R.id.iv_search);
        etSearch        = (EditText) findViewById(R.id.et_search);
        mProjects       = new ArrayList<>();
        mProjectsSearch = new ArrayList<>();
    }

    private void StartActivityMain() {
        Intent i = new Intent(this , MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        mProgressDialog.dismiss();

        Toast.makeText(ActivityVote.this, "Voto computado com sucesso! Obrigado pelo seu voto.", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_vote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // Check internet connection.
    public  boolean checkConnection() {
        boolean connected;

        ConnectivityManager conectivtyManager = (ConnectivityManager) ActivityVote.this.getSystemService(Context.CONNECTIVITY_SERVICE);

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
