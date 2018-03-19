package com.example.ibtes.whattoeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private TextView displayText;

    private RadioButton foodType = null;
    private RadioButton price = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayText = findViewById(R.id.displayText);
    }

    public void onFoodTypeClick(View view){

        foodType = (RadioButton) view;

    }

    public void onPriceClick(View view){

        price = (RadioButton) view;

    }

    public void search(View view){

        if(foodType == null || price == null){
            Toast.makeText(this, "Please select a price and type", Toast.LENGTH_SHORT).show();
            return;
        }

        //Conditional Logic to find location

        foodType.setChecked(false);
        price.setChecked(false);

        foodType = null;
        price = null;

    }

}
