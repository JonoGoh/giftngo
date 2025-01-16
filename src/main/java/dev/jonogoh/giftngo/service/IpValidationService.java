package dev.jonogoh.giftngo.service;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.jonogoh.giftngo.config.IpValidationConfig;
import dev.jonogoh.giftngo.exception.IpBlockedException;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Service
@AllArgsConstructor
public class IpValidationService {

  @NonNull
  private final IpValidationConfig ipValidationConfig;

  private static final String IP_API_URL = "http://ip-api.com/json/";

  @SuppressWarnings("unchecked")
  public Map<String, String> validateIp(String ip) {
    RestTemplate restTemplate = new RestTemplate();
    Map<String, Object> response = restTemplate.getForObject(IP_API_URL + ip, Map.class);

    if (response == null || !"success".equals(response.get("status"))) {
      throw new IpBlockedException("Unable to validate IP address.");
    }

    String country = (String) response.get("country");
    String isp = (String) response.get("isp");

    validateCountry(country);
    validateIsp(isp);

    String countryCode = (String) response.get("countryCode");
    return Map.of("countryCode", countryCode, "isp", isp);
  }

  private void validateCountry(String country) {
    if (ipValidationConfig.getBlockedCountries().contains(country)) {
      throw new IpBlockedException("IP from blocked country: " + country);
    }
  }

  private void validateIsp(String isp) {
    if (ipValidationConfig.getBlockedIsps().contains(isp)) {
      throw new IpBlockedException("IP from blocked ISP: " + isp);
    }
  }
}
