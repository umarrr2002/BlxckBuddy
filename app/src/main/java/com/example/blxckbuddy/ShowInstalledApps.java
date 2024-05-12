package com.example.blxckbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ShowInstalledApps extends AppCompatActivity {

    RecyclerView recyclerView;
    List<AppModel> appModelList = new ArrayList<>();

    //TODO: Shared Preferences Required for Keeping apps locked and not resetting on BlockBuddy reopen
    Context context;

    AppAdapter adapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_installed_apps);

        recyclerView = findViewById(R.id.recyclerview);

        adapter = new AppAdapter(appModelList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                getInstalledApps();
            }
        });
    }

    //loading bar for fetching installed apps
    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.setTitle("Fetching Installed Apps");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    //fetches all installed apps to list
    public void getInstalledApps(){

        //TODO: Shared Preferences Required for Keeping apps locked and not resetting on BlockBuddy reopen
        //List<String> appList = SharedPrefUtil.getInstance(con).getListString();

        List<PackageInfo> packageInfo = getPackageManager().getInstalledPackages(0);

        //retrieves all apps or "packages"
        for (int i = 0; i < packageInfo.size(); i++) {
            String name = packageInfo.get(i).applicationInfo.loadLabel(getPackageManager()).toString();
            Drawable icon = packageInfo.get(i).applicationInfo.loadIcon(getPackageManager());
            String packname = packageInfo.get(i).packageName;

            //TODO: Shared Preferences Required for Keeping apps locked and not resetting on BlockBuddy reopen
//            if (!appList.isEmpty()) {
//                if (appList.contains(packname)) {
//                    appModelList.add(new AppModel(name, icon, 1, packname));
//                } else {
//                    appModelList.add(new AppModel(name, icon, 0, packname));
//                }
//            } else {
                appModelList.add(new AppModel(name, icon, 0, packname));
//            }
        }

        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }
}