package com.probitorg.stroogle.loader;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
//import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.probitorg.stroogle.R;
import com.probitorg.stroogle.app.AppState;
import com.probitorg.stroogle.app.MainActivityInterface;
import com.probitorg.stroogle.app.MyActivity;
import com.probitorg.stroogle.app.MyApplication;
import com.probitorg.stroogle.loader.model.FileSpec;
import com.probitorg.stroogle.loader.model.SiteSpec;

/**
 * Activity，Fragment
 * <p>
 * （urlMappingLoaderActivity）
 * <p>
 * Intent：<br>
 * _site:SiteSpec，site<br>
 * _code:String，ClassLoaderFileID，APK ClassLoader<br>
 * _fragment:String，Fragment<br>
 * 
 * @author Yimin
 * 
 */
public class MainActivity extends MyActivity implements MainActivityInterface {

	private SiteSpec site;
	private FileSpec file;
    private String fragmentBegin;
    private String fragmentPlay;
    private String fragmentEnd;
	private boolean loaded;
	private MyClassLoader classLoader;
	private MyResources myResources;
	private AssetManager assetManager;
	private Resources resources;
	private Theme theme;
	private FrameLayout rootView;


	String winningStatus = null;
    private int time = 1200;//default unlimited
    private int canvas = 0;//default unlimited
    //app state
    private AppState appState;

