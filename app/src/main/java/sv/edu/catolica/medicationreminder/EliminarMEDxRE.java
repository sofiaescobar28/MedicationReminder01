package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class EliminarMEDxRE extends AppCompatActivity {

    private String Re_cod,Per_cod;
    private int codigo;
    ManejadorBD admin;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_m_e_dx_r_e);

        Bundle extras = getIntent().getExtras();
        Re_cod = extras.getString("RE_COD");
        Per_cod = extras.getString("PER_COD");
        codigo = extras.getInt("ID");

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);

    }

    public void elim(View v)
    {
        int fila= db.delete(" MEDXRE ", " MEDXRED_COD =" + codigo + ";", null);
        if (fila == 1) {
            Toast.makeText(getApplicationContext(),"Eliminado correctamente",Toast.LENGTH_LONG);
        }else{
            Toast.makeText(getApplicationContext(),"Error al eliminar",Toast.LENGTH_LONG);
        }

        Intent med_rec = new Intent(EliminarMEDxRE.this,medicamento_recordatorio.class);
        med_rec.putExtra("RE_COD",Re_cod);
        med_rec.putExtra("PEE_COD",Per_cod);
        startActivity(med_rec);
    }
}