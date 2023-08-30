package com.isaias.projectmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CrearProyectoActivity extends AppCompatActivity {
    private EditText txtnombre, txtubicacion, txtdescripcion;
    private DatePicker inicio, fin;
    private FirebaseAuth mAuth;
    private DatabaseReference projectsRef;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        projectsRef = FirebaseDatabase.getInstance().getReference().child("proyectos");
        setContentView(R.layout.activity_crear_proyecto);
        txtnombre = findViewById(R.id.txtnombre_proyecto);
        txtubicacion = findViewById(R.id.txtubicacion_proyecto);
        inicio = findViewById(R.id.fechainicio_proyecto);
        fin = findViewById(R.id.fechafin_proyecto);
        txtdescripcion = findViewById(R.id.txtdescripcion_proyecto);
        Button btnguardar = findViewById(R.id.btnguardarproyecto);
        currentUserID = mAuth.getCurrentUser().getUid();
        btnguardar.setOnClickListener(v -> {
            //Obtener datos del proyecto
            String nombre = txtnombre.getText().toString();
            String ubicacion = txtubicacion.getText().toString();
            String descripcion = txtdescripcion.getText().toString();
            //Obtener fecha de inicio
            int anioinicio = inicio.getYear();
            int mesinicio = inicio.getMonth();
            int diainicio = inicio.getDayOfMonth();
            //Convertir fecha a String
            String fechainicio = obtenerfechaString(anioinicio, mesinicio, diainicio);
            //Obtener fecha fin
            int aniofin = fin.getYear();
            int mesfin = fin.getMonth();
            int diafin = fin.getDayOfMonth();
            //Convertir fecha fin a String
            String fechafin = obtenerfechaString(aniofin, mesfin, diafin);
            //Validar datos
            if(TextUtils.isEmpty(nombre)||TextUtils.isEmpty(ubicacion)||
                    TextUtils.isEmpty(descripcion)||TextUtils.isEmpty(fechainicio)||TextUtils.isEmpty(fechafin)){
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            //Generar id de proyecto con firebase
            String projectid = projectsRef.push().getKey();
            //Crear instancia de la clase proyecto
            Proyecto proyecto = new Proyecto();
            proyecto.setNombre(nombre);
            proyecto.setUbicacion(ubicacion);
            proyecto.setFechainicio(fechainicio);
            proyecto.setFechafinprev(fechafin);
            proyecto.setDescripcion(descripcion);
            //Guardar proyecto en la base de datos
            projectsRef.child(currentUserID).child(projectid).setValue(proyecto).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //Se creo el proyecto, mostrar mensaje y enviar a inicio
                        Toast.makeText(CrearProyectoActivity.this, "Se creo el proyecto con éxito", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CrearProyectoActivity.this, InicioActivity.class);
                        startActivity(intent);
                    }else{
                        //No se creo el proyecto, mostrar mensaje de error
                        Toast.makeText(CrearProyectoActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
    //Metodo para convertir fecha a string
    private String obtenerfechaString(int anioinicio, int mesinicio, int diainicio) {
        // Crear una instancia de Calendar con la fecha seleccionada
        Calendar calendar = Calendar.getInstance();
        calendar.set(anioinicio, mesinicio, diainicio);
        // Formatear la fecha a una cadena (String)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}