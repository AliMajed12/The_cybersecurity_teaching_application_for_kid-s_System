package com.example.LearnChildrenSecurityApp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

        public class EvaluateActivity extends AppCompatActivity {
            // For Join With SqlServer And WebService
            public static  String SOAP_ADDRESS = "http://securitylessons-001-site1.btempurl.com/Service.asmx";

            private static final String WSDL_TARGET_NAMESPACE = "http://securitylessons-001-site1.btempurl.com/";
            private static final String SOAP_ACTION = "http://securitylessons-001-site1.btempurl.com/Evaluate";
            private static final String OPERATION_NAME = "Evaluate";

            // Tools in register
            Button btnevaluate;
            EditText etcomment;
            RatingBar evRate;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_evaluate);
                btnevaluate = (Button) findViewById(R.id.btnevaluate);
                etcomment = (EditText) findViewById(R.id.etComment);
                evRate = (RatingBar) findViewById(R.id.rateUser);

            }
            public void RegisterRating(View v)
            {



                RegisterRate register_rate = new RegisterRate();
                register_rate.execute(Float.toString(evRate.getRating()),etcomment.getText().toString(),
                        PublicData.LessonID);


            }
            public class RegisterRate extends AsyncTask<String,String,String>
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
                                Toast.makeText ( EvaluateActivity.this, "Rate Succefully", Toast.LENGTH_LONG ).show ();
                                break;

                            case "Fail":
                                Toast.makeText ( EvaluateActivity.this, "Error In Rating"+result, Toast.LENGTH_LONG ).show ();
                                break;
                            default:
                                Toast.makeText ( EvaluateActivity.this, "Error In Rating "+result, Toast.LENGTH_LONG ).show ();
                                break;
                        }
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(EvaluateActivity.this , ex.getMessage () , Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                protected String doInBackground(String... params)
                {

                    SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,     OPERATION_NAME);
                    request.addProperty("Value",params[0]);
                    request.addProperty("Comment",params[1]);
                    request.addProperty("LessonID",params[2]);

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