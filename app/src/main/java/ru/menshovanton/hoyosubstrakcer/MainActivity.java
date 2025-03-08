package ru.menshovanton.hoyosubstrakcer;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static ConstraintLayout constraintLayout;
    public int toDay;
    public static int toDayOfYear;
    public int misses;
    public int claims;
    public static int selectedMonth;
    public static int subsCount;

    public Date[] dateArray;
    public static TextView[] dateViewArray;
    public ImageView[] dateBackArray;

    public final int WISHES_COST = 160;
    public final int PRIMOGEMS_PER_DAY = 90;
    public final int SUMMARY_CLAIM = 2700;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        misses = 0;
        claims = 0;
        subsCount = 0;

        String toDayMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
        toDayMonth.substring(0, 1).toUpperCase();

        selectedMonth = LocalDate.now().getMonth().getValue();
        toDay = LocalDate.now().getDayOfMonth();
        toDayOfYear = LocalDate.now().getDayOfYear();

        constraintLayout = findViewById(R.id.main);

        if (DataManager.Deserialize(this) == null) {
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
            dateArray = DataManager.Deserialize(this);
        }

        dateViewArray = new TextView[365];
        for (int i = 0; i < dateViewArray.length; i++) {
            dateViewArray[i] = new TextView(this);
        }

        dateBackArray = new ImageView[365];
        for (int i = 0; i < dateBackArray.length; i++) {
            dateBackArray[i] = new ImageView(this);
        }

        if (dateArray[toDayOfYear - 1].subDaysRemaining <= 30) {
            subsCount = 1;
        } else if (dateArray[toDayOfYear - 1].subDaysRemaining <= 60) {
            subsCount = 2;
        } else if (dateArray[toDayOfYear - 1].subDaysRemaining <= 90) {
            subsCount = 3;
        } else if (dateArray[toDayOfYear - 1].subDaysRemaining <= 120) {
            subsCount = 4;
        } else if (dateArray[toDayOfYear - 1].subDaysRemaining <= 150) {
            subsCount = 5;
        } else if (dateArray[toDayOfYear - 1].subDaysRemaining <= 180) {
            subsCount = 6;
        }

        drawCalendar();

        setMonthHeader(selectedMonth);
        calculateStats();
        setContentView(constraintLayout);
        DataManager.Serialize(this, dateArray);
    }

    private void setMonthHeader(int month) {
        TextView header = findViewById(R.id.header);
        Month monthObj = Month.of(month);
        Locale locale = Locale.forLanguageTag("ru");
        String print = monthObj.getDisplayName(TextStyle.FULL_STANDALONE, locale);
        header.setText(print.substring(0, 1).toUpperCase() + print.substring(1));
    }

    public void onAddClick(View view)
    {
        if (subsCount <= 6) {
            if (dateArray[toDayOfYear - 1].subDaysRemaining == 0) {
                subsCount = 1;
                for (int i = 0; i < toDayOfYear - 1; i++) {
                    if (dateArray[i].status == 1) {
                        dateArray[i].status = 3;
                    }
                    if (dateArray[i].status == 0 && dateArray[i].subDaysRemaining > 0) {
                        dateArray[i].status = 2;
                    }
                }

                misses = 0;
                claims = 0;

                dateArray[toDayOfYear - 1].subDaysRemaining = 30;
            } else {
                subsCount++;
                dateArray[toDayOfYear - 1].subDaysRemaining += 30;
            }

            updateSubscribes();
            calculateStats();
            Toast.makeText(this, "Луна добавлена!", Toast.LENGTH_SHORT).show();
            onCheckClick(view);
        }
        else
        {   Toast.makeText(this, "Достигнут лимит Лун", Toast.LENGTH_SHORT).show();   }

    }

    public void onCheckClick(View view)
    {
        if (dateArray[toDayOfYear - 1].status == 1)
        {   Toast.makeText(this, "Вы уже отмечались сегодня", Toast.LENGTH_SHORT).show();   }
        else if (dateArray[toDayOfYear - 1].subDaysRemaining == 0)
        {   Toast.makeText(this, "У вас нет активных подписок", Toast.LENGTH_SHORT).show(); }
        else
        {
            dateArray[toDayOfYear - 1].status = 1;
            Toast.makeText(this, "Отметка выполнена!", Toast.LENGTH_SHORT).show();
            claims++;
            calculateStats();
            selectedMonth = LocalDate.now().getMonth().getValue();
            removeCalendar(constraintLayout);
            drawCalendar();
            setMonthHeader(selectedMonth);
            DataManager.Serialize(this, dateArray);
        }
    }

    public void onPreviousMonthClick(View view) {
        if (selectedMonth != 1) {
            selectedMonth--;
            removeCalendar(constraintLayout);
            drawCalendar();
            setMonthHeader(selectedMonth);
        }
    }

    public void onNextMonthClick(View view) {
        if (selectedMonth != 12) {
            selectedMonth++;
            removeCalendar(constraintLayout);
            drawCalendar();
            setMonthHeader(selectedMonth);
        }
    }

    public void calculateStats() {
        int miss = misses * PRIMOGEMS_PER_DAY;
        int claim = claims * PRIMOGEMS_PER_DAY;

        TextView claimPrimogems = findViewById(R.id.cliamPrimogems);
        String cp = String.valueOf(claim);
        claimPrimogems.setText(cp);

        TextView missedPrimogems = findViewById(R.id.missPrimogems);
        String mp = String.valueOf(miss);
        missedPrimogems.setText(mp);

        TextView claimWishes = findViewById(R.id.claimWishes);
        String cw = String.valueOf(claim / WISHES_COST);
        claimWishes.setText(cw);

        TextView missedWishes = findViewById(R.id.missWishes);
        String mw = String.valueOf(miss / WISHES_COST);
        missedWishes.setText(mw);

        TextView laterPrimogems = findViewById(R.id.laterPrimogems);
        int later = SUMMARY_CLAIM * subsCount - claim - miss;
        String lp = "0";
        if (later > 0 && claims > 0) {
            lp = String.valueOf(later);
        }
        laterPrimogems.setText(lp);

        TextView laterWishes = findViewById(R.id.laterWishes);
        int laterW = later / WISHES_COST;
        String lw = "0";
        if (later > 0 && claims > 0) {
            lw = String.valueOf(laterW);
        }
        laterWishes.setText(lw);
    }

    public void updateSubscribes() {
        for (int i = 0; i < dateArray[toDayOfYear - 1].subDaysRemaining; i++) {
            dateArray[toDayOfYear + i].subDaysRemaining = dateArray[toDayOfYear + i - 1].subDaysRemaining - 1;
        }
        DataManager.Serialize(this, dateArray);
    }

    public void createView(Date date, TextView textView, ImageView imageView, int leftMargin, int rightMargin, int topMargin) {
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(120, 120);
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.leftMargin = leftMargin;
        layoutParams.rightMargin = rightMargin;
        layoutParams.topMargin = topMargin;

        textView.setText(String.valueOf(date.dayOfMonth));
        textView.setTextSize(24);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.genshin_font);
        textView.setTypeface(typeface);
        textView.setTextColor(getColor(R.color.white));
        textView.setGravity(Gravity.CENTER);

        if (date.id == toDayOfYear - 1 && selectedMonth == LocalDate.now().getMonth().getValue()) {
            imageView.setImageResource(R.drawable.background_date_today);
        } else {
            imageView.setImageResource(R.drawable.background_date);
        }

        if ((date.status == 1 || date.status == 3) && date.month == selectedMonth) {
            textView.setTextColor(getColor(R.color.checked));
        }

        if ((date.status == 0 || date.status == 2) && date.id != toDayOfYear - 1 && date.subDaysRemaining != 0 && date.id < toDayOfYear && date.month == selectedMonth) {
            textView.setTextColor(getColor(R.color.missed));
        }

        constraintLayout.addView(imageView, layoutParams);
        constraintLayout.addView(textView, layoutParams);
    }

    public void drawCalendar() {
        int margin = 1200;
        int topMargin = 250;
        int j = 0;

        int daysOfYearForMonth = getDaysOfYearForMonth(selectedMonth);

        for (int i = 0; i < 365; i++) {
            if (dateArray[i].status == 0 && dateArray[i].subDaysRemaining > 0 && dateArray[i].id < toDayOfYear - 1) {
                misses++;
            }

            if (dateArray[i].status == 1 && dateArray[i].subDaysRemaining > 0 && dateArray[i].id < toDayOfYear - 1) {
                claims++;
            }

            if (i >= daysOfYearForMonth && i < daysOfYearForMonth + getDaysOfMonth(selectedMonth)) {
                if (j == 7) {
                    topMargin = topMargin + 150;
                    margin = 1200;
                    j = 0;
                }
                if (j <= 3) {
                    margin = margin - 300;
                    createView(dateArray[i], dateViewArray[i], dateBackArray[i], 0, margin, topMargin);
                } else {
                    margin = margin + 300;
                    createView(dateArray[i], dateViewArray[i], dateBackArray[i], margin, 0, topMargin);
                }
                j++;
            }
        }

        if (misses > 0) {
            misses++;
        }

        if (dateArray[toDayOfYear - 1].status == 1) {
            claims++;
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