package com.fungisearch.fudriver.warehouseEastMushrooms.web;

import com.fungisearch.fudriver.config.AppConfig;
import com.fungisearch.fudriver.warehouseEastMushrooms.query.dao.EastWarehouseDao;
import com.fungisearch.fudriver.warehouseEastMushrooms.query.dto.ProxyShipmentDto;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;

@Service
public class ProxyService {

    private final EastWarehouseDao eastWarehouseDao;
    private final AppConfig appConfig;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ProxyService(EastWarehouseDao eastWarehouseDao, AppConfig appConfig) {
        this.eastWarehouseDao = eastWarehouseDao;
        this.appConfig = appConfig;
        DisableSslVerification disableSslVerificationz = new DisableSslVerification();
    }


    @Bean
    public RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(10000);
        clientHttpRequestFactory.setReadTimeout(10000);
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }



    public void sendShipmentToProxy(long shipmentId) {
        ProxyShipmentDto shipmentDto = eastWarehouseDao.getShipmentForProxy(shipmentId);
        //Wysyłka shipmentu o ile odbiorcą jest Skup
        //if(shipmentDto.shipmentCustomerGroupId > 0) {
            shipmentDto.supplierId = Integer.parseInt(appConfig.getSupplierNr());
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            String auth = appConfig.getSkupUsername() + ":" + appConfig.getSkupPassword();
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            headers.add("Authorization", authHeader);
            HttpEntity<ProxyShipmentDto> entity = new HttpEntity<ProxyShipmentDto>(shipmentDto, headers);
            try {
                getRestTemplate().exchange(appConfig.getSkupUrl() + "/rest/farmShipment/" + appConfig.getSupplierNr(), HttpMethod.POST, entity, String.class);
            } catch (Exception ex) {
                logger.error("Błąd wysyłania shipmentu do PROXY id: " + shipmentDto.shipmentId);
                logger.error(ex.getMessage());
            }
        //}
    }
}
