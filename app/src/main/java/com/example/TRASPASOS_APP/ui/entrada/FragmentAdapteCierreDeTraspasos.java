package com.example.TRASPASOS_APP.ui.entrada;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapteCierreDeTraspasos extends FragmentStateAdapter {
    public FragmentAdapteCierreDeTraspasos(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position)
        {
            case 0 :
                fragment=new solicitudesTraspasoFragment();
                break;
            case 1 :
                fragment=new escaneoEntradaFragment(); //este se deja tal y como esta porque se seguira usando.
                break;

        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
