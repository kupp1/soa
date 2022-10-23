package ru.ifmo.se.common;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.ifmo.se.common.model.FlatDto;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FlatService {
    private final ModelMapper modelMapper;

    public FlatService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Iterable<FlatDto> mapFlatEntitiesToDtos(Iterable<Flat> flats) {
        return StreamSupport.stream(flats.spliterator(), false)
                .map(this::mapFlatEntityToDto)
                .collect(Collectors.toList());
    }

    public FlatDto mapFlatEntityToDto(Flat flat) {
        return modelMapper.map(flat, FlatDto.class);
    }

    public Flat mapFlatDtoToEntity(FlatDto flatDto) {
        return modelMapper.map(flatDto, Flat.class);
    }
}
