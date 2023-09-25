 package com.example.LearnChildrenSecurityApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.InputStream;
import java.util.ArrayList;

        public class ParentEvaluateLessonsActivity extends AppCompatActivity {
            // For Join With SqlServer And WebService
            public static String SOAP_ADDRESS = "http://securitylessons-001-site1.btempurl.com/Service.asmx";

            private static final String WSDL_TARGET_NAMESPACE = "http://securitylessons-001-site1.btempurl.com/";
            private static final String SOAP_ACTION = "http://securitylessons-001-site1.btempurl.com/displayDataOfLessonsParent";
            private static final String OPERATION_NAME = "displayDataOfLessonsParent";// your webservice web method name

            static ParentEvaluateLessonsActivity ManageData;
            TableLayout tableLayout;
            String TableID;
            String AllowAccess;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_parent_evaluate_lessons);
                ManageData = this;
                tableLayout = (TableLayout) findViewById(R.id.TableLayoutParentEvaluateLessons);
               DisplayDataForManage manage_data = new DisplayDataForManage();
                manage_data.execute();
            }
            private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
                ImageView imageView;
                public DownloadImageFromInternet(ImageView imageView) {
                    this.imageView=imageView;
                    Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
                }
                protected Bitmap doInBackground(String... urls) {
                    String imageURL=urls[0];
                    Bitmap bimage=null;
                    try {
                        InputStream in=new java.net.URL(imageURL).openStream();
                        bimage= BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        Log.e("Error Message", e.getMessage());
                        e.printStackTrace();
                    }
                    return bimage;
                }
                protected void onPostExecute(Bitmap result) {
                    imageView.setImageBitmap(result);
                }
            }
            //DisplayDataForManage
            public static ParentEvaluateLessonsActivity getInstance() {
                return ManageData;
            }

            public void FillTableLayout(ArrayList listItem) {
            /*
        Create Header Row
         */
                // Add header row
                CheckBox checkBox;
                Button ButtonEvaluate;
                TableRow rowHeader = new TableRow(this);

                rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
                //[ProviderID],[ServicesProviderFullName],[ServicesProviderMobile],[RequestID],[Notes],
                //[RequestsIsAcceptFromProvider],[RequestsIsPaymentByClient]
                String[] headerText = {"ID    ", "Lesson Name ","Details ",  "Image","Evaluations","Evaluate"};
                for (String c : headerText) {
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT));
                    tv.setBackgroundResource(R.drawable.border_1dp);
                    tv.setBackgroundColor(R.drawable.gradian);
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(14);
                    tv.setPadding(7, 7, 7, 7);
                    tv.setText(c);
                    rowHeader.addView(tv);
                }
                tableLayout.removeAllViews();
                tableLayout.addView(rowHeader);
                //*********

                String[] listItemNodes;
                if (listItem.size() > 0) {
                    for (int i = 0; i < listItem.size(); i++) {
                        listItemNodes = listItem.get(i).toString().split(";");
                        String LessonName;
                        String Details;

                        String image;
                        String Rating;


                        // Read columns data
                        TableID = listItemNodes[0];
                        LessonName = listItemNodes[1];
                        Details = listItemNodes[2];

                        image = listItemNodes[3];
                        Rating=listItemNodes[4];
                        TableRow row = new TableRow(this);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.MATCH_PARENT));
                        String[] colText = {TableID, LessonName,Details, image,Rating};
                        for (int s = 0; s < 5; s++) {

                            if (s == 3) {
                                ImageView img = new ImageView(this);
                                img.setLayoutParams(new TableRow.LayoutParams(250, 150));


                                img.setBackgroundResource(R.drawable.border_1dp);
                                new DownloadImageFromInternet(img).execute(colText[s]);

                                row.addView(img);
                            }else  if (s == 4) {
                                RatingBar rate = new RatingBar(this);
                                rate.setNumStars(5);
                                rate.setStepSize(0.5f);
                                LayerDrawable stars = (LayerDrawable) rate.getProgressDrawable(); stars.getDrawable(2)
                                        .setColorFilter(getResources().getColor(R.color.color_2),
                                                PorterDuff.Mode.SRC_ATOP);
                                // for filled stars stars.getDrawable(1)     .setColorFilter(getResources().getColor(R.color.light_gray),         PorterDuff.Mode.SRC_ATOP); // for half filled stars stars.getDrawable(0)     .setColorFilter(getResources().getColor(R.color.light_gray),         PorterDuff.Mode.SRC_ATOP); // for empty stars

                                rate.setRating(Float.parseFloat(colText[s]));
                                row.addView(rate);
                            }
                            else {
                                TextView tv = new TextView(this);
                                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                                        TableRow.LayoutParams.MATCH_PARENT));
                                tv.setBackgroundResource(R.drawable.border_1dp);
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(14);
                                tv.setPadding(7, 7, 7, 7);
                                tv.setText(colText[s]);
                                row.addView(tv);
                            }
                        }
                        ButtonEvaluate=new Button (this);

                        ButtonEvaluate.setText ( "Evaluate");
                        ButtonEvaluate.setLayoutParams ( new TableRow.LayoutParams ( TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.MATCH_PARENT ) );
                        ButtonEvaluate.setBackgroundResource(R.drawable.border_1dp);
                        String[]arrayActive={TableID,};
                        ButtonEvaluate.setTag (  arrayActive);
                        row.addView ( ButtonEvaluate );
                        //**********Start Code Active
                        ButtonEvaluate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Button btnActive=(Button)v;
                                String []arr=(String[])btnActive.getTag ();
                            PublicData.LessonID=arr[0];
                                Intent intent = new Intent( ParentEvaluateLessonsActivity.this, EvaluateActivity.class );
                                startActivity ( intent );

                            }
                        });


                        tableLayout.addView(row);


                    }
                }


            }

            //DisplayDataForManage
            public class DisplayDataForManage extends AsyncTask<String, String, String> {
                public ArrayList list = new ArrayList();

                String ConnectionResult = "";
                Boolean isSuccess = false;

                @Override
                protected void onPreExecute() {

                    super.onPreExecute();

                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);

                    try {
                        ManageData.getInstance().FillTableLayout(list);

                    } catch (Exception ex) {

                    }
                }


                @Override
                protected String doInBackground(String... params) {


                    SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME);


                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true;

                    envelope.setOutputSoapObject(request);

                    HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
                    httpTransport.debug = true;
                    String data = "";
                    try {
                        httpTransport.call(SOAP_ACTION, envelope);
                        SoapObject resultSOAP = (SoapObject) envelope.getResponse();
                        /* gets our result in JSON String */
                        String ResultObject = resultSOAP.getProperty(0).toString();
                        for (int i = 0; i < resultSOAP.getPropertyCount(); i++) {
                            list.add(resultSOAP.getProperty(i));
                        }


                        ConnectionResult = resultSOAP.getProperty(0).toString();
                    } catch (Exception exception) {
                        ConnectionResult = exception.toString() + "";
                    }


                    return ConnectionResult;
                }
            }

        }