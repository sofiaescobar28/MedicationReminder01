package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        /*Dosis = findViewById(R.id.labelvalorDosis);
        Dosificacion= findViewById(R.id.lblalorDosificacion);
        Estado=findViewById(R.id.labelvalorEstado);
        Comentario=findViewById(R.id.labelvalorComentario);
        Fecha = findViewById(R.id.labelvalorFecha);
        valor = findViewById(R.id.lblValidarCantidad);*/
       ly = findViewById(R.id.lySecundario);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);

        ArrayList<EHistorial> historial = new ArrayList<EHistorial>();
        historial =BuscarHistorial();
      for (EHistorial h: historial){
final String n = h.MEDxRE;
            TableLayout tl=new TableLayout(getApplicationContext());
          tl.setShrinkAllColumns(true);
          tl.setStretchAllColumns(true);


            TableRow rowTitle = new TableRow(getApplicationContext());
            rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
          TableRow rowBoton = new TableRow(getApplicationContext());
          TableRow rowid= new TableRow(getApplicationContext());
          TableRow rowDosificacion = new TableRow(getApplicationContext());
          TableRow rowDosis= new TableRow(getApplicationContext());
          TableRow rowFecha = new TableRow(getApplicationContext());
          TableRow rowEstado = new TableRow(getApplicationContext());
          TableRow rowComentario = new TableRow(getApplicationContext());


          final TextView title = new TextView(this);
          TextView empty = new TextView(this);
          TextView lblID =new TextView(this);
          TextView lblDosificacion=new TextView(this);
            TextView lblDosis=new TextView(this);
            TextView lblFecha=new TextView(this);
            TextView lblEstado=new TextView(this);
            TextView lblComentario=new TextView(this);


            title.setText(h.RECORDATORIO);
            empty.setText("");
          lblID.setText("Id: ");
            lblDosificacion.setText("Dosificación: ");
            lblDosis.setText("Dosis: ");
            lblFecha.setText("Fecha: ");
            lblEstado.setText("Estado: ");
            lblComentario.setText("Comentario: ");


            title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            title.setGravity(Gravity.CENTER);
            title.setTypeface(Typeface.SERIF, Typeface.BOLD);

            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.span = 2;

            rowTitle.addView(title, params);
            rowBoton.addView(empty);
            rowid.addView(lblID);
            rowDosificacion.addView(lblDosificacion);
            rowDosis.addView(lblDosis);
            rowFecha.addView(lblFecha);
            rowEstado.addView(lblEstado);
            rowComentario.addView(lblComentario);

            final  TextView valorIDH=new TextView(this);
            final TextView ValorDosificacion=new TextView(this);
            final TextView ValorDosis=new TextView(this);
            final TextView ValorFecha=new TextView(this);
            final TextView ValorEstado=new TextView(this);
            final TextView ValorComentario=new TextView(this);
          final Button btnEditar = new Button(this);
          btnEditar.setText("Editar");


            valorIDH.setText(String.valueOf(h.H_COD));
            ValorDosificacion.setText(h.DOSIFICACION);
            ValorDosis.setText(h.DOSIS);
            ValorFecha.setText(h.H_FECHA);
            ValorEstado.setText(h.H_ESTADO);
            ValorComentario.setText(h.H_COMENTARIO);
          btnEditar.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                                        finish();
                                                        Intent ventanaEditarHistorial = new Intent(getApplicationContext(),Editar_Historial.class);
                                                        ventanaEditarHistorial.putExtra("Titulo",title.getText().toString());
                                                        ventanaEditarHistorial.putExtra("Medre",n.toString());
                                                        ventanaEditarHistorial.putExtra("IDEH",valorIDH.getText());
                                                        ventanaEditarHistorial.putExtra("Dosificacion",ValorDosificacion.getText().toString());
                                                        ventanaEditarHistorial.putExtra("Dosis",ValorDosis.getText().toString());
                                                        ventanaEditarHistorial.putExtra("Fecha",ValorFecha.getText().toString());
                                                        ventanaEditarHistorial.putExtra("Estado",ValorEstado.getText().toString());
                                                        ventanaEditarHistorial.putExtra("Comentario",ValorComentario.getText().toString());
                                                        startActivity(ventanaEditarHistorial);

                                           }
                                       }
          );

          rowBoton.addView(btnEditar);
          rowDosificacion.addView(ValorDosificacion);
          rowDosis.addView(ValorDosis);
          rowFecha.addView(ValorFecha);
          rowEstado.addView(ValorEstado);
          rowComentario.addView(ValorComentario);


            tl.addView(rowTitle);
          tl.addView(rowBoton);
            tl.addView(rowDosificacion);
            tl.addView(rowDosis);
            tl.addView(rowFecha);
            tl.addView(rowEstado);
            tl.addView(rowComentario);


           ly.addView(tl);



        }
    }

    public void EditarHistorico(View view) {
    }
    public ArrayList<EHistorial> BuscarHistorial(){
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT HISTORIAL.H_COD,RECORDATORIO.RE_TITULO,"+
               " HISTORIAL.H_FECHA,HISTORIAL.H_ESTADO,HISTORIAL.H_COMENTARIO,"+
                " MEDXRE.MEDXRED_DOSIFICACION,MEDXRE.RE_DOSIS,MEDXRE.MEDXRED_COD"+
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
            _histo.MEDxRE=fila.getString(7);
            historial.add(_histo);

        }

        db.close();
        return historial;


    }
}