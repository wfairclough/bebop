package com.katbutler.bebop.alarmslist;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.katbutler.bebop.BebopIntents;
import com.katbutler.bebop.R;
import com.katbutler.bebop.model.Alarm;
import com.katbutler.bebopcommon.BaseFragment;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by kat on 15-09-05.
 */
public class AlarmsFragment extends BaseFragment<AlarmsPresenter, AlarmsPresenter.AlarmsUi> implements AlarmsPresenter.AlarmsUi {


    private RecyclerView mAlarmRecyclerView;
    private FloatingActionButton mAddAlarmFab;
    private AlarmAdapter mAdapter;

    @Override
    public AlarmsPresenter createPresenter() {
        return new AlarmsPresenter();
    }

    @Override
    public AlarmsPresenter.AlarmsUi getUi() {
        return this;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms, container);

        List<Alarm> alarms = new ArrayList<>();
        alarms.add(new Alarm(new LocalTime(7, 0)));
        alarms.add(new Alarm(new LocalTime(15, 30)));
        alarms.add(new Alarm(new LocalTime(11, 10)));
        alarms.add(new Alarm(new LocalTime(5, 55)));
        alarms.add(new Alarm(new LocalTime(6, 55)));
        alarms.add(new Alarm(new LocalTime(2, 22)));
        alarms.add(new Alarm(new LocalTime(14, 18)));
        alarms.add(new Alarm(new LocalTime(14, 22)));
        alarms.add(new Alarm(new LocalTime(12, 55)));

        LocalTime now = new DateTime(DateTimeZone.forTimeZone(TimeZone.getDefault())).toLocalTime();

        for (int i = 0; i < 100; i=i+5) {
            alarms.add(new Alarm(now.plusMinutes(i)));
        }

        Collections.sort(alarms);

        mAlarmRecyclerView = findViewOnView(view,R.id.alarm_recyclerview);
        mAdapter = new AlarmAdapter(alarms);
        mAdapter.setOnAlarmStateChangeListener(new AlarmAdapter.OnAlarmStateChangeListener() {
            @Override
            public void onAlarmStateChange(Alarm alarm) {
                Intent intent = new Intent(BebopIntents.ACTION_CHANGE_ALARM_STATE);
                intent.putExtra(BebopIntents.EXTRA_ALARM, alarm);

                LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
            }
        });
        mAlarmRecyclerView.setAdapter(mAdapter);
        mAlarmRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAddAlarmFab = findViewOnView(view, R.id.fab);
        mAddAlarmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(new SublimePickerFragment.Callback() {
                    @Override
                    public void onCancelled() {

                    }

                    @Override
                    public void onDateTimeRecurrenceSet(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
                        Alarm alarm = new Alarm(new LocalTime(hourOfDay, minute));
                        mAdapter.addAlarm(alarm);
                    }
                });

                Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                // Valid options
                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getFragmentManager(), "SUBLIME_PICKER");
            }
        });

        return view;
    }

    // Validates & returns SublimePicker options
    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER; // | SublimeOptions.ACTIVATE_RECURRENCE_PICKER;

        options.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);

        options.setDisplayOptions(displayOptions);

        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    @Override
    public List<String> getAlarmList() {
        return null;
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
