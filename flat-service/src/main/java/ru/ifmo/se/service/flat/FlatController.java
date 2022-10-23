package ru.ifmo.se.service.flat;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.se.common.Flat;
import ru.ifmo.se.common.FlatService;
import ru.ifmo.se.common.FlatValidator;
import ru.ifmo.se.common.repository.FlatRepository;
import ru.ifmo.se.common.model.FlatDto;

@Controller
@RequestMapping("/flat")
public class FlatController {
    private final FlatRepository flatRepository;
    private final FlatValidator flatValidator;
    private final FlatService flatService;

    public FlatController(FlatRepository flatRepository,
                          FlatValidator flatValidator,
                          FlatService flatService) {
        this.flatRepository = flatRepository;
        this.flatValidator = flatValidator;
        this.flatService = flatService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<FlatDto>> getAllFlats() {
        Iterable<Flat> flats = flatRepository.findAll();
        Iterable<FlatDto> flatDtos = flatService.mapFlatEntitiesToDtos(flats);

        return new ResponseEntity<>(flatDtos, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlatDto> createFlat(@RequestBody FlatDto newFlatDto) {
        Flat newFlat = flatService.mapFlatDtoToEntity(newFlatDto);
        flatValidator.checkFields(newFlat);
        flatRepository.save(newFlat);

        FlatDto response = flatService.mapFlatEntityToDto(newFlat);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlatDto> updateFlat(@RequestBody FlatDto updatedFlatDto) {
        Flat updatedFlat = flatService.mapFlatDtoToEntity(updatedFlatDto);
        flatValidator.checkFields(updatedFlat);
        flatValidator.checkId(updatedFlat);

        flatRepository.save(updatedFlat);

        FlatDto response = flatService.mapFlatEntityToDto(updatedFlat);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{flatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlatDto> getFlatById(@PathVariable Long flatId) {
        flatValidator.checkId(flatId);
        Flat flat = flatRepository.findById(flatId).get(); // checked above

        FlatDto response = flatService.mapFlatEntityToDto(flat);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{flatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlatDto> deleteFlat(@PathVariable Long flatId) {
        flatValidator.checkId(flatId);
        Flat flat = flatRepository.findById(flatId).get(); // checked above

        flatRepository.delete(flat);

        FlatDto response = flatService.mapFlatEntityToDto(flat);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/min-area", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlatDto> getFlatWithSmallestArea() {
        flatValidator.checkFlatsExists();

        Flat flat = flatRepository.findFirstByOrderByArea().get(); // checked above

        FlatDto response = flatService.mapFlatEntityToDto(flat);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
