package com.probitorg.stroogle.loader;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.probitorg.stroogle.R;
import com.probitorg.stroogle.app.AppState;
import com.probitorg.stroogle.app.HomeActivityInterface;
import com.probitorg.stroogle.app.MyApplication;
import com.probitorg.stroogle.db.SQLiteDB;
import com.probitorg.stroogle.network.ChallengeObject;
import com.probitorg.stroogle.network.GsonGetRequest;
import com.probitorg.stroogle.utils.ChallengeListCursorAdapter;

import java.util.ArrayList;

import butterknife.InjectView;
//import com.probitorg.stroogle.fragmentdeveloper.R;

public class ChallengeListFragment extends Fragment {

    //app state
    private AppState appState;
    HomeActivityInterface mCallback;

    //@InjectView(R.id.button_ask_for_more_challenges)            Button _askForMoreChallenges;
    //@InjectView(R.id.listView_challengeList)                    ListView _challengeListView;

    //TextView siteUrl;
    //Button btnGo;

    private ListView challengeListView;
    private Button askForMoreChallenges;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

		/*code*/
        View view = inflater.inflate(R.layout.challenge_list, container, false);

        //siteUrl = (TextView) view.findViewById(R.id.siteurl);

        askForMoreChallenges = (Button) view.findViewById(R.id.button_ask_for_more_challenges);

        //view.findViewById(R.id.go_last).setOnClickListener(clickListener);
        //view.findViewById(R.id.go_quizz).setOnClickListener(clickListener);

        //ArrayList array_list = mCallback.getChallengeList();
        //ArrayAdapter arrayAdapter = new ArrayAdapter((Activity)mCallback, android.R.layout.simple_list_item_1, array_list);
        ChallengeListCursorAdapter clcAdapter = new ChallengeListCursorAdapter((Activity)mCallback, mCallback.getChallengeList(), 0);
        //Log.d("xxx", "clcAdapter: " + clcAdapter);
        challengeListView = (ListView)view.findViewById(R.id.listView_challengeList);
        challengeListView.setAdapter(clcAdapter);
        challengeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                // TODO Auto-generated method stub

                SQLiteCursor sqlc = (SQLiteCursor)parentAdapter.getItemAtPosition(position);
                //sqlc.getLong(sqlc.getColumnIndex("_id"));
                //Log.d("xxx", "id_To_Search " + sqlc.getLong(sqlc.getColumnIndex("_id")));
                //Log.d("xxx", "id_To_Search " + sqlc.getString(sqlc.getColumnIndex("game_id")));

                mCallback.setChallengeId(sqlc.getInt(sqlc.getColumnIndex(SQLiteDB.CHALLENGE_COLUMN_ID)));
                mCallback.setUserIdFrom(sqlc.getInt(sqlc.getColumnIndex(SQLiteDB.CHALLENGE_COLUMN_USER_ID_FROM)));
                mCallback.setUserIdTo(sqlc.getInt(sqlc.getColumnIndex(SQLiteDB.CHALLENGE_COLUMN_USER_ID_TO)));
                mCallback.setPoints(sqlc.getInt(sqlc.getColumnIndex(SQLiteDB.CHALLENGE_COLUMN_POINTS)));
                //mCallback.setTime(sqlc.getInt(sqlc.getColumnIndex(SQLiteDB.CHALLENGE_COLUMN_TIME)));
                int time = sqlc.getInt(sqlc.getColumnIndex(SQLiteDB.CHALLENGE_COLUMN_TIME));

                //Log.d("xxx", "~~~ setTime in list: " + String.valueOf(time));

                mCallback.setTime(time);
                //Log.d("xxx", "~~~ sqlc.getColumnIndex(SQLiteDB.CHALLENGE_COLUMN_TIME)): " + String.valueOf(sqlc.getColumnIndex(SQLiteDB.CHALLENGE_COLUMN_ID)));
                mCallback.setCanvas(sqlc.getInt(sqlc.getColumnIndex(SQLiteDB.CHALLENGE_COLUMN_CANVAS)));
                mCallback.setBooleanGameStarted(true);

                String stringSiteUrl = "http://strgl.probitorg.com/public_download/projects/" + sqlc.getString(sqlc.getColumnIndex(SQLiteDB.CHALLENGE_COLUMN_GAME_STRING_ID)) + "/site.txt";
                //siteUrl.setText(stringSiteUrl);
                //btnGo.performClick();

                mCallback.startWorker(stringSiteUrl, mCallback.getTime(), mCallback.getCanvas());

            }
        });

        askForMoreChallenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForMoreChallenges();
            }
        });

        appState = ((MyApplication) getActivity().getApplication()).getAppStateRef();

		/*code*/

        return view;

    }

    public void askForMoreChallenges() {
        final GsonGetRequest<ArrayList<ChallengeObject>> gsonGetRequest =
                com.probitorg.stroogle.network.ApiRequests.getChallengeObjectArrayAfterAskForMoreChallenges
                        (
                                new Response.Listener<ArrayList<ChallengeObject>>() {

                                    @Override
                                    public void onResponse(ArrayList<ChallengeObject> challengeObjectArrayList) {
                                        OnSuccess(challengeObjectArrayList);
                                    }
                                }
                                ,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //Log.d("xxx", "error: " + error.networkResponse);
                                        OnFailed(error);
                                    }
                                }
                                ,
                                mCallback.getAppState().getUserId()
                        );
        MyApplication.addRequest(gsonGetRequest, "xxx");
    }
    public void OnSuccess(ArrayList<ChallengeObject> challengeObjectArrayList) {
        //Log.d("xxx", "^^^^ Succes: ");
        mCallback.updateLocalChallengeTableFromServer(challengeObjectArrayList);

        appState.setActiveUIFragment("ChallengeListFragment");
        mCallback.updateUI(appState.getActiveUIFragment());
    }
    public void OnFailed(VolleyError error) {
        //Log.d("xxx", "^^^^ OnFailed: ");
        if(error.networkResponse != null) {
            if(error.networkResponse.statusCode == 404) {
                //Log.d("xxx", "^^^^ statusCode == 404");
                Toast.makeText((Activity)mCallback, "No challenges. Please try later.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.go) {
                mCallback.startWorker(siteUrl.getText().toString());
            } else if (v.getId() == R.id.go_last) {
                String url = ((TextView) v.findViewById(R.id.last_url)).getText().toString();
                //startActivity(url);
            } else if (v.getId() == R.id.go_quizz) {
                siteUrl.setText("http://strgl.probitorg.com/public_download/projects/quizz/site.txt");
                btnGo.performClick();
                //Log.d("xxx", "30 - onClick(Quizz) - after Worker           - HomeActivity");
            }

        }
    };
    */


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }














    public ChallengeListFragment() {
    }

    public static ChallengeListFragment newInstance() {
        ChallengeListFragment fragment = new ChallengeListFragment();
        return fragment;
    }

    public static ChallengeListFragment newInstance(Bundle bundle) {
        ChallengeListFragment fragment = new ChallengeListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//<23
            onAttachToContext(activity);
        }
    }

    protected void onAttachToContext(Context context) {
        //Log.d("xxx", "1:");
        try {
            mCallback = (HomeActivityInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AppState interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }








}
