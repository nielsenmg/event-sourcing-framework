package br.com.zup.eventsourcing.core

import java.lang.reflect.ParameterizedType

interface Repository<T : AggregateRoot> {
    fun save(aggregate: T)
    fun save(aggregate: T, metaData: MetaData)
    fun get(id: AggregateId): T
}

fun <T : Repository<AggregateRoot>> T.getGenericName(): String {
    return ((javaClass
            .genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>).simpleName
}