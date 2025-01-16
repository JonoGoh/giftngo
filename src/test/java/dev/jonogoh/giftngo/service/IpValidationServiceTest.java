package dev.jonogoh.giftngo.service;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import dev.jonogoh.giftngo.config.IpValidationConfig;
import dev.jonogoh.giftngo.exception.IpBlockedException;

@ExtendWith(MockitoExtension.class)
public class IpValidationServiceTest {

  @Mock
  private IpValidationConfig ipValidationConfig;

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private IpValidationService ipValidationService;

  private static final String IP_API_URL = "http://ip-api.com/json/";

  @Test
  void testValidateIp_success() {
        String ip = "127.0.0.1";
        Map<String, Object> mockResponse = Map.of(
            "status", "success",
            "country", "Local",
            "isp", "Loopback",
            "countryCode", "LO"
        );

        when(restTemplate.getForObject(IP_API_URL + ip, Map.class)).thenReturn(mockResponse);
        when(ipValidationConfig.getBlockedCountries()).thenReturn(List.of("China", "Spain", "United States"));
        when(ipValidationConfig.getBlockedIsps()).thenReturn(List.of("AWS", "GCP", "Azure"));

        Map<String, String> result = ipValidationService.validateIp(ip);

        assertThat(result)
            .containsEntry("countryCode", "LO")
            .containsEntry("isp", "Loopback");
  }

    @Test
    void shouldThrowExceptionWhenIpApiFails() {
        String ip = "127.0.0.1";
        when(restTemplate.getForObject(IP_API_URL + ip, Map.class)).thenReturn(null);

        assertThatThrownBy(() -> ipValidationService.validateIp(ip))
            .isInstanceOf(IpBlockedException.class)
            .hasMessage("Unable to validate IP address.");
    }

    @Test
    void shouldThrowExceptionForBlockedCountry() {
        String ip = "192.168.0.1";
        Map<String, Object> mockResponse = Map.of(
            "status", "success",
            "country", "China",
            "isp", "SomeISP",
            "countryCode", "CN"
        );

        when(restTemplate.getForObject(IP_API_URL + ip, Map.class)).thenReturn(mockResponse);
        when(ipValidationConfig.getBlockedCountries()).thenReturn(List.of("China", "Spain", "United States"));

        assertThatThrownBy(() -> ipValidationService.validateIp(ip))
            .isInstanceOf(IpBlockedException.class)
            .hasMessage("IP from blocked country: China");
    }

    @Test
    void shouldThrowExceptionForBlockedIsp() {
        String ip = "192.168.1.1";
        Map<String, Object> mockResponse = Map.of(
            "status", "success",
            "country", "Local",
            "isp", "AWS",
            "countryCode", "LO"
        );

        when(restTemplate.getForObject(IP_API_URL + ip, Map.class)).thenReturn(mockResponse);
        when(ipValidationConfig.getBlockedCountries()).thenReturn(List.of("China", "Spain", "United States"));
        when(ipValidationConfig.getBlockedIsps()).thenReturn(List.of("AWS", "GCP", "Azure"));

        assertThatThrownBy(() -> ipValidationService.validateIp(ip))
            .isInstanceOf(IpBlockedException.class)
            .hasMessage("IP from blocked ISP: AWS");
    }
}