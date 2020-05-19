package com.maity.spotit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private String responce;
    private CardView cardView;
    private Button btn;
    private Map<String,String> Itemlist;
    private String bestguess="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Intent intent =getIntent();
        responce=intent.getStringExtra("response");
        parseJSONobj(responce);
        createButton();


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

                if(bestguess!="")
                    bestguess=(objres2.getJSONArray("bestGuessLabels")).getJSONObject(0).getString("label");
                else
                   bestguess=bestguess+","+(objres2.getJSONArray("bestGuessLabels")).getJSONObject(0).getString("label");

                JSONArray items=objres2.getJSONArray("bestGuessLabels");
                for (int j=0;j<items.length();i++)
                {
                    JSONObject obj =items.getJSONObject(j);

                    if(obj.getString("url").contains("mynta"))
                         Itemlist.put("Myntra",obj.getString("url"));
                    else if(obj.getString("url").contains("flixcart"))
                        Itemlist.put("Flipkart",obj.getString("url"));
                    else if(obj.getString("url").contains("amazon"))
                        Itemlist.put("Amazon",obj.getString("url"));

                }



            }



        }
        catch (Exception e)
        {

        }


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