	@Override
	public void onCreate(Bundle savedInstanceState) {

        appState = ((MyApplication) getApplication()).getAppStateRef();
		Intent intent = getIntent();
        setTime(intent.getIntExtra("time", 1200));
        setCanvas(intent.getIntExtra("canvas", 0));

        //Log.d("xxx", "!!! getCanvas(): " + getCanvas());
        if(getCanvas() == 1) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

		//Log.d("xxx", "40 - onCreate                         - MainActivity");

		int error = 0;
		// must be load at the first start
		do {
			site = intent.getParcelableExtra("_site");
			if (site == null) {
				error = 201; // #201
				break;
			}
            fragmentBegin = intent.getStringExtra("_fragmentBegin");
            fragmentPlay = intent.getStringExtra("_fragmentPlay");
            fragmentEnd = intent.getStringExtra("_fragmentEnd");

            if (TextUtils.isEmpty(fragmentBegin)) {
                error = 202; // #202
                break;
            }
            if (TextUtils.isEmpty(fragmentPlay)) {
                error = 202; // #202
                break;
            }
            if (TextUtils.isEmpty(fragmentEnd)) {
                error = 202; // #202
                break;
            }

			String code = intent.getStringExtra("_code");
			if (TextUtils.isEmpty(code)) {
				loaded = true;
				break;
			}
			file = site.getFile(code);
			if (file == null) {
				error = 205; // #205
				break;
			}
			classLoader = MyClassLoader.getClassLoader(site, file);
			loaded = classLoader != null;
			if (!loaded) {
				error = 210; // #210
				break;
			}
		} while (false);

		super.onCreate(savedInstanceState);

		rootView = new FrameLayout(this);
		rootView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		rootView.setId(android.R.id.primary);
		setContentView(rootView);

		if (!loaded) {
			TextView text = new TextView(this);
			text.setText("Unable to load page" + (error == 0 ? "" : " #" + error));
			text.setLayoutParams(new FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
			rootView.addView(text);

			return;
		}

		// the fragment will be restored by framework
		if (savedInstanceState != null) {
			//Log.d("xxx", "ddduu");
			return;
		}

        updateUI("fragmentBegin");

	}

    public void updateUI(String options) {


        Fragment fragment = null;
        try {

            //Log.d("xxx", "45 - fragment->loadClass              - MainActivity");

            switch (options) {
                case "fragmentBegin": {
                    fragment = (Fragment) getClassLoader().loadClass(fragmentBegin).newInstance();
                }   break;
                case "fragmentPlay": {
                    fragment = (Fragment) getClassLoader().loadClass(fragmentPlay).newInstance();
                }   break;
                case "fragmentEnd": {
                    fragment = (Fragment) getClassLoader().loadClass(fragmentEnd).newInstance();
                }   break;
            }


            //Log.d("xxx", "-------------- fragmentName: " + fragmentBegin);
            //Log.d("xxx", "-------------- fragmentName: " + fragmentPlay);
            //Log.d("xxx", "-------------- fragmentName: " + fragmentEnd);

        } catch (Exception e) {

            //Log.d("xxx", "nnnrr");

            loaded = false;
            classLoader = null;
            int error = 211; // #211
            TextView text = new TextView(this);
            text.setText("Unable to load page #" + error);
            text.append("\n" + e);
            text.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
            rootView.addView(text);

            return;
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(android.R.id.primary, fragment);
        ft.commit();

    }


	@Override
	public ClassLoader getClassLoader() {

		//Log.d("xxx", "50t- getClassLoader                   - MainActivity");

		return classLoader == null ? super.getClassLoader() : classLoader;
	}

	public SiteSpec getSite() {
		return site;
	}

	public FileSpec getFile() {
		return file;
	}

	//public String getFragmentName() {
	//	return fragmentName;
	//}

	@Override
	public Intent urlMap(Intent intent) {

		//Log.d("xxx", "rrrddqqqq");

		do {
			// only process my scheme uri
			Uri uri = intent.getData();
			if (uri == null)
				break;
			if (uri.getScheme() == null)
				break;
			if (!(MyApplication.PRIMARY_SCHEME
					.equalsIgnoreCase(uri.getScheme())))
				break;

			if (!intent.hasExtra("_site")) {
				intent.putExtra("_site", site);
			}
		} while (false);
		return super.urlMap(intent);
	}

	@Override
	public AssetManager getAssets() {
		return assetManager == null ? super.getAssets() : assetManager;
	}

	@Override
	public Resources getResources() {
		return resources == null ? super.getResources() : resources;
	}

	@Override
	public Theme getTheme() {
		return theme == null ? super.getTheme() : theme;
	}

	public MyResources getOverrideResources() {
		return myResources;
	}

	void setOverrideResources(MyResources myres) {
		if (myres == null) {
			this.myResources = null;
			this.resources = null;
			this.assetManager = null;
			this.theme = null;
		} else {
			this.myResources = myres;
			this.resources = myres.getResources();
			this.assetManager = myres.getAssets();
			Theme t = myres.getResources().newTheme();
			t.setTo(getTheme());
			this.theme = t;
		}
	}






    public void sendWinningStatus() {

        //Log.d("xxx", "winStatus:= " + winningStatus);
        //this.winningStatus = winningStatus;

        Intent returnIntent = new Intent();
        returnIntent.putExtra("winningStatus", this.winningStatus);
        setResult(RESULT_OK, returnIntent);

        finish();
    }

    public void setWinningStatus(String winningStatus) {
        //Log.d("xxx", "winStatus:= " + winningStatus);
        this.winningStatus = winningStatus;
    }

    public String getWinningStatus() {
        return this.winningStatus;
    }

    public int getTime() {
        return time;
    }
    public int getCanvas() {
        return canvas;
    }

    public void setTime(int time) {
        this.time = time;
        //Log.d("xxx", "~~~ setTime in main: " + time);
    }

    public void setCanvas(int canvas) {
        this.canvas = canvas;
    }

    @Override
	public void finish() {
		//code here
		//Log.d("xxx", "in finish()");
		super.finish();
	}

    @Override
    public MediaPlayer getMediaPlayer(int resid) {

        return MediaPlayer.create(this, resid);

    }

    public MediaPlayer getMediaPlayer(String name, String defType) {

        String defPackage = "com.probitorg.stroogle";

        return MediaPlayer.create(this, getResources().getIdentifier(
                name, defType, defPackage));

    }

    public MediaPlayer getMediaPlayer(String name, String defType,
                                      String defPackage, MyResources res) {

        return MediaPlayer.create(this, res.getResources().getIdentifier(
                name, defType, defPackage));

    }

	
	
	
	
}
