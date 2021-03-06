package com.ambinusian.adab.ui.lecturer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.airbnb.lottie.LottieAnimationView;
import com.ambinusian.adab.R;
import com.ambinusian.adab.all.ErrorFragment;
import com.ambinusian.adab.expandablenavigationdrawer.ExpandableListAdapter;
import com.ambinusian.adab.expandablenavigationdrawer.MenuModel;
import com.ambinusian.adab.room.ClassDatabase;
import com.ambinusian.adab.room.ClassEntity;
import com.ambinusian.adab.ui.student.FragmentCalendar;
import com.ambinusian.adab.ui.student.FragmentHelp;
import com.ambinusian.adab.ui.student.FragmentSetting;
import com.ambinusian.adab.ui.student.FragmentTopics;
import com.ambinusian.adab.ui.userprofile.UserProfileDialogFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    LottieAnimationView profilePicture;
    NavigationView navigationLecturer;
    DrawerLayout drawerLayout;
    Spinner SpinnerListSemester;
    ExpandableListView expandableListViewLecturer;
    ExpandableListAdapter expandableListAdapter;
    ArrayList<String> listSemester;
    List<MenuModel> groupList;
    List<MenuModel> courseSubject;
    HashMap<MenuModel,List<MenuModel>> childList;
    View headerView;
    ClassDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_main);

        toolbar = findViewById(R.id.tool_bar);
        profilePicture = findViewById(R.id.circle_image_profile_picture);
        navigationLecturer = findViewById(R.id.nv_adab_lecturer);
        drawerLayout = findViewById(R.id.mDrawerLayoutLecturer);
        expandableListViewLecturer = findViewById(R.id.expandableListViewLecturer);
        listSemester = new ArrayList<>();
        groupList = new ArrayList<>();
        courseSubject = new ArrayList<>();
        childList = new HashMap<>();
        headerView = getLayoutInflater().inflate(R.layout.adab_nav_header_layout,null);
        db = ClassDatabase.getDatabase(MainActivity.this);

        // set toolbar
        setSupportActionBar(toolbar);

        // remove title
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // profile picture onclick
        profilePicture.setOnClickListener(v -> showUserProfileDialog());

        //toolbar icon clicker
        toolbar.setNavigationOnClickListener((View view) -> drawerLayout.openDrawer(GravityCompat.START));

        //set first fragment launched
        getSupportFragmentManager().beginTransaction().replace(R.id.adab_lecturer_fragment,new FragmentHome()).commit();

        //add headerView to expandableListView
        expandableListViewLecturer.addHeaderView(headerView);

        //set up spinner
        SpinnerListSemester = headerView.findViewById(R.id.spinner_list_semesters);
        listSemester.add("2019 Semester 1");
        SpinnerListSemester.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,listSemester));

        //set first fragment launched
        getSupportFragmentManager().beginTransaction().replace(R.id.adab_lecturer_fragment,new FragmentHome()).commit();

        //set expandable navigation drawer
        prepareMenuData();
        populateExpandableList();

    }

    private void showUserProfileDialog() {
        FragmentManager fm = getSupportFragmentManager();
        UserProfileDialogFragment userProfileDialogFragment = new UserProfileDialogFragment();
        userProfileDialogFragment.show(fm, "fragment_user_profile_dialog");
    }

    public void prepareMenuData(){
        db.classDAO().getAllClass().observe(com.ambinusian.adab.ui.lecturer.MainActivity.this, new Observer<List<ClassEntity>>() {
            @Override
            public void onChanged(List<ClassEntity> classEntities) {
                for(int i=0;i<classEntities.size();i++){
                    int count = 0;
                    for(int j=0;j<courseSubject.size();j++)
                        if(classEntities.get(i).getCourseName().equals(courseSubject.get(j).menuName) && i!= j){
                            count++;
                        }
                    if(count == 0){
                        courseSubject.add(new MenuModel(6,classEntities.get(i).getCourseName(),false,false));
                    }
                }

                //set group menu list
                groupList.add(new MenuModel(0,"Home",false,false));
                groupList.add(new MenuModel(1,"Topics",true,true));
                groupList.add(new MenuModel(2,"Calendar",false,false));
                groupList.add(new MenuModel(3,"Discussion",false,false));
                groupList.add(new MenuModel(4,"Help",false,false));
                groupList.add(new MenuModel(5,"Setting",false,false));

                //set child menu list at topics menu, index == 1
                childList.put(groupList.get(1),courseSubject);

                populateExpandableList();
            }
        });
    }

    public void populateExpandableList(){

        expandableListAdapter = new ExpandableListAdapter(this, groupList,childList);
        expandableListViewLecturer.setAdapter(expandableListAdapter);
        expandableListViewLecturer.setItemChecked(1,true);

        expandableListViewLecturer.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            if(groupPosition == 0){
                getSupportFragmentManager().beginTransaction().replace(R.id.adab_lecturer_fragment,new FragmentHome()).commit();
            }
            else if(groupPosition == 2){
                getSupportFragmentManager().beginTransaction().replace(R.id.adab_lecturer_fragment,new FragmentCalendar()).commit();
            }
            else if(groupPosition == 3){
                getSupportFragmentManager().beginTransaction().replace(R.id.adab_lecturer_fragment,new ErrorFragment()).commit();
            }
            else if(groupPosition == 4){
                getSupportFragmentManager().beginTransaction().replace(R.id.adab_lecturer_fragment,new FragmentHelp()).commit();
            }
            else if(groupPosition == 5){
                getSupportFragmentManager().beginTransaction().replace(R.id.adab_lecturer_fragment,new FragmentSetting()).commit();
            }
            else if(groupPosition == 1){
                if(!expandableListViewLecturer.isGroupExpanded(1) && expandableListViewLecturer.getCheckedItemPosition() > 2){
                    parent.setItemChecked(expandableListViewLecturer.getCheckedItemPosition()+courseSubject.size(),true);
                } else if(!expandableListViewLecturer.isGroupExpanded(1) && expandableListViewLecturer.getCheckedItemPosition() < 0){
                    parent.setItemChecked(expandableListViewLecturer.getCheckedItemPosition()+100,true);
                } else if(expandableListViewLecturer.isGroupExpanded(1) && expandableListViewLecturer.getCheckedItemPosition() > courseSubject.size()+2 ){
                    parent.setItemChecked(expandableListViewLecturer.getCheckedItemPosition()-courseSubject.size(),true);
                }  else if(expandableListViewLecturer.isGroupExpanded(1) && (expandableListViewLecturer.getCheckedItemPosition() >= 3 && expandableListViewLecturer.getCheckedItemPosition() <= courseSubject.size()+2)) {
                    parent.setItemChecked(expandableListViewLecturer.getCheckedItemPosition()- 100, true);
                }
            }

            //don't close the drawer if selected Topics menu. Otherwise, just close it
            if(groupPosition != 1){
                drawerLayout.closeDrawer(GravityCompat.START);
            }

            //for highlight menu background
            int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(groupPosition));
            if(!(groupList.get(groupPosition).hasChildren)){
                parent.setItemChecked(index-2,true);
                parent.setItemChecked(index,true);
                drawerLayout.closeDrawer(GravityCompat.START);
            }

            expandableListAdapter.notifyDataSetChanged();
            return false;
        });

        expandableListViewLecturer.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,childPosition));

            parent.setItemChecked(index,true);
            drawerLayout.closeDrawer(GravityCompat.START);

            //send bundle to Topics Fragment
            Bundle bundle = new Bundle();
            bundle.putString("class_id","12");
            bundle.putString("topic_title", Objects.requireNonNull(childList.get(groupList.get(groupPosition))).get(childPosition).menuName);
            bundle.putString("topic_name",courseSubject.get(childPosition).menuName);
            FragmentTopics fragmentTopics = new FragmentTopics();
            fragmentTopics.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.adab_lecturer_fragment, fragmentTopics).commit();

            return false;
        });
    }
}
