package uvg.moviles.lab_07.data.mapper

import uvg.moviles.lab_07.Character
import uvg.moviles.lab_07.data.local.CharacterEntity
import uvg.moviles.lab_07.data.remote.CharacterDto

fun CharacterEntity.toModel() = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite
)

fun Character.toEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite
)

fun CharacterDto.toEntity() = CharacterEntity(id, name, status, species, gender, image)
