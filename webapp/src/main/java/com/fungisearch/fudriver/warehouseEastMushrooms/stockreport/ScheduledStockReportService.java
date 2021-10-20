package com.fungisearch.fudriver.warehouseEastMushrooms.stockreport;

import com.fungisearch.fudriver.config.AppConfig;
import com.fungisearch.fudriver.warehouseEastMushrooms.stockreport.dao.StockReportDao;
import com.fungisearch.fudriver.warehouseEastMushrooms.stockreport.dto.StockReportDto;
import com.fungisearch.fudriver.warehouseEastMushrooms.web.DisableSslVerification;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@Component
public class ScheduledStockReportService {

    private final AppConfig appConfig;
    private final StockReportDao stockReportDao;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ScheduledStockReportService(AppConfig appConfig, StockReportDao stockReportDao) {
        this.appConfig = appConfig;
        this.stockReportDao = stockReportDao;
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

    @Scheduled(fixedDelay = 600000)
    @Async
    public void setStockReportToProxy(){
        logger.info("----------- wysyłam raport stanu magazynowego");
        List<StockReportDto> list = stockReportDao.getStockReport();
        logger.info("----------- wielkość tabeli stanu magazynowego: " + list.size());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String auth = appConfig.getSkupUsername() + ":" + appConfig.getSkupPassword();
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        headers.add("Authorization", authHeader);
        HttpEntity<List<StockReportDto>> entity = new HttpEntity<List<StockReportDto>>(list, headers);
        try {
            getRestTemplate().exchange(appConfig.getSkupUrl() + "/rest/stock/" + appConfig.getSupplierNr(), HttpMethod.POST, entity, String.class);
        } catch (Exception ex) {
            logger.error("Błąd wysyłania stanu chłodni");
            logger.error(ex.getMessage());
        }

    }


}
