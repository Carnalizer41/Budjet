package com.example.carnalizer.budjet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DB extends SQLiteOpenHelper {

    private static final String db_name = "carnalizerApp";
    private static final int db_version = 1;

    private static final String db_table_categories = "categories";
    private static final String db_column_category_name = "categoryName";

    private static final String db_table_subcategories = "subcategories";
    private static final String db_column_subcategory_name = "subcategoryName";
    //ID_category

    private static final String db_table_expense = "expense";
    private static final String db_column_expense_date = "expenseDate";
    private static final String db_column_expense_amount = "expenseAmount";
    //ID_subcategory

    private static final String db_table_income = "income";
    private static final String db_column_income_date = "incomeDate";
    private static final String db_column_income_amount = "incomeAmount";

    private static final String db_table_dates = "dates";
    private static final String db_column_dates_1 = "firstDate";
    private static final String db_column_dates_2 = "secondDate";


    public DB(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");

        String queryCat = String.format("CREATE TABLE %s (ID_category INTEGER PRIMARY KEY AUTOINCREMENT, %s " +
                "TEXT NOT NULL);",db_table_categories, db_column_category_name);
        db.execSQL(queryCat);
        //fillCategories();

        String querySubcat = String.format("CREATE TABLE %s (ID_subcategory INTEGER PRIMARY KEY AUTOINCREMENT, %s " +
                        "TEXT NOT NULL,ID_category INTEGER, FOREIGN KEY (ID_category) REFERENCES %s(ID_category));",
                db_table_subcategories, db_column_subcategory_name,db_table_categories);
        db.execSQL(querySubcat);
        //fillSubcategories();

        String queryExpense = String.format("CREATE TABLE %s (ID_expense INTEGER PRIMARY KEY AUTOINCREMENT, %s " +
                        "TEXT NOT NULL, %s FLOAT NOT NULL, ID_subcategory INTEGER, FOREIGN KEY (ID_subcategory) REFERENCES " +
                        "%s(ID_subcategory));",
                db_table_expense, db_column_expense_date,db_column_expense_amount,db_table_subcategories);
        db.execSQL(queryExpense);

        String queryIncome = String.format("CREATE TABLE %s (ID_income INTEGER PRIMARY KEY AUTOINCREMENT, %s " +
                        "TEXT NOT NULL, %s FLOAT NOT NULL);",
                db_table_income, db_column_income_date,db_column_income_amount);
        db.execSQL(queryIncome);

        String queryDates = String.format("CREATE TABLE %s (ID_dates INTEGER PRIMARY KEY AUTOINCREMENT, %s " +
                        "TEXT NOT NULL, %s TEXT NOT NULL);",
                db_table_dates, db_column_dates_1,db_column_dates_2);
        db.execSQL(queryDates);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query1 = String.format("DELETE TABLE IF EXISTS %s",db_table_categories);
        db.execSQL(query1);
        String query2 = String.format("DELETE TABLE IF EXISTS %s",db_table_subcategories);
        db.execSQL(query2);
        String query3 = String.format("DELETE TABLE IF EXISTS %s",db_table_expense);
        db.execSQL(query3);
        String query4 = String.format("DELETE TABLE IF EXISTS %s",db_table_income);
        db.execSQL(query4);
        String query5 = String.format("DELETE TABLE IF EXISTS %s",db_table_dates);
        db.execSQL(query5);
        onCreate(db);
    }

    public void insertDates(String date1, String date2)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        if(isDatesExists())
            db.execSQL("DELETE FROM "+db_table_dates);
        ContentValues values = new ContentValues();
        values.put(db_column_dates_1, date1);
        values.put(db_column_dates_2, date2);
        db.insert(db_table_dates,null,values);
        db.close();
    }

    public Dates getDates()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+db_column_dates_1 +", "+db_column_dates_2 +" FROM "+ db_table_dates,null);
        Dates dates = new Dates();
        if(cursor.moveToFirst())
        {
            int index1 = cursor.getColumnIndex(db_column_dates_1);
            int index2 = cursor.getColumnIndex(db_column_dates_2);
            dates = new Dates(cursor.getString(index1),cursor.getString(index2));
        }

        cursor.close();
        return dates;
    }

    public boolean isDatesExists()
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT "+db_column_dates_1 +", "+db_column_dates_2 +" FROM "+ db_table_dates,null);
            if(cursor!=null && cursor.getCount()>0)
                return true;
            else
                return false;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    private void fillCategories()
    {
        deleteCategoriesData();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i=1;i<=15;i++)
        {
            String catName = null;
            switch (i)
            {
                case 1:
                    catName = "Регулярное";
                    break;
                case 2:
                    catName = "Развлечения";
                    break;
                case 3:
                    catName = "Подарки";
                    break;
                case 4:
                    catName = "Большие покупки";
                    break;
                case 5:
                    catName = "Образование";
                    break;

            }
            values.put(db_column_category_name, catName);
        }

        db.insertWithOnConflict(db_table_categories,null,values,SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void fillSubcategories()
    {
        deleteSubcategoriesData();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i=1;i<=15;i++)
        {
            String catName = null;
            int id_category = 0;
            switch (i)
            {
                case 1:
                    catName = "Одежда";
                    id_category = 1;
                    break;
                case 2:
                    catName = "Техника";
                    id_category = 4;
                    break;
                case 3:
                    catName = "Благо";
                    id_category = 3;
                    break;
                case 4:
                    catName = "Обучение";
                    id_category = 5;
                    break;
                case 5:
                    catName = "Спорт";
                    id_category = 5;
                    break;
                case 6:
                    catName = "Гигиена";
                    id_category = 1;
                    break;
                case 7:
                    catName = "Ремонт";
                    id_category = 4;
                    break;
                case 8:
                    catName = "Подарки";
                    id_category = 3;
                    break;
                case 9:
                    catName = "Еда";
                    id_category = 1;
                    break;
                case 10:
                    catName = "Цветы";
                    id_category = 3;
                    break;
                case 11:
                    catName = "Проезд";
                    id_category = 1;
                    break;
                case 12:
                    catName = "Кино";
                    id_category = 2;
                    break;
                case 13:
                    catName = "Кафе";
                    id_category = 2;
                    break;
                case 14:
                    catName = "Книги";
                    id_category = 5;
                    break;
                case 15:
                    catName = "Счета";
                    id_category = 1;
                    break;
            }
            values.put(db_column_subcategory_name, catName);
            values.put("ID_category", id_category);
        }

        db.insertWithOnConflict(db_table_subcategories,null,values,SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void insertIncomeData(String date, float amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db_column_income_date, date);
        values.put(db_column_income_amount, amount);
        db.insertWithOnConflict(db_table_income,null,values,SQLiteDatabase.CONFLICT_REPLACE);
    }
    public void insertExpenseData(String date, float amount, String subcategory)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int id = 0;
        switch (subcategory)
        {
            case "Одежда":
                id = 1;
                break;
            case "Техника":
                id = 2;
                break;
            case "Благо":
                id = 3;
                break;
            case "Обучение":
                id = 4;
                break;
            case "Спорт":
                id = 5;
                break;
            case "Гигиена":
                id = 6;
                break;
            case "Ремонт":
                id = 7;
                break;
            case "Подарки":
                id = 8;
                break;
            case "Еда":
                id = 9;
                break;
            case "Цветы":
                id = 10;
                break;
            case "Проезд":
                id = 11;
                break;
            case "Кино":
                id = 12;
                break;
            case "Кафе":
                id = 13;
                break;
            case "Книги":
                id = 14;
                break;
            case "Счета":
                id = 15;
                break;
        }

        values.put(db_column_expense_date, date);
        values.put(db_column_expense_amount, amount);
        values.put("ID_subcategory",id);
        db.insertWithOnConflict(db_table_expense,null,values,SQLiteDatabase.CONFLICT_REPLACE);
    }


    public void deleteCategoriesData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ db_table_categories);
        db.close();
    }

    public void deleteSubcategoriesData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ db_table_subcategories);
        db.close();
    }


    public ArrayList<Income> getIncome()
    {
        ArrayList<Income> allTasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ db_column_income_date +", "+ db_column_income_amount +" FROM "+db_table_income+" ORDER BY "+ db_column_income_date+" DESC", null);
        while(cursor.moveToNext())
        {
            int index1 = cursor.getColumnIndex(db_column_income_date);
            int index2 = cursor.getColumnIndex(db_column_income_amount);
            allTasks.add(new Income(cursor.getString(index1),cursor.getFloat(index2)));
        }
        cursor.close();
        db.close();

        return allTasks;
    }

    public float getIncomeSum()
    {
        float sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM("+ db_column_income_amount +") as TotalIncome FROM "+db_table_income+" WHERE "+ db_column_income_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_income_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+")",null);
        if(cursor.moveToFirst())
        {
            sum = cursor.getFloat(cursor.getColumnIndex("TotalIncome"));
        }
        cursor.close();
        return sum;
    }

    public float getExpenseSum()
    {
        float sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM("+ db_column_expense_amount +") as TotalExpense FROM "+db_table_expense+" WHERE "+ db_column_expense_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_expense_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+")",null);
        if(cursor.moveToFirst())
        {
            sum = cursor.getFloat(cursor.getColumnIndex("TotalExpense"));
        }
        cursor.close();
        return sum;
    }

    public float getCategorySum(String category)
    {
        float sum = 0;
        String subcats = "";
        switch (category)
        {
            case "regular":
                subcats = "WHERE ((ID_subcategory = 1 OR ID_subcategory = 6 OR ID_subcategory = 9 OR ID_subcategory = 11 OR ID_subcategory = 15) AND "+ db_column_expense_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_expense_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+"))";
                break;
            case "gifts":
                subcats = "WHERE ((ID_subcategory = 3 OR ID_subcategory = 8 OR ID_subcategory = 10) AND "+ db_column_expense_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_expense_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+"))";
                break;
            case "entertainment":
                subcats = "WHERE ((ID_subcategory = 12 OR ID_subcategory = 13) AND "+ db_column_expense_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_expense_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+"))";
                break;
            case "self":
                subcats = "WHERE ((ID_subcategory = 4 OR ID_subcategory = 5 OR ID_subcategory = 14) AND "+ db_column_expense_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_expense_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+"))";
                break;
            case "big":
                subcats = "WHERE ((ID_subcategory = 2 OR ID_subcategory = 7) AND "+ db_column_expense_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_expense_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+"))";
                break;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM("+ db_column_expense_amount +") as TotalExpense FROM (SELECT "+db_column_expense_date+", "+ db_column_expense_amount +", ID_subcategory FROM "+db_table_expense+" "+subcats+")",null);
        if(cursor.moveToFirst())
        {
            sum = cursor.getFloat(cursor.getColumnIndex("TotalExpense"));
        }
        cursor.close();
        return sum;
    }

    public ArrayList<Expense> getExpense()
    {
        ArrayList<Expense> allTasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ db_column_expense_date +", "+ db_column_expense_amount +", ID_subcategory FROM "+db_table_expense+" ORDER BY "+ db_column_expense_date+" DESC", null);

        while(cursor.moveToNext())
        {
            int index1 = cursor.getColumnIndex(db_column_expense_date);
            int index2 = cursor.getColumnIndex(db_column_expense_amount);
            int index3 = cursor.getColumnIndex("ID_subcategory");
            int id = cursor.getInt(index3);
            String catName = null;
            switch (id)
            {
                case 1:
                    catName = "Одежда";
                    break;
                case 2:
                    catName = "Техника";
                    break;
                case 3:
                    catName = "Благо";
                    break;
                case 4:
                    catName = "Обучение";
                    break;
                case 5:
                    catName = "Спорт";
                    break;
                case 6:
                    catName = "Гигиена";
                    break;
                case 7:
                    catName = "Ремонт";
                    break;
                case 8:
                    catName = "Подарки";
                    break;
                case 9:
                    catName = "Еда";
                    break;
                case 10:
                    catName = "Цветы";
                    break;
                case 11:
                    catName = "Проезд";
                    break;
                case 12:
                    catName = "Кино";
                    break;
                case 13:
                    catName = "Кафе";
                    break;
                case 14:
                    catName = "Книги";
                    break;
                case 15:
                    catName = "Счета";
                    break;
            }
            allTasks.add(new Expense(cursor.getString(index1),cursor.getFloat(index2),catName));
        }
        cursor.close();
        db.close();
        return allTasks;
    }

    public ArrayList<Expense> getCategoryHistory(String category)
    {
        ArrayList<Expense> allTasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String subcats = "";
        switch (category)
        {
            case "regular":
                subcats = "WHERE ((ID_subcategory = 1 OR ID_subcategory = 6 OR ID_subcategory = 9 OR ID_subcategory = 11 OR ID_subcategory = 15) AND "+ db_column_expense_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_expense_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+"))";
                break;
            case "gifts":
                subcats = "WHERE ((ID_subcategory = 3 OR ID_subcategory = 8 OR ID_subcategory = 10) AND "+ db_column_expense_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_expense_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+"))";
                break;
            case "entertainment":
                subcats = "WHERE ((ID_subcategory = 12 OR ID_subcategory = 13) AND "+ db_column_expense_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_expense_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+"))";
                break;
            case "self":
                subcats = "WHERE ((ID_subcategory = 4 OR ID_subcategory = 5 OR ID_subcategory = 14) AND "+ db_column_expense_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_expense_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+"))";
                break;
            case "big":
                subcats = "WHERE ((ID_subcategory = 2 OR ID_subcategory = 7) AND "+ db_column_expense_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_expense_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+"))";
                break;
        }
        Cursor cursor = db.rawQuery("SELECT "+ db_column_expense_date +", "+ db_column_expense_amount +", ID_subcategory FROM "+db_table_expense+" "+ subcats +" ORDER BY "+ db_column_expense_date+" DESC", null);

        while(cursor.moveToNext())
        {
            int index1 = cursor.getColumnIndex(db_column_expense_date);
            int index2 = cursor.getColumnIndex(db_column_expense_amount);
            int index3 = cursor.getColumnIndex("ID_subcategory");
            int id = cursor.getInt(index3);
            String catName = null;
            switch (id)
            {
                case 1:
                    catName = "Одежда";
                    break;
                case 2:
                    catName = "Техника";
                    break;
                case 3:
                    catName = "Благо";
                    break;
                case 4:
                    catName = "Обучение";
                    break;
                case 5:
                    catName = "Спорт";
                    break;
                case 6:
                    catName = "Гигиена";
                    break;
                case 7:
                    catName = "Ремонт";
                    break;
                case 8:
                    catName = "Подарки";
                    break;
                case 9:
                    catName = "Еда";
                    break;
                case 10:
                    catName = "Цветы";
                    break;
                case 11:
                    catName = "Проезд";
                    break;
                case 12:
                    catName = "Кино";
                    break;
                case 13:
                    catName = "Кафе";
                    break;
                case 14:
                    catName = "Книги";
                    break;
                case 15:
                    catName = "Счета";
                    break;
            }
            allTasks.add(new Expense(cursor.getString(index1),cursor.getFloat(index2),catName));
        }
        cursor.close();
        db.close();
        return allTasks;
    }

    public ArrayList<Expense> getHistory()
    {
        ArrayList<Expense> allTasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ db_column_expense_date +", "+ db_column_expense_amount +", ID_subcategory FROM "+db_table_expense+" WHERE "+ db_column_expense_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_expense_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+") ORDER BY "+ db_column_expense_date+" DESC", null);

        while(cursor.moveToNext())
        {
            int index1 = cursor.getColumnIndex(db_column_expense_date);
            int index2 = cursor.getColumnIndex(db_column_expense_amount);
            int index3 = cursor.getColumnIndex("ID_subcategory");
            int id = cursor.getInt(index3);
            String catName = null;
            switch (id)
            {
                case 1:
                    catName = "Одежда";
                    break;
                case 2:
                    catName = "Техника";
                    break;
                case 3:
                    catName = "Благо";
                    break;
                case 4:
                    catName = "Обучение";
                    break;
                case 5:
                    catName = "Спорт";
                    break;
                case 6:
                    catName = "Гигиена";
                    break;
                case 7:
                    catName = "Ремонт";
                    break;
                case 8:
                    catName = "Подарки";
                    break;
                case 9:
                    catName = "Еда";
                    break;
                case 10:
                    catName = "Цветы";
                    break;
                case 11:
                    catName = "Проезд";
                    break;
                case 12:
                    catName = "Кино";
                    break;
                case 13:
                    catName = "Кафе";
                    break;
                case 14:
                    catName = "Книги";
                    break;
                case 15:
                    catName = "Счета";
                    break;
            }
            allTasks.add(new Expense(cursor.getString(index1),cursor.getFloat(index2),catName));
        }
        cursor = db.rawQuery("SELECT "+ db_column_income_date +", "+ db_column_income_amount +" FROM "+db_table_income+" WHERE "+ db_column_income_date+" >= (SELECT "+ db_column_dates_1+" FROM "+ db_table_dates+") AND "+ db_column_income_date+" <= (SELECT "+ db_column_dates_2+" FROM "+ db_table_dates+") ORDER BY "+ db_column_income_date+" DESC", null);
        while(cursor.moveToNext())
        {
            int index1 = cursor.getColumnIndex(db_column_income_date);
            int index2 = cursor.getColumnIndex(db_column_income_amount);
            allTasks.add(new Expense(cursor.getString(index1),cursor.getFloat(index2),"Доход"));
        }
        cursor.close();
        db.close();
        return allTasks;
    }

    public void deleteIncomeData(String date, Float amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ db_table_income+" WHERE ("+ db_column_income_date +" = '"+date+ "' AND "+db_column_income_amount+" = "+amount+")");
        db.close();
    }

    public void deleteExpenseData(String date, Float amount, String subcategory)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;
        switch (subcategory)
        {
            case "Одежда":
                id = 1;
                break;
            case "Техника":
                id = 2;
                break;
            case "Благо":
                id = 3;
                break;
            case "Обучение":
                id = 4;
                break;
            case "Спорт":
                id = 5;
                break;
            case "Гигиена":
                id = 6;
                break;
            case "Ремонт":
                id = 7;
                break;
            case "Подарки":
                id = 8;
                break;
            case "Еда":
                id = 9;
                break;
            case "Цветы":
                id = 10;
                break;
            case "Проезд":
                id = 11;
                break;
            case "Кино":
                id = 12;
                break;
            case "Кафе":
                id = 13;
                break;
            case "Книги":
                id = 14;
                break;
            case "Счета":
                id = 15;
                break;
        }
        db.execSQL("DELETE FROM "+ db_table_expense+" WHERE "+ db_column_expense_date +" = '"+date+ "' AND "+db_column_expense_amount+" = "+amount+" AND ID_subcategory = "+id);
        db.close();
    }


}
