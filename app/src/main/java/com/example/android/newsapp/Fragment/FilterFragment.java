package com.example.android.newsapp.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DatePicker.*;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import com.example.android.newsapp.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.android.newsapp.R.id.btnSave;
import static com.example.android.newsapp.R.id.etDate;


public class FilterFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, DatePicker.OnDateChangedListener{
    Context mContext;
    EditText etDate = null;
    String beginDate;
    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // store the values selected into a Calendar instance
//        final Calendar c = Calendar.getInstance();
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, monthOfYear);
//        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        etDate.setText((month+1) + "/" + day + "/" + year);
        beginDate = year +"" +(month+1) + day;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container);

    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        etDate = (EditText) view.findViewById(R.id.etDate);
        final CheckBox chkArts = (CheckBox) view.findViewById(R.id.checkbox_art);
        final CheckBox chkFashion = (CheckBox) view.findViewById(R.id.checkbox_fashion_and_style);
        final CheckBox chkSports = (CheckBox) view.findViewById(R.id.checkbox_sports);
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                String date = etDate.getText().toString();
                boolean news_desk = (chkArts.isChecked() || chkFashion.isChecked() || chkSports.isChecked());
                String newsDesk = "news_desk:(";
                if(chkArts.isChecked()){
                    newsDesk = newsDesk + "\"Art\" ";
                }
                if(chkFashion.isChecked()){
                    newsDesk = newsDesk + "\"Fashion\" ";
                }
                if(chkSports.isChecked()){
                    newsDesk = newsDesk + "\"Sports\" ";
                }
                newsDesk = newsDesk + ")";
                Context context = getActivity();
                SharedPreferences sharedPref = context.getSharedPreferences("pref",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("date",beginDate);
                if(news_desk) {
                    editor.putString("newsDesk", newsDesk);
                }
                editor.commit();
            }
        });



        //Might use this
        /*etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePicker.setVisibility(View.VISIBLE);

            }
        });*/
        /*datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
            }
        });*/

        /*datePicker.init(maxYear,maxMonth,maxDay, new OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                datePicker.updateDate(year, month, day);
            }
        });*/
    }


    public Dialog createDatePicker(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);

    }



    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {

    }
}
