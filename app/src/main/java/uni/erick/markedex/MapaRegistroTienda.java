package uni.erick.markedex;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uni.erick.markedex.databinding.ActivityMapaRegistroTiendaBinding;
import uni.erick.markedex.extra.Extra;

public class MapaRegistroTienda extends FragmentActivity implements OnMapReadyCallback {
    //Objetos de Volley. envia solicitud en cola
    RequestQueue requestQueue;
    //Objeto de la clase con datos generales
    Extra extra = new Extra();

    Button btnGuardar,btnSat,btnNor;
    String idT ;
    int i = 7;
    String [] categorias = new String[i];

    private GoogleMap mMap;
    private ActivityMapaRegistroTiendaBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapaRegistroTiendaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestQueue = Volley.newRequestQueue(this);
        btnGuardar = findViewById(R.id.btn_savReg);
        btnSat = findViewById(R.id.btn_satReg);
        btnNor = findViewById(R.id.tbn_norReg);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
            }
        });
        btnNor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(mMap.MAP_TYPE_NORMAL);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Posición del marcador inicial
        LatLng SantaCruz = new LatLng(-17.78328373875767, -63.182153508153014);
        mMap.getUiSettings().setZoomControlsEnabled(true); //Coloca los botones de zoom
        mMap.addMarker(new MarkerOptions().position(SantaCruz).title("Marcar sitio")); //mensaje del marcador rojo
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SantaCruz));

        //Colocando la vista mas cerca del marcador (Santa Cruz)
        CameraPosition camara = new CameraPosition.Builder().target(SantaCruz).zoom(12).bearing(0).tilt(0).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camara));

        //Listener de nuevo marcador puesto por usuario
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng coordenada) { //coordenada: punto puesto por esl usuario
                //Obtener datos enviados con .puExtra desde Activity_registro_tienda3.class
                String usuario = getIntent().getStringExtra("USUARIO");
                String contraseña = getIntent().getStringExtra("CONTRASEÑA");
                String nombre = getIntent().getStringExtra("NOMBRETI");
                String telefono = getIntent().getStringExtra("TELEFONO");
                String descripcion = getIntent().getStringExtra("DESCRIPCION");
                String Esta = getIntent().getStringExtra("ESTACIONAMIENTO");
                String Delive = getIntent().getStringExtra("DELIVERY");

                //Colocar los datos en ell nuevo marcador
                MarkerOptions marca = new MarkerOptions();
                marca.position(coordenada);
                marca.title("Nombre: "+nombre +"  Telefono: "+ telefono); //título de marcador
                marca.snippet(coordenada.latitude+" - "+coordenada.longitude); //subtitulo de marcador
                marca.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_rojo_tienda));

                googleMap.animateCamera(CameraUpdateFactory.newLatLng(coordenada)); //mover la cámara a la posición de la coordeanda

                googleMap.clear(); //eliminar marcadores anteriores
                googleMap.addMarker(marca); //se envía todos los datos anteriores al marcador para que aparezca en mapa

                //Botón Guardar
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String lati = String.valueOf(coordenada.latitude);//String.valueOf transforma un double a string
                        String longi = String.valueOf(coordenada.longitude);
                        registrarTienda(usuario,contraseña,nombre,descripcion,Esta,Delive,telefono,lati,longi);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                traeridporNombre(nombre);
                            }
                        },4000);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent Obj = new Intent(MapaRegistroTienda.this,RegistroTienda3.class);
                                Obj.putExtra("IDTIENDA",idT);
                                System.out.println(idT);
                                startActivity(Obj);
                            }
                        },5000);

                    }
                });

            }
        });
    }

    private void registrarTienda(final String usuario, final String contraseña, final String nombre,final String descripcion, final String estacionamiento,final String delivery,final String telefono, final String latitud, final String longitud) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                //url del archivo.php:
                extra.url("crearTienda.php"),
                new Response.Listener<String>() { //verifica si se acepto la solicitud
                    @Override
                    public void onResponse(String response) {
                        //Encaso de que la BD responda, no hacer nada
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapaRegistroTienda.this, "Ocurrió un Error en la BD", Toast.LENGTH_SHORT).show();

                    }
                }
        ){ //Esto es para darle decodif
            //formando HashMap (clave:valor) desde los parámetros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                //nombre de variable en archivo php, parametro
                params.put("usuario", usuario);
                params.put("pass", contraseña);
                params.put("nombre", nombre);
                params.put("descripcion", descripcion);
                params.put("estacionamiento", estacionamiento);
                params.put("delivery", delivery);
                params.put("telefono", telefono);
                params.put("latitud", latitud);
                params.put("longitud", longitud);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void traeridporNombre (final String nombre){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                extra.url("traerIDTiendaPorNombre.php?nombre=" + nombre),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            idT = response.getString("id");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapaRegistroTienda.this, "Ocurrió un Error en la BD", Toast.LENGTH_SHORT).show();
                    }
                }

        );
        requestQueue.add(request);
    }
}