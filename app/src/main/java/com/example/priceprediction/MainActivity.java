package com.example.priceprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Object.*;


public class MainActivity extends AppCompatActivity {

    double[][] arr = {{2104,3,399900},{1600,3,329900},{2400,3,369000},{1416,2,232000},{3000,4,539900},{1985,4,299900},
            {1534,3,314900},{1427,3,198999},{1380,3,212000},{1494,3,242500},{1940,4,239999},{2000,3,347000},{1890,3,329999},
            {4478,5,699900},{1268,3,259900},{2300,4,449900},{1320,2,299900},{1236,3,199900},{2609,4,499998},{3031,4,599000},
            {1767,3,252900},{1888,2,255000},{1604,3,242900},{1962,4,259900},{3890,3,573900},{1100,3,249900},{1458,3,464500},
            {2526,3,469000},{2200,3,475000},{2637,3,299900},{1839,2,349900},{1000,1,169900},{2040,4,314900},{3137,3,579900},
            {1811,4,285900},{1437,3,249900},{1239,3,229900},{2132,4,345000},{4215,4,549000},{2162,4,287000},{1664,2,368500},
            {2238,3,329900},{2567,4,314000},{1200,3,299000},{852,2,179900},{1852,4,299900},{1203,3,239500}};

    private  EditText size, rooms;
    private Button predict;
    private TextView textView;
    private float alpha;
    double[] theta = new double[3];
    double[][] x1 = new double[47][2];
    double err;
    double sum1=0,sum2=0,sum3=0;
    double term1;
    double term2;
    double term3;
    double mu = 0, mu1 = 0, sigma=0,sigma1=0,price=0,X1,X2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        size = findViewById(R.id.editTextTextPersonName);
        rooms = findViewById(R.id.editTextTextPersonName2);
        predict = findViewById(R.id.predict);
        textView = findViewById(R.id.textView);
        alpha = (float) 0.01;


        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mu=0;mu1=0;sigma=0;sigma1=0;
                theta[0]=0;
                theta[1]=0;
                theta[2]=0;
                featureNormalize();
                gradientDecent();
                if(size.getText() != null && rooms !=null) {
                    X1 = Integer.parseInt(size.getText().toString());
                    X2 = Integer.parseInt(rooms.getText().toString());
                }
                X1 = (X1 - mu) / sigma;
                X2 = (X2 - mu1) / sigma1;
                price = theta[0] + theta[1]*X1 + theta[2]*X2;
                textView.setText("Estimated price: "+price);
                //Toast.makeText(getApplicationContext(),"price :-"+price,Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void featureNormalize() {
        for(int i = 0; i<47;i++){
            mu = mu + arr[i][0];
            mu1 = mu1 + arr[i][1];
        }
        mu = mu/47;
        mu1 = mu1/47;

        //sigma = 794.702354;
        //sigma1 = 0.760982;

        for (int i = 0; i < 47; i++)
        {
            sigma += (Math.pow(arr[i][0] - mu,2) / 47);
            sigma1 += (Math.pow(arr[i][1] - mu1,2) / 47);
        }
        /*sigma = Math.round(Math.sqrt(sigma));
        sigma1 = Math.round(Math.sqrt(sigma1));


        textView.setText("text: "+sigma);*/
        for(int i =0;i<47;i++){
            x1[i][0] = (arr[i][0] - mu) / sigma;
            x1[i][1] = (arr[i][1] - mu1) / sigma1;
            //Log.d("print","x1[i]"+x1[i][0]);
        }
        //Toast.makeText(getApplicationContext(),"x1 :-"+arr[0][0]+arr[0][1],Toast.LENGTH_SHORT).show();
    }

    private void gradientDecent() {
        for(int i=1;i<=400;i++){
            for(int j =0;j<47;j++) {
                term1 = theta[0] * 1;
                term2 = theta[1] * x1[j][0];
                term3 = theta[2] * x1[j][1];
                /*if(j<11 ){
                    Log.d("print","term1"+term1[j]);
                    Log.d("print","term2"+term2[j]);
                    Log.d("print","term3"+term3[j]);
                }*/
                err = (term1 + term2 + term3) - arr[j][2];
                sum1 += (err * 1);
                sum2 += (err * x1[j][0]);
                sum3 += (err * x1[j][1]);
            }

            theta[0] = theta[0] - alpha * sum1;
            theta[1] = theta[1] - alpha * sum2;
            theta[2] = theta[2] - alpha * sum3;
            sum1=0;sum2=0;sum3=0;
        }

    }

}