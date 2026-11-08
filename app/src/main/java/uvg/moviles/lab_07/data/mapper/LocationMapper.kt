package uvg.moviles.lab_07.data.mapper

import uvg.moviles.lab_07.Location
import uvg.moviles.lab_07.data.local.LocationEntity
import uvg.moviles.lab_07.data.remote.LocationDto

fun LocationEntity.toModel() = Location(
    id = id,
    name = name,
    type = type,
    dimension = dimension,
    isFavorite = isFavorite
)

fun Location.toEntity() = LocationEntity(
    id = id,
    name = name,
    type = type,
    dimension = dimension,
    isFavorite = isFavorite
)

fun LocationDto.toEntity() = LocationEntity(id, name, type, dimension)
