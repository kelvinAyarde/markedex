package uni.erick.markedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import uni.erick.markedex.extra.Extra;

public class UsuarioCategoria extends AppCompatActivity {
    ImageButton ropaAccesorios, comidaSnack, tecnologia, cosasHogar, frutasVerduras, materialEscolar, cosmeticos, ferreteria;

    //Objetos de Volley
    RequestQueue solicitud;
    //Objeto de la clase con datos generales
    Extra extra = new Extra();

    //Vectores
    String[] vNombre = new String[50];
    String[] vIdTienda = new String[50];
    String[] vLat = new String[50];
    String[] vLon = new String[50];

    int filasRecibidas = -1; //catnidad de casillas con datos


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_categoria);

        solicitud = Volley.newRequestQueue(this);
        findViewById();

        //Listeners
        ropaAccesorios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatosTiendaPorIdCat(1);
                irAMapa();
            }
        });
        comidaSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatosTiendaPorIdCat(2);
                irAMapa();
            }
        });
        tecnologia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatosTiendaPorIdCat(3);
                irAMapa();
            }
        });
        cosasHogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatosTiendaPorIdCat(4);
                irAMapa();
            }
        });
        frutasVerduras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatosTiendaPorIdCat(5);
                irAMapa();
            }
        });
        materialEscolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatosTiendaPorIdCat(6);
                irAMapa();
            }
        });
        cosmeticos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatosTiendaPorIdCat(7);
                irAMapa();
            }
        });
        ferreteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatosTiendaPorIdCat(8);
                irAMapa();
            }
        });
    }
    private void findViewById(){
        //Enlazando variables a items en xml
        ropaAccesorios = findViewById(R.id.btnUsuarioCategoria_RopaAccesorios);
        comidaSnack = findViewById(R.id.btnUsuarioCategoria_ComidaSnack);
        tecnologia = findViewById(R.id.btnUsuarioCategoria_Tecnologia);
        cosasHogar = findViewById(R.id.btnUsuarioCategoria_CosasHogar);
        frutasVerduras = findViewById(R.id.btnUsuarioCategoria_FrutasVerduras);
        materialEscolar = findViewById(R.id.btnUsuarioCategoria_MaterialEscolar);
        cosmeticos = findViewById(R.id.btnUsuarioCategoria_Cosmeticos);
        ferreteria = findViewById(R.id.btnUsuarioCategoria_Ferreteria);

    }

    private void getDatosTiendaPorIdCat(int idCategoria) {
        filasRecibidas = -1;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                extra.url("getDatosTiendaPorIdCat.php?id=" + idCategoria),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int size = response.length();
                        //Si llegó alguna fila:
                        if(size>0){
                            for(int i=0; i<size; i++){
                                try {
                                    JSONArray jsonArray = new JSONArray(response.get(i).toString());
                                    vNombre[i] = jsonArray.getString(0);
                                    vLat[i] = jsonArray.getString(1);
                                    vLon[i] = jsonArray.getString(2);
                                    vIdTienda[i] = jsonArray.getString(3);
                                    filasRecibidas = i+1; //cantidad de casillas actuales
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            Toast.makeText(UsuarioCategoria.this, "No se encontraron Tiendas", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UsuarioCategoria.this, "Ocurrió un error en la BD", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        Toast.makeText(UsuarioCategoria.this, "Cargando", Toast.LENGTH_SHORT).show();
        solicitud.add(jsonArrayRequest);
    }

    private void irAMapa(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (filasRecibidas == -1){
                    //Si  el nro de filas recibidas no ha cambiado, repetir
                    handler.postDelayed(this, 500);
                }else{
                    Intent aMapa = new Intent(UsuarioCategoria.this, MapaTienda.class);
                    aMapa.putExtra("nombre", vNombre);
                    aMapa.putExtra("lat", vLat);
                    aMapa.putExtra("lon", vLon);
                    aMapa.putExtra("cont", Integer.toString(filasRecibidas));
                    aMapa.putExtra("id", vIdTienda);
                    startActivity(aMapa);
                }
            }
        },500);
    }

}