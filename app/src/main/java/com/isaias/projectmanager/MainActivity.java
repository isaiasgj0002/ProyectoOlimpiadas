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

public class MainActivity extends AppCompatActivity {
    private EditText correo, clave;
    private CheckBox mostrar_clave;
    private Button login;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Iniciar componentes
        setContentView(R.layout.activity_main);
        correo = findViewById(R.id.txtcorreo_login);
        clave = findViewById(R.id.txtpassword_login);
        TextView abrir_registro = findViewById(R.id.btnopen_registro);
        mostrar_clave = findViewById(R.id.chkmostrarpassword_login);
        login = findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();
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
        //Abrir pantalla de registro
        abrir_registro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
        login.setOnClickListener(v -> {
            //Recoger datos
            String email = correo.getText().toString();
            String password = clave.getText().toString();
            //Validar datos
            if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        abrirInicio();
                    }else{
                        Toast.makeText(MainActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    private void abrirInicio() {
        Intent intent = new Intent(MainActivity.this, InicioActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            abrirInicio();
        }
    }
}