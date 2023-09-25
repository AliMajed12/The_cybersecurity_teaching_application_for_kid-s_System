package com.example.LearnChildrenSecurityApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

        public class ChildEvaluateActivity extends AppCompatActivity {
            // For Join With SqlServer And WebService
            public static  String SOAP_ADDRESS = "http://securitylessons-001-site1.btempurl.com/Service.asmx";

            private static final String WSDL_TARGET_NAMESPACE = "http://securitylessons-001-site1.btempurl.com/";
            private static final String SOAP_ACTION = "http://securitylessons-001-site1.btempurl.com/";
            private static final String OPERATION_NAME = "";// your webservice web method name

            static ChildEvaluateActivity ManageData;
            TableLayout tableLayout;
            String TableID;
            String AllowAccess;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_child_evaluate);
                ManageData=this;

               DisplayDataForManage manage_data = new DisplayDataForManage();
                manage_data.execute();
            }
            public static ChildEvaluateActivity getInstance(){
                return   ManageData;
            }
            public  void FillTableLayout(ArrayList listItem)
            {
            /*
        Create Header Row
         */
                // Add header row
                CheckBox checkBox;
                Button ButtonAllow;
                TableRow rowHeader = new TableRow(this);

                rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
                String[] headerText={"ID    ","Name    "," Mobile "," Identity "," Rate ","  Evaluate "};
                for(String c:headerText) {
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT));
                    tv.setBackgroundResource(R.drawable.border_1dp);
                    tv  .setBackgroundColor( R.drawable.gradian);
                    tv.setGravity( Gravity.CENTER);
                    tv.setTextSize(14);
                    tv.setPadding(7 , 7, 7, 7);
                    tv.setText(c);
                    rowHeader.addView(tv);
                }
                tableLayout.removeAllViews();
                tableLayout.addView(rowHeader);
                //*********

                String [] listItemNodes;
                if (listItem.size ()>0) {
                    for (int i = 0; i < listItem.size (); i++) {
                        listItemNodes = listItem.get ( i ).toString ().split ( ";" );
                        String Fullname ;
                        String Mobile;
                        String Identity;
                        String Rate;


                        // Read columns data
                        TableID = listItemNodes[0];
                        Fullname = listItemNodes[1];
                        Mobile = listItemNodes[2];
                        Identity= listItemNodes[3];
                        Rate = listItemNodes[4];

                        TableRow row = new TableRow ( this );
                        row.setLayoutParams ( new TableLayout.LayoutParams ( TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.MATCH_PARENT ) );
                        String[] colText = {TableID, Fullname,Mobile ,Identity,Rate};
                        for (int s=0;s<5;s++) {
                            if (s == 4) {
                                RatingBar rate = new RatingBar(this);
                                rate.setNumStars(5);
                                rate.setStepSize(0.5f);
                                LayerDrawable stars = (LayerDrawable) rate.getProgressDrawable(); stars.getDrawable(2)
                                        .setColorFilter(getResources().getColor(R.color.yellow),
                                                PorterDuff.Mode.SRC_ATOP);
                                rate.setBackgroundResource(R.drawable.border_1dp);
                                rate.setRating(Float.parseFloat(colText[s]));
                                row.addView(rate);
                            } else {
                            TextView tv = new TextView ( this );
                            tv.setLayoutParams ( new TableRow.LayoutParams ( TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.MATCH_PARENT ) );
                            tv.setBackgroundResource(R.drawable.border_1dp);
                            tv.setGravity ( Gravity.CENTER );
                            tv.setTextSize ( 14 );
                            tv.setPadding ( 7, 7, 7, 7 );
                            tv.setText (colText[s]  );
                            row.addView ( tv );
                            }
                        }

                        ButtonAllow=new Button (this);

                        ButtonAllow.setText ( "Evaluate");
                        ButtonAllow.setLayoutParams ( new TableRow.LayoutParams ( TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.MATCH_PARENT ) );
                        ButtonAllow.setBackgroundResource(R.drawable.border_1dp);
                        String[]arrayActive={TableID,AllowAccess};
                        ButtonAllow.setTag (  arrayActive);
                        row.addView ( ButtonAllow );
                        //**********Start Code Active
                        ButtonAllow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Button btnActive=(Button)v;
                                String []arr=(String[])btnActive.getTag ();

                                Intent intent = new Intent( ChildEvaluateActivity.this, EvaluateActivity.class );
                                startActivity ( intent );

                               DisplayDataForManage manage_data = new DisplayDataForManage();
                                manage_data.execute();

                            }
                        });
                        //*************EndActive

                        tableLayout.addView ( row );


                    }
                }


            }

            //DisplayDataForManage
            public class DisplayDataForManage extends AsyncTask<String,String,String>
            {
                public ArrayList list=new ArrayList (  );

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
                    super.onPostExecute(result);

                    try{
                        ManageData.getInstance ().FillTableLayout ( list );

                    }
                    catch (Exception ex)

                    {

                    }
                }


                @Override
                protected String doInBackground(String... params)
                {


                    SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,     OPERATION_NAME);


                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    envelope.setOutputSoapObject(request);

                    HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
                    httpTransport.debug = true;
                    String data="";
                    try  {
                        httpTransport.call(SOAP_ACTION, envelope);
                        SoapObject resultSOAP = (SoapObject) envelope.getResponse();
                        /* gets our result in JSON String */
                        String ResultObject = resultSOAP.getProperty(0).toString();
                        for (int i=0;i<resultSOAP.getPropertyCount ();i++)
                        {
                            list.add ( resultSOAP.getProperty ( i ) );
                        }


                        ConnectionResult=resultSOAP.getProperty ( 0 ).toString ();
                    }  catch (Exception exception)   {
                        ConnectionResult=exception.toString()+"";
                    }


                    return ConnectionResult;
                }
            }


        }