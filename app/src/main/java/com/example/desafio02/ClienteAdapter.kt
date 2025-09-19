package com.example.desafio02

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio02.models.Cliente

class ClienteAdapter(private val lista: List<Cliente>) :
    RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreCliente)
        val tvCorreo: TextView = itemView.findViewById(R.id.tvCorreoCliente)
        val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefonoCliente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = lista[position]
        holder.tvNombre.text = cliente.nombre
        holder.tvCorreo.text = cliente.correo
        holder.tvTelefono.text = cliente.telefono
    }

    override fun getItemCount(): Int = lista.size
}