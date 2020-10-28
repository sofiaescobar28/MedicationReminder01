package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Historial extends AppCompatActivity {
    private TextView Dosis,Dosificacion,Estado,Comentario,Fecha,valor;
    ManejadorBD admin;
    SQLiteDatabase db;
    private LinearLayout ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Dosis = findViewById(R.id.labelvalorDosis);
        Dosificacion= findViewById(R.id.lblalorDosificacion);
        Estado=findViewById(R.id.labelvalorEstado);
        Comentario=findViewById(R.id.labelvalorComentario);
        Fecha = findViewById(R.id.labelvalorFecha);
        valor = findViewById(R.id.lblValidarCantidad);
       ly = findViewById(R.id.lySecundario);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);

        ArrayList<EHistorial> historial = new ArrayList<EHistorial>();
        historial =BuscarHistorial();
      for (EHistorial h: historial){

            TableLayout tl=new TableLayout(getApplicationContext());
          tl.setShrinkAllColumns(true);
          tl.setStretchAllColumns(true);

            TableRow rowTitle = new TableRow(getApplicationContext());
            rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
          TableRow rowDosificacion = new TableRow(getApplicationContext());
          TableRow rowDosis= new TableRow(getApplicationContext());
          TableRow rowFecha = new TableRow(getApplicationContext());
          TableRow rowEstado = new TableRow(getApplicationContext());
          TableRow rowComentario = new TableRow(getApplicationContext());
          TableRow rowBoton = new TableRow(getApplicationContext());

          TextView title = new TextView(this);
          TextView lblDosificacion=new TextView(this);
            TextView lblDosis=new TextView(this);
            TextView lblFecha=new TextView(this);
            TextView lblEstado=new TextView(this);
            TextView lblComentario=new TextView(this);
            Button btnEditar = new Button(this);

            title.setText(h.RECORDATORIO);
            lblDosificacion.setText("Dosificación: ");
            lblDosis.setText("Dosis: ");
            lblFecha.setText("Fecha: ");
            lblEstado.setText("Estado: ");
            lblComentario.setText("Comentario: ");
            btnEditar.setText("Editar");
            btnEditar.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {

                                             }
                                         }
            );

            title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            title.setGravity(Gravity.CENTER);
            title.setTypeface(Typeface.SERIF, Typeface.BOLD);

            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.span = 2;

            rowTitle.addView(title, params);
            rowDosificacion.addView(lblDosificacion);
            rowDosis.addView(lblDosis);
            rowFecha.addView(lblFecha);
            rowEstado.addView(lblEstado);
            rowComentario.addView(lblComentario);
            rowBoton.addView(btnEditar);

            TextView ValorDosificacion=new TextView(this);
            TextView ValorDosis=new TextView(this);
            TextView ValorFecha=new TextView(this);
            TextView ValorEstado=new TextView(this);
            TextView ValorComentario=new TextView(this);

            ValorDosificacion.setText(h.DOSIFICACION);
            ValorDosis.setText(h.DOSIS);
            ValorFecha.setText(h.H_FECHA);
            ValorEstado.setText(h.H_ESTADO);
            ValorComentario.setText(h.H_COMENTARIO);

          rowDosificacion.addView(ValorDosificacion);
          rowDosis.addView(ValorDosis);
          rowFecha.addView(ValorFecha);
          rowEstado.addView(ValorEstado);
          rowComentario.addView(ValorComentario);


            tl.addView(rowTitle);
            tl.addView(rowDosificacion);
            tl.addView(rowDosis);
            tl.addView(rowFecha);
            tl.addView(rowEstado);
            tl.addView(rowComentario);
            tl.addView(rowBoton);

           ly.addView(tl);



        }
    }

    public void EditarHistorico(View view) {
    }
    public ArrayList<EHistorial> BuscarHistorial(){
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT HISTORIAL.H_COD,RECORDATORIO.RE_TITULO,"+
               " HISTORIAL.H_FECHA,HISTORIAL.H_ESTADO,HISTORIAL.H_COMENTARIO,"+
                " MEDXRE.MEDXRED_DOSIFICACION,MEDXRE.RE_DOSIS"+
                " FROM HISTORIAL"+
                " INNER JOIN MEDXRE  ON HISTORIAL.MEDXRED_COD = MEDXRE.MEDXRED_COD"+
                " INNER JOIN RECORDATORIO ON MEDXRE.RE_COD = RECORDATORIO.RE_COD"

                ,null);
        ArrayList<EHistorial> historial = new ArrayList<EHistorial>();

        while (fila.moveToNext()){
            EHistorial _histo = new EHistorial();
            _histo.H_COD = fila.getInt(0);
            _histo.RECORDATORIO = fila.getString(1);
            _histo.H_FECHA=fila.getString(2);
            _histo.H_ESTADO=fila.getString(3);
            _histo.H_COMENTARIO=fila.getString(4);
            _histo.DOSIFICACION=fila.getString(5);
            _histo.DOSIS=fila.getString(6);
            historial.add(_histo);

        }

        db.close();
        return historial;


    }
}