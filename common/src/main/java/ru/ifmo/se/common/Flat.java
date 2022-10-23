package ru.ifmo.se.common;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import ru.ifmo.se.common.model.Furnish;
import ru.ifmo.se.common.model.Transport;
import ru.ifmo.se.common.model.View;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Length(min = 1)
    private String name;

    @NotNull
    private Float coordinateX;

    @NotNull
    private Integer coordinateY;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @NotNull
    @Min(0)
    private Integer area;

    @NotNull
    @Min(0)
    private Integer numberOfRooms;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Furnish furnish;

    @NotNull
    @Enumerated(EnumType.STRING)
    private View view;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Transport transport;

    @Length(min = 1)
    private String houseName;

    @Min(1)
    @Max(808)
    @NotNull
    private Integer houseYear;

    @Min(1)
    @NotNull
    private Long houseNumberOfLifts;

    @NotNull
    @Min(0)
    private Long price;

    @NotNull
    private Boolean hasBalcony;

    @NotNull
    @Min(0)
    private Long timeToSubwayOnTransport;

    @NotNull
    @Min(0)
    private Long timeToSubwayOnFoot;
}
