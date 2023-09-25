package com.example.LearnChildrenSecurityApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LoginSecondActivity extends AppCompatActivity {
    public static String SOAP_ADDRESS = "http://securitylessons-001-site1.btempurl.com/Service.asmx";
    // For Join With SqlServer And WebService
    private static final String SOAP_ACTION = "http://securitylessons-001-site1.btempurl.com/LoginCheck2";

    // your webservice web method name
    private static final String OPERATION_NAME = "LoginCheck2";

    private static final String WSDL_TARGET_NAMESPACE = "http://securitylessons-001-site1.btempurl.com/";

    // Declaring Variables
    Button btn_login;
    EditText etCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_second);
    }

    public void GoMainJobs(View v) {

        // Getting values from Buttons, Edittext
        btn_login = (Button) findViewById(R.id.btnLogin);
        etCode = (EditText) findViewById(R.id.etVervication);

        //Validate  Input Data Username and Password
        if (etCode.getText().toString().trim().length() <= 0) {
            Toast.makeText(LoginSecondActivity.this, "Must Enter Vervication Code ", Toast.LENGTH_LONG).show();
            return;
        }

        ClassVervication login = new ClassVervication();
        login.execute(PublicData.UserType, PublicData.UserID,etCode.getText().toString());

    }
    public class ClassVervication extends AsyncTask<String,String,String>
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

                if (list.length>=1) {

                    Intent intent;
                    //  Toast.makeText(LoginCheckCodeActivity.this, "Invalid Vervication Code1" +result, Toast.LENGTH_LONG).show();

                    switch (list[0]) {

                        case "Succeed":
                            //Toast.makeText(LoginCheckCodeActivity.this, "Invalid Vervication Code" +GlobalData.UserType, Toast.LENGTH_LONG).show();

                            if (PublicData.UserType.equals("Parent")) {
                                intent = new Intent(LoginSecondActivity.this, ParentControlScreenActivity.class);
                                startActivity(intent);
                            }else if(PublicData.UserType.equals("Children"))
                        {
                                intent = new Intent(LoginSecondActivity.this, ChildControlScreenActivity.class);
                                startActivity(intent);
                            }
                            else   {
                                intent = new Intent(LoginSecondActivity.this, AdminControlScreenActivity.class);
                                startActivity(intent);
                            }
                            break;

                        case "Fail":
                            Toast.makeText(LoginSecondActivity.this, "Invalid Vervication Code" +PublicData.UserType, Toast.LENGTH_LONG).show();
                            break;

                        default:
                            Toast.makeText(LoginSecondActivity.this, "Invalid Code", Toast.LENGTH_LONG).show();

                            break;
                    }

                }

            }
            catch (Exception ex)
            {
                Toast.makeText(LoginSecondActivity.this , "InvalidCode", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected String doInBackground(String... params)
        {

            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,     OPERATION_NAME);
            request.addProperty("UserType",params[0]);
            request.addProperty("UserID",params[1]);
            request.addProperty("Code",params[2]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
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