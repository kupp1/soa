package ru.ifmo.se.service.agency;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.se.common.remote.AgencyService;
import ru.ifmo.se.common.remote.FlatServiceResponse;
import ru.ifmo.se.common.remote.IHello;
import ru.ifmo.se.common.exception.NoEntitiesException;
import ru.ifmo.se.common.model.FlatDto;
import ru.ifmo.se.common.model.FlatPageResponse;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/agency")
public class AgencyController {
    private static final String AGENCY_SERVICE_BEAN_NAME = "AgencyServiceBeanRemote";

    public final Context ejbContext;
    public final AgencyService agencyService;

    public AgencyController() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.RemoteInitialContextFactory");
        props.put(Context.PROVIDER_URL,"http://haproxy-agency-service-ejb:8080/tomee/ejb");
        ejbContext = new InitialContext(props);

        agencyService = (AgencyService) ejbContext.lookup(AGENCY_SERVICE_BEAN_NAME);
    }

    @GetMapping
    public String test() throws NamingException {
        IHello h = (IHello) ejbContext.lookup("HelloBeanRemote");

        return h.sayHello();
    }

    @GetMapping("/find-with-balcony/{cheapest}/{withBalcony}")
    public FlatDto findWithBalcony(@PathVariable Boolean cheapest, @PathVariable Boolean withBalcony) {
        FlatServiceResponse flatServiceResponse = agencyService.findWithBalcony(cheapest, withBalcony);

        if (flatServiceResponse.getHttpResponseCode() == HttpStatus.NOT_FOUND.value()) {
            throw new NoEntitiesException("No such flats");
        } else if (flatServiceResponse.getHttpResponseCode() != HttpStatus.OK.value()) {
            throw new IllegalStateException("Unable to get remote flats");
        } else if (flatServiceResponse.getResponse().getTotalFlats() == 0) {
            throw new NoEntitiesException("No such flats");
        }

        return IterableUtils.first(flatServiceResponse.getResponse().getFlats());
    }

    @GetMapping("/get-ordered-by-time-to-metro/{byTransport}/{desc}")
    public FlatPageResponse getOrderedByTimeToMetro(@PathVariable Boolean byTransport,
                                                    @PathVariable Boolean desc,
                                                    @RequestParam Integer page,
                                                    @RequestParam Integer pageSize,
                                                    @RequestParam Map<String, String> requestParams) {
        FlatServiceResponse flatServiceResponse =
                agencyService.getOrderedByTimeToMetro(byTransport, desc, requestParams);

        if (flatServiceResponse.getHttpResponseCode() == HttpStatus.NOT_FOUND.value()) {
            throw new NoEntitiesException("No such flats");
        } else if (flatServiceResponse.getHttpResponseCode() != HttpStatus.OK.value()) {
            throw new IllegalStateException("Unable to get remote flats");
        } else if (flatServiceResponse.getResponse().getTotalFlats() == 0) {
            throw new NoEntitiesException("No such flats");
        }

        return flatServiceResponse.getResponse();
    }
}
