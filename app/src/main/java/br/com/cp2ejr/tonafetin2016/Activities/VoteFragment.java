package br.com.cp2ejr.tonafetin2016.Activities;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import br.com.cp2ejr.tonafetin2016.Controls.HttpClientConnect;
import br.com.cp2ejr.tonafetin2016.Models.User;
import br.com.cp2ejr.tonafetin2016.R;

/**
 * Created by Lara on 04/07/2016.
 */
public class VoteFragment extends Fragment {


    View view;
    public LoginButton loginButton;
    public CallbackManager callbackManager;
    public ImageView ivIconVote, ivPicture, ivLogout, ivLogoCP2;
    public TextView tvLoginFacebook, tvThanks;
    public Context mContext;
    public User user = new User();
    public HttpClientConnect httpClientConnect = new HttpClientConnect();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }


        mContext = container.getContext();

        // Initialize Facebook SDK.
        FacebookSdk.sdkInitialize(mContext);
        callbackManager = CallbackManager.Factory.create();

        view = inflater.inflate(R.layout.vote_layout, container, false);

        initComponents(view);

        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                tvLoginFacebook.setVisibility(View.INVISIBLE);
                loginButton.setVisibility(View.INVISIBLE);
                ivLogout.setVisibility(View.VISIBLE);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        JSONObject jsonObj = new JSONObject();

                        try {
                            user.setId(object.getString("id"));
                            user.setName(object.getString("name"));
                            //user.setGender(object.getString("gender"));

                            JSONObject jsonPicture = new JSONObject(object.getString("picture"));
                            JSONObject jsonData = new JSONObject(jsonPicture.getString("data"));

                            user.setPhotoUrl(jsonData.getString("url"));

                            jsonObj.put("idUserFacebook", user.getId());
                            jsonObj.put("name", user.getName());
                            //jsonObj.put("gender", user.getGender());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(user.getPhotoUrl()).getContent());
                            //ivPicture.setImageBitmap(bitmap);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("json_str", jsonObj.toString()));

                        httpClientConnect.Connect("http://www.cp2ejr.com.br/to_na_fetin/InsertUser.php", params);

                        if (CheckVote(user.getId())) {
                            ivIconVote.setVisibility(View.INVISIBLE);
                            tvThanks.setVisibility(View.VISIBLE);
                        } else {
                            ivIconVote.setVisibility(View.VISIBLE);
                            tvThanks.setVisibility(View.INVISIBLE);
                        }


                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,picture");

                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

                if(checkConnection()) {
                    Toast.makeText(mContext, "Erro ao fazer login com o Facebook. Favor tentar novamente.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Erro ao fazer login com o Facebook. Favor verificar a conexão com a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Check if user is already logged.
        if(isLoggedIn()) {
            if(checkConnection()) {
                try {
                    GetUserInfo();

                    tvLoginFacebook.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.INVISIBLE);
                    ivLogout.setVisibility(View.VISIBLE);

                    // If user has no voted, show icon vote.
                    if (CheckVote(user.getId())) {
                        ivIconVote.setVisibility(View.INVISIBLE);
                        tvThanks.setVisibility(View.VISIBLE);
                    } else {
                        ivIconVote.setVisibility(View.VISIBLE);
                        tvThanks.setVisibility(View.INVISIBLE);
                    }

                } catch (Exception e) {
                    ivIconVote.setVisibility(View.INVISIBLE);
                }
            }

        } else {
            ivIconVote.setVisibility(View.INVISIBLE);
        }

        ivIconVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate = Calendar.getInstance();

                Calendar fetinDateBegin = Calendar.getInstance();
                fetinDateBegin.set(2016, 9, 15, 16, 0);

                Calendar fetinDateEnd = Calendar.getInstance();
                fetinDateEnd.set(2016, 10, 28, 20, 0);


                if(currentDate.before(fetinDateBegin) || currentDate.after(fetinDateEnd)) {
                    Toast.makeText(container.getContext(), "Fora do prazo de votação. A votação só será permitida durante a FETIN entre 28/10 às 16:00 até 30/10 às 20:00 de 2015.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(container.getContext() , ActivityVote.class);
                    i.putExtra("user", user);
                    startActivity(i);

                } else {
                    Intent i = new Intent(container.getContext() , ActivityVote.class);
                    i.putExtra("user", user);
                    startActivity(i);
                }
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();

                tvThanks.setVisibility(View.INVISIBLE);
                ivLogout.setVisibility(View.INVISIBLE);
                //ivPicture.setVisibility(View.INVISIBLE);
                ivIconVote.setVisibility(View.INVISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                tvLoginFacebook.setVisibility(View.VISIBLE);
            }
        });

        /*ivLogoCP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.cp2ejr.com.br"));

                startActivity(intent);
            }
        });*/

        return  view;
    }

    private void initComponents(View view) {
        //ivPicture 		= (ImageView) view.findViewById(R.id.iv_picture);
        ivIconVote 		= (ImageView) view.findViewById(R.id.iv_icon_vote);
        ivLogout 		= (ImageView) view.findViewById(R.id.iv_faceboo_logout);
        //ivLogoCP2 		= (ImageView) view.findViewById(R.id.iv_logo_cp2);
        tvLoginFacebook = (TextView) view.findViewById(R.id.tv_login_facebook);
        loginButton 	= (LoginButton) view.findViewById(R.id.login_button);
        tvThanks 		= (TextView) view.findViewById(R.id.tv_thanks_initial);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    // Check if user have already voted.
    public boolean CheckVote(String idUserFacebook) {

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        boolean vote = false;

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("idUserFacebook", idUserFacebook));

        String jsonString = httpClientConnect.Connect("http://www.cp2ejr.com.br/to_na_fetin/CheckVote.php", params);

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(jsonString);

            // ID: 1 - Already voted
            if(jsonObject.getString("id").equals("0")) {
                vote = false;
            } else {
                vote = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vote;
    }

    // Get user info from Facebook.
    public void GetUserInfo() {

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    user.setId(object.getString("id"));
                    user.setName(object.getString("name"));
                    //user.setGender(object.getString("gender"));

                    JSONObject jsonPicture = new JSONObject(object.getString("picture"));
                    JSONObject jsonData = new JSONObject(jsonPicture.getString("data"));

                    user.setPhotoUrl(jsonData.getString("url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(user.getPhotoUrl()).getContent());
                    //ivPicture.setImageBitmap(bitmap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(CheckVote(user.getId())) {
                    ivIconVote.setVisibility(View.INVISIBLE);
                    tvThanks.setVisibility(View.VISIBLE);
                } else {
                    ivIconVote.setVisibility(View.VISIBLE);
                    tvThanks.setVisibility(View.INVISIBLE);
                }
            }
        });


        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,gender,picture");

        request.setParameters(parameters);
        request.executeAsync();
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
