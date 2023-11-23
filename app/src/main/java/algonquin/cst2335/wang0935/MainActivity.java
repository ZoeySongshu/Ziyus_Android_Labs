package algonquin.cst2335.wang0935;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The `MainActivity` class is the main entry point for the Android application.
 * It provides a user interface with an EditText for entering a password, a Button to initiate the password check, and a TextView for displaying messages.
 *
 * @author Ziyu Wang
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**
     * This holds the text at the centre of the screen.
     */
    private TextView tv = null;

    /**
     * This holds the EditText at the centre of the screen.
     */
    private EditText et = null;

    /**
     * This holds the Button at the centre of the screen.
     */
    private Button btn = null;

    /**
     * This function checks the complexity of the provided password.
     *
     * @param pw The password string to be checked.
     * @return `true` if the password meets complexity requirements, `false` otherwise.
     */
    boolean checkPasswordComplexity(String pw){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;

        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;
        //for
        for(int i=0; i<pw.length();i++){
           if (Character.isDigit(pw.charAt(i))){
               foundNumber=true;
           }
            if (Character.isUpperCase(pw.charAt(i))){
                foundUpperCase=true;
            }
            if (Character.isLowerCase(pw.charAt(i))){
                foundLowerCase=true;
            }
            if (isSpecialCharacter(pw.charAt(i))) {
                foundSpecial = true;
            }

        }
        if(!foundUpperCase)
        {
            Toast.makeText(this, "You are missing an uppercase letter.", Toast.LENGTH_SHORT).show();// Say that they are missing an upper case letter;

            return false;
        }
        else if( ! foundLowerCase)
        {
            Toast.makeText( this, "You are missing a lower case letter.", Toast.LENGTH_SHORT).show(); // Say that they are missing a lower case letter;

            return false;
        }
        else if( ! foundNumber) {
            Toast.makeText(this, "You are missing a number.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(! foundSpecial) {
            Toast.makeText(this, "You are missing a special character.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true; //only get here if they're all true
    }

    /**
     * Checks if the provided character is a special character.
     *
     * @param c The character to be checked.
     * @return true if the character is a special character, false otherwise.
     */
    boolean isSpecialCharacter(char c)
    {
        switch (c) {
            // Return true if `c` is one of: #$%^&*!@?
            case '#':
            case '?':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
                return true;
            default:
                return false; // Return false otherwise
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.textView);
        EditText et = findViewById(R.id.editText);
        Button btn = findViewById(R.id.loginButton);

        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();
            if(checkPasswordComplexity(password)){
                tv.setText("Your password meets the requirements");
            }else{
                tv.setText("You shall not pass!");
            }
        });
    }
}
