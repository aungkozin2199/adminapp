package com.akz.movieapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class categorypopup extends DialogFragment {
    CategoryModel model;
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.category, container, false);
        final EditText name=myView.findViewById(R.id.catg);
        if(model!=null){
            name.setText(model.categoryName);
        }
        Button save=myView.findViewById(R.id.catgsavebut);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
                }
                else{
                    //Save
                    CategoryModel tempmodel=new CategoryModel();
                    tempmodel.categoryName=name.getText().toString().trim();
                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                    CollectionReference ref=db.collection("categories");
                    if(model!=null) {
                        ref.document(id).set(tempmodel);
                        Toast.makeText(getContext(),"Update OK",Toast.LENGTH_LONG).show();
                    }
                    else {
                        ref.add(tempmodel);
                        Toast.makeText(getContext(),"Save success",Toast.LENGTH_LONG).show();
                    }
                    name.setText("");

                }


            }
        });

        Button close=myView.findViewById(R.id.catgclsbut);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();;
            }
        });
        return myView;
    }
}

