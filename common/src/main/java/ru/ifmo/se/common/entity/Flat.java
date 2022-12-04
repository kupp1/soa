package ru.ifmo.se.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import ru.ifmo.se.common.model.Furnish;
import ru.ifmo.se.common.model.Transport;
import ru.ifmo.se.common.model.View;

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
    @Column(updatable = false)
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
