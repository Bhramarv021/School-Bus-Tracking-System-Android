package com.example.schoolbustrackingsystem.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolbustrackingsystem.Model.StudentModel;
import com.example.schoolbustrackingsystem.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<StudentModel> studentDataList;
    Context context;

    public MyAdapter(ArrayList<StudentModel> studentDataList, Context context) {
        this.studentDataList = studentDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_student_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        final StudentModel tempData = studentData.get(position);

        Log.d("MyAdapter", "Position and data is : "+position+" "+studentDataList.get(position).getStudentName());
        holder.name.setText(studentDataList.get(position).getStudentName());
        holder.rollNumber.setText(studentDataList.get(position).getRollNo());

    }

    @Override
    public int getItemCount() {
        Log.d("MyAdapter", "Size is : "+studentDataList.size());
        return studentDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView rollNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.viewStudentName);
            rollNumber = itemView.findViewById(R.id.viewStudentRollNumber);
        }
    }
}

