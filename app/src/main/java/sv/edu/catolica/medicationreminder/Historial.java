package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.format.DateFormat;
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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Historial extends AppCompatActivity {
    private TextView Dosis,Dosificacion,Estado,Comentario,Fecha,valor;
    ManejadorBD admin;
    SQLiteDatabase db;
    private LinearLayout ly;
    private String persona_cod,recor_cod;
    private boolean notificacion;
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

    Bundle extras = getIntent().getExtras();
    notificacion = extras.getBoolean("notificacion",false);
    recor_cod=extras.getString("RE_COD");
    persona_cod=extras.getString("PER_COD");
        if (notificacion){
            insertarRegistro(Integer.parseInt(recor_cod),Integer.parseInt(persona_cod));
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yy");
        SimpleDateFormat formatterHora = new SimpleDateFormat("HH:mm");

        ArrayList<EHistorial> historial = new ArrayList<EHistorial>();
        historial =BuscarHistorial();
      for (EHistorial h: historial){
final String n = h.MEDxRE;
final String f = h.H_FECHA;
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
          TableRow rowHora = new TableRow(getApplicationContext());
          TableRow rowEstado = new TableRow(getApplicationContext());
          TableRow rowComentario = new TableRow(getApplicationContext());


          final TextView title = new TextView(this);
          TextView empty = new TextView(this);
          TextView lblID =new TextView(this);
          TextView lblDosificacion=new TextView(this);
            TextView lblDosis=new TextView(this);
            TextView lblFecha=new TextView(this);
            TextView lblHora = new TextView(this);
            TextView lblEstado=new TextView(this);
            TextView lblComentario=new TextView(this);


            title.setText(h.MEDICAMENTO);
          title.setTextSize(20);
          title.setTextColor(Color.parseColor("#000000"));
            empty.setText("");
          empty.setTextSize(18);
          empty.setTextColor(Color.parseColor("#000000"));
          lblID.setText(R.string.id);
          lblID.setTextSize(18);
          lblID.setTextColor(Color.parseColor("#000000"));
            lblDosificacion.setText(R.string.dosificacion2);
          lblDosificacion.setTextSize(18);
          lblDosificacion.setTextColor(Color.parseColor("#000000"));
            lblDosis.setText(R.string.dosis2);
          lblDosis.setTextSize(18);
          lblDosis.setTextColor(Color.parseColor("#000000"));
            lblFecha.setText(R.string.fecha2);
          lblFecha.setTextSize(18);
          lblFecha.setTextColor(Color.parseColor("#000000"));
            lblHora.setText(R.string.time);
          lblHora.setTextSize(18);
          lblHora.setTextColor(Color.parseColor("#000000"));
            lblEstado.setText(R.string.estado2);
          lblEstado.setTextSize(18);
          lblEstado.setTextColor(Color.parseColor("#000000"));
            lblComentario.setText(R.string.comentario2);
          lblComentario.setTextSize(18);
          lblComentario.setTextColor(Color.parseColor("#000000"));


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
             rowHora.addView(lblHora);
            rowEstado.addView(lblEstado);
            rowComentario.addView(lblComentario);

            final  TextView valorIDH=new TextView(this);
            final TextView ValorDosificacion=new TextView(this);
            final TextView ValorDosis=new TextView(this);
            final TextView ValorFecha=new TextView(this);
            final  TextView ValorHora = new TextView(this);
            final TextView ValorEstado=new TextView(this);
            final TextView ValorComentario=new TextView(this);
          final Button btnEditar = new Button(this);
          btnEditar.setText(R.string.editar);
          btnEditar.setTextSize(20);
          btnEditar.setBackgroundColor(Color.parseColor("#FF9A76"));


            valorIDH.setText(String.valueOf(h.H_COD));
          valorIDH.setTextSize(18);
          valorIDH.setTextColor(Color.parseColor("#000000"));
            ValorDosificacion.setText(h.DOSIFICACION);
          ValorDosificacion.setTextSize(18);
          ValorDosificacion.setTextColor(Color.parseColor("#000000"));
            ValorDosis.setText(h.DOSIS);
          ValorDosis.setTextSize(18);
          ValorDosis.setTextColor(Color.parseColor("#000000"));
            Date date= new Date(h.H_FECHA);
            ValorFecha.setText(formatter.format(date));
          ValorFecha.setTextSize(18);
          ValorFecha.setTextColor(Color.parseColor("#000000"));
            ValorHora.setText(formatterHora.format(date));
          ValorHora.setTextSize(18);
          ValorHora.setTextColor(Color.parseColor("#000000"));

            if (h.H_ESTADO.equals("1")){
                ValorEstado.setText(R.string.tomado);
                ValorEstado.setTextSize(18);
                ValorEstado.setTextColor(Color.parseColor("#000000"));
            }else if (h.H_ESTADO.equals("2")){
                ValorEstado.setText(R.string.no_tomado);
                ValorEstado.setTextSize(18);
                ValorEstado.setTextColor(Color.parseColor("#000000"));
            }else if (h.H_ESTADO.equals("3")){
              ValorEstado.setText(R.string.retrasado);
                ValorEstado.setTextSize(18);
                ValorEstado.setTextColor(Color.parseColor("#000000"));
          }else {
                ValorEstado.setText(R.string.estado_i0);
                ValorEstado.setTextSize(18);
                ValorEstado.setTextColor(Color.parseColor("#000000"));
            }

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
                                                        ventanaEditarHistorial.putExtra("Fecha",f);
                                               ventanaEditarHistorial.putExtra("Hora",ValorHora.getText().toString());

                                               ventanaEditarHistorial.putExtra("Estado",ValorEstado.getText().toString());
                                                        ventanaEditarHistorial.putExtra("Comentario",ValorComentario.getText().toString());
                                               ventanaEditarHistorial.putExtra("RE_COD",recor_cod);
                                               ventanaEditarHistorial.putExtra("PER_COD",persona_cod);
                                                        startActivity(ventanaEditarHistorial);

                                           }
                                       }
          );

          rowBoton.addView(btnEditar);
          rowDosificacion.addView(ValorDosificacion);
          rowDosis.addView(ValorDosis);
          rowFecha.addView(ValorFecha);
          rowHora.addView(ValorHora);
          rowEstado.addView(ValorEstado);
          rowComentario.addView(ValorComentario);


            tl.addView(rowTitle);
          tl.addView(rowBoton);
            tl.addView(rowDosificacion);
            tl.addView(rowDosis);
            tl.addView(rowFecha);
            tl.addView(rowHora);
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
                " MEDXRE.MEDXRED_DOSIFICACION,MEDXRE.RE_DOSIS,MEDXRE.MEDXRED_COD," +
                        " MEDICAMENTO.MED_NOMBRE"+
                " FROM HISTORIAL"+
                " INNER JOIN MEDXRE  ON HISTORIAL.MEDXRED_COD = MEDXRE.MEDXRED_COD"+
                " INNER JOIN RECORDATORIO ON MEDXRE.RE_COD = RECORDATORIO.RE_COD" +
                        " INNER JOIN MEDICAMENTO ON MEDXRE.MED_COD = MEDICAMENTO.MED_COD" +
                        " WHERE RECORDATORIO.RE_COD = "+recor_cod

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
            _histo.MEDICAMENTO=fila.getString(8);
            historial.add(_histo);

        }

        db.close();
        return historial;


    }
    private void insertarRegistro(int recordatorio,int persona)
    {
        ArrayList<ECantidadMed> medicamentos =obtenerCantidadMedicamentos(recordatorio);
        for (ECantidadMed med: medicamentos) {

            ContentValues registro = new ContentValues();
            Date Hoy = new Date();
            String fecha;

            DecimalFormat df = new DecimalFormat("##");
            fecha = df.format(Hoy.getHours());
            fecha +=":";
            fecha += df.format(Hoy.getMinutes());
            fecha +=" ";
            fecha += new SimpleDateFormat("dd/MM/yyyy").format(Hoy);

            registro.put("H_COD",ultimoID_Historial());
            registro.put("MEDXRED_COD",obtenerMedxRed_COD(med.medicamento,recordatorio));
            registro.put("H_FECHA", fecha);
            registro.put("H_ESTADO", 1);
            registro.put("H_COMENTARIO", med.medicamento);

            db = admin.getWritableDatabase();
            int valor = (int) db.insert("HISTORIAL", null, registro);

            db.close();
        }

    }

    public int ultimoID_Historial(){
        db = admin.getWritableDatabase();
        int num=-1;
        Cursor fila = db.rawQuery("SELECT H_COD FROM HISTORIAL" +
                " ORDER BY H_COD DESC"+
                " LIMIT 1;",null);
        if (fila.moveToFirst()){
            num=fila.getInt(0);
            num++;
        }else   {
            num = 1;
        }
        db.close();
        return num;
    }
    public int obtenerMedxRed_COD(String medicamento, int recordatorio){
        db = admin.getWritableDatabase();

        int num=-1;
        Cursor fila = db.rawQuery("SELECT m.MEDXRED_COD FROM MEDXRE m " +
                " INNER JOIN RECORDATORIO r ON m.RE_COD = r.RE_COD " +
                " INNER JOIN MEDICAMENTO m2 ON m.MED_COD = m2.MED_COD " +
                " WHERE r.RE_COD = "+recordatorio+" AND m2.MED_NOMBRE ='"+medicamento+"'",null);
        if (fila.moveToFirst()){
            num=fila.getInt(0);

        }
        db.close();
        return num;
    }
    public ArrayList<ECantidadMed> obtenerCantidadMedicamentos(int recordatorio){
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT m.MED_NOMBRE " +
                " FROM MEDXRE m2" +
                " INNER JOIN MEDICAMENTO m ON m2.MED_COD = m.MED_COD " +
                " WHERE m2.RE_COD = "+recordatorio
                ,null);
        ArrayList<ECantidadMed> medicamentos=new ArrayList<ECantidadMed>();
        while (fila.moveToNext()){
            ECantidadMed _med = new ECantidadMed();
            _med.medicamento=fila.getString(0);
            medicamentos.add(_med);
        }
        db.close();
        return medicamentos;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent persona = new Intent(Historial.this,Recordatorios.class);
        persona.putExtra("persona_id",persona_cod);
        startActivity(persona);
    }
}