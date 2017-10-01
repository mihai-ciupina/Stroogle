package com.probitorg.stroogle.db;

/**
 * Created by Mihai on 06/06/2016.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.probitorg.stroogle.network.ChallengeObject;
import com.probitorg.stroogle.network.DummyObject;

public class SQLiteDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "strgl.db";
    // Data Base Version.
    private static final int DATABASE_VERSION = 2;

    public static final String APP_STATE_TABLE_NAME = "app_state";
    public static final String APP_STATE_COLUMN_ID = "id";
    public static final String APP_STATE_COLUMN_USERS_ID = "users_id";
    public static final String APP_STATE_COLUMN_NAME = "users_name";
    public static final String APP_STATE_COLUMN_USERNAME = "users_username";
    public static final String APP_STATE_COLUMN_EMAIL = "users_email";
    public static final String APP_STATE_COLUMN_PASSWORD = "users_password";
    public static final String APP_STATE_COLUMN_POINTS = "users_points";
    public static final String APP_STATE_COLUMN_SKILLS = "users_skills";
    public static final String APP_STATE_COLUMN_USER_STATUS = "user_status";

    public static final String CHALLENGE_TABLE_NAME = "challenges";
    public static final String CHALLENGE_COLUMN_ID = "_id";
    public static final String CHALLENGE_COLUMN_USER_ID_FROM = "user_id_from";
    public static final String CHALLENGE_COLUMN_USER_ID_TO = "user_id_to";
    public static final String CHALLENGE_COLUMN_GAME_STRING_ID = "game_string_id";
    public static final String CHALLENGE_COLUMN_GAME_NAME = "game_name";
    public static final String CHALLENGE_COLUMN_POINTS = "points";
    public static final String CHALLENGE_COLUMN_STATUS = "status";
    public static final String CHALLENGE_COLUMN_WINNER_ID = "winner_id";
    public static final String CHALLENGE_COLUMN_IS_WINNER = "is_winner";
    public static final String CHALLENGE_COLUMN_TIME = "time";
    public static final String CHALLENGE_COLUMN_CANVAS = "canvas";
    public static final String CHALLENGE_COLUMN_DIFFICULTY_LEVEL = "difficulty_level";

    public static final String CHALLENGE_COLUMN_STATUS_CLOSED = "closed";

    //private HashMap hp;

    boolean isCreating = false;
    SQLiteDatabase currentDB = null;



    public SQLiteDB(Context context)
    {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);

        //Log.d("xxx","xx - instantiating the database class SQLiteDB()");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        // Add default value for all tables
        isCreating = true;
        currentDB = db;



        //Log.d("xxx","xx - create table data");
        //Log.d("xxx","xx - create the table");
        db.execSQL(
                "create table " + APP_STATE_TABLE_NAME + " " +
                        "(" +
                        APP_STATE_COLUMN_ID + " integer primary key, " +
                        APP_STATE_COLUMN_USERS_ID + " integer, " +
                        APP_STATE_COLUMN_NAME + " text, " +
                        APP_STATE_COLUMN_USERNAME + " text, " +
                        APP_STATE_COLUMN_EMAIL + " text, " +
                        APP_STATE_COLUMN_PASSWORD + " text, " +
                        APP_STATE_COLUMN_POINTS + " integer, " +
                        APP_STATE_COLUMN_SKILLS + " integer, " +
                        APP_STATE_COLUMN_USER_STATUS + " text)"
        );

        //Log.d("xxx","xx - first time(and the only time) creating the state raw with id 1");
        insertAppState(db, 1, 0, "null","null","null","null",0,0, "alien");

        //Log.d("xxx","xx - create challenge table");
        db.execSQL(
                "create table " + CHALLENGE_TABLE_NAME + " " +
                        "(" +
                        CHALLENGE_COLUMN_ID + " integer primary key, " +
                        CHALLENGE_COLUMN_USER_ID_FROM + " integer, " +
                        CHALLENGE_COLUMN_USER_ID_TO + " integer, " +
                        CHALLENGE_COLUMN_GAME_STRING_ID + " text, " +
                        CHALLENGE_COLUMN_GAME_NAME + " text, " +
                        CHALLENGE_COLUMN_POINTS + " integer, " +
                        CHALLENGE_COLUMN_STATUS + " text, " +
                        CHALLENGE_COLUMN_WINNER_ID + " integer, " +
                        CHALLENGE_COLUMN_IS_WINNER + " integer," +
                        CHALLENGE_COLUMN_TIME + " integer," +
                        CHALLENGE_COLUMN_CANVAS + " integer," +
                        CHALLENGE_COLUMN_DIFFICULTY_LEVEL + " integer" +
                        ")"

        );
        //insertChallengeItem(db, 1, 1, 7, "guessthecardcolor", "Guess the card color", 5, "active");
        //insertChallengeItem(db, 2, 1, 7, "pushthebutton", "Push the button", 3, "active");
        //insertChallengeItem(db, 3, 1, 7, "quizz", "Quizz", 2, "active");
        //insertChallengeItem(db, 444, 1, 7, "xxx", "Guess the card color4", 5, "active", 0, 0);
        //insertChallengeItem(db, 555, 1, 7, "xxx", "Guess the card color5", 5, "active", 0, 0);
        //insertChallengeItem(db, 674, 1, 7, "xxx", "Guess the card color6", 5, "active", 0, 0);

        // release var
        isCreating = false;
        currentDB = null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + APP_STATE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHALLENGE_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertAppState (
            SQLiteDatabase db,
            Integer id,
            Integer users_id,
            String name,
            String username,
            String email,
            String password,
            Integer points,
            Integer skills,
            String userStatus)
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put(APP_STATE_COLUMN_ID, id);
        contentValues.put(APP_STATE_COLUMN_USERS_ID, users_id);
        contentValues.put(APP_STATE_COLUMN_NAME, name);
        contentValues.put(APP_STATE_COLUMN_USERNAME, username);
        contentValues.put(APP_STATE_COLUMN_EMAIL, email);
        contentValues.put(APP_STATE_COLUMN_PASSWORD, password);
        contentValues.put(APP_STATE_COLUMN_POINTS, points);
        contentValues.put(APP_STATE_COLUMN_SKILLS, skills);
        contentValues.put(APP_STATE_COLUMN_USER_STATUS, userStatus);
        db.insert(APP_STATE_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getAppState(){
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor res =  db.rawQuery( "select * from ? where ?=1", new String[] { APP_STATE_TABLE_NAME, APP_STATE_COLUMN_ID } );
        Cursor res =  db.rawQuery("select * from " + APP_STATE_TABLE_NAME + " where " + APP_STATE_COLUMN_ID + "=1", null);
        return res;
    }

    public boolean insertChallengeItem(
            SQLiteDatabase db,
            Integer id,
            Integer user_id_from,
            Integer user_id_to,
            String game_string_id,
            String game_name,
            Integer points,
            String status,
            Integer winner_id,
            Integer is_winner,
            Integer time,
            Integer canvas,
            Integer difficulty_level
            )
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CHALLENGE_COLUMN_ID, id);
        contentValues.put(CHALLENGE_COLUMN_USER_ID_FROM, user_id_from);
        contentValues.put(CHALLENGE_COLUMN_USER_ID_TO, user_id_to);
        contentValues.put(CHALLENGE_COLUMN_GAME_STRING_ID, game_string_id);
        contentValues.put(CHALLENGE_COLUMN_GAME_NAME, game_name);
        contentValues.put(CHALLENGE_COLUMN_POINTS, points);
        contentValues.put(CHALLENGE_COLUMN_STATUS, status);
        contentValues.put(CHALLENGE_COLUMN_WINNER_ID, winner_id);
        contentValues.put(CHALLENGE_COLUMN_IS_WINNER, is_winner);
        contentValues.put(CHALLENGE_COLUMN_TIME, time);
        contentValues.put(CHALLENGE_COLUMN_CANVAS, canvas);
        contentValues.put(CHALLENGE_COLUMN_DIFFICULTY_LEVEL, difficulty_level);

        db.insert(CHALLENGE_TABLE_NAME, null, contentValues);

        return true;
    }

    //not used yet
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, APP_STATE_TABLE_NAME);
        return numRows;
    }

    public boolean updateSQLiteAppState(
            String name,
            String username,
            String email,
            String password,
            String points,
            String skills,
            String userStatus)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(APP_STATE_COLUMN_NAME, name);
        contentValues.put(APP_STATE_COLUMN_USERNAME, username);
        contentValues.put(APP_STATE_COLUMN_EMAIL, email);
        contentValues.put(APP_STATE_COLUMN_PASSWORD, password);
        contentValues.put(APP_STATE_COLUMN_POINTS, points);
        contentValues.put(APP_STATE_COLUMN_SKILLS, skills);
        contentValues.put(APP_STATE_COLUMN_USER_STATUS, userStatus);
        db.update(APP_STATE_TABLE_NAME, contentValues, "" + APP_STATE_COLUMN_ID + "=1", null);
        return true;
    }

    public boolean updateSQLiteAppState(DummyObject dummyObject)
    {
        //Log.d("xxx", "dummyObject5" + dummyObject.getUsername());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(APP_STATE_COLUMN_USERS_ID, dummyObject.getId());
        contentValues.put(APP_STATE_COLUMN_NAME, dummyObject.getName());
        contentValues.put(APP_STATE_COLUMN_USERNAME, dummyObject.getUsername());
        contentValues.put(APP_STATE_COLUMN_EMAIL, dummyObject.getEmail());
        contentValues.put(APP_STATE_COLUMN_PASSWORD, dummyObject.getPassword());
        contentValues.put(APP_STATE_COLUMN_POINTS, dummyObject.getPoints());
        contentValues.put(APP_STATE_COLUMN_SKILLS, dummyObject.getSkills());
        contentValues.put(APP_STATE_COLUMN_USER_STATUS, dummyObject.getStatus());
        db.update(APP_STATE_TABLE_NAME, contentValues, "" + APP_STATE_COLUMN_ID + "=1", null);
        return true;
    }

    public boolean updateUserStatus(String userStatus)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(APP_STATE_COLUMN_USER_STATUS, userStatus);
        db.update(APP_STATE_TABLE_NAME, contentValues, "" + APP_STATE_COLUMN_ID + "=1", null);
        return true;
    }

    public Integer deleteAppState(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(APP_STATE_TABLE_NAME, "" + APP_STATE_COLUMN_ID + "=1", null);
    }


	public Cursor getChallengeList()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        //return db.rawQuery("select * from " + CHALLENGE_TABLE_NAME + " where user_id_to=7", null );
        String[] params = new String[]{ "active" };
        return db.rawQuery("select * from " + CHALLENGE_TABLE_NAME + " where status=?", params);
        //return db.rawQuery("select * from " + CHALLENGE_TABLE_NAME + "", null);
    }



    public void updateLocalChallengeTableFromServer(ArrayList<ChallengeObject> challengeObjectArrayList) {

        //Log.d("xxx", "array: " + challengeObjectArrayList.toString());

        //delete local table
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CHALLENGE_TABLE_NAME, null, null);
        //Log.d("xxx", "delete: " + db.delete(CHALLENGE_TABLE_NAME, null, null));

        //insert new values
        if(challengeObjectArrayList != null) {

            for (ChallengeObject challengeObject : challengeObjectArrayList) {

                insertChallengeItem(challengeObject);

            }

        }

    }

    public boolean insertChallengeItem(ChallengeObject challengeObject)
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CHALLENGE_COLUMN_ID, challengeObject.getId());
        contentValues.put(CHALLENGE_COLUMN_USER_ID_FROM, challengeObject.getUserIdFrom());
        contentValues.put(CHALLENGE_COLUMN_USER_ID_TO, challengeObject.getUserIdTo());
        contentValues.put(CHALLENGE_COLUMN_GAME_STRING_ID, challengeObject.getGameStringId());
        contentValues.put(CHALLENGE_COLUMN_GAME_NAME, challengeObject.getGameName());
        contentValues.put(CHALLENGE_COLUMN_POINTS, challengeObject.getPoints());
        contentValues.put(CHALLENGE_COLUMN_STATUS, challengeObject.getStatus());
        contentValues.put(CHALLENGE_COLUMN_WINNER_ID, challengeObject.getWinnerId());
        contentValues.put(CHALLENGE_COLUMN_IS_WINNER, challengeObject.getIsWinner());
        //Log.d("xxx", "~~~ challengeObject.getTime(): " + challengeObject.getTime());
        contentValues.put(CHALLENGE_COLUMN_TIME, challengeObject.getTime());
        contentValues.put(CHALLENGE_COLUMN_CANVAS, challengeObject.getCanvas());
        contentValues.put(CHALLENGE_COLUMN_DIFFICULTY_LEVEL, challengeObject.getDifficultyLevel());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(CHALLENGE_TABLE_NAME, null, contentValues);
        //Log.d("xxx", "insert: " + db.insert(CHALLENGE_TABLE_NAME, null, contentValues));


        return true;

    }


    public boolean updateChallengeOnSQLite_closeChallenge(int challengeId, int winner_id, boolean isWinner) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CHALLENGE_COLUMN_WINNER_ID, winner_id);
        contentValues.put(CHALLENGE_COLUMN_IS_WINNER, isWinner);
        contentValues.put(CHALLENGE_COLUMN_STATUS, CHALLENGE_COLUMN_STATUS_CLOSED);

        db.update(CHALLENGE_TABLE_NAME, contentValues, "" + CHALLENGE_COLUMN_ID + "=" + challengeId + "", null);

        return true;

    }

    public boolean updateUsersOnSQLite_AddPoints(int oldPoints, int points) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(APP_STATE_COLUMN_POINTS, oldPoints + points);

        db.update(APP_STATE_TABLE_NAME, contentValues, "" + APP_STATE_COLUMN_ID + "=1", null);

        return true;

    }













    /*


	public ArrayList<String> getChallengeList()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from challenges", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CHALLENGE_COLUMN_GAME_NAME)));
            res.moveToNext();
        }
        return array_list;
    }


    @Override
    public SQLiteDatabase getWritableDatabase() {
        // TODO Auto-generated method stub
        if(isCreating && currentDB != null){
            return currentDB;
        }
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        // TODO Auto-generated method stub
        if(isCreating && currentDB != null){
            return currentDB;
        }
        return super.getReadableDatabase();
    }

	*/



}