package ru.ifmo.se;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.util.UriComponentsBuilder;
import ru.ifmo.se.common.remote.AgencyService;
import ru.ifmo.se.common.remote.FlatServiceResponse;

import javax.ejb.Stateless;
import java.util.Map;

@Stateless
public class AgencyServiceBean implements AgencyService {
    private static final String FLAT_SERVICE_URL = "https://haproxy-flat-service:443";

    private final FlatServiceAccessor flatServiceAccessor = new FlatServiceAccessor();

    @Override
    public FlatServiceResponse findWithBalcony(Boolean cheapest, Boolean withBalcony) {
        String sort = cheapest ? "asc" : "desc";
        String hasBalconyFilterClause = "<eq>"
                + BooleanUtils.toString(withBalcony, "true", "false");

        String url = UriComponentsBuilder.fromHttpUrl(FLAT_SERVICE_URL + "/api/v1/flat")
                .queryParam("sort[price]", sort)
                .queryParam("filter[hasBalcony]", hasBalconyFilterClause)
                .queryParam("page", "0")
                .queryParam("pageSize", "1")
                .toUriString();

        return flatServiceAccessor.loadFlatPageResponse(url);
    }

    @Override
    public FlatServiceResponse getOrderedByTimeToMetro(Boolean byTransport, Boolean desc, Map<String,
            String> requestParams) {
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

        return flatServiceAccessor.loadFlatPageResponse(url);
    }
}
