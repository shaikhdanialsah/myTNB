package dev.lab.electriccalculator;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;

public class ElectricityCalculator extends AppCompatActivity implements View.OnClickListener {

    Button buttonCalculate, buttonReset;
    EditText elecText, rebateText;
    TextView electDisplay, chargeDisplay, rebateDisplay, totalDisplay, validation1, validation2;
    ImageView instructionImage, valid1Icon, valid2Icon;
    Switch switchChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.electricity_calculator);

        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonCalculate.setOnClickListener(this);
        buttonReset = findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(this);
        elecText = findViewById(R.id.elecText);
        rebateText = findViewById(R.id.rebateText);
        electDisplay = findViewById(R.id.elecDisplay);
        chargeDisplay = findViewById(R.id.chargeDisplay);
        rebateDisplay = findViewById(R.id.rebateDisplay);
        totalDisplay = findViewById(R.id.totalDisplay);
        validation1 = findViewById(R.id.validation1);
        validation2 = findViewById(R.id.validation2);
        switchChecker = findViewById(R.id.switchChecker);
        valid1Icon =findViewById(R.id.valid1Icon);
        valid2Icon =findViewById(R.id.valid2Icon);
        instructionImage = findViewById(R.id.instructionImage);
        instructionImage.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Calculator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Add Switch listener
        switchChecker.setOnCheckedChangeListener((buttonView, isChecked) -> editRebateVisible(isChecked));

        // textWatcher for elecText
        elecText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validation1.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    setValidation1();
                    validation1.setText("This field cannot be empty!");
                    return;
                }

                try {
                    double value = Double.parseDouble(s.toString());
                    validation1.setText("Amount in kWh");
                    valid1Icon.setVisibility(View.VISIBLE);
                    valid1Icon.setImageDrawable(getDrawable(R.drawable.success));
                    validation1.setTextColor(getColor(R.color.colorPrimary));
                    elecText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                } catch (NumberFormatException e) {
                    setValidation1();
                    validation1.setText("Please enter a valid number!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    setValidation1();
                    validation1.setText("This field cannot be empty!");
                }
            }
        });

        // textWatcher for rebateText
        rebateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validation2.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    setValidation2();
                    validation2.setText("This field cannot be empty!"); // Invalid number
                    return;
                }

                try {
                    double value = Double.parseDouble(s.toString());
                    if (value > 5 || value < 0) {
                        setValidation2();
                        validation2.setText("Please enter value between 0 and 5 only!"); // Invalid number
                    } else {
                        validation2.setText("Amount in %");
                        validation2.setTextColor(getColor(R.color.colorPrimary));// Clear the message if within range
                        rebateText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                        valid2Icon.setVisibility(View.VISIBLE);
                        valid2Icon.setImageDrawable(getDrawable(R.drawable.success));
                    }
                } catch (NumberFormatException e) {
                    setValidation2();
                    validation2.setText("Please enter a valid number!"); // Invalid number
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    setValidation2();
                    validation2.setText("This field cannot be empty!"); // Invalid number
                }
            }
        });
    }

    //For Switch button to include rebate or not
    public void editRebateVisible(boolean isChecked) {
        if (isChecked) {
            rebateText.setText("");
            rebateText.setVisibility(View.VISIBLE);
            validation2.setVisibility(View.VISIBLE);
            switchChecker.setText("Yes");
            switchChecker.setTextColor(getColor(R.color.colorPrimary));
            switchChecker.setTrackTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));

            if(elecText.getText().toString().isEmpty())
            {
                setValidation1();
                validation1.setText("This field cannot be empty!");
            }
        } else {
            rebateText.setVisibility(View.INVISIBLE);
            validation2.setVisibility(View.INVISIBLE);
            valid2Icon.setVisibility(View.INVISIBLE);
            switchChecker.setText("No");
            switchChecker.setTrackTintList(ColorStateList.valueOf(getColor(R.color.black)));
            switchChecker.setTextColor(getColor(R.color.black));
        }

    }

    public void setValidation1()
    {
        validation1.setTextColor(getColor(R.color.colorError));
        elecText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorError)));
        valid1Icon.setVisibility(View.VISIBLE);
        valid1Icon.setImageDrawable(getDrawable(R.drawable.error));
    }

    public void setValidation2()
    {
        validation2.setTextColor(getResources().getColor(R.color.colorError));
        rebateText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorError)));
        valid2Icon.setVisibility(View.VISIBLE);
        valid2Icon.setImageDrawable(getDrawable(R.drawable.error));
    }

    //After User Click the Calculate Button
    @Override
    public void onClick(View view) {
        if (view == buttonCalculate) {
            electDisplay.setText("");
            chargeDisplay.setText("");
            rebateDisplay.setText("");
            totalDisplay.setText("");
            DecimalFormat df = new DecimalFormat("#,##0.00");
            DecimalFormat ddf = new DecimalFormat("#,##0.0");



            if(rebateText.getVisibility()==View.VISIBLE)
            {
                if (elecText.getText().toString().isEmpty() && rebateText.getText().toString().isEmpty()) {
                    setValidation1();
                    validation1.setText("This field cannot be empty!");
                    setValidation2();
                    validation2.setText("This field cannot be empty!");

                } else if (elecText.getText().toString().isEmpty()) {
                    setValidation1();
                    validation1.setText("This field cannot be empty!");
                } else if (rebateText.getText().toString().isEmpty()) {
                    setValidation2();
                    validation2.setText("This field cannot be empty!");
                }
            }
            else
            {
                if (elecText.getText().toString().isEmpty()) {
                    setValidation1();
                    validation1.setText("This field cannot be empty!");
                }
            }

            try {

                double electUsage = Double.parseDouble(elecText.getText().toString());
                double rebateReceived = 0.0;
                double totalCharges = 0.00;
                double total = 0.00;
                double totalRebate = 0.00;

                if (electUsage <= 200) {
                    totalCharges = electUsage * 0.218;
                } else if (electUsage > 200 && electUsage <= 300) {
                    totalCharges = (200 * 0.218) + ((electUsage - 200) * 0.334);
                } else if (electUsage > 300 && electUsage <= 600) {
                    totalCharges = (200 * 0.218) + (100 * 0.334) + ((electUsage - 300) * 0.516);
                } else if (electUsage > 600 && electUsage <= 900) {
                    totalCharges = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((electUsage - 600) * 0.546);
                } else if (electUsage > 900) {
                    totalCharges = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + (300 * 0.546) + ((electUsage - 900) * 0.571);
                }

                if(rebateText.getVisibility()==View.INVISIBLE)
                {
                    totalRebate = 0.0;
                }

                else if(rebateText.getVisibility()==View.VISIBLE)
                {
                    rebateReceived=Double.parseDouble(rebateText.getText().toString());
                    if (rebateReceived > 5) {
                        throw new IllegalArgumentException("The maximum rebate limit is 5%!");
                    }
                    else
                    {
                        totalRebate = rebateReceived / 100.0 * totalCharges;
                    }

                }

                String totalRebateString = df.format(totalRebate);
                String totalChargesString = df.format(totalCharges);
                electDisplay.setText(electUsage + "kWh");

                if(rebateText.getVisibility()==View.INVISIBLE)
                {
                    rebateDisplay.setText("Not Included" );
                }
                else
                {
                    rebateDisplay.setText("- RM " + totalRebateString);
                }

                chargeDisplay.setText("RM " + totalChargesString);

                totalRebate = Double.parseDouble(totalRebateString.replace(",", ""));
                totalCharges = Double.parseDouble(totalChargesString.replace(",", ""));
                total = totalCharges - totalRebate;
                totalDisplay.setText("RM " + df.format(total));
            } catch (NumberFormatException nfe) {

                String toastMessage = "";
                if(rebateText.getVisibility()==View.VISIBLE)
                {
                    if(rebateText.getText().toString().isEmpty()||elecText.getText().toString().isEmpty())
                    {
                        toastMessage = "Please enter both values!";
                    }
                    else
                        toastMessage ="Both values should be numbers!";
                }
                else
                {
                    if(elecText.getText().toString().isEmpty())
                    {
                        toastMessage = "Please enter electricity amount!";
                    }
                    else
                        toastMessage = "Electricity amount should be a number!";
                }
                    Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException iae) {
                Toast.makeText(this, iae.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Please enter both values in number format!", Toast.LENGTH_SHORT).show();
            }
        } else if (view == instructionImage) {
            Intent intent = new Intent(ElectricityCalculator.this, InstructionPage.class);
            startActivity(intent);
        } else if (view == buttonReset) {
            electDisplay.setText("");
            rebateDisplay.setText("");
            chargeDisplay.setText("");
            totalDisplay.setText("");
            rebateText.setText("");
            elecText.setText("");
            validation1.setText("");
            validation2.setText("");
            valid1Icon.setVisibility(View.INVISIBLE);
            valid2Icon.setVisibility(View.INVISIBLE);
            if(rebateText.getVisibility()==View.VISIBLE)
            {
                switchChecker.setChecked(false);
            }
            rebateText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            elecText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        }
    }
}
