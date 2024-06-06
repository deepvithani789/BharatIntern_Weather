package com.example.myweather;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText et;
    TextView tv;
    String url="https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
    String apikey="56ca4c9f497b2d2e4400087211b7a479";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            et = findViewById(R.id.et);
            tv = findViewById(R.id.tv);


            return insets;

        });
    }

    public void getweather(View v)
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/").addConverterFactory(GsonConverterFactory.create()).build();
        tv.setVisibility(View.VISIBLE);
        weatherapi myapi = retrofit.create(weatherapi.class);
        Call<Example> examplecall = myapi.getweather(et.getText().toString().trim() , apikey);
        examplecall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if(response.code()==404){
                    Toast.makeText(MainActivity.this,"Enter Correct City Name",Toast.LENGTH_LONG).show();
                } else if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this,response.code(), Toast.LENGTH_SHORT).show();
                }
                Example mydata = response.body();
                Main main = mydata.getMain();
                Double temp = main.getTemp();
                Integer pressure = main.getPressure();
                Integer temperature = (int)(temp - 273.15);
                Integer humidity = main.getHumidity();
                Double tempmin = main.getTempMin();
                Integer tempMin = (int)(tempmin - 273.15);
                Double tempmax = main.getTempMax();
                Integer tempMax = (int)(tempmax - 273.15);
                Toast.makeText(MainActivity.this, "Weather of your city shown above", Toast.LENGTH_SHORT).show();
                tv.setText("Temperature : "+String.valueOf(temperature)+"°C" +"\n" + "Pressure : "+String.valueOf(pressure)+" Pa" +"\n" + "Humidity : "+String.valueOf(humidity)+" %"
                        +"\n" + "Min Temperature : "+String.valueOf(tempMin)+" °C" +"\n" + "Max Temperature : "+String.valueOf(tempMax)+" °C");
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}