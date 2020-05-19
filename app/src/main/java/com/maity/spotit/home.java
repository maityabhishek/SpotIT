package com.maity.spotit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class home extends AppCompatActivity {

    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private final String KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_home);
    }
    private void showProgress()
    {
        progressDialog = new ProgressDialog(home.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

    }
    public void captureImg(android.view.View v) {

        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        showProgress();
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap  bitmap=(Bitmap)data.getExtras().get("data");
        String encImg=encodeImage(bitmap);
        submit(encImg);
    }
    public void submit(String encImage)
    {
        String Url="https://vision.googleapis.com/v1/images:annotate?key="+KEY;
        final String savedata="{\n" +
                "  \"requests\":[\n" +
                "    {\n" +
                "      \"image\":{\n" +
                "        \"content\":\""+encImage+"\"\n" +
                "      },\n" +
                "      \"features\":[\n" +
                "        {\n" +
                "          \"type\":\"WEB_DETECTION\",\n" +
                "          \"maxResults\":10\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest =new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Intent gotoOutputResult=new Intent(home.this, Output_Result.class);
                gotoOutputResult.putExtra("response",response);
                startActivity(gotoOutputResult);
                try {


                   // JSONObject objres = new JSONObject(response);
                    //Toast.makeText(getApplicationContext(), objres.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            public String getBodyContentType()
            {
                return "application/json; chatset=utf-8";
            }

            @Override
            public byte[] getBody()throws AuthFailureError
            {
                try{
                    return savedata== null ? null:savedata.getBytes("utf-8");
                }
                catch(UnsupportedEncodingException uee)
                {
                    return  null;
                }

            }

        };
        requestQueue.add(stringRequest);
    }
    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }
}
