package com.example.blxckbuddy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AdapterDesign>{

    List<AppModel> appModels = new ArrayList<>();
    Context con;

    List<String> lockedApps = new ArrayList<>();

    public AppAdapter(List<AppModel> appModels, Context con) {
        this.appModels = appModels;
        this.con = con;
    }

    @NonNull
    @Override
    public AdapterDesign onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(con).inflate(R.layout.adapter_layout, parent, false);
        AdapterDesign design = new AdapterDesign(view);
        return design;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDesign holder, int position) {

        AppModel app = appModels.get(position);

        holder.appname.setText(app.getAppname());
        holder.appicon.setImageDrawable(app.getAppicon());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //on item click set unlocked app to locked
                if(app.getStatus()==0){
                    app.setStatus(1);
                    holder.appstatus.setImageResource(R.drawable.iconlocked);
                    holder.appstatus.setColorFilter(0xFF3BBCDC);
                    holder.appname.setTextColor(0xFF3BBCDC);
                    Toast.makeText(con, app.getAppname() + " has been locked", Toast.LENGTH_LONG).show();
                    lockedApps.add(app.getPackagename());
                    //SharedPrefUtil.getInstance(con).setListString(lockedApps);

                    //on item click set locked app to unlocked
                } else {
                    app.setStatus(0);
                    holder.appstatus.setImageResource(R.drawable.iconunlocked);
                    holder.appstatus.setColorFilter(0xFFFFFFFF);
                    holder.appname.setTextColor(0xFFFFFFFF);
                    Toast.makeText(con, app.getAppname() + " has been unlocked", Toast.LENGTH_LONG).show();
                    lockedApps.remove(app.getPackagename());
                    //SharedPrefUtil.getInstance(con).setListString(lockedApps);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appModels.size();
    }

    public class AdapterDesign extends RecyclerView.ViewHolder{

        TextView appname;
        ImageView appicon;
        ImageView appstatus;

        public AdapterDesign(@NonNull View itemView) {
            super(itemView);

            appname = itemView.findViewById(R.id.appName);
            appicon = itemView.findViewById(R.id.appIcon);
            appstatus = itemView.findViewById(R.id.appStatus);
        }

    }
}
