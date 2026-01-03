package cl.arriagada.learningxcloude.Factorypattern

sealed class Notificacion {
     abstract fun enviar(): String
}

data class NotificacionEmail(val email: String, val mensaje: String): Notificacion(){
    override fun enviar(): String = "Email enviado a $email : $mensaje"
}
data class NotificacionSMS(val telefono: String, val mensaje: String): Notificacion(){
    override fun enviar(): String  = "SMS enviado a $telefono : $mensaje"
}

class FabricaNotificaciones{
    fun crearNotificacionesEmail(email: String, mensaje: String): Notificacion{
        return NotificacionEmail(email, mensaje)
    }
    fun crearNotificacionSMS(telefono: String, mensaje: String): Notificacion{
        return NotificacionSMS(telefono, mensaje)
    }
}
