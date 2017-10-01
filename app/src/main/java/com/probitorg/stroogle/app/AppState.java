package com.probitorg.stroogle.app;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.probitorg.stroogle.db.SQLiteDB;
import com.probitorg.stroogle.network.ChallengeObject;
import com.probitorg.stroogle.network.DummyObject;
import com.probitorg.stroogle.network.GsonPostRequest;

import java.util.ArrayList;

/**
 * Created by Mihai on 27/06/2016.
 * Class used for the app state
 */
public class AppState {

    public final static String USER_STATUS_ALIEN    = "alien";// 0 - alien (fara amprenta in baza de date)
    public final static String USER_STATUS_GUEST    = "guest";// 1 - guest (anonim cu amprenta in baza de date - nume generat)
    public final static String USER_STATUS_NORMAL_NO_CONFIRM   = "normalnoconfirm";// 3 - normal (utilizator autentificat cu email ne-confirmat si parola)
    public final static String USER_STATUS_NORMAL   = "normal";// 3 - normal (utilizator autentificat cu email confirmat si parola)

    private SQLiteDB mSQLiteDB;

    //bean
    private int user_id = 0;
    private String username = "you"; //default value
    private int points = -1; //default value
    private String password = null; //default value
    private String userStatus = USER_STATUS_ALIEN; //default value

    private String activeUIFragment = "LoginUserFragment";

    private boolean bUpdateLocalChallengeTableFromServer = false;

    private boolean isWinner = false;//per challenge

    public AppState(Context context) {
        mSQLiteDB = new SQLiteDB(context);
        getAppStateFromSQLite();
        setActiveUIFragment(getUserStatus());
        //Log.d("xxx", "+++ setActiveUIFragment(getUserStatus());");

    }

    public boolean getAppStateFromSQLite() {

        Cursor rs = mSQLiteDB.getAppState();
        rs.moveToFirst();

        user_id = rs.getInt(rs.getColumnIndex(SQLiteDB.APP_STATE_COLUMN_USERS_ID));
        username = rs.getString(rs.getColumnIndex(SQLiteDB.APP_STATE_COLUMN_USERNAME));
        password = rs.getString(rs.getColumnIndex(SQLiteDB.APP_STATE_COLUMN_PASSWORD));
        points = rs.getInt(rs.getColumnIndex(SQLiteDB.APP_STATE_COLUMN_POINTS));
        userStatus = rs.getString(rs.getColumnIndex(SQLiteDB.APP_STATE_COLUMN_USER_STATUS));

        //Log.d("xxx","----- userStatus: " + userStatus + " [AppState]/[updateAppStateFromSQLite]");
        //Log.d("xxx","----- username: " + username + " [AppState]/[updateAppStateFromSQLite]");
        //Log.d("xxx","----- user_id: " + user_id + " [AppState]/[updateAppStateFromSQLite]");

        if (!rs.isClosed())
        {
            rs.close();
        }

        return true;
    }


    public void setBooleanUpdateLocalChallengeTableFromServer(boolean value) {
        this.bUpdateLocalChallengeTableFromServer = value;
    }

    public void updateLocalChallengeTableFromServer(ArrayList<ChallengeObject> challengeObjectArrayList) {
        mSQLiteDB.updateLocalChallengeTableFromServer(challengeObjectArrayList);
    }

    public void setState(DummyObject dummyObject)
    {
        //Log.d("xxx", "dummyObject4" + dummyObject.getUsername());

        mSQLiteDB.updateSQLiteAppState(dummyObject);
        getAppStateFromSQLite();
    }

    public Cursor getChallengeListFromSQLite() {
        return mSQLiteDB.getChallengeList();
    }

    //public ArrayList<String> getChallengeList() {
    //    return mSQLiteDB.getChallengeList();
    //}

    public void updateData(int challengeId, int winner_id, int points, boolean isWinner) {

        //transaction
        mSQLiteDB.updateChallengeOnSQLite_closeChallenge(challengeId, winner_id, isWinner);
        updateChallengeOnServer_closeChallenge(challengeId, winner_id, isWinner);
        
        //transaction
        if(isWinner) {setPoints(getPoints() + points);}
        mSQLiteDB.updateUsersOnSQLite_AddPoints(getPoints(), points);
        updateUsersOnServer_AddPoints(winner_id, points);
        
    }


    public void updateUsersOnServer_AddPoints(int winner_id, int points) {

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", winner_id);
        jsonObject.addProperty("points", points);

        //Log.d("xxx", "jsonObject: " + jsonObject);

        final GsonPostRequest<DummyObject> gsonPostRequest =
                com.probitorg.stroogle.network.ApiRequests.getDummyObjectArrayWithPost
                        (
                                new Response.Listener<DummyObject>() {
                                    @Override
                                    public void onResponse(DummyObject challengeObject) {
                                        //Log.d("xxx", "challengeObject: " + challengeObject.getPoints());
                                        //onLoginSuccess(dummyObject);
                                    }
                                }
                                ,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //onLoginFailed();
                                        //Log.d("xxx", "points failed: ");
                                    }
                                }
                                ,
                                jsonObject.toString()

                        );
        MyApplication.addRequest(gsonPostRequest, "xxx");

    }

    public void updateChallengeOnServer_closeChallenge(int challengeId, int winner_id, boolean isWinner) {

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", challengeId);
        jsonObject.addProperty("winner_id", winner_id);
        if(isWinner) {jsonObject.addProperty("is_winner", 1);}

        //Log.d("xxx", "jsonObject: " + jsonObject);

        final GsonPostRequest<ChallengeObject> gsonPostRequest =
                com.probitorg.stroogle.network.ApiRequests.updateChallengeOnServer_closeChallenge
                        (
                                new Response.Listener<ChallengeObject>() {
                                    @Override
                                    public void onResponse(ChallengeObject challengeObject) {
                                        //onLoginSuccess(dummyObject);
                                    }
                                }
                                ,
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //onLoginFailed();
                                    }
                                }
                                ,
                                jsonObject.toString()

                        );
        MyApplication.addRequest(gsonPostRequest, "xxx");

    }

    public void setIsWinner(boolean value) {
        isWinner = value;
    }




    //bean

    public String getUsername() {
        return username;
    }
    public int getUserId() {
        return user_id;
    }
    public String getPassword() {
        return password;
    }
    public int getPoints() {
        return points;
    }
    public String getUserStatus() {
        return userStatus;
    }

    public void setUsername(String value) {
        username = value;
    }
    public void setUserId(int value) {
        user_id = value;
    }
    public void setPassword(String value) {
        password = value;
    }
    public void setPoints(int value) {
        points = value;
    }
    public boolean setUserStatus(String _userStatus) {
        userStatus = _userStatus;
        mSQLiteDB.updateUserStatus(userStatus);
        return true;
    }

    //
    public String getActiveUIFragment() {
        return activeUIFragment;
    }

    public void setActiveUIFragment(String value) {
        activeUIFragment = value;
    }















}
