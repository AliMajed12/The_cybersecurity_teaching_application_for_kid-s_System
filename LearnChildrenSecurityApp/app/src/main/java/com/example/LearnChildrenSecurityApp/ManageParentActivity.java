package com.example.LearnChildrenSecurityApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class ManageParentActivity extends AppCompatActivity {
    // For Join With SqlServer And WebService
    public static  String SOAP_ADDRESS = "http://securitylessons-001-site1.btempurl.com/Service.asmx";

    private static final String WSDL_TARGET_NAMESPACE = "http://securitylessons-001-site1.btempurl.com/";
    private static final String SOAP_ACTION = "http://securitylessons-001-site1.btempurl.com/displayDataOfParent";
    private static final String OPERATION_NAME = "displayDataOfParent";// your webservice web method name
    private static final String SOAP_ACTION_ACTIVE = "http://securitylessons-001-site1.btempurl.com/AcceptParent";
    private static final String OPERATION_NAME_ACTIVE = "AcceptParent";// your webservice web method name
    static ManageParentActivity ManageData;
    TableLayout tableLayout;
    String TableID;
    String AllowAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_parent);
        ManageData=this;
        tableLayout=(TableLayout)findViewById(R.id.TableLayoutManageParent);
        DisplayDataForManage manage_data = new DisplayDataForManage();
        manage_data.execute();
    }
    public static ManageParentActivity getInstance(){
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
        String[] headerText={"ID    ","Name    "," Email "," Mobil "," Accept ","  Allow Access "};
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
                String Email;
                String Mobile;


                // Read columns data
                TableID = listItemNodes[0];
                Fullname = listItemNodes[1];
                Email = listItemNodes[2];
                Mobile= listItemNodes[3];
                AllowAccess = listItemNodes[4];

                TableRow row = new TableRow ( this );
                row.setLayoutParams ( new TableLayout.LayoutParams ( TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT ) );
                String[] colText = {TableID, Fullname,Email ,Mobile};
                for (int s=0;s<4;s++) {

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
                checkBox=new CheckBox ( this );
                boolean bool=Boolean.parseBoolean ( AllowAccess );
                checkBox.setChecked ( bool );
                checkBox.setLayoutParams ( new TableRow.LayoutParams ( TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT ) );
                checkBox.setBackgroundResource(R.drawable.border_1dp);
                row.addView ( checkBox );
                ButtonAllow=new Button (this);

                ButtonAllow.setText ( "Access");
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
                        AllowAccessData allowAccess_Data = new AllowAccessData();
                        allowAccess_Data.execute(arr[0],arr[1]);
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

    //Allow Access
    public class AllowAccessData extends AsyncTask<String,String,String>
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
            super.onPostExecute(result);

            if (result.equals("Succeed")) {
                Toast.makeText ( ManageParentActivity.this, "Allowed Access Successfully", Toast.LENGTH_LONG ).show ();

            }
            else if (result.equals("Fail")||result.isEmpty()==true) {
                Toast.makeText ( ManageParentActivity.this, "Fail to Allowed Access", Toast.LENGTH_LONG ).show ();

            }
            else
            {
                Toast.makeText ( ManageParentActivity.this, "Fail to Allowed Access ", Toast.LENGTH_LONG ).show ();

            }


        }
        @Override
        protected String doInBackground(String... params)
        {

            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, OPERATION_NAME_ACTIVE);

            request.addProperty("ID",params[0]);
            request.addProperty("Allow",params[1]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            try {
                httpTransport.call(SOAP_ACTION_ACTIVE, envelope);
                Object response = envelope.getResponse();

                ConnectionResult=response.toString();

            }  catch (Exception exception)   {
                ConnectionResult=exception.toString()+"";
            }


            return ConnectionResult;
        }
    }
}