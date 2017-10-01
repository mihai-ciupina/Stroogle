package com.probitorg.stroogle.utils;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.probitorg.stroogle.R;

/**
 * Created by Mihai on 18/07/2016.
 */
public class ChallengeListCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public ChallengeListCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        // TODO Auto-generated constructor stub
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO Auto-generated method stub
        /*
        if(cursor.getPosition()%2==1) {            view.setBackgroundColor(context.getResources().getColor(R.color.background_odd, null));
        }        else {            view.setBackgroundColor(context.getResources().getColor(R.color.background_even, null));        }*/

        TextView tvName = (TextView) view.findViewById(R.id.tvBody);
        TextView tvId = (TextView) view.findViewById(R.id.tvPriority);
        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow("game_name"));
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        // Populate fields with extracted properties
        tvName.setText(name);
        tvId.setText(String.valueOf(id));


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // TODO Auto-generated method stub
        //return mInflater.inflate(R.layout.listitem, parent, false);
        return LayoutInflater.from(context).inflate(R.layout.challenge_item, parent, false);
    }

    @Override
    public long getItemId(int position) {
        Object item = getItem(position);
        //return mIdMap.get(item);
        return 999;
    }


}
