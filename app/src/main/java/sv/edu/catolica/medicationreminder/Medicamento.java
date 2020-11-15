package sv.edu.catolica.medicationreminder;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Medicamento extends AppCompatActivity {
    ManejadorBD admin;
    SQLiteDatabase db;
    private LinearLayout lyMedicamentos;
    private EditText busqueda;
    private TextView suceso;
    FloatingActionButton boton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamento);
        lyMedicamentos=findViewById(R.id.lyContenedorMedicamentos);
        busqueda=findViewById(R.id.txtBuscarMedicamento);
        suceso = findViewById(R.id.lblsuceso);
        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);
        busqueda.findFocus();
        boton = findViewById(R.id.addMed);
        boton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pGris)));

        buscarMedNombres();
        busqueda.addTextChangedListener(new TextWatcher() {
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

    public void construirInterfaz(ArrayList<EMedicamento> MedHistory){
        for ( EMedicamento m: MedHistory){
            TableLayout tlmed=new TableLayout(getApplicationContext());
            tlmed.setShrinkAllColumns(true);
            tlmed.setStretchAllColumns(true);

            TableRow rowBoton = new TableRow(getApplicationContext());
            TableRow rowIdMed = new TableRow(getApplicationContext());
            TableRow rowNombre = new TableRow(getApplicationContext());
            TableRow rowTipo= new TableRow(getApplicationContext());

            TextView empty = new TextView(this);
            TextView lblID = new TextView(this);
            TextView lblNombre=new TextView(this);
            TextView lblTipo=new TextView(this);


            empty.setText("");
            lblID.setText(R.string.id);
            lblNombre.setText(R.string.medicamento2);
            lblTipo.setText(R.string.tipo2);


            rowBoton.addView(empty);
            rowIdMed.addView(lblID);
            rowNombre.addView(lblNombre);
            rowTipo.addView(lblTipo);


            Button btnEditarMed = new Button(this);
            btnEditarMed.setText(R.string.editar);

            final TextView lblIdValor=new TextView(this);
            final TextView lblNombreValor=new TextView(this);
            final TextView lblTipoValor=new TextView(this);


            lblIdValor.setText(String.valueOf(m.MED_COD));
            lblNombreValor.setText(m.MED_NOMBRE);
            lblTipoValor.setText(m.MED_TIPO);
            btnEditarMed.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    finish();
                                                    Intent ventanaEditar = new Intent(getApplicationContext(),Editar_Medicamento.class);
                                                    ventanaEditar.putExtra("id_medicamento",lblIdValor.getText());
                                                    ventanaEditar.putExtra("nombre_medicamento",lblNombreValor.getText());
                                                    ventanaEditar.putExtra("tipo_medicamento",lblTipoValor.getText());
                                                   startActivity(ventanaEditar);
                                                }
                                            }
            );

            rowBoton.addView(btnEditarMed);
            rowIdMed.addView(lblIdValor);
            rowNombre.addView(lblNombreValor);
            rowTipo.addView(lblTipoValor);

            tlmed.addView(rowBoton);
            tlmed.addView(rowIdMed);
            tlmed.addView(rowNombre);
            tlmed.addView(rowTipo);


            lyMedicamentos.addView(tlmed);

        }
    }

    public void pantallaNuevoMed(View view) {
        Intent nuevo = new Intent(getApplicationContext(),Agregar_Medicamento.class);
        startActivity(nuevo);
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


    public  void buscarMedNombres() {
        if (lyMedicamentos.getChildCount()>0){
            lyMedicamentos.removeAllViews();
        }
        if (!busqueda.getText().toString().trim().isEmpty()){

            db = admin.getWritableDatabase();
            Cursor fila = db.rawQuery("SELECT MED_COD,MED_NOMBRE,MED_TIPO FROM MEDICAMENTO" +
                            " WHERE MED_NOMBRE like '%"+busqueda.getText().toString().trim()+"%'"
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
            if (lyMedicamentos.getChildCount() > 0) lyMedicamentos.removeAllViews();
            construirInterfaz(meds);

        }else{
            ArrayList<EMedicamento> HisMedicamentos = new ArrayList<EMedicamento>();
            HisMedicamentos =BuscarMedicamentos();
            if (HisMedicamentos.size()>0){
                construirInterfaz(HisMedicamentos);
            }else{
                suceso.setTextColor(getResources().getColor(R.color.naranja));
                suceso.setText(R.string.busquedanoencontrada);
            }


        }




    }
}