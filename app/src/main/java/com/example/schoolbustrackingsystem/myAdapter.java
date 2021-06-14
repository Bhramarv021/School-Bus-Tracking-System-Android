package com.example.schoolbustrackingsystem;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myviewholder>
{
    ArrayList<model> datalist;
    Context context;

    public myAdapter(ArrayList<model> datalist, Context context ) {
        this.datalist = datalist;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        final model temp=datalist.get(position);

        holder.Name.setText(datalist.get(position).getStudentName());
        holder.Rollno.setText(datalist.get(position).getRollNo());

        holder.Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context,student_Detail.class);
                intent.putExtra("sname",temp.getStudentName());
                intent.putExtra("srollno",temp.getRollNo());
                intent.putExtra("sadr",temp.getAddress());
                intent.putExtra("scity",temp.getCity());
                intent.putExtra("sstate",temp.getState());
                intent.putExtra("scountry",temp.getCountry());
                intent.putExtra("spickndrp",temp.getPicDropLocation());
                intent.putExtra("sprtname",temp.getParentName());
                intent.putExtra("sprtno",temp.getParentContactNumber());
                intent.putExtra("sprntemail",temp.getParentEmail());
                intent.putExtra("sbusrte",temp.getBusRoute());
                intent.putExtra("sbusno",temp.getBusNumber());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
           TextView Name,
                    Rollno;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.Name);
            Rollno=itemView.findViewById(R.id.Rollno);
        }
    }
}


//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class myAdapter extends RecyclerView.Adapter<myAdapter.myviewholder> {
//    ArrayList<model> datalist;
//    Context context;
//
//    public myAdapter(ArrayList<model> datalist) {
//
//        this.datalist = datalist;
//    }
//
//    @NonNull
//    @Override
//    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
//        return new myviewholder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
//
//        holder.Name.setText(datalist.get(position).getStudentName());
//        holder.Rollno.setText(datalist.get(position).getRollNo());
//

//
//    }
//
//    @Override
//    public int getItemCount() {
//        return datalist.size();
//    }
//
//    class myviewholder extends RecyclerView.ViewHolder {
//           TextView Name ,
//                    Rollno;
////
//
//        public myviewholder(@NonNull View itemView) {
//            super(itemView);
//            Name=itemView.findViewById(R.id.Name);
//            Rollno=itemView.findViewById(R.id.Rollno);
////            Add=itemView.findViewById(R.id.adrs);
////            City=itemView.findViewById(R.id.city);
////            State=itemView.findViewById(R.id.state);
////            Country=itemView.findViewById(R.id.country);
////            PicknDrop=itemView.findViewById(R.id.pickdrop);
////            Prntname=itemView.findViewById(R.id.prntname);
////            Prntno=itemView.findViewById(R.id.prntno);
////            Prntemail=itemView.findViewById(R.id.prntemail);
////            Busroute=itemView.findViewById(R.id.busrte);
////            Busno=itemView.findViewById(R.id.busno);
//        }
//    }
//}
//
//
