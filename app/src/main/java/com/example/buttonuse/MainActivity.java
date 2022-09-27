package com.example.buttonuse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
//import android.renderscript.Sampler;
import android.text.Editable;
//import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import java.text.MessageFormat;
import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    private int numberOfCoffee = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find a reference to the TextView in the layout. Change the text.
        TextView TextView_WhippedCream= (TextView) findViewById(R.id.whipped_cream_checkbox);
        TextView_WhippedCream.setText(MessageFormat.format("Whipped Cream (@ {0} )", NumberFormat.getCurrencyInstance().format(5)));

        // Find a reference to the TextView in the layout. Change the text.
        TextView TextView_Chocolate = (TextView) findViewById(R.id.chocolate_checkbox);
        TextView_Chocolate.setText(MessageFormat.format("Chocolate (@ {0} )", NumberFormat.getCurrencyInstance().format(5)));

        //Find a reference to the TextView in the layout. Change the text.
        TextView TextView_CoffeeRate = (TextView) findViewById(R.id.coffee_Rate);
        TextView_CoffeeRate.setText(MessageFormat.format("Coffee (@ {0} )", NumberFormat.getCurrencyInstance().format(20)));

    }

    /**
     * This method is called when the order button is clicked.
     */

    public void submitOrder(View view){
        if(numberOfCoffee == 0){
            Toast.makeText(this, "You can't Order less 1 than Coffee", Toast.LENGTH_SHORT).show();
        }
        else {
            //Printing out the user name for Order Summary
            EditText Name = findViewById(R.id.your_name_editText);
            Editable NameEditable = Name.getText();
            String name = NameEditable.toString();
            EditText Email = findViewById(R.id.your_email_editText);

            if (name.length() == 0) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            } else if (Email.getText().toString().length() == 0) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                //        Name.setOnClickListener(new View.OnClickListener(){
                //            @Override
                //            public void onClick(View v) {
                //                String s = Name.getText().toString();
                //            }
                //        });

                //Figure out if the user wants whipped cream topping
                CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
                boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

                //Figure out if the user wants chocolate topping
                CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
                boolean hasChocolate = chocolateCheckBox.isChecked();

                // calculate price
                int price = calculatePrice();


                //        This is use to initialize string from strings.xml and then call in composEmail ;
                String message = createOrderSummary(name, price, hasWhippedCream, hasChocolate);
                String[] email = {Email.getText().toString()};
                composeEmail(email, "Order Summary", message);


                //        OLD VERSION OF INTENT MAIL

                //        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts( "mailto","abc@gmail.com", null));
                //        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Order Summary");
                //        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
                //        startActivity(Intent.createChooser(emailIntent, "Send email..."));

                // Use an intent to launch an email app.
                // Send the order summary in the email body.
                //        Intent intent = new Intent(Intent.ACTION_SENDTO);
                //        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                //        intent.putExtra(Intent.EXTRA_SUBJECT,
                //                getString(R.string.order_summary_email_subject, name));
                //        intent.putExtra(Intent.EXTRA_TEXT, message);
                //
                //        if (intent.resolveActivity(getPackageManager()) != null) {
                //            startActivity(intent);
                //        }


                //Display the Order summary on the screen
//            displayMessage(message);
            }
        }
    }
    /**
     *This is the new version for intent mail.
     */
    public void composeEmail(String []addresses, String subject, String Message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_TEXT,Message);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        //}
    }


    /**
     * calculates the price of the order
     * @return total price
     */
    private int calculatePrice(){
        return numberOfCoffee*5;
    }

    /**
     * Create summary of the order
     *
     * @param Name              on the order
     * @param price             of the order
     * @param addWhippedCream   is whether or not to add chocolate to the coffee
     * @param addChocolate      is whether or not to add chocolate to the coffee
     * @return  text summary
     */

    private String createOrderSummary(String Name, int price, boolean addWhippedCream, boolean addChocolate){
//        String priceMessage = "Name : "+Name;
        String priceMessage = getString(R.string.oder_Summary_name,Name);
        priceMessage += "\nAdd whipped cream? " + (addWhippedCream? "Yes" : "NO");
        priceMessage += "\nAdd chocolate? " + (addChocolate? "YES" : "NO");
        priceMessage += "\nQuantity: " + numberOfCoffee;
        if(addWhippedCream){
            int WhipCreamPrice = 5;
            priceMessage += "\nExtra Charge "+ NumberFormat.getCurrencyInstance().format((long) WhipCreamPrice *numberOfCoffee)+" for WhippedCream";
            price += (WhipCreamPrice*numberOfCoffee);
        }
        if(addChocolate){
            int chocolatePrice = 5;
            priceMessage += "\nExtra Charge "+ NumberFormat.getCurrencyInstance().format((long)chocolatePrice*numberOfCoffee)+" for Chocolate";
            price += (chocolatePrice*numberOfCoffee);
        }
        priceMessage += "\nTotal: " + NumberFormat.getCurrencyInstance().format(price);
        priceMessage += "\nThank you!";
        return priceMessage;
    }
    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view){
        numberOfCoffee++;
        display_Quantity(numberOfCoffee);
        displayPrice(numberOfCoffee*20);
    }
    /**
     * This method is called when minus button is clicked.
     */
    public void decrement(View view){
        if(numberOfCoffee >= 1) {
            numberOfCoffee--;
        }
        else{
            Toast.makeText(this, "You can't have less than 1 coffee", Toast.LENGTH_SHORT).show();
        }
        display_Quantity(numberOfCoffee);
        displayPrice(numberOfCoffee*20);
    }
    /**
     * This method displays the given quantity value on the screen.
     */
    private void display_Quantity(int number) {
        TextView quantityTextView = findViewById(R.id.text_quantity_no);
        quantityTextView.setText(""+number);
    }
    /**
     * This method displays the given price on the screen.
     */
    private void displayPrice(int number) {
        TextView priceTextView = findViewById(R.id.oder_summary_details);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }
    /**
     * This method displays the given text on the screen.
     */
//    private void displayMessage(String message) {
//        TextView priceTextView =  findViewById(R.id.oder_summary_details);
//        priceTextView.setText(message);
//    }
}




//    boolean isRaining = false;
//    Log.v("WeatherActivity", "Thank you for using the WhetherWeather App.");
//        if (isRaining) {
//        Log.v("WeatherActivity", "It's raining, better bring an umbrella.");
//        } else {
//        Log.v("WeatherActivity", "It's unlikely to rain.");
//        }


//    int numberOfEmailsInInbox = 0;
//    int numberOfDraftEmails = 2;
//    String emailMessage = "You have " + numberOfEmailsInInbox + " emails. ";
//    String draftMessage = "You have " + numberOfDraftEmails + " email drafts.";
//    if (numberOfEmailsInInbox == 0) {
//        emailMessage = "You have no new messages. ";
//        }
//
//        if (numberOfDraftEmails == 0) {
//        draftMessage = "You have no new drafts.";
//        }
//
//        Log.v("InboxActivity", emailMessage);
//        Log.v("InboxActivity", draftMessage);


//    int numberOfSmoothiesTillPrize = 10;
//    if (numberOfSmoothiesTillPrize > 9) {
//        Log.v("SmoothieActivity", "Congratulations, you get a free smoothie!");
//        numberOfSmoothiesTillPrize = numberOfSmoothiesTillPrize - 10;
//        } else {
//        Log.v("SmoothieActivity", "No free smoothie this time.");
//        }
//        Log.v("SmoothieActivity", "You currently have " + numberOfSmoothiesTillPrize + " out of 10 smoothies needed for your next free smoothie.");
