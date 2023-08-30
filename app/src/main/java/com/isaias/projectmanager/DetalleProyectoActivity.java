package com.isaias.projectmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetalleProyectoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView txtmain;
    private Button btn_nueva_tarea;
    private ListView listatareas;
    private DatabaseReference tareasRef;
    private final ArrayList<String> tareasList = new ArrayList<>();
    private String currentUserId, proyectoid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_proyecto);
        listatareas = findViewById(R.id.listviewTareas);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        tareasRef = FirebaseDatabase.getInstance().getReference().child("tareas");
        btn_nueva_tarea = findViewById(R.id.nuevatarea);
        txtmain = findViewById(R.id.txtnombre_pro);
        proyectoid = getIntent().getStringExtra("projectID");
        //Toast.makeText(this, proyectoid, Toast.LENGTH_SHORT).show();
        obtenerTareas();
        txtmain.setText("Tareas de: "+proyectoid);
        btn_nueva_tarea.setOnClickListener(v -> {
            // Crear un Intent para abrir la nueva actividad
            Intent intent = new Intent(DetalleProyectoActivity.this, NuevaTareaActivity.class);
            intent.putExtra("projectID", proyectoid); // Pasar el ID del proyecto como extra
            startActivity(intent);
        });
    }

    private void obtenerTareas() {
        tareasRef.child(proyectoid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    tareasList.clear();
                    for (DataSnapshot tareasSnapshot : snapshot.getChildren()) {
                        Tarea tarea = tareasSnapshot.getValue(Tarea.class);
                        String nombretarea = tarea.getTarea();
                        tareasList.add(nombretarea);
                    }
                }
                actualizarListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void actualizarListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tareasList);
        listatareas.setAdapter(adapter);
    }
}