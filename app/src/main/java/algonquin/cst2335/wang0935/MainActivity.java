package algonquin.cst2335.wang0935;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.myTextview);

        Button b = findViewById(R.id.mybutton);

        EditText et = findViewById(R.id.myEditText);

        //OnClickListener
        b.setOnClickListener (v  ->  {
                tv.setText("You clicked the button");
                et.setText("You clicked the button");
                b.setText("You clicked the button");

            });
        }

    }
