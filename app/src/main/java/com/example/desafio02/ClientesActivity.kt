package com.example.desafio02


import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.desafio02.models.Cliente
import android.content.Intent

class ClientesActivity : AppCompatActivity() {

    private lateinit var rvClientes: RecyclerView
    private lateinit var btnAgregar: Button
    private lateinit var dbRef: DatabaseReference
    private val listaClientes = mutableListOf<Cliente>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        rvClientes = findViewById(R.id.rvClientes)
        btnAgregar = findViewById(R.id.btnAgregarCliente)
        rvClientes.layoutManager = LinearLayoutManager(this)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("clientes").child(uid!!)

        cargarClientes()

        btnAgregar.setOnClickListener {
            val intent = Intent(this, AgregarClienteActivity::class.java)
            startActivity(intent)
        }

    }

    private fun cargarClientes() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaClientes.clear()
                for (snap in snapshot.children) {
                    val cliente = snap.getValue(Cliente::class.java)
                    if (cliente != null) listaClientes.add(cliente)
                }
                rvClientes.adapter = ClienteAdapter(listaClientes)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
