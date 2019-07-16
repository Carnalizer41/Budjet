package com.example.carnalizer.budjet;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity {

    private DonutProgress regular_spend;
    private DonutProgress entertainment_spend;
    private DonutProgress gifts_spend;
    private DonutProgress big_spend;
    private DonutProgress self_spend;
    private CircleProgress total_progress;
    DB dbHelper;
    BudjetSystem budjetSystem;
    private Calendar dateAndTime1=new GregorianCalendar();
    private Calendar dateAndTime2=new GregorianCalendar();
    private TextView currentDateTime1;
    private TextView currentDateTime2;
    private float TotalInc;
    private float TotalExp;
    private ArrayList<OverCat> overCats;
    private ArrayList<OverCat> lessCats;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DB(this);
        regular_spend = findViewById(R.id.regular_spend);
        entertainment_spend = findViewById(R.id.entertainment_spend);
        gifts_spend = findViewById(R.id.gifts_spend);
        big_spend = findViewById(R.id.big_spend);
        self_spend = findViewById(R.id.self_spend);
        total_progress = findViewById(R.id.total_progress);
        regular_spend.setProgress(0);
        entertainment_spend.setProgress(0);
        gifts_spend.setProgress(0);
        big_spend.setProgress(0);
        self_spend.setProgress(0);
        budjetSystem = new BudjetSystem();
        currentDateTime1=(TextView)findViewById(R.id.calendar_text_1);
        currentDateTime2=(TextView)findViewById(R.id.calendar_text_2);
        overCats = new ArrayList<>();
        lessCats = new ArrayList<>();
        setInitialStartDate();
        setAllProgress();

    }

    // установка начальных даты и времени
    private void setInitialStartDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        if(dbHelper.isDatesExists())
        {
            Dates dates = dbHelper.getDates();
            try {
                dateAndTime1.setTime(sdf.parse(dates.getDate1()));
                dateAndTime2.setTime(sdf.parse(dates.getDate2()));
                currentDateTime1.setText(sdf.format(dateAndTime1.getTime()));
                currentDateTime2.setText(sdf.format(dateAndTime2.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else
        {
            if(dateAndTime1.get(Calendar.MONTH)!=0)
                dateAndTime1.set(dateAndTime1.get(Calendar.YEAR),dateAndTime1.get(Calendar.MONTH)-1,dateAndTime1.get(Calendar.DATE));
            else
                dateAndTime1.set(dateAndTime1.get(Calendar.YEAR)-1,dateAndTime1.get(Calendar.MONTH)-1,dateAndTime1.get(Calendar.DATE));

            currentDateTime1.setText(sdf.format(dateAndTime1.getTime()));
            currentDateTime2.setText(sdf.format(dateAndTime2.getTime()));
            dbHelper.insertDates(sdf.format(dateAndTime1.getTime()),sdf.format(dateAndTime2.getTime()));
        }



    }

    private void setInitialDate()
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        currentDateTime1.setText(sdf.format(dateAndTime1.getTime()));
        currentDateTime2.setText(sdf.format(dateAndTime2.getTime()));
        dbHelper.insertDates(sdf.format(dateAndTime1.getTime()),sdf.format(dateAndTime2.getTime()));


    }

    public void setDate1(View view)
    {
        new DatePickerDialog(MainActivity.this, d1,
                dateAndTime1.get(Calendar.YEAR),
                dateAndTime1.get(Calendar.MONTH),
                dateAndTime1.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setDate2(View view)
    {
        new DatePickerDialog(MainActivity.this, d2,
                dateAndTime2.get(Calendar.YEAR),
                dateAndTime2.get(Calendar.MONTH),
                dateAndTime2.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener d1=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cal = new GregorianCalendar();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if(cal.getTimeInMillis()>=dateAndTime2.getTimeInMillis())
            {
                Toast.makeText(MainActivity.this, "Начальная дата не может быть позже конечной", Toast.LENGTH_SHORT).show();
            }
            else
            {
                dateAndTime1.set(Calendar.YEAR, year);
                dateAndTime1.set(Calendar.MONTH, monthOfYear);
                dateAndTime1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setInitialDate();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }

        }
    };

    DatePickerDialog.OnDateSetListener d2=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cal = new GregorianCalendar();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if(cal.getTimeInMillis()<=dateAndTime1.getTimeInMillis())
            {
                Toast.makeText(MainActivity.this, "Конечная дата не может быть раньше начальной", Toast.LENGTH_SHORT).show();
            }
            else
            {
                dateAndTime2.set(Calendar.YEAR, year);
                dateAndTime2.set(Calendar.MONTH, monthOfYear);
                dateAndTime2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setInitialDate();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    };

    private void setAllProgress()
    {
        total_progress.setProgress(getTotalProgress());
        regular_spend.setProgress(getCategoryProgress("regular"));
        entertainment_spend.setProgress(getCategoryProgress("entertainment"));
        big_spend.setProgress(getCategoryProgress("big"));
        self_spend.setProgress(getCategoryProgress("self"));
        gifts_spend.setProgress(getCategoryProgress("gifts"));
    }

    private int getTotalProgress()
    {
        int TotalProgress = 0;
        float TotalIncome = dbHelper.getIncomeSum();
        if(TotalIncome != 0)
        {
            float TotalExpense = dbHelper.getExpenseSum();
            TotalInc = TotalIncome;
            TotalExp = TotalExpense;
            TotalProgress = (int) ((TotalExpense/TotalIncome)*100);
        }




        return TotalProgress;
    }

    private float getCategoryProgress(String category)
    {
        float catProgress = 0;
        float TotalIncome = dbHelper.getIncomeSum();
        TextView lab = new TextView(this);
        String catName = "";
        if(TotalIncome != 0)
        {
            switch (category)
            {
                case "regular":
                    TotalIncome *= (budjetSystem.getRegular());
                    lab = findViewById(R.id.regular_lab);
                    catName = "Регулярное";
                    break;
                case "gifts":
                    TotalIncome *= (budjetSystem.getGifts());
                    lab = findViewById(R.id.gifts_lab);
                    catName = "Подарки";
                    break;
                case "entertainment":
                    TotalIncome *= (budjetSystem.getEntertainment());
                    lab = findViewById(R.id.entertainment_lab);
                    catName = "Развлечения";
                    break;
                case "self":
                    TotalIncome *= (budjetSystem.getSelf());
                    lab = findViewById(R.id.self_lab);
                    catName = "Самообразование";
                    break;
                case "big":
                    TotalIncome *= (budjetSystem.getBig());
                    lab = findViewById(R.id.big_lab);
                    catName = "Большие покупки";
                    break;
            }
            float CatExpense = dbHelper.getCategorySum(category);
            catProgress = (CatExpense/TotalIncome)*100;
            if(catProgress!=0)
            {
                BigDecimal bd = new BigDecimal(Float.toString(catProgress));
                bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                catProgress = bd.floatValue();
            }
            if(catProgress>=100)
            {
                if(catProgress>100)
                {
                    OverCat overCat = new OverCat(catName, CatExpense, TotalIncome);
                    overCats.add(overCat);
                }

                catProgress = 100;
                lab.setTextColor(Color.parseColor("#c71b18"));

            }
            if(catProgress<30)
            {
                OverCat overCat = new OverCat(catName, CatExpense, TotalIncome);
                lessCats.add(overCat);
            }

        }


        return catProgress;
    }

    public void onClickRep(View view)
    {
        String overString = "";
        if(overCats.size()==1)
        {
            overString = "Вы превысили лимит расходов на категорию '"+overCats.get(0).getName()+"' на "+(overCats.get(0).getCurrAmount()-overCats.get(0).getAmount())+" ₴!\nСоветуем в дальнейшем сократить расходы на эту категорию.";
        }
        else {
            if (overCats.size() > 1) {
                overString = "Вы превысили лимит расходов на категории:\n";
                for (int i = 0; i < overCats.size(); i++) {
                    overString += "" + overCats.get(i).getName() + " превышена на " + (overCats.get(i).getCurrAmount() - overCats.get(i).getAmount()) + " ₴!\n";
                }
                overString += "Советуем в дальнейшем сократить расходы на эти категории.";
            }
        }
        String lessString = "";
        if(lessCats.size()==1)
        {
            lessString = "Вы уделяете мало внимания категории '"+lessCats.get(0).getName()+"'";
        }
        else {
            if (lessCats.size() > 1) {
                lessString = "Вы уделяете мало внимания категориям:\n";
                for (int i = 0; i < lessCats.size(); i++) {
                    if(i!=lessCats.size()-1)
                        lessString += "" + lessCats.get(i).getName() + ", ";
                    else
                        lessString += "" + lessCats.get(i).getName() + ".";
                }
            }
        }
        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Отчет за период с "+currentDateTime1.getText()+" по "+currentDateTime2.getText())
                .setMessage("Общая сумма дохода - "+TotalInc+" ₴\nОбщая сумма расходов - "+TotalExp+" ₴\n\n"+overString+"\n\n"+lessString)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }

    public void onClickExpense(View view)
    {
        Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
        startActivity(intent);
    }

    public void onClickIncome(View view)
    {
        Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
        startActivity(intent);
    }

    public void onClickTotal(View view)
    {
        Intent intent = new Intent(MainActivity.this, TotalActivity.class);
        startActivity(intent);
    }

    public static String cat;

    public void onClickCategory(View view)
    {
        switch (view.getId())
        {
            case R.id.regular_spend_btn:
                cat = "regular";
                break;
            case R.id.big_spend_btn:
                cat = "big";
                break;
            case R.id.entertainment_spend_btn:
                cat = "entertainment";
                break;
            case R.id.gifts_spend_btn:
                cat = "gifts";
                break;
            case R.id.self_spend_btn:
                cat = "self";
                break;
        }
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        startActivity(intent);
    }

    public void onClickInfo(View view)
    {

        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("О программе")
                .setMessage("Приложение Budjet разработано с целью оптимизировать Ваш личный бюджет с помощью проверенной 'Системы кувшинов'.\n\nОна распределяет Ваши доходы и расходы на 5 категорий:\n\nРегулярные расходы - 60%\nРазвлечения - 10%\nСамообразование - 10%\nБольшие покупки - 10%\nПодарки - 10%\n\nAndrew&Mary - All rights reserved ©")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }


}
