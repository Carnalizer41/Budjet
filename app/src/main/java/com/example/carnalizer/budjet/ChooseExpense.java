package com.example.carnalizer.budjet;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ChooseExpense extends AppCompatActivity {

    DB dbHelper;
    private Calendar dateAndTime1=new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_expense);
        dbHelper = new DB(this);
    }

    public void onClickAddExpense(View view)
    {
        String catName = null;
        switch (view.getId())
        {
            case R.id.wear_cat:
                catName = "Одежда";
                break;
            case R.id.tech_cat:
                catName = "Техника";
                break;
            case R.id.support_cat:
                catName = "Благо";
                break;
            case R.id.study_cat:
                catName = "Обучение";
                break;
            case R.id.sport_cat:
                catName = "Спорт";
                break;
            case R.id.soap_cat:
                catName = "Гигиена";
                break;
            case R.id.repair_cat:
                catName = "Ремонт";
                break;
            case R.id.gifts_cat:
                catName = "Подарки";
                break;
            case R.id.food_cat:
                catName = "Еда";
                break;
            case R.id.flowers_cat:
                catName = "Цветы";
                break;
            case R.id.drive_cat:
                catName = "Проезд";
                break;
            case R.id.cinema_cat:
                catName = "Кино";
                break;
            case R.id.cafe_cat:
                catName = "Кафе";
                break;
            case R.id.books_cat:
                catName = "Книги";
                break;
            case R.id.account_cat:
                catName = "Счета";
                break;
        }
        final EditText userGetMoney = new EditText(this);
        final String finalCatName = catName;
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Новый расход - "+catName)
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
                    Toast.makeText(ChooseExpense.this, "Введите сумму!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!isNumeric(String.valueOf(userGetMoney.getText())))
                    {
                        Toast.makeText(ChooseExpense.this, "Вы ввели неверное значение суммы", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        wantToCloseDialog = true;
                    }
                }
                if(wantToCloseDialog)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                    Expense expense = new Expense(sdf.format(dateAndTime1.getTime()),
                            Float.parseFloat(String.valueOf(userGetMoney.getText())), finalCatName);
                    dbHelper.insertExpenseData(expense.getDate(),expense.getAmount(),expense.getID_subcategory());
                    Intent intent = new Intent(ChooseExpense.this, ExpenseActivity.class);
                    startActivity(intent);
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
                new DatePickerDialog(ChooseExpense.this, d1,
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

    public void onClickMainMenu(View view)
    {
        Intent intent = new Intent(ChooseExpense.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickBack(View view)
    {
        Intent intent = new Intent(ChooseExpense.this, ExpenseActivity.class);
        startActivity(intent);
    }
}
