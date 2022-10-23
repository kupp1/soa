package ru.ifmo.se.common;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.ifmo.se.common.model.CoordinatesDto;
import ru.ifmo.se.common.model.FlatDto;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

@Configuration
public class UtilsConfig {
    @Bean
    @Scope("singleton")
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(FlatDto.class, Flat.class).addMappings(mapper -> {
            mapper.map(src -> src.getCoordinates().getX(), Flat::setCoordinateX);
            mapper.map(src -> src.getCoordinates().getY(), Flat::setCoordinateY);
            mapper.skip(Flat::setCreateDate);
        });

        Converter<Flat, CoordinatesDto> flatToCoordinates = ctx -> {
            Flat flat = ctx.getSource();
            return new CoordinatesDto(flat.getCoordinateX(), flat.getCoordinateY());
        };
        modelMapper.typeMap(Flat.class, FlatDto.class).addMappings(mapper -> {
            mapper.using(flatToCoordinates).map(flat -> flat, FlatDto::setCoordinates);
        });

        return modelMapper;
    }

    @Bean
    public ValidatorFactory validatorFactory() {
        return Validation.buildDefaultValidatorFactory();
    }
}
