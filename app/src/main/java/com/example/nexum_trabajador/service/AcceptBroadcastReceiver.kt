package com.example.nexum_trabajador.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AcceptBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.v("BroadcastReceiver", "Aceptar clickeado")

        // Agrega la lógica que deseas ejecutar cuando se hace clic en "Aceptar"
        // Por ejemplo, puedes abrir una nueva actividad, mostrar un Toast, etc.

        Toast.makeText(context, "Aceptar clickeado", Toast.LENGTH_SHORT).show()
    }
}