package com.om.seeeend;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Pattern;

public class MainActivity extends FragmentActivity {

    private EditText mEdit;
    private EditText eFN;
    private EditText eDate;
    private EditText eEmail;
    private EditText eUser;
    private EditText ePass;
    private FloatingActionButton sendEmail;

    //Регулярки для проверки почты
    public static boolean checkEmail(String email) {
        Pattern EMAIL_ADDRESS_PATTERN = Pattern
                .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton startBtn = (FloatingActionButton) findViewById(R.id.sendEmail);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }

    //Date
    public void selectDate(View view) {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    public void populateSetDate(int year, int month, int day) {
        mEdit = (EditText) findViewById(R.id.eDate);
        mEdit.setText(month + "/" + day + "/" + year);
    }
    //

    //Отправка
    protected void sendEmail() {
        String[] TO = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);


        String stFN = eFN.getText().toString();
        if (stFN.length() < 1 || stFN.length() > 50) {
            eFN.setError("Введите коректные данные");
            return;
        }

        String stDate = eDate.getText().toString();
        if (stDate.length() < 8 || stDate.length() > 11) {
            eDate.setError("Введите коректные данные");
            return;
        }

        String stEmail = eEmail.getText().toString();
        if (false == checkEmail(stEmail)) {
            eEmail.setError("Введите коректные данные");
            return;
        }

        String stUser = eUser.getText().toString();
        if (stUser.length() < 1 || stUser.length() > 50) {
            eUser.setError("Введите коректные данные");
            return;
        }
        String stPass = ePass.getText().toString();
        if (stPass.length() < 6 || stPass.length() > 50) {
            ePass.setError("Введите коректные данные мин 6");
            return;
        }

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, "spam@gmail.com");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Регистрационные данные");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Full Name: " + stFN + " Date: " + stDate + " Email: " + stEmail + " User Name: " + stUser + " Password:" + stPass);

        try {
            startActivity(Intent.createChooser(emailIntent, "Отправка сообщения..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Печалька, неустановлены почтовые программы.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        eFN = (EditText) findViewById(R.id.eFN);
        eDate = (EditText) findViewById(R.id.eDate);
        eEmail = (EditText) findViewById(R.id.eEmail);
        eUser = (EditText) findViewById(R.id.eUser);
        ePass = (EditText) findViewById(R.id.ePass);
        sendEmail = (FloatingActionButton) findViewById(R.id.sendEmail);
        final Animation left = AnimationUtils.loadAnimation(this,
                R.anim.left);
        final Animation right = AnimationUtils.loadAnimation(this,
                R.anim.right);
        eFN.startAnimation(left);
        eDate.startAnimation(right);
        eEmail.startAnimation(left);
        eUser.startAnimation(right);
        ePass.startAnimation(left);
        sendEmail.startAnimation(right);
    }

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }
    }
}