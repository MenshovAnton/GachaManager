package ru.menshovanton.hoyosubstrakcer;

import static androidx.core.content.ContextCompat.getColor;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class HomeFragment extends Fragment {
    TextView subsCounter;
    static ConstraintLayout constraintLayout;
    Button checkButton;
    Button addButton;
    Button previousMonthButton;
    Button nextMonthButton;

    public Calendar calendar;
    public int toDay;
    public static int toDayOfYear;
    public static int misses;
    public static int claims;
    public static int selectedMonth;
    public static int subsCount;

    public final int WISHES_COST = 160;
    public final int PRIMOGEMS_PER_DAY = 90;
    public final int SUMMARY_CLAIM = 2700;

    public HomeFragment() {}

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        misses = 0;
        claims = 0;
        subsCount = 0;

        String toDayMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
        toDayMonth.substring(0, 1).toUpperCase();

        selectedMonth = LocalDate.now().getMonth().getValue();
        toDay = LocalDate.now().getDayOfMonth();
        toDayOfYear = LocalDate.now().getDayOfYear();


        calendar = new Calendar(MainActivity.context, MainActivity.mainActivity);

        if (calendar.dateArray[toDayOfYear - 1].subDaysRemaining <= 30) { subsCount = 1; }
        else if (calendar.dateArray[toDayOfYear - 1].subDaysRemaining <= 60) { subsCount = 2;}
        else if (calendar.dateArray[toDayOfYear - 1].subDaysRemaining <= 90) { subsCount = 3; }
        else if (calendar.dateArray[toDayOfYear - 1].subDaysRemaining <= 120) { subsCount = 4; }
        else if (calendar.dateArray[toDayOfYear - 1].subDaysRemaining <= 150) { subsCount = 5; }
        else if (calendar.dateArray[toDayOfYear - 1].subDaysRemaining <= 180) { subsCount = 6; }

        DataManager.Serialize(MainActivity.context, calendar.dateArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        subsCounter = view.findViewById(R.id.subsCount);
        constraintLayout = view.findViewById(R.id.constraint);
        checkButton = view.findViewById(R.id.check);
        addButton = view.findViewById(R.id.add);
        previousMonthButton = view.findViewById(R.id.previous);
        nextMonthButton = view.findViewById(R.id.next);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendar.drawCalendar();
        setMonthHeader(selectedMonth);
        calculateStats();

        checkButton.setOnClickListener(this::onCheckClick);
        addButton.setOnClickListener(this::onAddClick);
        previousMonthButton.setOnClickListener(this::onPreviousMonthClick);
        nextMonthButton.setOnClickListener(this::onNextMonthClick);
    }

    public void calculateStats() {
        int missedPrimogemsCount = misses * PRIMOGEMS_PER_DAY;
        int claimPrimogemsCount = claims * PRIMOGEMS_PER_DAY;
        int laterPrimogemsCount = SUMMARY_CLAIM * subsCount - claimPrimogemsCount - missedPrimogemsCount;
        int laterWishesCount = laterPrimogemsCount / WISHES_COST;

        String laterPrimogemsText = "0";
        String laterWishesText = "0";
        String claimPrimogemsText = String.valueOf(claimPrimogemsCount);
        String missedPrimogemsText = String.valueOf(missedPrimogemsCount);
        String claimWishesText = String.valueOf(claimPrimogemsCount / WISHES_COST);
        String missedWishesText = String.valueOf(missedPrimogemsCount / WISHES_COST);

        TextView claimPrimogems = getView().findViewById(R.id.cliamPrimogems);
        claimPrimogems.setText(claimPrimogemsText);

        TextView missedPrimogems = getView().findViewById(R.id.missPrimogems);
        missedPrimogems.setText(missedPrimogemsText);

        TextView claimWishes = getView().findViewById(R.id.claimWishes);
        claimWishes.setText(claimWishesText);

        TextView missedWishes = getView().findViewById(R.id.missWishes);
        missedWishes.setText(missedWishesText);

        TextView laterPrimogems = getView().findViewById(R.id.laterPrimogems);
        if (laterPrimogemsCount > 0 && claims > 0)
        {   laterPrimogemsText = String.valueOf(laterPrimogemsCount);   }
        laterPrimogems.setText(laterPrimogemsText);

        TextView laterWishes = getView().findViewById(R.id.laterWishes);
        if (laterPrimogemsCount > 0 && claims > 0)
        {   laterWishesText = String.valueOf(laterWishesCount); }
        laterWishes.setText(laterWishesText);
    }

    public void updateSubscribes() {
        for (int i = 0; i < calendar.dateArray[toDayOfYear - 1].subDaysRemaining; i++) {
            calendar.dateArray[toDayOfYear + i].subDaysRemaining = calendar.dateArray[toDayOfYear + i - 1].subDaysRemaining - 1;
        }
        DataManager.Serialize(MainActivity.context, calendar.dateArray);
    }

    @SuppressLint("SetTextI18n")
    public void setMonthHeader(int month) {
        TextView header = getView().findViewById(R.id.header);
        Month monthObj = Month.of(month);
        Locale locale = Locale.forLanguageTag("ru");
        String print = monthObj.getDisplayName(TextStyle.FULL_STANDALONE, locale);
        header.setText(print.substring(0, 1).toUpperCase() + print.substring(1));
    }

    public static void createView(Date date, TextView textView, ImageView imageView, int leftMargin, int rightMargin, int topMargin) {
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(120, 120);
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.leftMargin = leftMargin;
        layoutParams.rightMargin = rightMargin;
        layoutParams.topMargin = topMargin;

        textView.setText(String.valueOf(date.dayOfMonth));
        textView.setTextSize(24);
        Typeface typeface = ResourcesCompat.getFont(MainActivity.context, R.font.genshin_font);
        textView.setTypeface(typeface);
        textView.setTextColor(getColor(MainActivity.context, R.color.white));
        textView.setGravity(Gravity.CENTER);

        if (date.id == toDayOfYear - 1 && selectedMonth == LocalDate.now().getMonth().getValue()) {
            imageView.setImageResource(R.drawable.background_date_today);
        } else {
            imageView.setImageResource(R.drawable.background_date);
        }

        if ((date.status == 1 || date.status == 3) && date.month == selectedMonth) {
            textView.setTextColor(getColor(MainActivity.context, R.color.checked));
        }

        if ((date.status == 0 || date.status == 2) && date.id != toDayOfYear - 1 && date.subDaysRemaining != 0 && date.id < toDayOfYear && date.month == selectedMonth) {
            textView.setTextColor(getColor(MainActivity.context, R.color.missed));
        }

        constraintLayout.addView(imageView, layoutParams);
        constraintLayout.addView(textView, layoutParams);
    }

    public void onAddClick(View view)
    {
        if (subsCount <= 6) {
            if (calendar.dateArray[toDayOfYear - 1].subDaysRemaining == 0)
            {
                subsCount = 1;
                for (int i = 0; i < toDayOfYear - 1; i++) {
                    if (calendar.dateArray[i].status == 1) {
                        calendar.dateArray[i].status = 3;
                    }
                    if (calendar.dateArray[i].status == 0 && calendar.dateArray[i].subDaysRemaining > 0) {
                        calendar.dateArray[i].status = 2;
                    }
                }

                misses = 0;
                claims = 0;

                calendar.dateArray[toDayOfYear - 1].subDaysRemaining = 30;
            }
            else
            {
                subsCount++;
                calendar.dateArray[toDayOfYear - 1].subDaysRemaining += 30;
            }

            updateSubscribes();
            calculateStats();
            Toast.makeText(MainActivity.mainActivity, getString(R.string.add_sub), Toast.LENGTH_SHORT).show();
            onCheckClick(view);
        }
        else
        {   Toast.makeText(MainActivity.mainActivity, getString(R.string.subs_limit), Toast.LENGTH_SHORT).show(); }

    }

    public void onCheckClick(View view)
    {
        if (calendar.dateArray[toDayOfYear - 1].status == 1)
        {   Toast.makeText(MainActivity.mainActivity, getString(R.string.already_cheked), Toast.LENGTH_SHORT).show();  }
        else if (calendar.dateArray[toDayOfYear - 1].subDaysRemaining == 0)
        {   Toast.makeText(MainActivity.mainActivity, getString(R.string.active_subs_null), Toast.LENGTH_SHORT).show();    }
        else
        {
            calendar.dateArray[toDayOfYear - 1].status = 1;
            Toast.makeText(MainActivity.mainActivity, getString(R.string.check_today), Toast.LENGTH_SHORT).show();
            claims++;
            calculateStats();
            selectedMonth = LocalDate.now().getMonth().getValue();
            calendar.removeCalendar(constraintLayout);
            calendar.drawCalendar();
            setMonthHeader(selectedMonth);
            DataManager.Serialize(MainActivity.context, calendar.dateArray);
        }
    }

    public void onPreviousMonthClick(View view) {
        if (selectedMonth != 1) {
            selectedMonth--;
            calendar.removeCalendar(constraintLayout);
            calendar.drawCalendar();
            setMonthHeader(selectedMonth);
        }
    }

    public void onNextMonthClick(View view) {
        if (selectedMonth != 12) {
            selectedMonth++;
            calendar.removeCalendar(constraintLayout);
            calendar.drawCalendar();
            setMonthHeader(selectedMonth);
        }
    }
}