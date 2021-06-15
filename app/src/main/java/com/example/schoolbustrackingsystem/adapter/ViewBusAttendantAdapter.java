package com.example.schoolbustrackingsystem.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolbustrackingsystem.Model.BusAttendantModel;
import com.example.schoolbustrackingsystem.R;

import java.util.ArrayList;

public class ViewBusAttendantAdapter extends RecyclerView.Adapter<ViewBusAttendantAdapter.BusAttendantViewHolder> {

    ArrayList<BusAttendantModel> busAttendantDataList;
    Context context;

    public ViewBusAttendantAdapter(ArrayList<BusAttendantModel> studentDataList, Context context) {
        this.busAttendantDataList = studentDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewBusAttendantAdapter.BusAttendantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_bus_attendant_view,parent,false);
        return new ViewBusAttendantAdapter.BusAttendantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewBusAttendantAdapter.BusAttendantViewHolder holder, int position) {
//        final StudentModel tempData = studentData.get(position);

        Log.d("MyAdapter", "Position and data is : "+position+" "+busAttendantDataList.get(position).getName());
        holder.name.setText(busAttendantDataList.get(position).getName());
        holder.routeNumber.setText(busAttendantDataList.get(position).getRouteNumber());

    }

    @Override
    public int getItemCount() {
        Log.d("MyAdapter", "Size is : "+busAttendantDataList.size());
        return busAttendantDataList.size();
    }

    class BusAttendantViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView routeNumber;

        public BusAttendantViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.viewBusAttendantName);
            routeNumber = itemView.findViewById(R.id.viewAttendantRouteNumber);
        }
    }
}
