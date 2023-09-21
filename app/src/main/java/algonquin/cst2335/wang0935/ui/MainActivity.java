package algonquin.cst2335.wang0935.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import algonquin.cst2335.wang0935.R;
import algonquin.cst2335.wang0935.data.MainViewModel;
import algonquin.cst2335.wang0935.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);


        setContentView( binding.getRoot());


        TextView tv = binding.myTextview;

        Button b = binding.mybutton;

        EditText et = binding.myEditText;

        viewModel.userString.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tv.setText(s);
            }
        });
        et.setText(viewModel.userString.getValue());
        //OnClickListener
        b.setOnClickListener (v  ->  {

                String string = et.getText().toString();
                viewModel.userString.postValue( string );
                b.setText("You clicked the button");

            });
        }

    }
