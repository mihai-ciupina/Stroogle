package com.probitorg.stroogle.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
//import com.sottocorp.okhttpvolleygson.BuildConfig;

import java.util.ArrayList;

/**
 * Api requests
 */
public class ApiRequests
{
    final static String apiDomainName = "http://strgl.probitorg.com/index.php";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(DummyObject.class, new DummyObjectDeserializer())
            .create();

    private static final Gson gsonChallenge = new GsonBuilder()
            .registerTypeAdapter(ChallengeObject.class, new ChallengeObjectDeserializer())
            .create();


    /**
     * Returns a dummy object
     *
     * @param listener is the listener for the correct answer
     * @param errorListener is the listener for the error response
     *
     * @return {@link GsonGetRequest}
     */
    public static GsonGetRequest<DummyObject> getDummyObject
    (
            @NonNull final Response.Listener<DummyObject> listener,
            @NonNull final Response.ErrorListener errorListener
    )
    {
        final String url = apiDomainName + "/users/user/id/1";

        return new GsonGetRequest<>
                (
                        url,
                        new TypeToken<DummyObject>() {}.getType(),
                        gson,
                        listener,
                        errorListener
                );
    }

    /**
     * Returns a dummy object's array
     *
     * @param listener is the listener for the correct answer
     * @param errorListener is the listener for the error response
     *
     * @return {@link GsonGetRequest}
     */
    public static GsonGetRequest<ArrayList<DummyObject>> getDummyObjectArray
    (
            @NonNull final Response.Listener<ArrayList<DummyObject>> listener,
            @NonNull final Response.ErrorListener errorListener
    )
    {
        final String url = apiDomainName + "/users/user/id/1";

        return new GsonGetRequest<>
                (
                        url,
                        new TypeToken<ArrayList<DummyObject>>() {}.getType(),
                        gson,
                        listener,
                        errorListener
                );
    }

    /**
     * Returns a dummy object's array
     *
     * @param listener is the listener for the correct answer
     * @param errorListener is the listener for the error response
     *
     * @return {@link GsonGetRequest}
     */
    public static GsonGetRequest<ArrayList<ChallengeObject>> getChallengeObjectArray
    (
            @NonNull final Response.Listener<ArrayList<ChallengeObject>> listener,
            @NonNull final Response.ErrorListener errorListener,
            int user_id_to
    )
    {
        final String url = apiDomainName + "/challenges/challenge/user_id_to/" + user_id_to;
        //Log.d("xxx", "user_id_to: " + user_id_to);

        final GsonGetRequest gsonGetRequest = new GsonGetRequest<>
                (
                        url,
                        new TypeToken<ArrayList<ChallengeObject>>() {}.getType(),
                        gsonChallenge,
                        listener,
                        errorListener
                );

        gsonGetRequest.setShouldCache(false);

        return gsonGetRequest;
    }


    /**
     * An example call (not used in this app example) to demonstrate how to do a Volley POST call
     * and parse the response with Gson.
     *
     * @param listener is the listener for the success response
     * @param errorListener is the listener for the error response
     *
     * @return {@link GsonPostRequest}
     */
    public static GsonPostRequest getDummyObjectArrayWithPost
    (
            @NonNull final Response.Listener<DummyObject> listener,
            @NonNull final Response.ErrorListener errorListener,
            @NonNull final String body

    )
    {
        final String url = apiDomainName + "/users/user_add_points/";

        //Log.d("xxx", "---" + body);

        final GsonPostRequest gsonPostRequest = new GsonPostRequest<>
                (
                        url,
                        body,
                        new TypeToken<DummyObject>() {}.getType(),
                        gson,
                        listener,
                        errorListener
                );

        gsonPostRequest.setShouldCache(false);

        return gsonPostRequest;
    }









    /**
     * Returns a dummy object
     *
     * @param listener is the listener for the correct answer
     * @param errorListener is the listener for the error response
     *
     * @return {@link GsonPutRequest}
     */
    public static GsonPutRequest<DummyObject> putDummyObject
    (
            @NonNull final Response.Listener<DummyObject> listener,
            @NonNull final Response.ErrorListener errorListener,
            @NonNull final String body
    )
    {
        final String url = apiDomainName + "/users/user/";

        final GsonPutRequest gsonPutRequest = new GsonPutRequest<>
                (
                        url,
                        body,
                        new TypeToken<DummyObject>() {}.getType(),
                        gson,
                        listener,
                        errorListener
                );

        gsonPutRequest.setShouldCache(false);

        return gsonPutRequest;
    }





    public static GsonPutRequest<DummyObject> login
            (
                    @NonNull final Response.Listener<DummyObject> listener,
                    @NonNull final Response.ErrorListener errorListener,
                    @NonNull final String body
            )
    {
        final String url = apiDomainName + "/users/user_login/";

        final GsonPutRequest gsonPutRequest = new GsonPutRequest<>
                (
                        url,
                        body,
                        new TypeToken<DummyObject>() {}.getType(),
                        gson,
                        listener,
                        errorListener
                );

        gsonPutRequest.setShouldCache(false);

        return gsonPutRequest;
    }

    public static GsonPostRequest<ChallengeObject> updateChallengeOnServer_closeChallenge
            (
                    @NonNull final Response.Listener<ChallengeObject> listener,
                    @NonNull final Response.ErrorListener errorListener,
                    @NonNull final String body
            )
    {
        final String url = apiDomainName + "/challenges/challenge/";

        //Log.d("xxx", "---" + body);

        final GsonPostRequest gsonPostRequest = new GsonPostRequest<>
                (
                        url,
                        body,
                        new TypeToken<ChallengeObject>() {}.getType(),
                        gsonChallenge,
                        listener,
                        errorListener
                );

        gsonPostRequest.setShouldCache(false);

        return gsonPostRequest;
    }





    /**
     * Returns a dummy object's array
     *
     * @param listener is the listener for the correct answer
     * @param errorListener is the listener for the error response
     *
     * @return {@link GsonGetRequest}
     */
    public static GsonGetRequest<ArrayList<ChallengeObject>> getChallengeObjectArrayAfterAskForMoreChallenges
    (
            @NonNull final Response.Listener<ArrayList<ChallengeObject>> listener,
            @NonNull final Response.ErrorListener errorListener,
            int user_id_to
    )
    {
        final String url = apiDomainName + "/challenges/challenge_and_ask/user_id_to/" + user_id_to;
        //Log.d("xxx", "### user_id_to: " + user_id_to);

        final GsonGetRequest gsonGetRequest = new GsonGetRequest<>
                (
                        url,
                        new TypeToken<ArrayList<ChallengeObject>>() {}.getType(),
                        gsonChallenge,
                        listener,
                        errorListener
                );

        gsonGetRequest.setShouldCache(false);

        return gsonGetRequest;
    }




}

/*        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "Ficus");
        jsonObject.addProperty("surname", "Kirkpatrick");

        final JsonArray squareGuys = new JsonArray();
        final JsonObject dev1 = new JsonObject();
        final JsonObject dev2 = new JsonObject();
        dev1.addProperty("name", "Jake Wharton");
        dev2.addProperty("name", "Jesse Wilson");
        squareGuys.add(dev1);
        squareGuys.add(dev2);

        jsonObject.add("squareGuys", squareGuys);
*/