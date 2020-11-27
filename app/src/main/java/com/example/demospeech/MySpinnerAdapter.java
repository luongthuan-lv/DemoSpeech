package com.example.demospeech;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

public class MySpinnerAdapter implements SpinnerAdapter {


    private Context context;
    private List<Language> LanguageList;

    public MySpinnerAdapter(Context context, List<Language> LanguageList) {
        this.context = context;
        this.LanguageList = LanguageList;
    }


    // giao dien cho hang khi hien thi spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.row, parent, false);

        TextView tvInfo;

        tvInfo = convertView.findViewById(R.id.tvInfo);

        tvInfo.setText(LanguageList.get(position).getLanguageName());

        return convertView;

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return LanguageList.size();
    }

    @Override
    public Language getItem(int position) {
        return LanguageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    // giao dien cho hang duoc chon
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row, parent, false);

        TextView tvInfo;

        tvInfo = convertView.findViewById(R.id.tvInfo);


        tvInfo.setText(LanguageList.get(position).getLanguageName());

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
