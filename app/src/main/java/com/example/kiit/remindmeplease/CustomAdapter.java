package com.example.kiit.remindmeplease;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter {

    public CustomAdapter(@NonNull Context context, List reminders) {
        super(context, R.layout.rows,reminders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View custom = layoutInflater.inflate(R.layout.rows,parent,false);
        String event = String.valueOf(getItem(position));
        int index = event.indexOf('@');

        TextView t1 = custom.findViewById(R.id.event1);
        TextView t2 = custom.findViewById(R.id.time1);
        t1.setText(event.substring(index+1,event.length()));
        t2.setText(event.substring(0,index));
        return custom;
    }
}
