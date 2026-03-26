package com.example.mockmidterm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String KEY_CURRENT_INPUT = "current_input";
    private static final String KEY_OPERAND_A = "operand_a";
    private static final String KEY_OPERAND_B = "operand_b";
    private static final String KEY_OPERATOR = "operator";
    private static final String KEY_IS_OPERATOR_SELECTED = "is_operator_selected";
    private static final String KEY_IS_RESULT_DISPLAYED = "is_result_displayed";
    private static final String KEY_IS_ERROR_STATE = "is_error_state";
    private static final String KEY_CALCULATION_TEXT = "calculation_text";
    private static final String KEY_RESULT_TEXT = "result_text";

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_CURRENT_INPUT, currentInput);
        outState.putString(KEY_OPERAND_A, operandA);
        outState.putString(KEY_OPERAND_B, operandB);
        outState.putString(KEY_OPERATOR, operator);
        outState.putBoolean(KEY_IS_OPERATOR_SELECTED, isOperatorSelected);
        outState.putBoolean(KEY_IS_RESULT_DISPLAYED, isResultDisplayed);
        outState.putBoolean(KEY_IS_ERROR_STATE, isError);

        outState.putString(KEY_CALCULATION_TEXT, calculationTextView.getText().toString());
        outState.putString(KEY_RESULT_TEXT, resultTextView.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculationTextView = findViewById(R.id.calculation);
        resultTextView = findViewById(R.id.result);

        if(savedInstanceState != null) {
            restoreState(savedInstanceState);
        }

        initializeButtons();
    }

    private void restoreState(Bundle savedInstanceState) {
        currentInput = savedInstanceState.getString(KEY_CURRENT_INPUT, "");
        operandA = savedInstanceState.getString(KEY_OPERAND_A, "");
        operandB = savedInstanceState.getString(KEY_OPERAND_B, "");
        operator = savedInstanceState.getString(KEY_OPERATOR, "");
        isOperatorSelected = savedInstanceState.getBoolean(KEY_IS_OPERATOR_SELECTED, false);
        isResultDisplayed = savedInstanceState.getBoolean(KEY_IS_RESULT_DISPLAYED, false);
        isError = savedInstanceState.getBoolean(KEY_IS_ERROR_STATE, false);

        String calculationText = savedInstanceState.getString(KEY_CALCULATION_TEXT, "");
        String resultText = savedInstanceState.getString(KEY_RESULT_TEXT, "");

        calculationTextView.setText(calculationText);
        resultTextView.setText(resultText);
    }
    private void initializeButtons() {
        //Numbers
        int[] allButtons = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
                R.id.btnPeriod, R.id.btnEquals, R.id.btnAC, R.id.btnDelete,
                R.id.btnCustom
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
        else if (id == R.id.btnCustom) {
            customOperator();
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
    private void customOperator() {
        if (isError) {
            return;
        }

        String currentValue;

        if (isResultDisplayed && !currentInput.isEmpty()) {
            currentValue = currentInput;
        } else if (!currentInput.isEmpty()) {
            currentValue = currentInput;
        } else if (!operandA.isEmpty()) {
            currentValue = operandA;
        } else {
            showError("Nothing to multiply");
            return;
        }

        try {
            double value = Double.parseDouble(currentValue);

            double result = value * 918;

            DecimalFormat df = new DecimalFormat("0.##########");
            String formattedResult = df.format(result);

            String equation = currentValue + " × 918 =";
            calculationTextView.setText(equation);

            resultTextView.setText(formattedResult);

            currentInput = formattedResult;
            operandA = formattedResult;
            operandB = "";
            operator = "";
            isOperatorSelected = false;
            isResultDisplayed = true;
            isError = false;

        } catch (NumberFormatException e) {
            showError("Invalid number format");
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