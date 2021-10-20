package com.fungisearch.fudriver.reclassification.scheduler;

import com.fungisearch.fudriver.reclassification.query.dto.RemoteReclassificationDto;
import com.fungisearch.fudriver.reclassification.query.dto.RemoteReclassificationDetailDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * Created by marcin on 03.02.16.
 */
public class RemoteReclassificationsImporter {

    private String skupUrl;
    private String supplierNr;
    private String username;
    private String password;

    public RemoteReclassificationsImporter(String skupUrl,String supplierNr,String username, String password){
        this.skupUrl = skupUrl;
        this.supplierNr = supplierNr;
        this.username = username;
        this.password = password;
    }

    public List<Long> getRemoteReclassificationIds(){
        String path = "/api/supplier/" + supplierNr + "/reclassification";
        SkupService skupService = new SkupService(skupUrl,path,username,password);
        String ids = skupService.getDataFromServer();
        return convertTypesJsonToList(ids);
    }

    private List<Long> convertTypesJsonToList(String json){
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Long>>(){}.getType();
        List<Long> ids = gson.fromJson(json,collectionType);
        return ids;
    }

    public RemoteReclassificationDto getRemoteReclassification(Long id){
        String path = "/api/reclassificationHeader/" + id;
        SkupService skupService = new SkupService(skupUrl, path,username,password);
        String reclassHeader = skupService.getDataFromServer();
        return convertJsonToReclassification(reclassHeader);
    }

    private RemoteReclassificationDto convertJsonToReclassification(String json){
        //Gson gson = new Gson();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.fromJson(json, RemoteReclassificationDto.class);
    }

    public List<RemoteReclassificationDetailDto> getRemoteHeaders(Long id){
        String path = "/api/reclassificationHeader/" + id + "/reclassificationDetails";
        SkupService skupService = new SkupService(skupUrl,path,username,password);
        String json = skupService.getDataFromServer();
        return convertJsonToReclassificationHeaders(json);
    }

    private List<RemoteReclassificationDetailDto> convertJsonToReclassificationHeaders(String json){
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<RemoteReclassificationDetailDto>>(){}.getType();
        List<RemoteReclassificationDetailDto> headers = gson.fromJson(json,collectionType);
        return headers;
    }

}
