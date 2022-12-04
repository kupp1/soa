package ru.ifmo.se.service.agency;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.ifmo.se.common.exception.NoEntitiesException;
import ru.ifmo.se.common.model.FlatDto;
import ru.ifmo.se.common.model.FlatPageResponse;

import java.util.Map;

@RestController
@RequestMapping("/agency")
public class AgencyController {
    private static final String FLAT_SERVICE_URL = "https://localhost:47232/";

    private final RestTemplate restTemplate;

    public AgencyController() {
        this.restTemplate = new RestTemplate();
    }

    @GetMapping
    public String test() {
        return "test";
    }

    @GetMapping("/find-with-balcony/{cheapest}/{withBalcony}")
    public FlatDto findWithBalcony(@PathVariable Boolean cheapest, @PathVariable Boolean withBalcony) {
        String sort = cheapest ? "asc" : "desc";
        String hasBalconyFilterClause = "<eq>"
                + BooleanUtils.toString(withBalcony, "true", "false");

        String url = UriComponentsBuilder.fromHttpUrl(FLAT_SERVICE_URL + "/api/v1/flat")
                .queryParam("sort[price]", sort)
                .queryParam("filter[hasBalcony]", hasBalconyFilterClause)
                .queryParam("page", "0")
                .queryParam("pageSize", "1")
                .toUriString();

        FlatPageResponse flatPageResponse = loadFlatPageResponse(url);

        return IterableUtils.first(flatPageResponse.getFlats());
    }

    @GetMapping("/get-ordered-by-time-to-metro/{byTransport}/{desc}")
    public FlatPageResponse getOrderedByTimeToMetro(@PathVariable Boolean byTransport,
                                                    @PathVariable Boolean desc,
                                                    @RequestParam Integer page,
                                                    @RequestParam Integer pageSize,
                                                    @RequestParam Map<String, String> requestParams) {
        String field = byTransport ? "timeToSubwayOnTransport" : "timeToSubwayOnFoot";

        String sortLeftValue = "sort[" + field + "]";
        String sortRightValue = desc ? "desc" : "asc";

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(FLAT_SERVICE_URL + "/api/v1/flat")
                .queryParam(sortLeftValue, sortRightValue);
        for (var paramEntry : requestParams.entrySet()) {
            String paramName = paramEntry.getKey();
            String paramValue = paramEntry.getValue();

            urlBuilder.queryParam(paramName, paramValue);
        }

        String url = urlBuilder.toUriString();
        FlatPageResponse response = loadFlatPageResponse(url);

        return response;
    }

    private FlatPageResponse loadFlatPageResponse(String url) {
        ResponseEntity<FlatPageResponse> response;
        try {
            response = restTemplate.getForEntity(url, FlatPageResponse.class);
        } catch (HttpClientErrorException e) {
            HttpStatus status = e.getStatusCode();
            if (status == HttpStatus.NOT_FOUND) {
                throw new NoEntitiesException("No such flats");
            } else {
                throw new IllegalStateException("Unable to get remote flats");
            }
        }
        if (response.getBody().getTotalFlats() == 0) {
            throw new NoEntitiesException("No such flats");
        }

        return response.getBody();
    }
}
