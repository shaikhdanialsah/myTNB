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
    ImageView instructionImage;
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

        instructionImage = findViewById(R.id.instructionImage);
        instructionImage.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Electricity Calculator");
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
                    validation1.setText(""); // Clear any previous message
                    elecText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorError)));
                    return;
                }

                try {
                    double value = Double.parseDouble(s.toString());
                    validation1.setText("Amount in kWh");
                    validation1.setTextColor(getColor(R.color.colorPrimary));
                    elecText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                } catch (NumberFormatException e) {
                    validation1.setText("Please enter a valid number!"); // Invalid number
                    validation1.setTextColor(getColor(R.color.colorError));
                    elecText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorError)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    validation1.setText("This field cannot be empty!"); // When input is cleared
                    validation1.setTextColor(getColor(R.color.colorError));
                }
            }
        });

        // textWatcher for rebateText
        rebateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validation2.setText("");
                rebateText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorError)));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    validation2.setText(""); // Clear any previous message
                    rebateText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorError)));
                    return;
                }

                try {
                    double value = Double.parseDouble(s.toString());
                    if (value > 5 || value < 0) {
                        validation2.setText("Value should be between 0 and 5 only!");
                        validation2.setTextColor(getColor(R.color.colorError));
                        rebateText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorError)));
                    } else {
                        validation2.setText("Amount in %");
                        validation2.setTextColor(getColor(R.color.colorPrimary));// Clear the message if within range
                        rebateText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                    }
                } catch (NumberFormatException e) {
                    validation2.setText("Please enter a valid number!"); // Invalid number
                    validation2.setTextColor(getResources().getColor(R.color.colorError));
                    rebateText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorError)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    validation2.setText("This field cannot be empty!"); // When input is cleared
                    validation2.setTextColor(getColor(R.color.colorError));
                }
            }
        });
    }

    public void editRebateVisible(boolean isChecked) {
        if (isChecked) {
            rebateText.setText("0.0");
            rebateText.setVisibility(View.VISIBLE);
            validation2.setVisibility(View.VISIBLE);
            switchChecker.setText("Yes");
            switchChecker.setTextColor(getColor(R.color.colorPrimary));
            switchChecker.setTrackTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));

            if(elecText.getText().toString().isEmpty())
            {
                validation1.setText("This field cannot be empty!");
                validation1.setTextColor(getColor(R.color.colorError));
                elecText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorError)));
            }
        } else {
            rebateText.setVisibility(View.INVISIBLE);
            validation2.setVisibility(View.INVISIBLE);
            switchChecker.setText("No");
            switchChecker.setTrackTintList(ColorStateList.valueOf(getColor(R.color.black)));
            switchChecker.setTextColor(getColor(R.color.black));
        }

    }

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
                    validation1.setText("This field cannot be empty!");
                    validation2.setText("This field cannot be empty!");
                    validation1.setTextColor(getColor(R.color.colorError));
                    validation2.setTextColor(getColor(R.color.colorError));
                    rebateText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorError)));
                    elecText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorError)));

                } else if (elecText.getText().toString().isEmpty()) {
                    elecText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorError)));
                    validation1.setText("This field cannot be empty!");
                } else if (rebateText.getText().toString().isEmpty()) {
                    rebateText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorError)));
                    validation2.setText("This field cannot be empty!");
                }
            }
            else
            {
                if (elecText.getText().toString().isEmpty()) {
                    elecText.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorError)));
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

                if(rebateText.getVisibility()==View.VISIBLE)
                {
                    Toast.makeText(this, "Both values needs to be numbers.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "Electricity value needs to be a number.", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException iae) {
                Toast.makeText(this, iae.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Please enter both value in number format.", Toast.LENGTH_SHORT).show();
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
            rebateText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            elecText.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        }
    }
}
