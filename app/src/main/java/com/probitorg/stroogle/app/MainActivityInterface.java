package com.probitorg.stroogle.app;

import android.media.MediaPlayer;

import com.probitorg.stroogle.loader.MyResources;

/**
 * Created by Mihai on 11/07/2016.
 */
public interface MainActivityInterface {

    public void sendWinningStatus();

    public void setWinningStatus(String winningStatus);

    public String getWinningStatus();

    public int getTime();

    public void setTime(int time);

    public void updateUI(String options);

    public MediaPlayer getMediaPlayer(int resid);

    public MediaPlayer getMediaPlayer(String name, String defType);

    public MediaPlayer getMediaPlayer(String name, String defType, String defPackage, MyResources res);

}
