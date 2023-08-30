package com.isaias.projectmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class InicioActivity extends AppCompatActivity {
    private ListView listaproyectos;
    private DatabaseReference projectsRef;
    private final ArrayList<String> proyectoslist = new ArrayList<>();
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        listaproyectos = findViewById(R.id.listaproyectos);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        projectsRef = FirebaseDatabase.getInstance().getReference().child("proyectos");
        obtenerProyectos();
        Button btn_new_project = findViewById(R.id.crear_proyecto);
        btn_new_project.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, CrearProyectoActivity.class);
            startActivity(intent);
        });
        listaproyectos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String projectID = proyectoslist.get(position); // Obtener el ID del proyecto seleccionado

                // Crear un Intent para abrir la nueva actividad
                Intent intent = new Intent(InicioActivity.this, DetalleProyectoActivity.class);
                intent.putExtra("projectID", projectID); // Pasar el ID del proyecto como extra
                startActivity(intent);
            }
        });
    }

    private void obtenerProyectos() {
        projectsRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    proyectoslist.clear();
                        for (DataSnapshot projectSnapshot : snapshot.getChildren()) {
                            Proyecto proyecto = projectSnapshot.getValue(Proyecto.class);
                            String nombreProyecto = proyecto.getNombre();
                            proyectoslist.add(nombreProyecto);
                        }
                    }
                    actualizarListView();
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});
    }

    private void actualizarListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, proyectoslist);
        listaproyectos.setAdapter(adapter);
    }
}