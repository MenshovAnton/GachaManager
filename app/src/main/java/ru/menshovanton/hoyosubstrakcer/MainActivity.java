package ru.menshovanton.hoyosubstrakcer;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

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

        String toDayMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
        toDayMonth.substring(0, 1).toUpperCase();

        int toDay = LocalDate.now().getDayOfMonth();

        constraintLayout = findViewById(R.id.main);

        Date[] dateArray = new Date[31];
        for (int i = 0; i < dateArray.length; i++) {
            dateArray[i] = new Date(i + 1, 0);
        }

        TextView[] dateViewArray = new TextView[31];
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

        dateBackArray[toDay].setImageResource(R.drawable.background_date_today);

        setContentView(constraintLayout);
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

        constraintLayout.addView(imageView, layoutParams);
        constraintLayout.addView(textView, layoutParams);
    }
}