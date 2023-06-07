package uni.erick.markedex;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import uni.erick.markedex.databinding.ActivityMapaTiendaBinding;

public class MapaTienda extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapaTiendaBinding binding;

    //Variables de XML
    Button btnSatelite, btnNormal, btnVerTienda;

    //Vectores
    String[] vNombres = new String[50];
    String[] vIdTienda = new String[50];
    LatLng vCoord[] = new LatLng[50]; //coordenadas
    int tiendasEncontradas;
    String tiendaSelect = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapaTiendaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        enlazarXML();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        recibirDatosTienda();

        //Listeners:
        btnSatelite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
            }
        });
        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(mMap.MAP_TYPE_NORMAL);
            }
        });
        btnVerTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tiendaSelect == "-1") {
                    Toast.makeText(MapaTienda.this, "No ha Seleccionado ninguna Tienda", Toast.LENGTH_SHORT).show();
                } else {
                    Intent irCatalogo = new Intent(MapaTienda.this, CatalogoTienda.class);
                    irCatalogo.putExtra("idTienda", tiendaSelect);
                    startActivity(irCatalogo);
                }
            }
        });
    }
    private void enlazarXML() {
        btnSatelite = findViewById(R.id.btnMapaTienda_satelite);
        btnNormal = findViewById(R.id.btnMapaTienda_normal);
        btnVerTienda = findViewById(R.id.btnMapaTienda_ver);
    }
    private void recibirDatosTienda() {
        //Carga los datos enviados del activity anteiror a los vectores
        vNombres = getIntent().getStringArrayExtra("nombre");
        vIdTienda = getIntent().getStringArrayExtra("id");
        String[] vLat = getIntent().getStringArrayExtra("lat");
        String[] vLon = getIntent().getStringArrayExtra("lon");
        tiendasEncontradas = Integer.parseInt(getIntent().getStringExtra("cont"));

        //Pasando cooredanas a vector de tipo LatLng
        for (int i = 0; i < tiendasEncontradas; i++) {
            vCoord[i] = new LatLng(Double.parseDouble(vLat[i]), Double.parseDouble(vLon[i]));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //habilitar controles de zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //eliminar marcadores anteriores
        googleMap.clear();

        for(int i = 0; i< tiendasEncontradas; i++){
            //nuevo objeto de tipo marca (punto rojo)
            MarkerOptions marca = new MarkerOptions();
            marca.position(vCoord[i]);
            marca.title(vNombres[i]);
            marca.snippet(vIdTienda[i]);
            marca.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_rojo_tienda));
            mMap.addMarker(marca); //añadir todas las marcas al mapa
        }
        CameraPosition camara = new CameraPosition.Builder().target(vCoord[0]).zoom(13).bearing(90).tilt(45).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camara));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            //Cuando se presiona un punto, obtener su Snippet y guardarlo en la var "TiendaSelect"
            @Override
            public boolean onMarkerClick(@NonNull Marker punto) {
                //El snippet tiene el id de la tienda
                tiendaSelect = punto.getSnippet();
                return false;
            }
        });
    }
}