package com.example.nexum_trabajador.data.mapper

/**
 * Interfaz genérica para realizar mapeos entre un objeto de transferencia de datos (DTO) y una Entidad.
 * @param <DTO> El tipo de objeto de datos de entrada (generalmente de una respuesta de red).
 * @param <ENTITY> El tipo de objeto de entidad de salida (generalmente para la base de datos local).
 */
interface EntityMapper<DTO, ENTITY> {

    fun toEntity(dto: DTO): ENTITY

    fun toEntity(dtos: List<DTO>): List<ENTITY> = dtos.map { toEntity(it) }
}

/**
 * Interfaz genérica para realizar mapeos entre una Entidad de base de datos y un Modelo de Dominio.
 * @param <ENTITY> El tipo de objeto de entidad de entrada (generalmente de la base de datos local).
 * @param <DOMAIN_MODEL> El tipo de objeto de dominio de salida (para ser usado en la UI).
 */
interface DomainMapper<ENTITY, DOMAIN_MODEL> {

    fun toDomain(entity: ENTITY): DOMAIN_MODEL

    fun toDomain(entities: List<ENTITY>): List<DOMAIN_MODEL> = entities.map { toDomain(it) }
}

/**
 * Interfaz genérica cuya responsabilidad es mapear de un state de un viewmodel y pasarlo a un request, para hacer una solicitud en una fuente de datos remota.
 * @param <STATE> El tipo de objeto del estado del ViewModel.
 * @param <REQUEST> El tipo de objeto de solicitud para la fuente de datos remota.
 */
interface RequestMapper<STATE, REQUEST> {
    fun toRequest(state: STATE): REQUEST
    fun toRequest(states: List<STATE>): List<REQUEST> = states.map { toRequest(it) }
}
