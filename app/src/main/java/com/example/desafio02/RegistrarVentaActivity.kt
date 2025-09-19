package com.example.desafio02

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.desafio02.models.Cliente
import com.example.desafio02.Producto
import com.example.desafio02.models.Venta
import java.text.SimpleDateFormat
import java.util.*

class RegistrarVentaActivity : AppCompatActivity() {

    private lateinit var spinnerClientes: Spinner
    private lateinit var listaProductosLayout: LinearLayout
    private lateinit var btnGuardar: Button

    private val listaClientes = mutableListOf<Cliente>()
    private val listaProductos = mutableListOf<Producto>()
    private val cantidadProductos = mutableMapOf<String, EditText>()

    private lateinit var dbClientes: DatabaseReference
    private lateinit var dbProductos: DatabaseReference
    private lateinit var dbVentas: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_venta)

        spinnerClientes = findViewById(R.id.spinnerClientes)
        listaProductosLayout = findViewById(R.id.listaProductos)
        btnGuardar = findViewById(R.id.btnGuardarVenta)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        dbClientes = FirebaseDatabase.getInstance().getReference("clientes").child(uid!!)
        dbProductos = FirebaseDatabase.getInstance().getReference("productos").child(uid)
        dbVentas = FirebaseDatabase.getInstance().getReference("ventas").child(uid)

        cargarClientes()
        cargarProductos()

        btnGuardar.setOnClickListener { guardarVenta() }
    }

    private fun cargarClientes() {
        dbClientes.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listaClientes.clear()
                for(snap in snapshot.children){
                    val cliente = snap.getValue(Cliente::class.java)
                    if(cliente != null) listaClientes.add(cliente)
                }
                val nombres = listaClientes.map { it.nombre }
                val adapter = ArrayAdapter(this@RegistrarVentaActivity, android.R.layout.simple_spinner_item, nombres)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerClientes.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun cargarProductos() {
        dbProductos.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listaProductos.clear()
                listaProductosLayout.removeAllViews()
                for(snap in snapshot.children){
                    val producto = snap.getValue(Producto::class.java)
                    if(producto != null){
                        listaProductos.add(producto)
                        val tv = TextView(this@RegistrarVentaActivity)
                        tv.text = "${producto.nombre} ($${producto.precio}) Stock: ${producto.stock}"
                        listaProductosLayout.addView(tv)

                        val et = EditText(this@RegistrarVentaActivity)
                        et.hint = "Cantidad a vender"
                        et.inputType = android.text.InputType.TYPE_CLASS_NUMBER
                        listaProductosLayout.addView(et)

                        cantidadProductos[producto.id] = et
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun guardarVenta() {
        val clientePos = spinnerClientes.selectedItemPosition
        if(clientePos == -1){
            Toast.makeText(this, "Selecciona un cliente", Toast.LENGTH_SHORT).show()
            return
        }
        val clienteId = listaClientes[clientePos].id

        val productosVendidos = mutableMapOf<String, Int>()
        var total = 0.0

        for(producto in listaProductos){
            val et = cantidadProductos[producto.id]
            val cantStr = et?.text.toString()
            if(cantStr.isNotEmpty()){
                val cant = cantStr.toInt()
                if(cant > 0){
                    if(cant > producto.stock){
                        Toast.makeText(this, "Stock insuficiente de ${producto.nombre}", Toast.LENGTH_SHORT).show()
                        return
                    }
                    productosVendidos[producto.id] = cant
                    total += cant * producto.precio
                }
            }
        }

        if(productosVendidos.isEmpty()){
            Toast.makeText(this, "Ingresa al menos un producto", Toast.LENGTH_SHORT).show()
            return
        }

        val idVenta = dbVentas.push().key!!
        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val venta = Venta(idVenta, clienteId, productosVendidos, total, fecha)

        dbVentas.child(idVenta).setValue(venta).addOnCompleteListener{
            if(it.isSuccessful){
                // Reducir stock de productos
                for((prodId, cant) in productosVendidos){
                    val prodRef = dbProductos.child(prodId).child("stock")
                    prodRef.setValue(listaProductos.find { it.id == prodId }!!.stock - cant)
                }
                Toast.makeText(this, "Venta registrada", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al guardar venta", Toast.LENGTH_SHORT).show()
            }
        }
    }
}