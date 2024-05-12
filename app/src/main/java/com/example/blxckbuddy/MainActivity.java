package com.example.blxckbuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonInstalledApps, buttonLockedApps, buttonAllowPermissions, buttonPassword, buttonSettings;

    String password;
    static final String KEY = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = SharedPrefUtil.getInstance(this).getString(KEY);

        final Context context = this;

        buttonInstalledApps = findViewById(R.id.buttonInstalledApps);
        buttonLockedApps = findViewById(R.id.buttonLockedApps);
        buttonAllowPermissions = findViewById(R.id.buttonAllowPermissions);
        buttonPassword = findViewById(R.id.buttonPassword);
        buttonSettings = findViewById(R.id.buttonSettings);

        //button text set or update password depending on if empty
        if (password.isEmpty()){
            buttonPassword.setText("Set Password");
        } else {
            buttonPassword.setText("Update Password");
        }

        //show all installed apps
        buttonInstalledApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //access must be granted prior to accessing list of installed apps
                if (isAccessGranted()){
                    if (!password.isEmpty()){
                        startActivity(new Intent(MainActivity.this, ShowInstalledApps.class));
                    } else {
                        Toast.makeText(context, "Set a Password", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Please Allow Permissions", Toast.LENGTH_LONG).show();
                }
            }
        });

        //set or update password
        buttonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.isEmpty()){
                    setPassword(context);
                } else {
                    updatePassword(context);
                }
            }
        });

        //open allow permissions
        buttonAllowPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        });

        //open settings
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });

    }

    //set password function
    private void setPassword(Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialog));

        //create new linear layout
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //add text view "Enter your password" to linear layout
        TextView textView = new TextView(context);
        textView.setText("Enter Your Password");
        linearLayout.addView(textView);

        //add edit text to linear layout - type as text, variation as password
        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        linearLayout.addView(editText);

        //set dialog to linear layout
        dialog.setView(linearLayout);

        //TODO: Add error message for setting empty passwords

        //confirm button sets password and sets button text value to "Update Password"
        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPrefUtil.getInstance(context).putString(KEY, editText.getText().toString());
                Toast.makeText(context,"Password Set Successfully", Toast.LENGTH_LONG).show();
                password = editText.getText().toString();
                buttonPassword.setText("Update Password");
            }
            //cancel button dismisses linear layout
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //update password function
    private void updatePassword(final Context context){
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialog));

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(context);
        textView.setText("Enter Old Password");
        linearLayout.addView(textView);

        EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        linearLayout.addView(editText);

        TextView textView2 = new TextView(context);
        textView2.setText("Enter New Password");
        linearLayout.addView(textView2);

        EditText editText2 = new EditText(context);
        editText2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        linearLayout.addView(editText2);

        dialog.setView(linearLayout);

        //TODO: Add error message for setting empty passwords

        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (password.equals(editText.getText().toString())){
                    SharedPrefUtil.getInstance(context).putString(KEY, editText2.getText().toString());
                    Toast.makeText(context,"Password Updated Successfully", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(context,"Incorrect Password", Toast.LENGTH_LONG).show();
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //checks permissions are enabled
    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}