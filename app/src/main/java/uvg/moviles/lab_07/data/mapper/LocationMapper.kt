package uvg.moviles.lab_07.data.mapper

import uvg.moviles.lab_07.Location
import uvg.moviles.lab_07.data.local.LocationEntity

fun LocationEntity.toModel() = Location(
    id = id,
    name = name,
    type = type,
    dimension = dimension
)

fun Location.toEntity() = LocationEntity(
    id = id,
    name = name,
    type = type,
    dimension = dimension
)
