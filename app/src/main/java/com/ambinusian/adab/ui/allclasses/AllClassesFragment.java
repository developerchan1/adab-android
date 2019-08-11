package com.ambinusian.adab.ui.allclasses;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ambinusian.adab.ui.mainactivity.courses.recyclerview.CourseAdapter;
import com.ambinusian.adab.ui.mainactivity.courses.recyclerview.CourseModel;
import com.ambinusian.adab.R;

import java.util.ArrayList;

public class AllClassesFragment extends Fragment {

    RecyclerView coursesRecyclerView;
    ArrayList<CourseModel> coursesList;
    LinearLayout liveLayout;
    ImageView liveClassIcon;
    TextView liveClassTitle, liveCourse, liveClassMeeting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_classes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coursesRecyclerView = view.findViewById(R.id.rv_courses);
        liveLayout = view.findViewById(R.id.liveLayout);
        liveClassIcon = view.findViewById(R.id.liveClassIcon);
        liveClassTitle  = view.findViewById(R.id.tv_liveClassTitle);
        liveCourse = view.findViewById(R.id.tv_liveCourse);
        liveClassMeeting = view.findViewById(R.id.tv_liveClassMeeting);
        coursesList = new ArrayList<>();

        //if live class is starting
        if(true){
            liveLayout.setVisibility(View.VISIBLE);
            liveClassIcon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_class_59_pencilpaper));
            liveClassTitle.setText("Design");
            liveCourse.setText("Design");
            liveClassMeeting.setText("Meeting 99999");
        }


        //set layout manager for recycler view
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //set list data for recycler view
        coursesList.add(new CourseModel(0,"Yesterday", "Storage", "MOOP","Meeting 11","MOBI009","LA03","LEC"));
        coursesList.add(new CourseModel(0,"Yesterday", "Storage", "MOOP","Meeting 11","MOBI009","LA03","LEC"));
        coursesList.add(new CourseModel(0,"Yesterday", "Storage", "MOOP","Meeting 11","MOBI009","LA03","LEC"));

        //set adapter for recycler view
        coursesRecyclerView.setAdapter(new CourseAdapter(getContext(),coursesList));


    }
}
