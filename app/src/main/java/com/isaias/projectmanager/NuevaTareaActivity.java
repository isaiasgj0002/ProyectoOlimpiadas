package com.isaias.projectmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NuevaTareaActivity extends AppCompatActivity {
    private EditText txttitulo, txtplazo;
    private DatePicker inicio;
    private RadioButton si, no;
    private Button guardar;
    boolean finalizada;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);
        txttitulo = findViewById(R.id.txtnombre_tarea);
        txtplazo = findViewById(R.id.txtplazodeejecucion);
        inicio = findViewById(R.id.fechainicio_tarea);
        si = findViewById(R.id.rb_finalizada);
        no = findViewById(R.id.rb_no_finalizada);
        guardar = findViewById(R.id.btnguardartarea);
        String proyectoid = getIntent().getStringExtra("projectID");
        ref = FirebaseDatabase.getInstance().getReference().child("tareas");
        if(si.isChecked()){
            finalizada = true;
        } else if (no.isChecked()) {
            finalizada = false;
        }
        guardar.setOnClickListener(v -> {
            //Recojer datos
            String titulo = txttitulo.getText().toString();
            String plazo = txtplazo.getText().toString();
            //Obtener fecha de inicio
            int anioinicio = inicio.getYear();
            int mesinicio = inicio.getMonth();
            int diainicio = inicio.getDayOfMonth();
            //Convertir fecha a String
            String fechainicio = obtenerfechaString(anioinicio, mesinicio, diainicio);
            //Validar datos
            if(TextUtils.isEmpty(titulo)||TextUtils.isEmpty(plazo)||TextUtils.isEmpty(fechainicio)||!si.isChecked()&&!no.isChecked()){
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            //Crear Instancia de la clase Tarea
            Tarea tarea = new Tarea();
            tarea.setTarea(titulo);
            tarea.setFecha_inicio(fechainicio);
            tarea.setPlazo_ejecucion(plazo);
            tarea.setFinalizada(String.valueOf(finalizada));
            //Generar ID de tarea
            String idtarea = ref.push().getKey();
            //Guardar en BD
            ref.child(proyectoid).child(idtarea).setValue(tarea).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(NuevaTareaActivity.this, "Se creo la tarea con éxito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NuevaTareaActivity.this, DetalleProyectoActivity.class);
                    intent.putExtra("projectID", proyectoid); // Pasar el ID del proyecto como extra
                    startActivity(intent);
                    startActivity(intent);
                }
            });
        });
    }
    private String obtenerfechaString(int anioinicio, int mesinicio, int diainicio) {
        // Crear una instancia de Calendar con la fecha seleccionada
        Calendar calendar = Calendar.getInstance();
        calendar.set(anioinicio, mesinicio, diainicio);
        // Formatear la fecha a una cadena (String)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}