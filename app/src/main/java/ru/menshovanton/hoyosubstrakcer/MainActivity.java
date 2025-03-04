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
import java.time.format.TextStyle;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static ConstraintLayout constraintLayout;
    public Date[] dateArray;
    public TextView[] dateViewArray;
    public int toDay;
    public int misses;
    public int claims;

    public final int WISHES_COST = 300;
    public final int PRIMOGEMS_PER_DAY = 90;

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

        String toDayMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
        toDayMonth.substring(0, 1).toUpperCase();

        toDay = LocalDate.now().getDayOfMonth();

        constraintLayout = findViewById(R.id.main);

        if (DataManager.Deserialize(this) == null) {
            dateArray = new Date[31];
            for (int i = 0; i < dateArray.length; i++) {
                dateArray[i] = new Date(i + 1, 0, 0);
            }
        } else {
            dateArray = DataManager.Deserialize(this);
        }

        dateViewArray = new TextView[31];
        for (int i = 0; i < dateViewArray.length; i++) {
            dateViewArray[i] = new TextView(this);
        }

        ImageView[] dateBackArray = new ImageView[31];
        for (int i = 0; i < dateBackArray.length; i++) {
            dateBackArray[i] = new ImageView(this);
        }

        int margin = 1200;
        int topMargin = 250;
        int j = 0;

        for (int i = 0; i < dateArray.length; i++) {
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

        TextView header = findViewById(R.id.header);
        String month = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
        month = month.substring(0, 1).toUpperCase() + month.substring(1);
        header.setText(month);

        dateBackArray[toDay - 1].setImageResource(R.drawable.background_date_today);

        calculate();

        setContentView(constraintLayout);

        DataManager.Serialize(this, dateArray);
    }

    private void createView(Date date, TextView textView, ImageView imageView, int leftMargin, int rightMargin, int topMargin) {
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(120, 120);
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.leftMargin = leftMargin;
        layoutParams.rightMargin = rightMargin;
        layoutParams.topMargin = topMargin;

        textView.setText(String.valueOf(date.id));
        textView.setTextSize(24);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.genshin_font);
        textView.setTypeface(typeface);
        textView.setTextColor(getColor(R.color.white));
        textView.setGravity(Gravity.CENTER);

        imageView.setImageResource(R.drawable.background_date);

        if (date.status == 1) {
            textView.setTextColor(getColor(R.color.checked));
            claims++;
        }

        if (date.status == 0 && date.id != toDay && date.id < toDay && date.subDaysRemaining != 0) {
            textView.setTextColor(getColor(R.color.missed));
            misses++;
        }

        constraintLayout.addView(imageView, layoutParams);
        constraintLayout.addView(textView, layoutParams);
    }

    public void onAddClick(View view)
    {
        dateArray[toDay - 1].subDaysRemaining = dateArray[toDay - 1].subDaysRemaining + 30;
        updateSubscribes();
        calculate();
        Toast.makeText(this, "Луна добавлена!", Toast.LENGTH_SHORT).show();
    }

    public void onCheckClick(View view)
    {
        if (dateArray[toDay - 1].status == 1)
        {
            Toast.makeText(this, "Вы уже отмечались сегодня", Toast.LENGTH_SHORT).show();
        }
        else
        {
            dateArray[toDay - 1].status = 1;
            dateViewArray[toDay - 1].setTextColor(getColor(R.color.checked));
            DataManager.Serialize(this, dateArray);
            Toast.makeText(this, "Отметка выполнена!", Toast.LENGTH_SHORT).show();
            claims++;
            dateArray[toDay - 1].subDaysRemaining = dateArray[toDay - 1].subDaysRemaining - 1;
            updateSubscribes();
            calculate();
            DataManager.Serialize(this, dateArray);
        }
    }

    public void calculate() {
        TextView claimPrimogems = findViewById(R.id.cliamPrimogems);
        String cp = String.valueOf(claims * PRIMOGEMS_PER_DAY);
        claimPrimogems.setText(cp);

        TextView missedPrimogems = findViewById(R.id.missPrimogems);
        String mp = String.valueOf(misses * PRIMOGEMS_PER_DAY);
        missedPrimogems.setText(mp);

        TextView claimWishes = findViewById(R.id.claimWishes);
        String cw = String.valueOf(claims * PRIMOGEMS_PER_DAY / WISHES_COST);
        claimWishes.setText(cw);

        TextView missedWishes = findViewById(R.id.missWishes);
        String mw = String.valueOf(misses * PRIMOGEMS_PER_DAY / WISHES_COST);
        missedWishes.setText(mw);

        int miss = misses * 90;

        TextView laterPrimogems = findViewById(R.id.laterPrimogems);
        String lp = String.valueOf(dateArray[toDay - 1].subDaysRemaining * PRIMOGEMS_PER_DAY - miss);
        laterPrimogems.setText(lp);

        TextView laterWishes = findViewById(R.id.laterWishes);
        String lw = String.valueOf(dateArray[toDay - 1].subDaysRemaining * PRIMOGEMS_PER_DAY - miss / WISHES_COST);
        laterWishes.setText(lw);
    }

    public void updateSubscribes() {
        for (int i = 0; i < dateArray[toDay - 1].subDaysRemaining - toDay; i++) {
            dateArray[toDay + i].subDaysRemaining = dateArray[toDay - 1].subDaysRemaining;
        }
        DataManager.Serialize(this, dateArray);
    }
}