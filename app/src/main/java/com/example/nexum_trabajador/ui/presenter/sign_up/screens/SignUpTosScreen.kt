package com.example.nexum_trabajador.ui.presenter.sign_up.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUpTosScreen() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            "Términos y Condiciones de Uso de Nexum Trabajador",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            "Última actualización: 17 de Julio de 2024",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TosSection(
            title = "1. Descripción del Servicio",
            content = """Nexum Trabajador es una plataforma digital que tiene como objetivo conectar a trabajadores cualificados (\"Trabajadores\") con oportunidades laborales y proyectos ofrecidos por empleadores o clientes (\"Empleadores\"). El Servicio facilita la creación de perfiles profesionales, la búsqueda de ofertas y la comunicación entre las partes."""
        )

        TosSection(
            title = "2. Cuentas de Usuario",
            content = """• Para usar nuestro Servicio, debes crear una cuenta, garantizando que la información proporcionada es precisa, completa y actual en todo momento. Proporcionar información falsa puede resultar en la terminación inmediata de tu cuenta.
• Eres responsable de salvaguardar la contraseña que utilizas para acceder al Servicio y de cualquier actividad o acción bajo tu contraseña.
• Aceptas no divulgar tu contraseña a terceros y debes notificarnos inmediatamente si tienes conocimiento de cualquier violación de seguridad o uso no autorizado de tu cuenta."""
        )

        TosSection(
            title = "3. Conducta del Usuario y Uso Aceptable",
            content = """Te comprometes a no utilizar el Servicio para ningún propósito ilegal o prohibido por estos Términos. Te comprometes a no:
• Publicar información falsa, engañosa o fraudulenta.
• Acosar, abusar o dañar a otra persona.
• Distribuir spam o publicidad no solicitada.
• Intentar descompilar o aplicar ingeniería inversa a cualquier software contenido en el Servicio.
• Utilizar la plataforma para cualquier actividad que viole las leyes laborales o fiscales locales."""
        )

        TosSection(
            title = "4. Contenido del Usuario",
            content = """Nuestro Servicio te permite publicar, enlazar, almacenar, compartir y poner a disposición cierta información, texto, gráficos u otro material (\"Contenido\"). Eres responsable del Contenido que publicas en el Servicio, incluyendo su legalidad, fiabilidad y adecuación.

Al publicar Contenido, nos otorgas una licencia mundial, no exclusiva, libre de regalías para usar, modificar, ejecutar públicamente, mostrar públicamente, reproducir y distribuir dicho Contenido en y a través del Servicio. Conservas todos tus derechos sobre cualquier Contenido que envíes."""
        )

        TosSection(
            title = "5. Propiedad Intelectual",
            content = """El Servicio y su contenido original (excluyendo el Contenido proporcionado por los usuarios), características y funcionalidades son y seguirán siendo propiedad exclusiva de [Nombre de tu Empresa/Desarrollador] y sus licenciantes."""
        )

        TosSection(
            title = "6. Política de Privacidad",
            content = """Nuestra Política de Privacidad, que describe cómo recopilamos, usamos y compartimos tu información, forma parte de estos Términos. Puedes consultarla dentro de la misma aplicación."""
        )

        TosSection(
            title = "7. Terminación",
            content = """Podemos terminar o suspender tu cuenta de inmediato, sin previo aviso ni responsabilidad, por cualquier motivo, incluido, entre otros, el incumplimiento de los Términos."""
        )

        TosSection(
            title = "8. Limitación de Responsabilidad",
            content = """En ningún caso [Nombre de tu Empresa/Desarrollador], ni sus directores, empleados o socios, serán responsables de daños indirectos, incidentales, especiales, consecuentes o punitivos, resultantes de tu acceso o uso del Servicio. El Servicio se proporciona \"TAL CUAL\" y \"SEGÚN DISPONIBILIDAD\"."""
        )

        TosSection(
            title = "9. Contacto",
            content = """Si tienes alguna pregunta sobre estos Términos, por favor contáctanos en: [Tu correo electrónico de soporte]"""
        )
    }
}

@Composable
fun TosSection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
