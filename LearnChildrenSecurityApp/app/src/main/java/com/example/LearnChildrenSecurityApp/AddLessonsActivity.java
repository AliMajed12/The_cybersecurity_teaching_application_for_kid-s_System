package com.example.LearnChildrenSecurityApp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddLessonsActivity extends AppCompatActivity {
    // For Join With SqlServer And WebService
    public static  String SOAP_ADDRESS = "http://securitylessons-001-site1.btempurl.com/Service.asmx";

    private static final String WSDL_TARGET_NAMESPACE = "http://securitylessons-001-site1.btempurl.com/";
    private static final String SOAP_ACTION = "http://securitylessons-001-site1.btempurl.com/addlessons";
    private static final String OPERATION_NAME = "addlessons";
    EditText etName,etDescription;
    ImageView imageView1;
    Button btnImage1,btnImage2,btnImage3;
    Bitmap selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lessons);
        btnImage1=(Button)findViewById(R.id.btnImage1);
        etName=(EditText) findViewById(R.id.etLessonName);
        etDescription=(EditText) findViewById(R.id.etDescription);
        imageView1=(ImageView)findViewById(R.id.viewImage);
        /*btnImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    public void uploadImage(View v)
    {

        selectImage();

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
                bimage=BitmapFactory.decodeStream(in);
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
    private void selectImage() {
        // final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        final CharSequence[] options = {  "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddLessonsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.
                            Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                // final Bitmap
                selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView1.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddLessonsActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(AddLessonsActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    public void addLessons(View v)
    {
        try {
        //Validate  Input Data
        if(etName.getText ().toString ().trim().length()<=0)
        {
            Toast.makeText ( AddLessonsActivity.this, "Must Complete Name", Toast.LENGTH_LONG ).show ();
            return;
        }
//***************************



            if(etDescription.getText ().toString ().trim().length()<=0)
        {
            Toast.makeText ( AddLessonsActivity.this, "Must Complete Details", Toast.LENGTH_LONG ).show ();
            return;
        }



        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG,40, outputStream);
        byte[] imgByte = outputStream.toByteArray();
        String base64Str = Base64.encodeToString(imgByte, Base64.DEFAULT);
        AddlessonsClass lesson=new AddlessonsClass();
        lesson.execute(etName.getText().toString(),etDescription.getText().toString(),base64Str,PublicData.UserID);
        }
        catch (Exception ex){
            Toast.makeText(AddLessonsActivity.this , ex.getMessage () , Toast.LENGTH_LONG).show();

        }

    }
    public class AddlessonsClass extends AsyncTask<String,String,String>
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
                        Toast.makeText ( AddLessonsActivity.this, "Adding  Successfully", Toast.LENGTH_LONG ).show ();
                        break;

                    case "Fail":
                        Toast.makeText ( AddLessonsActivity.this, "fError In saving Data fail"+result, Toast.LENGTH_LONG ).show ();
                        break;
                    default:
                        Toast.makeText ( AddLessonsActivity.this, "Error In saving Data "+result, Toast.LENGTH_LONG ).show ();
                        break;
                }
            }
            catch (Exception ex)
            {
                Toast.makeText(AddLessonsActivity.this , ex.getMessage () +result, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected String doInBackground(String... params)
        {

            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,     OPERATION_NAME);
            request.addProperty("LessonName",params[0]);
            request.addProperty("Details",params[1]);
            request.addProperty("Image1",params[2]);
            request.addProperty("UserID",params[3]);



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
                ConnectionResult=exception.toString()+exception.getMessage()+exception.getLocalizedMessage()+"";
            }


            return ConnectionResult;
        }
    }
}