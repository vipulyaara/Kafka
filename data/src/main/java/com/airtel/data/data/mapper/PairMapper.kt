package com.airtel.data.data.mapper

private class PairMapper<F, T1, T2>(
    private val firstMapper: Mapper<F, T1>,
    private val secondMapper: Mapper<F, T2>
) : Mapper<List<F>, List<Pair<T1, T2>>> {
    override fun map(from: List<F>): List<Pair<T1, T2>> = from.map {
        firstMapper.map(it) to secondMapper.map(it)
    }
}

private class PairMapper2<F, T1, T2>(
    private val firstMapper: Mapper<F, T1>,
    private val secondMapper: IndexedMapper<F, T2>
) : Mapper<List<F>, List<Pair<T1, T2>>> {
    override fun map(from: List<F>): List<Pair<T1, T2>> = from.mapIndexed { index, value ->
        firstMapper.map(value) to secondMapper.map(index, value)
    }
}

fun <F, T1, T2> pairMapperOf(
    firstMapper: Mapper<F, T1>,
    secondMapper: Mapper<F, T2>
): Mapper<List<F>, List<Pair<T1, T2>>> {
    return PairMapper(firstMapper, secondMapper)
}

fun <F, T1, T2> pairMapperOf(
    firstMapper: Mapper<F, T1>,
    secondMapper: IndexedMapper<F, T2>
): Mapper<List<F>, List<Pair<T1, T2>>> {
    return PairMapper2(firstMapper, secondMapper)
}
