package com.example.nexum_trabajador.ui.navigation.graphs

import kotlinx.serialization.Serializable

sealed class Graph {
    @Serializable
    data object InitialGraph : Graph()

    @Serializable
    data object HomeGraph : Graph()
}
