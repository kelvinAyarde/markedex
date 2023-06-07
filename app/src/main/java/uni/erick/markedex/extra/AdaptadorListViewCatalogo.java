package uni.erick.markedex.extra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import uni.erick.markedex.R;

public class AdaptadorListViewCatalogo extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context contexto;
    String[] vNombre;
    String[] vPrecio;
    int cantArt;

    //Constructor
    public AdaptadorListViewCatalogo(Context contexto, String[] vNombre, String[] vPrecio, int cantArt){
        this.contexto = contexto;
        this.vNombre = vNombre;
        this.vPrecio = vPrecio;
        this.cantArt = cantArt;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Este método se ejecuta por cada fila
        //int i es el número de fila actual
        final View vista = inflater.inflate(R.layout.elemento_lista_catalogo, null); //Indicando el file.xml que contendrá cada fila
        TextView nombreArt = vista.findViewById(R.id.txtElmentoLista_nombre);
        TextView precioArt = vista.findViewById(R.id.txtElementoLista_precio);
        nombreArt.setText(vNombre[i]);
        precioArt.setText("Precio: "+vPrecio[i]+" Bs.");
        return vista;
    }
    @Override
    public int getCount() {
        //Configura la cantidad de filas que contendrá la lista
        return cantArt;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}
