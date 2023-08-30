package com.isaias.projectmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {
    private EditText nombre, correo, clave;
    private CheckBox mostrar_clave;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //Iniciar componentes
        mAuth = FirebaseAuth.getInstance();
        nombre = findViewById(R.id.txtnombre_registro);
        correo = findViewById(R.id.txtcorreo_registro);
        clave = findViewById(R.id.txtclave_registro);
        TextView abrir_login = findViewById(R.id.btn_open_login);
        mostrar_clave = findViewById(R.id.chkmostrar_registro);
        Button registro = findViewById(R.id.btn_registro);
        reference = FirebaseDatabase.getInstance().getReference();
        //Mostrar y ocultar contraseña
        mostrar_clave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Mostrar contraseña
                    clave.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // Ocultar contraseña
                    clave.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                // Mover el cursor al final del texto
                clave.setSelection(clave.getText().length());
            }
        });
        abrir_login.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
            startActivity(intent);
        });
        //Registrar el usuario
        registro.setOnClickListener(v -> {
            //Obtener datos de los textbox
            String username = nombre.getText().toString();
            String email = correo.getText().toString();
            String password = clave.getText().toString();
            //Validar datos
            if(TextUtils.isEmpty(username)||TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            //Crear usuario en Firebase Auth
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegistroActivity.this, "Usuario registrado con exito", Toast.LENGTH_SHORT).show();
                        //Obtener usuario registrado
                        FirebaseUser user = mAuth.getCurrentUser();
                        //Verificar si se creo el usuario
                        if(user!=null){
                            String userId = user.getUid();
                            guardarUsuarioBD(username, email, userId);
                            //Enviar al login
                            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }else{
                        //Mostrar mensaje si hubo algun error
                        Toast.makeText(RegistroActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
    //Metodo para guardar datos de un usuario en la base de datos
    private void guardarUsuarioBD(String nombre, String correo, String id) {
        User user = new User();
        user.setId(id);
        user.setCorreo(correo);
        user.setNombre(nombre);
        reference.child("usuarios").child(id).setValue(user);
    }
}