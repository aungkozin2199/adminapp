package com.akz.movieapp;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class category extends Fragment {
    ListView mylist;
    ArrayList<String> ids=new ArrayList<String>();
    public category() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView= inflater.inflate(R.layout.fragment_category,container,false);
        FloatingActionButton add=myView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorypopup popup=new categorypopup();
                popup.show(getFragmentManager(),"Add Category");
            }
        });
         mylist=myView.findViewById(R.id.categorylist);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference ref=db.collection("categories");
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<CategoryModel> cm=new ArrayList<CategoryModel>();
                ids.clear();
                for (DocumentSnapshot snapshot: queryDocumentSnapshots){
                    cm.add(snapshot.toObject(CategoryModel.class));
                    ids.add(snapshot.getId());

                }
                CategoryAdapter adapter=new CategoryAdapter(cm);
                mylist.setAdapter(adapter);
            }
        });
        return myView;
    }
    private class CategoryAdapter extends BaseAdapter{
        ArrayList<CategoryModel> models=new ArrayList<>();

        public CategoryAdapter(ArrayList<CategoryModel> models)
        {
            this.models = models;
        }

        @Override
        public int getCount() {
            return models.size();
        }

        @Override
        public Object getItem(int position) {

            return models.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View catView=inflater.inflate(R.layout.category_name,null);
            TextView sr=catView.findViewById(R.id.catgsr);
            TextView name=catView.findViewById(R.id.catgname);
            sr.setText(position+1+"");
            name.setText(models.get(position).categoryName);
            final ImageView op=catView.findViewById(R.id.options);
            op.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu pmenu=new PopupMenu(getContext(),op);
                    MenuInflater inf=pmenu.getMenuInflater();
                    inf.inflate(R.menu.popmenu,pmenu.getMenu());
                    pmenu.show();
                    pmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getItemId()==R.id.edit_menu){
                                categorypopup popUp=new categorypopup();
                                popUp.model=models.get(position);
                                popUp.id=ids.get(position);
                                popUp.show(getFragmentManager(),"Edit");

                            }
                            //can also use else
                            if (item.getItemId()==R.id.delete_menu){
                                FirebaseFirestore db=FirebaseFirestore.getInstance();
                                CollectionReference ref=db.collection("categories");
                                ref.document(ids.get(position)).delete();
                                ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        ArrayList<CategoryModel> cm=new ArrayList<CategoryModel>();
                                        ids.clear();
                                        for (DocumentSnapshot snapshot: queryDocumentSnapshots){
                                            cm.add(snapshot.toObject(CategoryModel.class));
                                            ids.add(snapshot.getId());

                                        }
                                        CategoryAdapter adapter=new CategoryAdapter(cm);
                                        mylist.setAdapter(adapter);
                                    }
                                });
                            }
                            return true;
                        }
                    });


                }
            });
            return catView;
        }
    }

}
