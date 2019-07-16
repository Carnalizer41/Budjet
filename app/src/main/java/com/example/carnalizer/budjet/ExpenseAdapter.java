package com.example.carnalizer.budjet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense> {

    private LayoutInflater inflater;
    private int layout;
    private List<Expense> expenses;

    public ExpenseAdapter(Context context, int resource, List<Expense> expenses) {
        super(context, resource, expenses);
        this.expenses = expenses;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView dateView = (TextView) view.findViewById(R.id.expense_date);
        TextView nameView = (TextView) view.findViewById(R.id.expense_name);
        TextView moneyView = (TextView) view.findViewById(R.id.expense_money);

        Expense expense = expenses.get(position);

        dateView.setText(expense.getDate());
        nameView.setText(expense.getID_subcategory());
        moneyView.setText(String.valueOf(expense.getAmount())+" ₴");

        return view;
    }

}
