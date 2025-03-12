package ru.menshovanton.hoyosubstrakcer;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Calendar {
    public Date[] dateArray;
    public TextView[] dateViewArray;
    public ImageView[] dateBackArray;
    public Context context;
    public MainActivity mainActivity;

    Calendar(Context context, MainActivity mainActivity) {
        this.context = context;
        this.mainActivity = mainActivity;

        if (DataManager.Deserialize(context) == null) {
            dateArray = new Date[365];
            int day = 0;
            int month = 1;
            for (int i = 0; i < dateArray.length; i++) {
                day++;
                if (day > getDaysOfMonth(month)) {
                    month++;
                    day = 0;
                    i--;
                } else {
                    dateArray[i] = new Date(i, day, 0, 0, month);
                }
            }
        } else {
            dateArray = DataManager.Deserialize(context);
        }

        dateViewArray = new TextView[365];
        for (int i = 0; i < dateViewArray.length; i++) {
            dateViewArray[i] = new TextView(context);
        }

        dateBackArray = new ImageView[365];
        for (int i = 0; i < dateBackArray.length; i++) {
            dateBackArray[i] = new ImageView(context);
        }
    }

    public void drawCalendar() {
        int margin = 1200;
        int topMargin = 250;
        int j = 0;

        int daysOfYearForMonth = getDaysOfYearForMonth(HomeFragment.selectedMonth);

        for (int i = 0; i < 365; i++) {
            if (dateArray[i].status == 0 && dateArray[i].subDaysRemaining > 0 && dateArray[i].id < HomeFragment.toDayOfYear - 1) {
                HomeFragment.misses++;
            }

            if (dateArray[i].status == 1 && dateArray[i].subDaysRemaining > 0 && dateArray[i].id < HomeFragment.toDayOfYear - 1) {
                HomeFragment.claims++;
            }

            if (i >= daysOfYearForMonth && i < daysOfYearForMonth + getDaysOfMonth(HomeFragment.selectedMonth)) {
                if (j == 7) {
                    topMargin = topMargin + 150;
                    margin = 1200;
                    j = 0;
                }
                if (j <= 3) {
                    margin = margin - 300;
                    HomeFragment.createView(dateArray[i], dateViewArray[i], dateBackArray[i], 0, margin, topMargin);
                } else {
                    margin = margin + 300;
                    HomeFragment.createView(dateArray[i], dateViewArray[i], dateBackArray[i], margin, 0, topMargin);
                }
                j++;
            }
        }

        if (HomeFragment.misses > 0) {
            HomeFragment.misses++;
        }

        if (dateArray[HomeFragment.toDayOfYear - 1].status == 1) {
            HomeFragment.claims++;
        }
    }

    public void removeCalendar(ConstraintLayout constraintLayout) {
        for (TextView textView : dateViewArray) {
            constraintLayout.removeView(textView);
        }
        for (ImageView imageView : dateBackArray) {
            constraintLayout.removeView(imageView);
        }
    }

    public enum Months {
        Default,
        January,
        February,
        March,
        April,
        May,
        June,
        July,
        August,
        September,
        October,
        November,
        December
    }

    public static int getDaysOfMonth(int month) {
        if (month == Months.January.ordinal() ||
                month == Months.March.ordinal() ||
                month == Months.May.ordinal() ||
                month == Months.July.ordinal() ||
                month == Months.August.ordinal() ||
                month == Months.October.ordinal() ||
                month == Months.December.ordinal()) {
            return 31;
        } else if (month == Months.February.ordinal()) {
            return 28;
        } else {
            return 30;
        }
    }

    public static int getDaysOfYearForMonth(int month) {
        int num = 0;
        for (int i = 1; i < month; i++) {
            num += getDaysOfMonth(i);
        }
        return num;
    }
}
