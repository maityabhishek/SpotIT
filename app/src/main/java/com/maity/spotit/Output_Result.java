package com.maity.spotit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Output_Result extends AppCompatActivity {

    private String response,resurl,sitedata;
    private TextView tx,txurl,txsite;
    private Map<String,String> Itemlist=new HashMap<String, String>();
    private String bestguess="BestGuess:";
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_output__result);
        tx=(TextView)findViewById(R.id.textView);
        txurl=(TextView)findViewById(R.id.textView2);
        txsite=(TextView)findViewById(R.id.textViewsitename) ;
       // txurl.setText("No URLs");
      //  tx.setText(bestguess);
        Intent intent =getIntent();
        response = intent.getStringExtra("response");

            parseJSONobj(response);
           createButton();
        txurl.setText(resurl);
        txsite.setText(sitedata);


    }

    private void parseJSONobj(String responce)
    {
        JSONObject objres;
        try {
            objres = new JSONObject(responce);
            JSONArray responseArr=objres.getJSONArray("responses");
            for(int i=0;i<responseArr.length();i++)
            {

                JSONObject objres2=responseArr.getJSONObject(i);
                objres2=objres2.getJSONObject("webDetection");

                if(bestguess=="")
                    bestguess=(objres2.getJSONArray("bestGuessLabels")).getJSONObject(0).getString("label");
                else
                    bestguess=bestguess+","+(objres2.getJSONArray("bestGuessLabels")).getJSONObject(0).getString("label");

                tx.setText(bestguess);
                int m=0;
                String s;
                while(m<objres2.getJSONArray("partialMatchingImages").length())
                {
                    s=  objres2.getJSONArray("partialMatchingImages").getJSONObject(m).get("url").toString();

                    if(s.contains("mynta"))
                    {
                        Itemlist.put("Myntra",s);
                        sitedata=sitedata+"\n"+"Myntra";
                    }
                    else if(s.contains("flixcart"))
                    {
                        Itemlist.put("Flipkart",s);
                        sitedata=sitedata+"\n"+"Flipkart";
                    }
                    else if(s.contains("amazon"))
                    {
                        Itemlist.put("Amazon",s);
                        sitedata=sitedata+"\n"+"Amazon";
                    }

                    resurl=resurl+"\n"+s;

                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
        }


    }

    public void gotoHome(View view) {
        Intent goToHomeIntent=new Intent(Output_Result.this, home.class);
        startActivity(goToHomeIntent);
    }
    private void createButton()
    {
        for (Map.Entry<String,String> entry : Itemlist.entrySet())
        {
            generate(entry.getKey(),entry.getValue());
        }

    }
    public void generate(String Name,final String url)
    {
        btn=new Button(this);
        btn.setText(Name);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);
        linearLayout.addView(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.google.co.in/search?q="+url)) ;
                startActivity(intent);
            }
        });

    }


}
