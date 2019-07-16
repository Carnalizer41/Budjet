package com.example.carnalizer.budjet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    DB dbHelper;
    private ListView allTasks;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        dbHelper = new DB(this);
        allTasks = findViewById(R.id.cat_list);

        loadAllTasks();
    }

    private void loadAllTasks()
    {
        setLabel();
        ArrayList<Expense> taskList = dbHelper.getCategoryHistory(MainActivity.cat);
        if(adapter==null)
        {
            adapter = new ExpenseAdapter(this,R.layout.expense_row, taskList);
            allTasks.setAdapter(adapter);
        }
        else
        {
            adapter.clear();
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClickDeleteExpense(View view)
    {
        View parent = (View) view.getParent();
        TextView textDate = (TextView) parent.findViewById(R.id.expense_date);
        TextView textAmount = (TextView) parent.findViewById(R.id.expense_money);
        TextView textCat = (TextView) parent.findViewById(R.id.expense_name);
        final String date = String.valueOf(textDate.getText());
        String s = String.valueOf(textAmount.getText());
        s = s.replaceAll("₴","");
        final Float amount = Float.parseFloat(s);
        final String cat = String.valueOf(textCat.getText());

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Удалить расход?")
                .setMessage("Вы хотите удалить расход на "+cat+" от "+date+" на сумму "+amount+" ?")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteExpenseData(date, amount, cat);
                        loadAllTasks();
                    }
                })
                .setNegativeButton("ГАЛЯ, ОТМЕНА!",null)
                .create();
        dialog.show();
    }

    private void setLabel()
    {
        TextView textView = findViewById(R.id.CategoryLab);
        switch (MainActivity.cat)
        {
            case "regular":
                textView.setText("История регулярных расходов");
                break;
            case "big":
                textView.setText("История больших покупок");
                break;
            case "entertainment":
                textView.setText("История развлечений");
                break;
            case "gifts":
                textView.setText("История подарков");
                break;
            case "self":
                textView.setText("История самообразования");
                break;
        }
    }

    public void onClickMainMenu(View view)
    {
        Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
