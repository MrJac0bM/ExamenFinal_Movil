// utils/AppConstants.kt
package com.example.examfinal.utils

object AppConstants {

    const val ARCHITECTURE_INFO = """
        Arquitectura: MVVM + Clean Architecture
        
        - Modelo-Vista-ViewModel (MVVM) para separar la lógica de UI de la lógica de negocio.
        
        Capa de Presentación: ViewModels y Vistas o Composables 
        Capa de Dominio: Casos de Uso (Use Cases) para la lógica de negocio y Modelos de dominio :).
        Capa de Datos: Repositorios, Api Service y el Data store (cache)
    """

    const val PREFERENCES_STRATEGY = """
        Estrategia de Guardado de Preferencias
        
        Este muy importante ya que se utilizo el Data Store de Jetpack para guardar las preferencias de la aplicación.
         
         Los modulos que implemente fueron los siguiente:
         
            - Preferences Manager: Clase Singleton que va a encapsular la logica.
            Flow : Basicamente va a ser el observador que va a escuchar o a observar los cambios en tiempo real 
            Guardado Autmatico: Al visitar un pais se va a guardar la posicion en la lista en la que estaba.
            Recuperacion de Datos: Al iniciar la aplicacion se va a recuperar la ultima posicion guarda y va a realizar un scroll automatico 
       
    """

    const val SEARCH_STRATEGY = """
        Estrategia de Búsqueda
        
        Filtrado : Busca por nombre y nombre oficil del país ademas de que no va a distinguir entre minusculas y mayusculas y va a filtrar los datos sin llamar a la api solamente la lista que tienen en la memoria 
    """
}