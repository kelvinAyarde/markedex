package uni.erick.markedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistroTienda extends AppCompatActivity {

    EditText usu,con;
    Button btnReg;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK){
            Intent irInicio = new Intent(RegistroTienda.this, MainActivity.class);
            startActivity(irInicio);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tienda);
        findViewById();
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Obj = new Intent(RegistroTienda.this,RegistroTienda2.class);
                Obj.putExtra("USUARIO",usu.getText().toString());
                Obj.putExtra("CONTRASEÑA",con.getText().toString());
                startActivity(Obj);
            }
        });
    }
    private void findViewById() {
        //Relaciona las variables con los elementos del xml
        //Es mejor colocarlas aquí en lugar de que ocupen espacio en el onCreate
        usu = findViewById(R.id.txtRegistroTienda_usuario);
        con = findViewById(R.id.txtRegistroTienda_pass);
        btnReg = findViewById(R.id.btnRegistroTienda_registrar);
    }
}