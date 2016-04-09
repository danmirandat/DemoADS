package pe.edu.unmsm.sistemas.ads.demoads;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class GPSActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    TextView tvLatitud;
    TextView tvLongitud;
    TextView tvTraza;
    ToggleButton tgbtActivarGPS;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvLatitud = (TextView) findViewById(R.id.tvLatitud);
        tvLongitud = (TextView) findViewById(R.id.tvLongitud);
        tvTraza = (TextView) findViewById(R.id.tvTraza);
        tgbtActivarGPS = (ToggleButton) findViewById(R.id.tgbtActivarGPS);

        tgbtActivarGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    verificarPermisoGPS();
                    tgbtActivarGPS.setText("Desactivar");
                } else {
                    desactivarGPS();
                    tgbtActivarGPS.setText("Activar");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(getApplicationContext(), "On start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Toast.makeText(getApplicationContext(), "On restart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(getApplicationContext(), "On resume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(getApplicationContext(), "On pause", Toast.LENGTH_SHORT).show();
        desactivarGPS();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Toast.makeText(getApplicationContext(), "On stop", Toast.LENGTH_SHORT).show();
        desactivarGPS();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Toast.makeText(getApplicationContext(), "On destroy", Toast.LENGTH_SHORT).show();
        desactivarGPS();
    }

    public void verificarPermisoGPS() {
        tvTraza.append("Verificando permisos" + "\n");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getApplicationContext(), "Debe otorgar el permiso de GPS", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
            return;
        }
        activarGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    activarGPS();
                } else {
                    tvTraza.append("Permiso denegado :(" + "\n");
                }
                return;
            }
        }
    }

    public void activarGPS() {
        tvTraza.append("Activando GPS" + "\n");

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                tvTraza.append("Posición cambiada" + "\n");

                // Called when a new location is found by the gps location provider.
                tvLatitud.setText("Latitud: " + location.getLatitude());
                tvLongitud.setText("Longitud: " + location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                tvTraza.append("Estado cambiado" + "\n");
            }

            public void onProviderEnabled(String provider) {
                tvTraza.append("Proveedor habilitado" + "\n");
            }

            public void onProviderDisabled(String provider) {
                tvTraza.append("Proveedor deshabilitado" + "\n");
            }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException se) {
            System.out.println("Excepcion: " + se.getMessage());
            se.printStackTrace();
        }
    }

    public void desactivarGPS () {
        tvTraza.append("Desactivando GPS" + "\n");

        try {
            locationManager.removeUpdates(locationListener);
        } catch (SecurityException se) {
            System.out.println("Excepcion: " + se.getMessage());
            se.printStackTrace();
        }
    }
}
