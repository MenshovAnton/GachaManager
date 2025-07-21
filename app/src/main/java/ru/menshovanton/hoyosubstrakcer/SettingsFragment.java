package ru.menshovanton.hoyosubstrakcer;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsFragment extends Fragment {

    TextView blessingOfTheWelkinMoonSelectButton;
    TextView starRailSpecialPassSelectButton;
    TextView interKnotMembershipSelectButton;
    TextView hourTextView;
    TextView minuteTextView;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch notificationsSwitch;

    ImageView edit;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        blessingOfTheWelkinMoonSelectButton = view.findViewById(R.id.blessingOfTheWelkinMoonSelectButton);
        starRailSpecialPassSelectButton = view.findViewById(R.id.starRailSpecialPassSelectButton);
        interKnotMembershipSelectButton = view.findViewById(R.id.interKnotSelectButton);
        notificationsSwitch = view.findViewById(R.id.notificationsSwitch);
        hourTextView = view.findViewById(R.id.hourTextView);
        minuteTextView = view.findViewById(R.id.minutesTextView);
        edit = view.findViewById(R.id.editTime);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        blessingOfTheWelkinMoonSelectButton.setOnClickListener(this::onMoonClick);
        starRailSpecialPassSelectButton.setOnClickListener(this::onPassClick);
        interKnotMembershipSelectButton.setOnClickListener(this::onInterknotClick);

        switch (MainActivity.subType) {
            case 0:
                check(blessingOfTheWelkinMoonSelectButton, getString(R.string.blessing_of_the_welkin_moon_checked));
                break;
            case 1:
                check(starRailSpecialPassSelectButton, getString(R.string.star_rail_special_pass_checked));
                break;
            case 2:
                check(interKnotMembershipSelectButton, getString(R.string.inter_knot_member_checked));
        }

        notificationsSwitch.setOnCheckedChangeListener(this::onCheckedChange);
        notificationsSwitch.setChecked(Notification.allowNotifications);

        edit.setOnClickListener(this::showTimePicker);

        updateTimeDisplay();
    }

    private void onCheckedChange(CompoundButton compoundButton, boolean b) {
        Notification.allowNotifications = b;
    }

    public void onMoonClick(View view) {
        MainActivity.subType = 0;
        check(blessingOfTheWelkinMoonSelectButton, getString(R.string.blessing_of_the_welkin_moon_checked));
    }

    public void onPassClick(View view) {
        MainActivity.subType = 1;
        check(starRailSpecialPassSelectButton, getString(R.string.star_rail_special_pass_checked));
    }

    public void onInterknotClick(View view) {
        MainActivity.subType = 2;
        check(interKnotMembershipSelectButton, getString(R.string.inter_knot_member_checked));
    }

    private void check(TextView view, String str) {
        blessingOfTheWelkinMoonSelectButton.setText(getString(R.string.blessing_of_the_welkin_moon));
        starRailSpecialPassSelectButton.setText(getString(R.string.star_rail_special_pass));
        interKnotMembershipSelectButton.setText(getString(R.string.inter_knot_member));
        view.setText(str);
    }

    public void showTimePicker(View view) {
        TimePickerDialog dialog = new TimePickerDialog(
                MainActivity.context,
                (view1, hourOfDay, minute) -> {
                    AlarmHelper.alarmHour = hourOfDay;
                    AlarmHelper.alarmMinute = minute;
                    updateTimeDisplay();
                    AlarmHelper.cancelAlarm(MainActivity.context);
                    AlarmHelper.setDailyAlarm(MainActivity.context);
                },
                AlarmHelper.alarmHour,
                AlarmHelper.alarmMinute,
                true
        );
        dialog.show();
    }

    @SuppressLint("DefaultLocale")
    private void updateTimeDisplay() {
        hourTextView.setText(String.valueOf(AlarmHelper.alarmHour));
        minuteTextView.setText(String.valueOf(AlarmHelper.alarmMinute));
    }
}