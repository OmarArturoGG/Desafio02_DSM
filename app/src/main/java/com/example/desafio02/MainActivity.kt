package com.example.desafio02


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var btnProductos: Button
    private lateinit var btnClientes: Button
    private lateinit var btnVentas: Button
    private lateinit var btnLogout: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        btnProductos = findViewById(R.id.btnProductos)
        btnClientes = findViewById(R.id.btnClientes)
        btnVentas = findViewById(R.id.btnVentas)
        btnLogout = findViewById(R.id.btnLogout)

        btnProductos.setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }

        btnClientes.setOnClickListener {
            startActivity(Intent(this, ClientesActivity::class.java))
        }

        btnVentas.setOnClickListener {
            startActivity(Intent(this, VentasActivity::class.java))
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}