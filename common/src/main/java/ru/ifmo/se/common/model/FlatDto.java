package ru.ifmo.se.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class FlatDto implements Serializable {
    private Long id;
    private String name;
    private CoordinatesDto coordinates;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date createDate;

    private Integer area;
    private Integer numberOfRooms;
    private Furnish furnish;
    private View view;
    private Transport transport;
    private HouseDto house;
    private Long price;
    private Boolean hasBalcony;
    private Long timeToSubwayOnTransport;
    private Long timeToSubwayOnFoot;
}