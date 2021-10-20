package com.fungisearch.fudriver.reclassification.scheduler;

/**
 * Created by marcin on 03.02.16.
 */

public class RemoteDataImporter {

    private String skupUrl;
    private String path;
    private String username;
    private String password;

    public RemoteDataImporter(String skupUrl, String path, String username, String password){
        this.skupUrl = skupUrl;
        this.path = path;
        this.username = username;
        this.password = password;
    }

    public String getRemoteTypes(){
        SkupService getRemoteTypes = new SkupService(skupUrl,path,username,password);
        String json = getRemoteTypes.getDataFromServer();
        return json;
    }


}
