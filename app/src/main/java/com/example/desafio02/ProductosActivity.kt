package com.example.desafio02

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.desafio02.Producto

class ProductosActivity : AppCompatActivity() {

    private lateinit var rvProducts: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var dbRef: DatabaseReference
    private val listaProductos = mutableListOf<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        rvProducts = findViewById(R.id.rvProducts)
        fabAdd = findViewById(R.id.fabAddProduct)

        rvProducts.layoutManager = LinearLayoutManager(this)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("productos").child(uid!!)

        cargarProductos()

        fabAdd.setOnClickListener {
            val intent = Intent(this, AgregarProductoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarProductos() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaProductos.clear()
                for (productoSnap in snapshot.children) {
                    val producto = productoSnap.getValue(Producto::class.java)
                    if (producto != null) {
                        listaProductos.add(producto)
                    }
                }
                rvProducts.adapter = ProductoAdapter(listaProductos)
            }

            override fun onCancelled(error: DatabaseError) {
                // manejar error si quieres
            }
        })
    }
}
