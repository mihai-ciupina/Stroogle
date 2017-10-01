package com.probitorg.stroogle.app;

import android.database.Cursor;

import com.probitorg.stroogle.network.ChallengeObject;
import com.probitorg.stroogle.network.DummyObject;

import java.util.ArrayList;

/**
 * Created by Mihai on 11/07/2016.
 */
public interface HomeActivityInterface {

    public void setUserStatus(String _userStatus);

    public void startWorker(String url, int time, int canvas);

    public void updateUI(String options);

    public void setState(DummyObject dummyObject);

    public Cursor getChallengeList();

    public void setBooleanUpdateLocalChallengeTableFromServer(boolean value);

    public void updateLocalChallengeTableFromServer(ArrayList<ChallengeObject> challengeObjectArrayList);

    public void setBooleanGameStarted(boolean value);

    public void setChallengeId(int value);

    public void setUserIdFrom(int value);

    public void setUserIdTo(int value);

    public void setPoints(int value);

    public AppState getAppState();

    public void setTime(int value);

    public void setCanvas(int value);

    public int getTime();

    public int getCanvas();









}
