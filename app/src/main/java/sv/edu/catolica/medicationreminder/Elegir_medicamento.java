package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Elegir_medicamento extends AppCompatActivity {
    ManejadorBD admin;
    SQLiteDatabase db;
    private LinearLayout contenedor;
    private EditText buscar;
    private int IDrec=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_medicamento);

        /*Bundle extras = getIntent().getExtras();
        IDrec=extras.getInt("RE_COD");*/

        buscar=findViewById(R.id.etBuscarM);
        contenedor=findViewById(R.id.lyConMedicinas);
        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);
        buscar.findFocus();

        buscarMedNombres();

        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                buscarMedNombres();
            }
        });
    }


    public  void buscarMedNombres() {
        if (contenedor.getChildCount()>0){
            contenedor.removeAllViews();
        }
        if (!buscar.getText().toString().trim().isEmpty()){

            db = admin.getWritableDatabase();
            Cursor filas = db.rawQuery("SELECT M.MED_COD,M.MED_NOMBRE,M.MED_TIPO FROM MEDICAMENTO M"+
                            " WHERE M.MED_NOMBRE like '%"+buscar.getText().toString().trim()+"%'"
                    ,null);
            ArrayList<EMedicamento> meds = new ArrayList<EMedicamento>();

            while (filas.moveToNext()){
                EMedicamento _med = new EMedicamento();
                _med.MED_COD = filas.getInt(0);
                _med.MED_NOMBRE = filas.getString(1);
                _med.MED_TIPO=filas.getString(2);
                meds.add(_med);
            }

            db.close();
            if (contenedor.getChildCount() > 0) contenedor.removeAllViews();
            construirInterfaz(meds);

        }else{
            ArrayList<EMedicamento> HisMedicamentos = new ArrayList<EMedicamento>();
            HisMedicamentos =BuscarMedicamentos();
            if (HisMedicamentos.size()>0){
                construirInterfaz(HisMedicamentos);
            }else{
                Toast.makeText(getApplicationContext(),getText(R.string.busquedanoencontrada),Toast.LENGTH_LONG).show();
            }
        }
    }
    public  ArrayList<EMedicamento> BuscarMedicamentos(){
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT MED_COD,MED_NOMBRE,MED_TIPO FROM MEDICAMENTO"
                ,null);
        ArrayList<EMedicamento> meds = new ArrayList<EMedicamento>();

        while (fila.moveToNext()){
            EMedicamento _med = new EMedicamento();
            _med.MED_COD = fila.getInt(0);
            _med.MED_NOMBRE = fila.getString(1);
            _med.MED_TIPO=fila.getString(2);
            meds.add(_med);

        }

        db.close();
        return meds;

    }
    public void construirInterfaz(ArrayList<EMedicamento> MedHistory){
        for ( EMedicamento m: MedHistory){
            TableLayout tablaVista=new TableLayout(getApplicationContext());
            tablaVista.setShrinkAllColumns(true);
            tablaVista.setStretchAllColumns(true);

            TableRow lineaIdMed = new TableRow(getApplicationContext());
            TableRow lineaNombre = new TableRow(getApplicationContext());
            TableRow lineaTipo= new TableRow(getApplicationContext());
            TableRow lineaBoton = new TableRow(getApplicationContext());

            TextView vacio = new TextView(this);
            TextView tvID = new TextView(this);
            final TextView tvNombre = new TextView(this);
            TextView tvTipo=new TextView(this);

            vacio.setText("");
            tvID.setText(R.string.id);
            tvNombre.setText(R.string.medicamento2);
            tvTipo.setText(R.string.tipo2);

            lineaBoton.addView(vacio);
            lineaIdMed.addView(tvID);
            lineaNombre.addView(tvNombre);
            lineaTipo.addView(tvTipo);

            Button btnSel = new Button(this);
            btnSel.setText(R.string.seleccionar);

            final TextView tvIdValor=new TextView(this);
            final TextView tvNombreValor=new TextView(this);
            final TextView tvTipoValor=new TextView(this);

            tvIdValor.setText(String.valueOf(m.MED_COD));
            tvNombreValor.setText(m.MED_NOMBRE);
            tvTipoValor.setText(m.MED_TIPO);
            btnSel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    finish();
                                                    Intent MEDxRE = new Intent(Elegir_medicamento.this,medicina_por_recordatorio.class);
                                                    MEDxRE.putExtra("id_medicamento",tvIdValor.getText());
                                                    MEDxRE.putExtra("id_recordatorio",IDrec);
                                                    MEDxRE.putExtra("medicamento",tvNombreValor.getText());
                                                    MEDxRE.putExtra("tipo",tvTipoValor.getText());
                                                    startActivity(MEDxRE);
                                                }
                                            }
            );
            lineaIdMed.addView(tvIdValor);
            lineaNombre.addView(tvNombreValor);
            lineaTipo.addView(tvTipoValor);
            lineaBoton.addView(btnSel);

            tablaVista.addView(lineaIdMed);
            tablaVista.addView(lineaNombre);
            tablaVista.addView(lineaTipo);
            tablaVista.addView(lineaBoton);

            contenedor.addView(tablaVista);
        }
    }
}