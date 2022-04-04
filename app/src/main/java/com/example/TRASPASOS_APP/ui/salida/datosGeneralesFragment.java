package com.example.TRASPASOS_APP.ui.salida;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.TRASPASOS_APP.R;
import com.google.android.material.tabs.TabLayout;


public class datosGeneralesFragment extends Fragment {

    private TabLayout tabLayout, tabLayout2;
    public ViewPager2 pager1;
    public ViewPager2 pager2;
    private FragmentAdapterDatosGenerales adapter;

    public datosGeneralesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_datos_generales, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        pager1 = view.findViewById(R.id.vpSalida);

        tabLayout = view.findViewById(R.id.tabDatosGenerales);
        tabLayout2 = view.findViewById(R.id.tabSalida);
        pager2 = view.findViewById(R.id.vpDatosGenerales);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        adapter = new FragmentAdapterDatosGenerales(fm, getLifecycle());

        pager2.setAdapter(adapter);
        pager2.setUserInputEnabled(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());

                //pager1.setUserInputEnabled(false);
                //tabLayout2.removeTabAt(tab.getPosition());

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
                if(position==1)
                {


                }
            }
        });
    }

}