package com.data.base.mapper

interface Mapper<F, T> {

    fun map(from: F): T
}

interface IndexedMapper<F, T> {
    fun map(index: Int, from: F): T
}

fun <F, T1, T2> pairMapperOf(
    firstMapper: Mapper<F, T1>,
    secondMapper: Mapper<F, T2>
): suspend (F) -> Pair<T1, T2> {
    return { from ->
        from.let { firstMapper.map(it) to secondMapper.map(it) }
    }
}


fun <F, T> Mapper<F, T>.toLambda(): suspend (F) -> T {
    return { map(it) }
}
