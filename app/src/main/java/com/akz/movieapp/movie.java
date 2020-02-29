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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class movie extends Fragment {
    ArrayList<String> ids=new ArrayList<String>();
    ListView list;

    public movie() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview=inflater.inflate(R.layout.fragment_movie,container,false);
        FloatingActionButton add=myview.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moviePopup popUp=new moviePopup();
                popUp.show(getFragmentManager(),"Show Movies");
            }
        });
        list=myview.findViewById(R.id.movielist);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        CollectionReference ref=db.collection("movies");
        ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<MovieModel> myModels=new ArrayList<MovieModel>();
                ids.clear();
                for (DocumentSnapshot snapshot: queryDocumentSnapshots){
                    myModels.add(snapshot.toObject(MovieModel.class));
                    ids.add(snapshot.getId());
                }
                movie.MovieAdapter adapter=new movie.MovieAdapter(myModels);
                list.setAdapter(adapter);
            }
        });

        // Inflate the layout for this fragment
        return myview;
    }
    private class MovieAdapter extends BaseAdapter {
        ArrayList<MovieModel> movieModels=new ArrayList<>();

        public MovieAdapter(ArrayList<MovieModel> movieModels) {
            this.movieModels = movieModels;
        }

        @Override
        public int getCount() {
            return movieModels.size();
        }

        @Override
        public Object getItem(int position) {
            return movieModels.get(position);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View myView=getLayoutInflater().inflate(R.layout.movieitem,null);
            final MovieModel temp=movieModels.get(position);
            TextView sr=myView.findViewById(R.id.txtsr);
            TextView name=myView.findViewById(R.id.txtname);
            final ImageView myimage=myView.findViewById(R.id.imageView);

            sr.setText(String.valueOf(position+1));

            name.setText(temp.name);
            Glide.with(getContext())
                    .load(temp.imageLink)
                    .into(myimage);
            myimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu pmenu=new PopupMenu(getContext(),myimage);
                    MenuInflater inf=pmenu.getMenuInflater();
                    inf.inflate(R.menu.popmenu,pmenu.getMenu());
                    pmenu.show();
                    pmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getItemId()==R.id.edit_menu){
                                moviePopup popUp=new moviePopup();
                                popUp.model=temp;
                                popUp.id=ids.get(position);
                                popUp.show(getFragmentManager(),"Edit");

                            }
                            //can also use else
                            if (item.getItemId()==R.id.delete_menu){
                                FirebaseFirestore db=FirebaseFirestore.getInstance();
                                CollectionReference ref=db.collection("movies");
                                ref.document(ids.get(position)).delete();
                                ref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        ArrayList<MovieModel> cm=new ArrayList<MovieModel>();
                                        ids.clear();
                                        for (DocumentSnapshot snapshot: queryDocumentSnapshots){
                                            cm.add(snapshot.toObject(MovieModel.class));
                                            ids.add(snapshot.getId());

                                        }
                                        com.akz.movieapp.movie.MovieAdapter adapter=new movie.MovieAdapter(cm);
                                        list.setAdapter(adapter);
                                    }
                                });
                            }
                            return true;
                        }
                    });
                }
            });



            return myView;
        }
    }
}
