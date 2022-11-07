package ru.ifmo.se.service.flat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.se.common.exception.NoEntitiesException;
import ru.ifmo.se.service.flat.service.FilterQueryService;
import ru.ifmo.se.common.entity.Flat;
import ru.ifmo.se.service.flat.service.FlatService;
import ru.ifmo.se.service.flat.service.FlatValidator;
import ru.ifmo.se.common.model.FlatPageResponse;
import ru.ifmo.se.service.flat.repository.FlatRepository;
import ru.ifmo.se.common.model.FlatDto;
import ru.ifmo.se.service.flat.service.SortQueryService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flat")
public class FlatController {
    private final FlatRepository flatRepository;
    private final FlatValidator flatValidator;
    private final FlatService flatService;
    private final SortQueryService sortQueryService;
    private final FilterQueryService filterQueryService;

    public FlatController(FlatRepository flatRepository,
                          FlatValidator flatValidator,
                          FlatService flatService,
                          SortQueryService sortQueryService,
                          FilterQueryService filterQueryService) {
        this.flatRepository = flatRepository;
        this.flatValidator = flatValidator;
        this.flatService = flatService;
        this.sortQueryService = sortQueryService;
        this.filterQueryService = filterQueryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<FlatPageResponse> getAllFlats(@RequestParam Integer page,
                                                        @RequestParam Integer pageSize,
                                                        @RequestParam Map<String, String> requestParams) {
        Sort sort = sortQueryService.parseSortQuery(requestParams);
        Specification<Flat> specification = filterQueryService.generateSpecification(requestParams);
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<Flat> flatPage = flatRepository.findAll(specification, pageable);
        if (flatPage.getTotalElements() == 0) {
            throw new NoEntitiesException("No such flats");
        }

        FlatPageResponse response = flatService.mapPageToResponse(flatPage);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<FlatDto> createFlat(@RequestBody FlatDto newFlatDto) {
        Flat newFlat = flatService.mapFlatDtoToEntity(newFlatDto);
        flatValidator.checkFields(newFlat);
        flatRepository.save(newFlat);

        FlatDto response = flatService.mapFlatEntityToDto(newFlat);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<FlatDto> updateFlat(@RequestBody FlatDto updatedFlatDto) {
        Flat updatedFlat = flatService.mapFlatDtoToEntity(updatedFlatDto);
        flatValidator.checkFields(updatedFlat);
        flatValidator.checkId(updatedFlat);

        flatRepository.save(updatedFlat);

        Flat flat = flatRepository.findById(updatedFlat.getId()).get(); // load for creation date
        FlatDto response = flatService.mapFlatEntityToDto(flat);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Iterable<FlatDto>> deleteFilteredFlats(@RequestParam Map<String, String> requestParams) {
        Specification<Flat> specification = filterQueryService.generateSpecification(requestParams);
        List<Flat> flatsToDelete = flatRepository.findAll(specification);
        Iterable<FlatDto> response = flatService.mapFlatEntitiesToDtos(flatsToDelete);

        flatRepository.deleteAll(flatsToDelete);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{flatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<FlatDto> getFlatById(@PathVariable Long flatId) {
        flatValidator.checkId(flatId);
        Flat flat = flatRepository.findById(flatId).get(); // checked above

        FlatDto response = flatService.mapFlatEntityToDto(flat);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{flatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<FlatDto> deleteFlat(@PathVariable Long flatId) {
        flatValidator.checkId(flatId);
        Flat flat = flatRepository.findById(flatId).get(); // checked above

        flatRepository.delete(flat);

        FlatDto response = flatService.mapFlatEntityToDto(flat);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/min-area", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<FlatDto> getFlatWithSmallestArea() {
        flatValidator.checkFlatsExists();

        Flat flat = flatRepository.findFirstByOrderByArea().get(); // checked above

        FlatDto response = flatService.mapFlatEntityToDto(flat);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
