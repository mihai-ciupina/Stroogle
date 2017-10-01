package com.probitorg.stroogle.network;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Deserializer for a dummy object
 *
 * Convert a JsonObject into a Dummy object.
 */
public class DummyObjectDeserializer implements JsonDeserializer<DummyObject>
{
    @Override
    public DummyObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        final DummyObject dummyObject = new DummyObject();
        final JsonObject jsonObject = json.getAsJsonObject();

        //Log.d("xxx", "json= " + json.toString());

        dummyObject.setId(jsonObject.get("id").isJsonNull() ? 0 : jsonObject.get("id").getAsInt());
        dummyObject.setName(jsonObject.get("name").isJsonNull() ? "null" : jsonObject.get("name").getAsString());
        dummyObject.setUsername(jsonObject.get("username").isJsonNull() ? "null" : jsonObject.get("username").getAsString());
        dummyObject.setEmail(jsonObject.get("email").isJsonNull() ? "null" : jsonObject.get("email").getAsString());
        dummyObject.setPassword(jsonObject.get("password").isJsonNull() ? "null" : jsonObject.get("password").getAsString());
        dummyObject.setDateCreated(jsonObject.get("date_created").isJsonNull() ? "0000-00-00 00:00:00" : jsonObject.get("date_created").getAsString());
        dummyObject.setDateUpdated(jsonObject.get("date_updated").isJsonNull() ? "0000-00-00 00:00:00" : jsonObject.get("date_updated").getAsString());
        dummyObject.setPoints(jsonObject.get("points").isJsonNull() ? 0 : jsonObject.get("points").getAsInt());
        dummyObject.setSkills(jsonObject.get("skills").isJsonNull() ? 0 : jsonObject.get("skills").getAsInt());
        dummyObject.setStatus(jsonObject.get("status").isJsonNull() ? "alien" : jsonObject.get("status").getAsString());

        return dummyObject;
    }
}
