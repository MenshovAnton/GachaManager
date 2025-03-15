package ru.menshovanton.hoyosubstrakcer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment {

    TextView blessingOfTheWelkinMoonSelectButton;
    TextView starRailSpecialPassSelectButton;
    TextView interKnotMembershipSelectButton;

    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        blessingOfTheWelkinMoonSelectButton = view.findViewById(R.id.blessingOfTheWelkinMoonSelectButton);
        starRailSpecialPassSelectButton = view.findViewById(R.id.starRailSpecialPassSelectButton);
        interKnotMembershipSelectButton = view.findViewById(R.id.interKnotSelectButton);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        blessingOfTheWelkinMoonSelectButton.setOnClickListener(this::onMoonClick);
        starRailSpecialPassSelectButton.setOnClickListener(this::onPassClick);
        interKnotMembershipSelectButton.setOnClickListener(this::onIntertonClick);

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
    }

    public void onMoonClick(View view) {
        MainActivity.subType = 0;
        check(blessingOfTheWelkinMoonSelectButton, getString(R.string.blessing_of_the_welkin_moon_checked));
    }

    public void onPassClick(View view) {
        MainActivity.subType = 1;
        check(starRailSpecialPassSelectButton, getString(R.string.star_rail_special_pass_checked));}

    public void onIntertonClick(View view) {
        MainActivity.subType = 2;
        check(interKnotMembershipSelectButton, getString(R.string.inter_knot_member_checked));}

    private void check(TextView view, String str) {
        blessingOfTheWelkinMoonSelectButton.setText(getString(R.string.blessing_of_the_welkin_moon));
        starRailSpecialPassSelectButton.setText(getString(R.string.star_rail_special_pass));
        interKnotMembershipSelectButton.setText(getString(R.string.inter_knot_member));
        view.setText(str);
    }
}