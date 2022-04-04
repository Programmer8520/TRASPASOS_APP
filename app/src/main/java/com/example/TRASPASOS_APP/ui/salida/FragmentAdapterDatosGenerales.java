package com.example.TRASPASOS_APP.ui.salida;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapterDatosGenerales extends FragmentStateAdapter {
    public FragmentAdapterDatosGenerales(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position)
        {
            case 0 :
                fragment=new almacenFragment();
                break;
            case 1 :
                fragment=new personalFragment();
                break;
            case 2 :
                fragment=new transporteFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
