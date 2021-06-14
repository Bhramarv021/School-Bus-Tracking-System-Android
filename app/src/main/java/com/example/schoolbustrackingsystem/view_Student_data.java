package com.example.schoolbustrackingsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class view_Student_data extends AppCompatActivity
{
    RecyclerView recview;
    ArrayList<model> datalist;
    FirebaseFirestore mfbs;
    myAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_data);

        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        datalist=new ArrayList<>();
        adapter=new myAdapter(datalist,getApplicationContext());
        recview.setAdapter(adapter);

        mfbs=FirebaseFirestore.getInstance();


        mfbs.collection("Schools")
                .document("School_1")
                .collection("Students")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list)
                        {
                            model obj=d.toObject(model.class);
                            datalist.add(obj);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}




//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots)
//                    {
//                        List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
//                        for (DocumentSnapshot d:list)
//                        {
//                            model obj = d.toObject(model.class);
//                            datalist.add(obj);
//                        }
//                    }
//                });
//
//
//
//    }
//
//
//}