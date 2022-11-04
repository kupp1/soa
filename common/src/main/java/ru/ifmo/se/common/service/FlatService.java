package ru.ifmo.se.common.service;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.ifmo.se.common.Flat;
import ru.ifmo.se.common.model.FlatDto;
import ru.ifmo.se.common.model.FlatPageResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FlatService {
    private static final List<String> FIELDS = List.of(
            "id",
            "name",
            "coordinateX",
            "coordinateY",
            "createDate",
            "area",
            "numberOfRooms",
            "furnish",
            "view",
            "transport",
            "houseName",
            "houseYear",
            "houseNumberOfLifts",
            "price",
            "hasBalcony",
            "timeToSubwayOnTransport",
            "timeToSubwayOnFoot");
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

    public FlatPageResponse mapPageToResponse(Page<Flat> page) {
        Iterable<Flat> flats = page.getContent();
        Iterable<FlatDto> flatDtos = mapFlatEntitiesToDtos(flats);

        return FlatPageResponse.builder()
                .page(page.getNumber())
                .pageSize(page.getSize())
                .totalFlats(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .flats(flatDtos)
                .build();
    }

    public static List<String> getFlatFields() {
        return FIELDS;
    }
}
