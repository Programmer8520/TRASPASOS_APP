package com.example.TRASPASOS_APP.ui.salida;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.TRASPASOS_APP.R;
import com.google.android.material.tabs.TabLayout;


public class recuperacionFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 pager2;

    private FragmentAdapterDatosRecuperados adapter;


    public recuperacionFragment() {
        // Required empty public constructor

    }


    public static recuperacionFragment newInstance(String param1, String param2) {
        recuperacionFragment fragment = new recuperacionFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recuperacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);

        tabLayout = view.findViewById(R.id.tabDatosRecuperados);
        pager2 = view.findViewById(R.id.vpDatosRecuperados);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        adapter = new FragmentAdapterDatosRecuperados(fm, getLifecycle());
        pager2.setAdapter(adapter);
        //pager2.setUserInputEnabled(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

}