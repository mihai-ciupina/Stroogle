package com.probitorg.stroogle.network;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Deserializer for a ChallengeObject
 *
 * Convert a JsonObject into a ChallengeObject
 */
public class ChallengeObjectDeserializer implements JsonDeserializer<ChallengeObject>
{
    @Override
    public ChallengeObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        final ChallengeObject challengeObject = new ChallengeObject();
        final JsonObject jsonObject = json.getAsJsonObject();

        //Log.d("xxx", "json= " + json.toString());

        challengeObject.setId(jsonObject.get("id").isJsonNull() ? 0 : jsonObject.get("id").getAsInt());
        challengeObject.setUserIdFrom(jsonObject.get("user_id_from").isJsonNull() ? 0 : jsonObject.get("user_id_from").getAsInt());
        challengeObject.setUserIdTo(jsonObject.get("user_id_to").isJsonNull() ? 0 : jsonObject.get("user_id_to").getAsInt());
        challengeObject.setName(jsonObject.get("game_name").isJsonNull() ? "null" : jsonObject.get("game_name").getAsString());
        challengeObject.setGameStringId(jsonObject.get("game_string_id").isJsonNull() ? "null" : jsonObject.get("game_string_id").getAsString());
        challengeObject.setPoints(jsonObject.get("points").isJsonNull() ? 0 : jsonObject.get("points").getAsInt());
        challengeObject.setStatus(jsonObject.get("status").isJsonNull() ? "" : jsonObject.get("status").getAsString());
        challengeObject.setWinnerId(jsonObject.get("winner_id").isJsonNull() ? 0 : jsonObject.get("winner_id").getAsInt());
        challengeObject.setIsWinner(jsonObject.get("is_winner").isJsonNull() ? 0 : jsonObject.get("is_winner").getAsInt());
        challengeObject.setTime(jsonObject.get("time").isJsonNull() ? 1200 : jsonObject.get("time").getAsInt());
        challengeObject.setCanvas(jsonObject.get("canvas").isJsonNull() ? 0 : jsonObject.get("canvas").getAsInt());
        challengeObject.setDifficultyLevel(jsonObject.get("difficulty_level").isJsonNull() ? 1 : jsonObject.get("difficulty_level").getAsInt());

        return challengeObject;
    }
}
