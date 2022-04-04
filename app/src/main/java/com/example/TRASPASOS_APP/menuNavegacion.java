package com.example.TRASPASOS_APP;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.TRASPASOS_APP.databinding.ActivityMenuNavegacionBinding;
import com.example.TRASPASOS_APP.ui.salida.almacenFragment;
import com.example.TRASPASOS_APP.ui.salida.datosGeneralesFragment;
import com.example.TRASPASOS_APP.ui.salida.personalFragment;
import com.google.android.material.navigation.NavigationView;


public class menuNavegacion extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuNavegacionBinding binding;
    String mod;
    datosGeneralesFragment dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuNavegacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenuNavegacion.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.menuSalidaFragment, R.id.menuEntradaFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_navegacion);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // Menu de navegacion, la opcion a la que se le de click es a la pantalla que llevara al usuario.
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        navController.navigate(R.id.nav_home);
                        break;
                    case R.id.escaneoSalida:
                        navController.navigate(R.id.navigationSalida);
                        break;
                    case R.id.escaneoEntrada:
                        navController.navigate(R.id.navigationEntrada);
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "DEFAULT", Toast.LENGTH_SHORT).show();
                        break;
                }
                //close navigation drawer
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

        });


        dt = new datosGeneralesFragment();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_navegacion);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

}