package uni.erick.markedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uni.erick.markedex.extra.Extra;

public class NuevoArticulo extends AppCompatActivity {
    //Objetos de Volley
    RequestQueue solicitud;

    EditText txtNombre, txtPrecio, txtDesc;
    Button btnAgregar;

    Extra extra = new Extra();

    String idTienda;
    String idArtNuevo;
    boolean articuloCreado = false;
    boolean traerIdArt = false;
    boolean listo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_articulo);

        solicitud = Volley.newRequestQueue(this);
        enlazarXML();

        idTienda = getIntent().getStringExtra("idTienda");

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreArt = txtNombre.getText().toString();
                String precioArt = txtPrecio.getText().toString();
                String descArt = txtDesc.getText().toString();

                crearNuevoArticulo(nombreArt, precioArt, descArt);
                Toast.makeText(NuevoArticulo.this, "Agrengando", Toast.LENGTH_SHORT).show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!listo){
                            //Si aún no se agegó el nuevo articulo a la BD, repetir
                            handler.postDelayed(this, 100);
                        }else{
                            Intent irCat = new Intent(NuevoArticulo.this, AdmCatalogo.class);
                            irCat.putExtra("idTienda", idTienda);
                            startActivity(irCat);
                        }
                    }
                },100);


            }
        });
    }
    private void enlazarXML(){
        txtNombre = findViewById(R.id.txtNuevoArt_nombre);
        txtPrecio = findViewById(R.id.txtNuevoArt_precio);
        txtDesc = findViewById(R.id.txtNuevoArt_desc);

        btnAgregar = findViewById(R.id.btnNuevoArticulo_agregar);
    }

    private void crearNuevoArticulo(final String nombre, final String precio, final String desc){
        nuevoArticulo(nombre, precio, desc);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!articuloCreado){
                    //Si aún no se agegó el nuevo articulo a la BD, repetir
                    handler.postDelayed(this, 100);
                }else{
                    traerIdArtCreado(nombre, precio);
                }
            }
        },100);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!traerIdArt){
                    handler.postDelayed(this, 100);
                }else{
                    agrerarArtACatalogo();
                }
            }
        },100);





    }




    private void nuevoArticulo(final String nombre, final String precio, final String desc) {
        articuloCreado = false;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                //url del archivo.php:
                extra.url("crearArticulo.php"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String respuesta) {
                        articuloCreado = true;
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NuevoArticulo.this, "Ocurrió un error en la BD", Toast.LENGTH_SHORT).show();

                    }
                }
        ){ //Esto es para darle decodif
            //formando HashMap (clave:valor) desde los parámetros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                //nombre de variable en archivo php, parametro
                params.put("precio", precio);
                params.put("nombre", nombre);
                params.put("descripcion", desc);
                return params;
            }
        };
        solicitud.add(stringRequest);
    }
    private void traerIdArtCreado(final String nombre, final String precio) {
        traerIdArt = false;
        JsonObjectRequest datos = new JsonObjectRequest(
                Request.Method.GET,
                extra.url("getIdArticulo.php?nombre="+nombre+"&precio="+precio),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respuesta) {
                        try {
                            idArtNuevo = respuesta.getString("id");
                            traerIdArt = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NuevoArticulo.this, "Ocurrió un error al traer ID", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        solicitud.add(datos);
    }
    private void agrerarArtACatalogo() {
        listo = false;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                //url del archivo.php:
                extra.url("agregarArtACatalogo.php"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String respuesta) {
                        listo = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NuevoArticulo.this, "Ocurrió un error al agregar al Catalogo", Toast.LENGTH_SHORT).show();

                    }
                }
        ){ //Esto es para darle decodif
            //formando HashMap (clave:valor) desde los parámetros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                //nombre de variable en archivo php, parametro
                params.put("idArt", idArtNuevo);
                params.put("idTienda", idTienda);
                return params;
            }
        };
        solicitud.add(stringRequest);
    }
}