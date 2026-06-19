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
fun SignUpPolicyScreen() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            "Política de Privacidad de Nexum Trabajador",
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

        PolicySection(
            title = "1. Introducción",
            content = """Bienvenido a Nexum Trabajador. Nos comprometemos a proteger tu privacidad. Esta Política de Privacidad explica cómo recopilamos, usamos, divulgamos y salvaguardamos tu información cuando utilizas nuestra aplicación móvil. Lee esta política de privacidad cuidadosamente. Si no estás de acuerdo con los términos de esta política de privacidad, por favor no accedas a la aplicación."""
        )

        PolicySection(
            title = "2. Recopilación de tu Información",
            content = """Podemos recopilar información sobre ti de varias maneras. La información que podemos recopilar a través de la Aplicación incluye:

Datos Personales: Información de identificación personal, como tu nombre, dirección de correo electrónico, número de teléfono y datos demográficos (como tu edad y ciudad natal), que nos proporcionas voluntariamente cuando te registras en la Aplicación.

Datos de Geolocalización: Podemos solicitar acceso o permiso para rastrear información basada en la ubicación de tu dispositivo móvil, ya sea de forma continua o mientras utilizas la Aplicación, para proporcionar servicios basados en la ubicación. Si deseas cambiar nuestros permisos de acceso, puedes hacerlo en la configuración de tu dispositivo.

Datos del Perfil Profesional: Información que proporcionas para tu perfil de trabajador, como tu experiencia laboral, habilidades, certificaciones y fotografía."""
        )

        PolicySection(
            title = "3. Uso de tu Información",
            content = """Tener información precisa sobre ti nos permite ofrecerte una experiencia fluida, eficiente y personalizada. Específicamente, podemos usar la información recopilada sobre ti a través de la Aplicación para:

• Crear y gestionar tu cuenta.
• Facilitar la conexión entre trabajadores y empleadores.
• Enviarte un correo electrónico de confirmación.
• Notificarte sobre actualizaciones de la aplicación.
• Solicitar retroalimentación y contactarte sobre tu uso de la Aplicación.
• Proteger la seguridad y el funcionamiento de la plataforma."""
        )

        PolicySection(
            title = "4. Divulgación de tu Información",
            content = """No compartiremos tu información personal con terceros, excepto en las siguientes situaciones:

• Con Empleadores: La información de tu perfil será visible para los empleadores registrados en la plataforma para que puedan evaluar tu idoneidad para las oportunidades laborales.
• Por Ley o para Proteger Derechos: Si la divulgación es necesaria para responder a un proceso legal, para investigar o remediar posibles violaciones de nuestras políticas, o para proteger los derechos, la propiedad y la seguridad de otros.
• Con tu Consentimiento: Podemos divulgar tu información personal para cualquier otro propósito con tu consentimiento explícito."""
        )

        PolicySection(
            title = "5. Seguridad de tu Información",
            content = """Utilizamos medidas de seguridad administrativas, técnicas y físicas para ayudar a proteger tu información personal. Si bien hemos tomado medidas razonables para asegurar la información personal que nos proporcionas, ten en cuenta que a pesar de nuestros esfuerzos, ninguna medida de seguridad es perfecta o impenetrable."""
        )

        PolicySection(
            title = "6. Derechos sobre tus Datos",
            content = """Tienes derecho a revisar, editar o eliminar tu información personal en cualquier momento accediendo a la configuración de tu perfil. Si deseas eliminar tu cuenta, puedes hacerlo desde la aplicación o contactándonos directamente."""
        )

        PolicySection(
            title = "7. Contacto",
            content = """Si tienes preguntas o comentarios sobre esta Política de Privacidad, por favor contáctanos en: [Tu correo electrónico de soporte]"""
        )
    }
}

@Composable
fun PolicySection(title: String, content: String) {
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
