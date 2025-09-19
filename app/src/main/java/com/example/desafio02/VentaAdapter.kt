package com.example.desafio02


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio02.models.Venta


class VentaAdapter(private val lista: List<Venta>) :
    RecyclerView.Adapter<VentaAdapter.VentaViewHolder>() {

    class VentaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCliente: TextView = itemView.findViewById(R.id.tvClienteVenta)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotalVenta)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFechaVenta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venta, parent, false)
        return VentaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VentaViewHolder, position: Int) {
        val venta = lista[position]
        holder.tvCliente.text = "Cliente: ${venta.clienteId}"
        holder.tvTotal.text = "Total: $${venta.total}"
        holder.tvFecha.text = "Fecha: ${venta.fecha}"
    }

    override fun getItemCount(): Int = lista.size
}