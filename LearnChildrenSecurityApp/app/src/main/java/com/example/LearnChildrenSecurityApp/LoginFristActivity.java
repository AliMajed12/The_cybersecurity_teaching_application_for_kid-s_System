package com.example.LearnChildrenSecurityApp;

import androidx.appcompat.app.AppCompatActivity;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
//////////////////////////////////
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginFristActivity extends AppCompatActivity {
    public static  String SOAP_ADDRESS = "http://securitylessons-001-site1.btempurl.com/Service.asmx";
    // For Join With SqlServer And WebService
    private static final String SOAP_ACTION = "http://securitylessons-001-site1.btempurl.com/LoginCheck1";

    // your webservice web method name
    private static final String OPERATION_NAME = "LoginCheck1";

    private static final String WSDL_TARGET_NAMESPACE = "http://securitylessons-001-site1.btempurl.com/";

    // Declaring Variables
    TextView tvResult;
    Button btn_login;
    EditText etUsername,etPassword;
    String UserName,Password;
    public static final String UserEmail = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_frist);

    }
    public void loginForVerification(View v) {
        try {



            btn_login = (Button) findViewById(R.id.btnLogin);
            etUsername = (EditText) findViewById(R.id.etUsername);
            etPassword = (EditText) findViewById(R.id.etPassword);
            UserName=etUsername.getText().toString();
            Password=etPassword.getText().toString();

            //Validate  Input Data Username and Password
            if(UserName.trim().length()<=0)
            {
                Toast.makeText ( LoginFristActivity.this, "Must Enter Username ", Toast.LENGTH_LONG ).show ();
                return;
            }
            if(Password.trim().length()<=0)
            {
                Toast.makeText ( LoginFristActivity.this, "Must Enter  Password", Toast.LENGTH_LONG ).show ();
                return;
            }
            PublicData.UserName=UserName;
            ClassLogin login = new ClassLogin();
            login.execute(UserName,Password,PublicData.UserType);

        }catch(Exception ex)
        {
            Toast.makeText(LoginFristActivity.this,ex.getMessage()+ "Invalid UserName Or Password" , Toast.LENGTH_LONG).show();

        }



    }
    public class ClassLogin extends AsyncTask<String,String,String>
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
                String[] list=result.split(";");
                //  Toast.makeText(LoginActivity.this, "  Password" +result, Toast.LENGTH_LONG).show();

                if (list.length>=1) {

                    Intent intent;

                    switch (list[0]) {

                        case "Succeed":
                            PublicData.Email= list[2];
                            PublicData.UserID = list[1];
                            PublicData.UserName = UserName;

                            intent = new Intent(LoginFristActivity.this, LoginSecondActivity.class);
                            startActivity(intent);
                            break;

                        case "Fail":
                            Toast.makeText(LoginFristActivity.this, result+"Invalid UserName Or Password", Toast.LENGTH_LONG).show();
                            break;

                        default:
                            Toast.makeText(LoginFristActivity.this, "Invalid UserName Or Password", Toast.LENGTH_LONG).show();

                            break;
                    }

                }

            }
            catch (Exception ex)
            {
                Toast.makeText(LoginFristActivity.this , ex.getMessage()+"Invalid UserName Or Password ", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected String doInBackground(String... params) {


            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);

            request.addProperty("UserName", params[0]);
            request.addProperty("Password", params[1]);
            request.addProperty("UserType", params[2]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);

            try {
                httpTransport.call(SOAP_ACTION, envelope);
                Object response = envelope.getResponse();

                ConnectionResult = response.toString();
            } catch (Exception exception) {
                ConnectionResult = exception.toString() +"error here";
            }


            return ConnectionResult;
        }
    }

}