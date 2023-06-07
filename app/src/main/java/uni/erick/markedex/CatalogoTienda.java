package uni.erick.markedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uni.erick.markedex.extra.AdaptadorListViewCatalogo;
import uni.erick.markedex.extra.Extra;

public class CatalogoTienda extends AppCompatActivity {
    //Objetos de Volley
    RequestQueue solicitud;
    //Objeto de la clase con datos generales
    Extra extra = new Extra();
    //Elementos XML
    TextView nombreTienda, estacionamientoTienda, deliveryTienda, descTienda, telf;
    ListView listaArticulos;
    LinearLayout linkWP;

    String idTienda;
    String telefTienda;
    private String linkAWap = "https://api.whatsapp.com/send?phone=591";

    //Vectores Articulos
    String[] vPrecioArt = new String[50];
    String[] vNombreArt = new String[50];
    String[] vDescArt = new String[50];
    int cantArt = -1; //Cantidad de articulos encontrados

    //Emojis
    String emojiMoto = "\uD83D\uDEF5";
    String emojiCompra = "\uD83D\uDECD️";
    String emojiCheck = "✅";
    String emojiNo = "❎";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_tienda);
        solicitud = Volley.newRequestQueue(this);
        enlazarXML();
        idTienda = getIntent().getStringExtra("idTienda");
        Toast.makeText(this, "Cargando", Toast.LENGTH_SHORT).show();

        traerDatosTienda();
        traerCatalogo();
        cargarListaArticulos();

        linkWP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wp = new Intent(Intent.ACTION_VIEW);
                wp.setData(Uri.parse(linkAWap));
                startActivity(wp);
            }
        });
    }
    private void enlazarXML(){
        nombreTienda = findViewById(R.id.txtCatalogoTienda_nombreTienda);
        estacionamientoTienda = findViewById(R.id.txtCatalogoTienda_estacionamientoTienda);
        deliveryTienda = findViewById(R.id.txtCatalogoTienda_deliveryTienda);
        descTienda = findViewById(R.id.txtCatalogoTienda_descTienda);

        listaArticulos = findViewById(R.id.lvAdmCatalogo_listaArticulos);
        telf = findViewById(R.id.txtCatalogoTienda_numtelf);
        linkWP = findViewById(R.id.lyAdmCatalogo_agregarArt);
    }
    private void cargarListaArticulos() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(cantArt == -1){
                    //Si  el nro de artículos recibidos no ha cambiado, repetir
                    handler.postDelayed(this, 500);
                }else{
                    //Adaptador de vector a lisView
                    listaArticulos.setAdapter(new AdaptadorListViewCatalogo(CatalogoTienda.this, vNombreArt, vPrecioArt, cantArt));
                    listaArticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast.makeText(CatalogoTienda.this, vDescArt[i], Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        },500);
    }

    private void traerDatosTienda() {
        JsonObjectRequest datos = new JsonObjectRequest(
                Request.Method.GET,
                extra.url("getDatosTiendaPorId.php?id=" + idTienda),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject respuesta) {
                        try {
                            nombreTienda.setText(respuesta.getString("nombre"));
                            descTienda.setText(respuesta.getString("descripcion"));
                            telefTienda = respuesta.getString("telefono");

                            telf.setText("Cel: "+telefTienda);
                            linkAWap = linkAWap+telefTienda;

                            if((respuesta.getString("estacionamiento")).equals("1")){
                                estacionamientoTienda.setText(emojiCheck+" Estacionamiento");
                            }else{
                                estacionamientoTienda.setText(emojiNo+" SIN Estacionamiento");
                            }

                            if((respuesta.getString("delivery")).equals("1")){
                                deliveryTienda.setText(emojiMoto+" Delivery Disponible ");
                            }else{
                                deliveryTienda.setText(emojiCompra+" Solo compras en Tienda");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CatalogoTienda.this, "Ocurrió un error en la BD", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        solicitud.add(datos);
    }
    private void traerCatalogo(){
        cantArt = -1;
        JsonArrayRequest datos = new JsonArrayRequest(
                Request.Method.GET,
                extra.url("getCatalogoTiendaPorId.php?id="+idTienda), //aqui va el id de tienda
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray respuesta) {
                        //Recorrer la respuesta
                        int size = respuesta.length();
                        //Si llegó la menos 1 elemento en el array
                        if(size>0){
                            for(int i=0; i<size; i++){
                                try {
                                    JSONArray articulo = new JSONArray(respuesta.get(i).toString());
                                    vPrecioArt[i] = articulo.getString(0);
                                    vNombreArt[i] = articulo.getString(1);
                                    vDescArt[i] = articulo.getString(2);
                                    cantArt = i+1;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else{
                            Toast.makeText(CatalogoTienda.this, "Esta tienda no registró ningún Artículo", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CatalogoTienda.this, "Ocurrió un problema con la BD", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        solicitud.add(datos);
    }
}