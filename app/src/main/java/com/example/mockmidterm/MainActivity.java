package com.example.mockmidterm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView calculationTextView;
    private TextView resultTextView;
    private String currentInput = "";
    private String operandA= "";
    private String operandB = "";
    private String operator = "";
    private boolean isOperatorSelected = false;
    private boolean isResultDisplayed = false;
    private boolean isError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculationTextView = findViewById(R.id.calculation);
        resultTextView = findViewById(R.id.result);

        initializeButtons();
    }

    private void initializeButtons() {
        //Numbers
        int[] allButtons = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
                R.id.btnPeriod, R.id.btnEquals, R.id.btnAC, R.id.btnDelete
        };

        for (int id : allButtons) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnAC) {
            clearAll();
        }
        else if (id == R.id.btnDelete) {
            handleDelete();
        }
        else if (id == R.id.btnEquals) {
            calculateResult();
        }
        else if (id == R.id.btnPeriod) {
            handleDecimal();
        }
        else {
            String text = ((Button) v).getText().toString();
            if (isOperator(text)) {
                handleOperator(text);
            } else {
                handleNumber(text);
            }
        }
    }

    private void handleNumber(String number) {
        if (isError) {
            return;
        }
        if (isResultDisplayed) {
            clearAll();
        }
        currentInput += number;
        updateDisplay();
    }

    private void handleOperator(String op) {
        if (isError) {
            return;
        }
        if (currentInput.isEmpty() && operandA.isEmpty()) {
            return;
        }
        if (isResultDisplayed) {
            operandA = currentInput;
            isResultDisplayed = false;
        }
        if (isOperatorSelected && !currentInput.isEmpty()) {
            calculateResult();
            operandA = resultTextView.getText().toString();
            currentInput = "";
        }
        operator = op;
        operandA = operandA.isEmpty() ? currentInput : operandA;
        currentInput = "";
        isOperatorSelected = true;

        calculationTextView.setText(operandA + " " + operator);
        resultTextView.setText("");
    }

    private void calculateResult() {
        if (operandA.isEmpty() || operator.isEmpty()) {
            if(!currentInput.isEmpty()) {
                resultTextView.setText(currentInput);
                calculationTextView.setText(currentInput);
            }
            return;
        }

        operandB = currentInput.isEmpty() ? operandA : currentInput;

        try {
            double a = Double.parseDouble(operandA);
            double b = Double.parseDouble(operandB);
            double result = Calculator.calculate(a, b, operator);

            DecimalFormat df = new DecimalFormat("0.##########");
            String formattedResult = df.format(result);

            String equation = operandA + " " + operator + " " + operandB + " =";
            calculationTextView.setText(equation);

            currentInput = formattedResult;
            operandA = formattedResult;
            operandB = "";
            operator = "";
            isOperatorSelected = false;
            isResultDisplayed = true;
            isError = false;
            resultTextView.setText(formattedResult);

        } catch (ArithmeticException e) {
            showError(e.getMessage());

        } catch (Exception e) {
            showError("Calculation error");
        }
    }

    private void handleDecimal() {
        if (isError) {
            return;
        }
        if (isResultDisplayed) {
            clearAll();
        }
        if (!currentInput.contains(".")) {
            currentInput += currentInput.isEmpty() ? "0." : ".";
            updateDisplay();
        }
    }

    private void handleDelete() {
        if (isError) {
            return;
        }
        if (isResultDisplayed) {
            clearAll();
        } else if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() -1);
            updateDisplay();
        } else if (isOperatorSelected) {
            operator = "";
            isOperatorSelected = false;
            currentInput = operandA;
            operandA = "";
            calculationTextView.setText("");
            updateDisplay();
        }
    }

    private void clearAll() {
        currentInput = "";
        operandA = "";
        operandB = "";
        operator = "";
        isOperatorSelected = false;
        isResultDisplayed = false;
        isError = false;
        calculationTextView.setText("");
        resultTextView.setText("");
    }

    private void updateDisplay() {
        if (isOperatorSelected && !operandA.isEmpty()) {
            calculationTextView.setText(operandA + " " + operator);
            resultTextView.setText(currentInput);
        } else {
            resultTextView.setText(currentInput.isEmpty() ? "0" : currentInput);
            if (!isResultDisplayed) {
                calculationTextView.setText("");
            }
        }
    }

    private void showError(String msg) {
        resultTextView.setText(msg);
        calculationTextView.setText("");
        isError = true;
        isResultDisplayed = true;
    }

    private boolean isOperator(String text) {
        return "+-*/÷".contains(text);
    }
}

//Helper class
class Calculator {
    public static double calculate(double a, double b, String operator) {
        switch (operator) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "÷":
                if (b == 0) {
                    throw new ArithmeticException("Can't divide by 0");
                } else {
                    return a / b;
                }
            default:
                throw new IllegalArgumentException("Unknown operator");
        }
    }
}