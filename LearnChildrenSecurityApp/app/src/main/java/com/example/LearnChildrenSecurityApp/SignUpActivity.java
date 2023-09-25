package com.example.LearnChildrenSecurityApp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {
    public static  String SOAP_ADDRESS = "http://securitylessons-001-site1.btempurl.com/Service.asmx";
    // For Join With SqlServer And WebService
    private static final String SOAP_ACTION = "http://securitylessons-001-site1.btempurl.com/RegisterUser";

    // your webservice web method name
    private static final String OPERATION_NAME = "RegisterUser";

    private static final String WSDL_TARGET_NAMESPACE = "http://securitylessons-001-site1.btempurl.com/";
    Button btnRegister;
    EditText etUsername,etFullName,etMobile,etEmail,etPassword,etDOB,
            etConfirmPassword,etAddress;
    RadioButton rdGender;
    Spinner spinnerType;
    Context context = this;
    Calendar myCalendar = Calendar.getInstance();
    String dateFormat = "yyyy-MM-dd";
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etDOB = (EditText) findViewById(R.id.etDOB);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etFullName = (EditText) findViewById(R.id.etFullName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        rdGender = (RadioButton) findViewById(R.id.radioButton2);
        spinnerType = (Spinner) findViewById(R.id.spinnertype);
        // set calendar date and update Date
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }

        };
        // onclick - popup datepicker  Date
        etDOB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //



    }
    private void updateDate() {
        etDOB.setText(sdf.format(myCalendar.getTime()));
    }
    public void Register(View v)
    {

        //Validate  Input Data
        if(etFullName.getText ().toString ().trim().length()<=0)
        {
            Toast.makeText ( SignUpActivity.this, "Must Complete FullName", Toast.LENGTH_LONG ).show ();
            return;
        }



//***************************
        // [a-zA-Z]+\u0020[a-zA-Z]+\u0020[a-zA-Z]+
        // \s*\S+(\s\S+)?\s*
        // ^[a-zA-Z0-9_ ]*$
        if(etFullName.getText().toString().matches("[a-zA-Z]+[a-zA-Z\u0020]+")==false)
        {
            Toast.makeText(getApplicationContext(),"Invalid Full name must be alphabitic",Toast.LENGTH_SHORT).show();
            return;
        }

        if(etUsername.getText ().toString ().trim().length()<=0)
        {
            Toast.makeText ( SignUpActivity.this, "Must Complete UserName", Toast.LENGTH_LONG ).show ();
            return;
        }
        if(etPassword.getText ().toString ().trim().length()<=0)
        {
            Toast.makeText ( SignUpActivity.this, "Must Complete Password", Toast.LENGTH_LONG ).show ();
            return;
        }

        if(etPassword.getText ().toString ().equals(etConfirmPassword.getText ().toString ())!=true)
        {
            Toast.makeText ( SignUpActivity.this, "Must Password and confirm is Equals", Toast.LENGTH_LONG ).show ();
            return;
        }
        //[a-zA-Z0-9-]+@[a-z]+\\.+[a-z]+
        String Pass_Patern = "[a-zA-Z0-9-]{7}[a-zA-Z0-9-]+";
        if(etPassword.getText ().toString ().matches(Pass_Patern)==false)
        {
            Toast.makeText ( SignUpActivity.this, "Must 8 digit or letters", Toast.LENGTH_LONG ).show ();
            return;
        }
        if(etMobile.getText ().toString ().trim().length()<=0)
        {
            Toast.makeText ( SignUpActivity.this, "Must Complete Mobile", Toast.LENGTH_LONG ).show ();
            return;
        }
        String Number_Patern = "\\d{10}";
        if(etMobile.getText ().toString ().matches(Number_Patern)==false)
        {
            Toast.makeText ( SignUpActivity.this, "Must 10 Numbers", Toast.LENGTH_LONG ).show ();
            return;
        }



        if(etEmail.getText ().toString ().trim().length()<=0)
        {
            Toast.makeText ( SignUpActivity.this, "Must Complete Email", Toast.LENGTH_LONG ).show ();
            return;
        }


        if(etUsername.getText().toString().matches("[a-zA-Z0-9._]+")==false)
        {
            Toast.makeText(getApplicationContext(),"Invalid User name must be alphabitic and Numric",Toast.LENGTH_SHORT).show();
            return;
        }
        String  emailString = etEmail.getText().toString().trim();

        // String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (emailString.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")==false )
        {

            Toast.makeText(getApplicationContext(),"Invalid email address Must be in correct format",Toast.LENGTH_SHORT).show();
            return;
        }
        String gender="";
if (rdGender.isChecked())
{
    gender="Male";
}
else{
    gender="Female";
}

        RegisterUsers register_users = new RegisterUsers();
        register_users.execute( etFullName.getText ().toString (),etDOB.getText ().toString (),
                etEmail.getText ().toString (), etMobile.getText ().toString (),gender,etAddress.getText ().toString (),
               etUsername.getText ().toString (), etPassword.getText ().toString (),spinnerType.getSelectedItem().toString()
                  );

    }

    public class RegisterUsers extends AsyncTask<String,String,String>
    {
        String ConnectionResult = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {

            super.onPreExecute();

        }
        @Override
        protected void onPostExecute(String result)
        {
            try{
                super.onPostExecute(result);

                Intent intent;
                switch (result) {
                    case "Succeed":
                        Toast.makeText ( SignUpActivity.this, "Register User Successfully", Toast.LENGTH_LONG ).show ();
                        break;
                    case "FoundUser":
                        Toast.makeText ( SignUpActivity.this, "This UserName Is Register Before", Toast.LENGTH_LONG ).show ();
                        break;
                    case "FoundEmail":
                        Toast.makeText ( SignUpActivity.this, "This Email Is Register Before", Toast.LENGTH_LONG ).show ();
                        break;

                    case "Fail":
                        Toast.makeText ( SignUpActivity.this, "Error In saveing Data fail"+result, Toast.LENGTH_LONG ).show ();
                        break;
                    default:
                        Toast.makeText ( SignUpActivity.this, "Error In saveing Data "+result, Toast.LENGTH_LONG ).show ();
                        break;
                }
            }
            catch (Exception ex)
            {
                Toast.makeText(SignUpActivity.this , ex.getMessage () +result, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected String doInBackground(String... params)
        {

            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,     OPERATION_NAME);
            request.addProperty("FullName",params[0]);
            request.addProperty("DOB",params[1]);
            request.addProperty("Email",params[2]);
            request.addProperty("Mobile",params[3]);
            request.addProperty("Gender",params[4]);
            request.addProperty("Address",params[5]);
            request.addProperty("UserName",params[6]);
            request.addProperty("Password",params[7]);
            request.addProperty("UserType",params[8]);



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER12);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

            try  {
                httpTransport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                ConnectionResult=response.toString();
            }  catch (Exception exception)   {
                ConnectionResult=exception.toString()+"";
            }


            return ConnectionResult;
        }
    }

}