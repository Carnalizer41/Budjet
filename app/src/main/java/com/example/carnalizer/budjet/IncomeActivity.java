package com.example.carnalizer.budjet;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class IncomeActivity extends AppCompatActivity {

    DB dbHelper;
    private ListView allTasks;
    private IncomeAdapter adapter;
    private Calendar dateAndTime1=new GregorianCalendar();
    private ImageButton button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        dbHelper = new DB(this);
        allTasks = findViewById(R.id.income_list);
        button = findViewById(R.id.delete_income_btn);
        loadAllTasks();
    }

    private void loadAllTasks()
    {
        ArrayList<Income> taskList = dbHelper.getIncome();
        if(adapter==null)
        {
            adapter = new IncomeAdapter(this,R.layout.income_row, taskList);
            allTasks.setAdapter(adapter);
        }
        else
        {
            adapter.clear();
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClickAddIncome(View view)
    {
        final EditText userGetMoney = new EditText(this);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Новый доход!")
                .setMessage("Введите сумму и дату")
                .setNeutralButton("Дата",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setView(userGetMoney)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("ОТМЕНА",null)
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;
                if(String.valueOf(userGetMoney.getText()).isEmpty())
                {
                    Toast.makeText(IncomeActivity.this, "Введите сумму!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!isNumeric(String.valueOf(userGetMoney.getText())))
                    {
                        Toast.makeText(IncomeActivity.this, "Вы ввели неверное значение суммы", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        wantToCloseDialog = true;
                    }
                }
                if(wantToCloseDialog)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                    Income income = new Income(sdf.format(dateAndTime1.getTime()),
                            Float.parseFloat(String.valueOf(userGetMoney.getText())));
                    dbHelper.insertIncomeData(income.getIncomeDate(),income.getIncomeAmount());
                    loadAllTasks();
                    dialog.dismiss();
                }
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = false;
                //Do stuff, possibly set wantToCloseDialog to true then...
                new DatePickerDialog(IncomeActivity.this, d1,
                        dateAndTime1.get(Calendar.YEAR),
                        dateAndTime1.get(Calendar.MONTH),
                        dateAndTime1.get(Calendar.DAY_OF_MONTH))
                        .show();
                if(wantToCloseDialog)
                    dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d1=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime1.set(Calendar.YEAR, year);
            dateAndTime1.set(Calendar.MONTH, monthOfYear);
            dateAndTime1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    };

    public static boolean isNumeric(String str)
    {
        try
        {
            float f = Float.parseFloat(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public void onClickDeleteIncome(final View view)
    {

        final AlertDialog dialog = new AlertDialog.Builder(IncomeActivity.this)
                .setTitle("Удалить доход?")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View parent = (View) view.getParent();
                        TextView textDate = (TextView) parent.findViewById(R.id.date);
                        String date = String.valueOf(textDate.getText());
                        TextView textAmount = (TextView) parent.findViewById(R.id.money);
                        String s = String.valueOf(textAmount.getText());
                        s = s.replaceAll("₴","");
                        Float amount = Float.parseFloat(s);
                        dbHelper.deleteIncomeData(date, amount);
                        loadAllTasks();
                    }
                })
                .setNegativeButton("ГАЛЯ, ОТМЕНА!",null)
                .create();
        dialog.show();
    }


    public void onClickMainMenu(View view)
    {
        Intent intent = new Intent(IncomeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
