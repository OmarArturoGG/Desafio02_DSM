package com.example.desafio02

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.desafio02.models.Venta


class VentasActivity : AppCompatActivity() {

    private lateinit var rvVentas: RecyclerView
    private lateinit var btnNuevaVenta: Button
    private val listaVentas = mutableListOf<Venta>()
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventas)

        rvVentas = findViewById(R.id.rvVentas)
        btnNuevaVenta = findViewById(R.id.btnNuevaVenta)

        rvVentas.layoutManager = LinearLayoutManager(this)
        rvVentas.adapter = VentaAdapter(listaVentas) // Creamos adapter abajo

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("ventas").child(uid!!)

        // Cargar ventas
        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listaVentas.clear()
                for (snap in snapshot.children) {
                    val venta = snap.getValue(Venta::class.java)
                    if (venta != null) listaVentas.add(venta)
                }
                rvVentas.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        btnNuevaVenta.setOnClickListener {
            val intent = Intent(this, RegistrarVentaActivity::class.java)
            startActivity(intent)
        }
    }
}