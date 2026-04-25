package ru.mtuci.coursemanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.URI;
import java.util.List;

@RestController
public class ProxyController {

    @GetMapping("/api/proxy")
    public String proxy(@RequestParam("targetUrl") String targetUrl) throws Exception {
        validateUrl(targetUrl);

        RestTemplate rt = new RestTemplate();
        return rt.getForObject(targetUrl, String.class);
    }

    private void validateUrl(String targetUrl) throws Exception {
        URI uri = URI.create(targetUrl);

        if (!List.of("http", "https").contains(uri.getScheme())) {
            throw new IllegalArgumentException("Only HTTP/HTTPS allowed");
        }

        InetAddress address = InetAddress.getByName(uri.getHost());

        if (address.isLoopbackAddress()
                || address.isAnyLocalAddress()
                || address.isSiteLocalAddress()
                || address.isLinkLocalAddress()) {
            throw new IllegalArgumentException("Private/internal addresses are blocked");
        }
    }
}
