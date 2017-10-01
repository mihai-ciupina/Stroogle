package com.probitorg.stroogle.loader;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.probitorg.stroogle.R;
import com.probitorg.stroogle.app.AppState;
import com.probitorg.stroogle.app.HomeActivityInterface;
import com.probitorg.stroogle.app.MyApplication;
import com.probitorg.stroogle.network.ChallengeObject;
import com.probitorg.stroogle.network.DummyObject;
import com.probitorg.stroogle.network.GsonGetRequest;
import com.probitorg.stroogle.network.GsonPutRequest;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeMenuFragment extends Fragment {

    private AppState appState;
    HomeActivityInterface mCallback;

    @InjectView(R.id.btn_challenges)            Button _challengesButton;
    @InjectView(R.id.btn_challenge_someone)     Button _challengeSomeoneButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		/*code*/

        View view = inflater.inflate(R.layout.home_menu, container, false);

        ButterKnife.inject(this, view);

       _challengesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //update sqlite table with data from server
                //ip//mCallback.setBooleanUpdateLocalChallengeTableFromServer(false);

                final GsonGetRequest<ArrayList<ChallengeObject>> gsonGetRequest =
                        com.probitorg.stroogle.network.ApiRequests.getChallengeObjectArray
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

                //mCallback.updateUI("ChallengeListFragment");
            }
        });

        appState = ((MyApplication) getActivity().getApplication()).getAppStateRef();
		/*code*/

        return view;

    }

    public void OnSuccess(ArrayList<ChallengeObject> challengeObjectArrayList) {
        //Log.d("xxx", "*** OnSuccess: ");

        mCallback.updateLocalChallengeTableFromServer(challengeObjectArrayList);

        appState.setActiveUIFragment("ChallengeListFragment");
        mCallback.updateUI(appState.getActiveUIFragment());

    }

    public void OnFailed(VolleyError error) {
        //Log.d("xxx", "*** OnFailed: ");

        if(error.networkResponse != null) {
            if(error.networkResponse.statusCode == 404) {
                //Log.d("xxx", "*** statusCode == 404");
                Toast.makeText((Activity)mCallback, "No challenges. Try later or ask for more", Toast.LENGTH_LONG).show();
                mCallback.updateLocalChallengeTableFromServer(null);

                appState.setActiveUIFragment("ChallengeListFragment");
                mCallback.updateUI(appState.getActiveUIFragment());

            }
        }
    }







    public HomeMenuFragment() {
    }

    public static HomeMenuFragment newInstance() {
        HomeMenuFragment fragment = new HomeMenuFragment();
        return fragment;
    }

    public static HomeMenuFragment newInstance(Bundle bundle) {
        HomeMenuFragment fragment = new HomeMenuFragment();
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
