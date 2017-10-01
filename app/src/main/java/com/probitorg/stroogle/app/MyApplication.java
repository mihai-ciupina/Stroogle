package com.probitorg.stroogle.app;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.probitorg.stroogle.loader.LoaderActivity;
import com.probitorg.stroogle.loader.MainActivity;
import com.probitorg.stroogle.loader.MyClassLoader;
import com.probitorg.stroogle.loader.RepositoryManager;
import com.probitorg.stroogle.loader.model.FileSpec;
import com.probitorg.stroogle.loader.model.FragmentSpec;
import com.probitorg.stroogle.loader.model.SiteSpec;
import com.probitorg.stroogle.network.OkHttpStack;
import com.squareup.okhttp.OkHttpClient;

public class MyApplication extends Application {
	public static final String PRIMARY_SCHEME = "app";
    public static final String WINNING_STATUS_WINNER = "winner";
    public static final String WINNING_STATUS_LOOSER = "looser";

	private static MyApplication instance;
	private RepositoryManager repoManager;

	//app state
	private AppState appState;

    // Volley request queue
	private RequestQueue mRequestQueue;

	public static MyApplication instance() {
		if (instance == null) {
			throw new IllegalStateException("Application has not been created");
		}

		return instance;
	}

	public MyApplication() {
		instance = this;
	}

	public RepositoryManager repositoryManager() {
		if (repoManager == null) {
			repoManager = new RepositoryManager(this);
		}
		return repoManager;
	}

	public SiteSpec readSite() {


		File dir = new File(getFilesDir(), "repo");
		File local = new File(dir, "site.txt");
		if (local.length() > 0) {
			try {
				FileInputStream fis = new FileInputStream(local);
				byte[] bytes = new byte[fis.available()];
				int l = fis.read(bytes);
				fis.close();
				String str = new String(bytes, 0, l, "UTF-8");
				JSONObject json = new JSONObject(str);
				return new SiteSpec(json);
			} catch (Exception e) {
				Log.w("loader", "fail to load site.txt from " + local, e);
			}
		}
		return new SiteSpec("empty.0", "0", new FileSpec[0],
				new FragmentSpec[0]);
	}

	public Intent urlMap(Intent intent) {


        do {
			// already specify a class, no need to map url
			if (intent.getComponent() != null)
				break;

			// only process my scheme uri
			Uri uri = intent.getData();
			if (uri == null)
				break;
			if (uri.getScheme() == null)
				break;
			if (!(PRIMARY_SCHEME.equalsIgnoreCase(uri.getScheme())))
				break;

			SiteSpec site = null;
			if (intent.hasExtra("_site")) {

				site = intent.getParcelableExtra("_site");
			}
			if (site == null) {

				site = readSite();
				intent.putExtra("_site", site);
			}

			// i'm responsible
			intent.setClass(this, LoaderActivity.class);

			String host = uri.getHost();
            //Log.d("xxx", "-------------- host: " + host);

            if (TextUtils.isEmpty(host))
				break;
			host = host.toLowerCase(Locale.US);
            FragmentSpec fragmentBegin = site.getFragment("fragmentBegin");
            FragmentSpec fragmentPlay = site.getFragment("fragmentPlay");
            FragmentSpec fragmentEnd = site.getFragment("fragmentEnd");

            if (fragmentBegin  == null)				break;
            if (fragmentPlay == null)				break;
            if (fragmentEnd == null)				break;

            intent.putExtra("_fragmentBegin", fragmentBegin.name());
            intent.putExtra("_fragmentPlay", fragmentPlay.name());
            intent.putExtra("_fragmentEnd", fragmentEnd.name());

			// class loader
			ClassLoader classLoader;
			if (TextUtils.isEmpty(fragmentPlay.code())) {
				classLoader = getClassLoader();
			} else {
				intent.putExtra("_code", fragmentPlay.code());
				FileSpec fs = site.getFile(fragmentPlay.code());
				if (fs == null)
					break;
				classLoader = MyClassLoader.getClassLoader(site, fs);
				if (classLoader == null)
					break;
			}

			intent.setClass(this, MainActivity.class);
		} while (false);

		return intent;
	}

	@Override
	public void startActivity(Intent intent) {


        intent = urlMap(intent);
		super.startActivity(intent);
	}

	
	
	
	public AppState getAppStateRef() {
		if (appState == null) {
            //Log.d("xxx", "+++ appState == null");
			appState = new AppState(this);
		} else {
            //Log.d("xxx", "+++ appState != null");
        }
		return appState;
	}

	/**
	 * Returns a Volley request queue for creating network requests
	 *
	 * @return {@link com.android.volley.RequestQueue}
	 */
	public RequestQueue getVolleyRequestQueue()
	{
		if (mRequestQueue == null)
		{
			mRequestQueue = Volley.newRequestQueue(this, new OkHttpStack(new OkHttpClient()));
		}
		return mRequestQueue;
	}

	/**
	 * Adds a request to the Volley request queue
	 *
	 * @param request to be added to the Volley requests queue
	 */
	private static void addRequest(@NonNull final Request<?> request)
	{
		instance().getVolleyRequestQueue().add(request);
	}

	/**
	 * Adds a request to the Volley request queue with a given tag
	 *
	 * @param request is the request to be added
	 * @param tag tag identifying the request
	 */
	public static void addRequest(@NonNull final Request<?> request, @NonNull final String tag)
	{
		request.setTag(tag);
		addRequest(request);
	}

	/**
	 * Cancels all the request in the Volley queue for a given tag
	 *
	 * @param tag associated with the Volley requests to be cancelled
	 */
	public static void cancelAllRequests(String tag)
	{
		if (instance().getVolleyRequestQueue() != null)
		{
			instance().getVolleyRequestQueue().cancelAll(tag);
		}
	}

	
	
	
	
	
}
