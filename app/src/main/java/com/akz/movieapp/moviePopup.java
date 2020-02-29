package com.akz.movieapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class moviePopup extends DialogFragment {

    MovieModel model;
    String id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.moviename,container,false);
        final EditText edtname=myview.findViewById(R.id.movie_name);
        final EditText edtimage=myview.findViewById(R.id.movieimg_link);
        final EditText edtvideo=myview.findViewById(R.id.movie_link);
        final Spinner spcategory=myview.findViewById(R.id.series_spinner);
        final Spinner spseries=myview.findViewById(R.id.spinner2);


        if (model!=null){
            edtname.setText(model.name);
            edtvideo.setText(model.videoLink);
            edtimage.setText(model.imageLink);

        }
        final ArrayList<String> categoryName=new ArrayList<String>();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference ref=db.collection("categories");
        CollectionReference serieref=db.collection("series");

        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot s:queryDocumentSnapshots){
                    CategoryModel c=s.toObject(CategoryModel.class);
                    categoryName.add(c.categoryName);
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,categoryName);
                spcategory.setAdapter(adapter);
                if (model!=null){
                    for (int i=0;i<categoryName.size();i++){
                        if (categoryName.get(i).equals(model.category)){
                            spcategory.setSelection(i);
                            break;
                        }
                    }
                }

            }
        });
        final ArrayList<String> serieName=new ArrayList<String>();

        serieref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot:queryDocumentSnapshots){
                    serieName.add(snapshot.toObject(SeriesModel.class).name);

                }ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,serieName);
                spseries.setAdapter(adapter);
                if (model!=null){
                    for (int i=0;i<serieName.size();i++){
                        if (serieName.get(i).equals(model.series)){
                            spseries.setSelection(i);
                        }
                    }
                }



            }
        });
        final CollectionReference moviesRef=db.collection("movies");
        Button save=myview.findViewById(R.id.savebutt);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieModel newModel=new MovieModel();
                newModel.name=edtname.getText().toString().trim();
                newModel.imageLink=edtimage.getText().toString().trim();
                newModel.videoLink=edtvideo.getText().toString().trim();
                newModel.category=categoryName.get(spcategory.getSelectedItemPosition());
                newModel.series=serieName.get(spseries.getSelectedItemPosition());

                if(model!=null){
                    moviesRef.document(id).set(newModel);
                    Toast.makeText(getContext(),"Update OK",Toast.LENGTH_LONG).show();

                }else{
                    moviesRef.add(newModel);
                    Toast.makeText(getContext(),"Save OK",Toast.LENGTH_LONG).show();
                }
                edtimage.setText("");
                edtname.setText("");
                edtvideo.setText("");


            }
        });

        Button cancel=myview.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtimage.setText("");
                edtname.setText("");
                edtvideo.setText("");
                dismiss();
            }
        });
        Button close=myview.findViewById(R.id.moviecls);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return myview;
    }
}
