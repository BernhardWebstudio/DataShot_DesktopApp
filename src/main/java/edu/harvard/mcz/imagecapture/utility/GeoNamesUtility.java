package edu.harvard.mcz.imagecapture.utility;

import org.geonames.WebService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

public class GeoNamesUtility extends AbstractRestClient {
    private final WebService webService;

    public GeoNamesUtility() {
        super();
        this.webService = new WebService();
        WebService.setUserName("datashot");
    }

    public Map<String, Object> reverseSearchValues(BigDecimal lat, BigDecimal lon,
                                                   Collection<String> keys) {
//        CountrySubdivision subdivision = this.webService.countrySubdivision(lat, longitude);
        String url = "http://api.geonames.org/countrySubdivisionJSON?lat=" + lat.toString() + "&lng=" + lon.toString() + "&username=datashot&lang=DE";
        return super.fetchValues(url, keys);
    }
}
