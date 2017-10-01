package com.probitorg.stroogle.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.probitorg.stroogle.R;
import com.probitorg.stroogle.app.AppState;
import com.probitorg.stroogle.app.HomeActivityInterface;
import com.probitorg.stroogle.app.MyActivity;
import com.probitorg.stroogle.app.MyApplication;
import com.probitorg.stroogle.loader.model.SiteSpec;
import com.probitorg.stroogle.network.ChallengeObject;
import com.probitorg.stroogle.network.DummyObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * The activity that holds the first page of the application
 */
public class HomeActivity extends MyActivity implements HomeActivityInterface {

    //TextView siteUrl;
    final int REQUEST_CODE = 42;

    String winStatus = null;

    private AppState appState;//app state

    private boolean bGameStarted = false;
    private int challengeId = 0;
    private int user_id_from = 0;
    private int user_id_to = 0;
    private int points = 0;
    private int time = -1;
    private int canvas = 0;

    @InjectView(R.id.edittext_username_status) TextView _edittextUsernameStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Log.d("xxx", "10 - onCreate                                - HomeActivity");

        appState = ((MyApplication) getApplication()).getAppStateRef();

        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            //Log.d("xxx", "savedInstanceState == null   -   updateUI                                - HomeActivity");
        }
        updateUI(appState.getActiveUIFragment());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.d("xxx", "14 - onStart - HomeActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Log.d("xxx", "15 - onResume                                - HomeActivity");

        File dir = new File(getFilesDir(), "repo");
        File site = new File(dir, "site.txt");
        //findViewById(R.id.go_last).setEnabled(site.length() > 0);
        //TextView lastUrl = (TextView) findViewById(R.id.last_url);
        //lastUrl.setText(null);
        if (site.length() > 0) {
            File file = new File(dir, "lastUrl.txt");
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] bytes = new byte[fis.available()];
                fis.read(bytes);
                fis.close();
                String url = new String(bytes, "UTF-8");
                //lastUrl.setText(url);
            } catch (Exception e) {
                SiteSpec lastSite = MyApplication.instance().readSite();
                String url = MyApplication.PRIMARY_SCHEME + "://"
                        + lastSite.fragments()[0].host();
                //lastUrl.setText(url);
            }
        }
    }

    public void setBooleanGameStarted(boolean value) {
        bGameStarted = value;
    }

    public void setChallengeId(int value) {
        challengeId = value;
    }

    public void setUserIdFrom(int value) {
        user_id_from = value;
    }

    public void setUserIdTo(int value) {
        user_id_to = value;
    }

    public void setPoints(int value) {
        points = value;
    }

    public void setTime(int value) {
        time = value;
        //Log.d("xxx", "~~~ setTime in home: " + String.valueOf(time) + "---" + String.valueOf(value));

    }
    public int getTime() {
        return time;
    }

    public void setCanvas(int value) {
        canvas = value;
    }
    public int getCanvas() {
        return canvas;
    }

    public void setBooleanUpdateLocalChallengeTableFromServer(boolean value) {
        appState.setBooleanUpdateLocalChallengeTableFromServer(value);
    }

    public void updateLocalChallengeTableFromServer(ArrayList<ChallengeObject> challengeObjectArrayList) {
        appState.updateLocalChallengeTableFromServer(challengeObjectArrayList);
    }

    public void startWorker(String url, int time, int canvas) {
        Worker worker = new Worker(url, time, canvas);
        //Worker worker = new Worker(url, getTime(), getCanvas());
        worker.start();
    }

    private class Worker extends Thread {

        private String url;
        private int time;
        private int canvas;

        public Worker(String url, int time, int canvas) {
            this.url = url;
            this.time = time;
            this.canvas = canvas;
        }

        @Override
        public void run() {
            //Log.d("xxx", "25 - run - in Worker                         - HomeActivity");
            final String siteUrl = url;
            try {
                URL url = new URL(siteUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(15000);
                InputStream ins = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream(16 * 1024);
                byte[] buf = new byte[1024 * 4]; // 4k buffer
                int l;
                while ((l = ins.read(buf, 0, buf.length)) != -1) {
                    bos.write(buf, 0, l);
                }
                bos.close();
                ins.close();
                conn.disconnect();

                byte[] bytes = bos.toByteArray();
                String str = new String(bytes, "UTF-8");

                // TODO:
                // verify signature

                JSONObject json = new JSONObject(str);
                //Log.d("xxx", "sssssssssss  str: " + str);
                final SiteSpec fSite = new SiteSpec(json);
                //Log.d("xxx", "jjjjjjjjj json: " + json);

                File dir = new File(getFilesDir(), "repo");
                new File(dir, "lastUrl.txt").delete();
                dir.mkdir();
                File local = new File(dir, "site.txt");
                File tmp = new File(dir, "site_tmp");
                try {
                    FileOutputStream fos = new FileOutputStream(tmp);
                    fos.write(bytes);
                    fos.close();
                    tmp.renameTo(local);
                } catch (Exception e) {
                    tmp.delete();
                }

                //Log.i("loader", "site.xml updated to " + fSite.id());
                //Log.d("xxx", "34 - site.xml updated to " + fSite.id() + " - HomeActivity");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d("xxx", "35 - first line - in run/Worker(runOnUiThread) - - HomeActivity");
                        //findViewById(R.id.go).setEnabled(true);

                        String url = MyApplication.PRIMARY_SCHEME + "://" + fSite.fragments()[0].host();
                        //Log.d("xxx", "36 - url - in Worker(runOnUiThread) - HomeActivity : " + url);

                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        i.putExtra("_site", fSite);
                        //Log.d("xxx", "36 - fSite - in Worker(runOnUiThread) - HomeActivity : " + fSite);
                        //startActivity(i);
                        i.putExtra("time", time);//time(in seconds) to finish the game
                        i.putExtra("canvas", canvas);//time(in seconds) to finish the game

                        //Log.d("xxx", "~~~ time: " + time);
                        //Log.d("xxx", "~~~ canvas: " + canvas);
                        startActivityForResult(i, REQUEST_CODE);
                        //Log.d("xxx", "39 - startActivity - in run/Worker(runOnUiThread) - HomeActivity");
                    }
                });
            } catch (final Exception e) {
                //Log.w("loader", "fail to download site from " + siteUrl, e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //findViewById(R.id.go).setEnabled(true);
                        Toast.makeText(HomeActivity.this, e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Log.d("xxx", "home activity - winningStatus : " + winStatus);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data.hasExtra("winningStatus")) {
                winStatus = data.getExtras().getString("winningStatus");
                //Log.d("xxx", "winningStatus: " + winStatus);
                //Log.d("xxx", "bGameStarted: " + bGameStarted);
                //Log.d("xxx", "challengeId: " + challengeId);
                //Log.d("xxx", "user_id_from: " + user_id_from);
                //Log.d("xxx", "user_id_to: " + user_id_to);
                //Log.d("xxx", "~~~ points: " + points);
                if(bGameStarted) {
                    if (winStatus.equals(MyApplication.WINNING_STATUS_WINNER)) {
                        appState.setIsWinner(true);
                        appState.updateData(challengeId, user_id_to, points, true);
                    } else if (winStatus.equals(MyApplication.WINNING_STATUS_LOOSER)) {
                        appState.updateData(challengeId, user_id_from, points, false);
                    }
                    bGameStarted = false;
                    user_id_from = 0;
                    user_id_to = 0;
                    points = 0;
                    time = 1200;
                    canvas = 0;
                    appState.setIsWinner(false);

                    appState.setActiveUIFragment("ChallengeListFragment");
                    updateUI(appState.getActiveUIFragment());
                }

            }
        }
    }

    public void setUserStatus(String _userStatus) {
        appState.setUserStatus(_userStatus);
        //appState.setUserStatus("username");
        //Log.d("xxx", "xx - onCreate - appstate:user: " + appState.getUserStatus() + "       - MainActivity");
    }

    public void setState(DummyObject dummyObject) {
        //Log.d("xxx", "dummyObject3" + dummyObject.getUsername());
        appState.setState(dummyObject);
    }

    public void updateUI(String options) {

        /*Log.d("xxx", "----- appState: "
                + " un [" + appState.getUsername()
                + "] p [" + appState.getPoints()
                + "] s [" + appState.getUserStatus()
                + "] id [" + appState.getUserId()
                + "] id [" + appState.getPassword()
        );*/
        _edittextUsernameStatus.setText("Welcome [ " + appState.getUsername() + " ]<" + appState.getPoints() + "p>");

        //String test = _edittextUsernameStatus.getText().toString();

        switch(options) {

            case AppState.USER_STATUS_ALIEN:
            case "LoginUserFragment":
            {
                Fragment f = new LoginUserFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.home_activity_id, f);
                ft.commit();
            }
            break;

            case "RegisterUserFragment":
            {
                Fragment f = new RegisterUserFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.home_activity_id, f);
                ft.commit();
            }
            break;

            case "ChallengeListFragment":
            {
                Fragment f = new ChallengeListFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.home_activity_id, f);
                ft.commit();
            }
            break;

            case AppState.USER_STATUS_GUEST:
            case AppState.USER_STATUS_NORMAL_NO_CONFIRM:
            case AppState.USER_STATUS_NORMAL:
            case "HomeMenuFragment":
            {
                Fragment f = HomeMenuFragment.newInstance();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.home_activity_id, f);
                ft.commit();
            }
            break;

        }

    }



    public Cursor getChallengeList() {
        return appState.getChallengeListFromSQLite();
    }

    public AppState getAppState() {
        return appState;
    }













}
