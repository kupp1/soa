package ru.ifmo.se.common.remote;

import javax.ejb.Remote;
import java.util.Map;

@Remote
public interface AgencyService {
    FlatServiceResponse findWithBalcony(Boolean cheapest,
                                        Boolean withBalcony);
    FlatServiceResponse getOrderedByTimeToMetro(Boolean byTransport, Boolean desc,
                                                Map<String, String> requestParams);
}
