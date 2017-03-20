package com.example.android.newsapp.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import com.example.android.newsapp.R;
import com.example.android.newsapp.databinding.FragmentFilterBinding;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.android.newsapp.R.id.btnSave;
import static com.example.android.newsapp.R.id.etDate;


public class FilterFragment extends DialogFragment {
    private FragmentFilterBinding binding;
    Context mContext;
    EditText etDate = null;
    String beginDate;
    DatePickerDialog.OnDateSetListener date;
    // handle the date selected

//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int day) {
//        // store the values selected into a Calendar instance
////        final Calendar c = Calendar.getInstance();
////        c.set(Calendar.YEAR, year);
////        c.set(Calendar.MONTH, monthOfYear);
////        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//        etDate.setText((month+1) + "/" + day + "/" + year);
//        beginDate = year +"" +(month+1) + day;
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false);
        View view = binding.getRoot();
        return view;

    }
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        this.etDate = binding.etDate;
                //(EditText) view.findViewById(R.id.etDate);
        final Spinner spinner = binding.sort;
                //= (Spinner) view.findViewById(R.id.sort);
        final CheckBox chkArts = binding.checkboxArt;
                //(CheckBox) view.findViewById(R.id.checkbox_art);
        final CheckBox chkFashion = binding.checkboxFashionAndStyle;
                //(CheckBox) view.findViewById(R.id.checkbox_fashion_and_style);
        final CheckBox chkSports = binding.checkboxSports;
                //(CheckBox) view.findViewById(R.id.checkbox_sports);
        Button btnSave = binding.btnSave;
                //(Button) view.findViewById(R.id.btnSave);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pref",Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("art", false)){
            chkArts.setChecked(true);
        }
        if(sharedPreferences.getBoolean("fashion", false)){
            chkFashion.setChecked(true);
        }
        if(sharedPreferences.getBoolean("sports", false)){
            chkSports.setChecked(true);
        }
        etDate.setText(sharedPreferences.getString("displayDate", ""));
        int sortOrder = sharedPreferences.getInt("sort", 0);
        spinner.setSelection(sortOrder);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                etDate.setText((month+1) + "/" + day + "/" + year);
                beginDate = year +"" +(month+1) + day;
            }

        };
        etDate.setOnClickListener(view1 -> {
//                DatePickerFragment newFragment = new DatePickerFragment();
//                newFragment.show(getFragmentManager(), "datePicker");
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, year, month, day);
            //datePickerDialog.getDatePicker().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            datePickerDialog.show();

        });



        btnSave.setOnClickListener(view12 -> {

            String date1 = etDate.getText().toString();
            //String sort = "sort=" + spinner.getSelectedItemPosition();
            int sort = spinner.getSelectedItemPosition();
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
            editor.putBoolean("newsDeskBoolean",news_desk);
            editor.putString("displayDate", date1);
            editor.putInt("sort",sort);
            if(news_desk) {
                editor.putString("newsDesk", newsDesk);
            }
            editor.putBoolean("art",chkArts.isChecked());
            editor.putBoolean("fashion",chkFashion.isChecked());
            editor.putBoolean("sports", chkSports.isChecked());
            editor.commit();
            getDialog().dismiss();

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
}
