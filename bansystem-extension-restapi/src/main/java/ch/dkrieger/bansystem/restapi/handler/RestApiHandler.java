package ch.dkrieger.bansystem.restapi.handler;

import ch.dkrieger.bansystem.lib.utils.Document;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class RestApiHandler {

    private final String path;

    public RestApiHandler(String path) {
        if(path.contains(".")) path = path.substring(0,path.indexOf("."));
        this.path = path;
    }
    public String getPath() {
        return this.path;
    }
    public abstract void onRequest(Query query, Document response);

    public static class Query {
        private final Map<String,String> values;

        public Query(URI uri){
            this.values = new LinkedHashMap<>();
            try{
                String[] query = uri.getQuery().split("&");
                for(String subQuery : query){
                    if(subQuery.length() >= 3 && subQuery.contains("=")){
                        String[] rawSubQuery = subQuery.split("=");
                        if(rawSubQuery.length >= 2) this.values.put(rawSubQuery[0],rawSubQuery[1]);
                    }
                }
            }catch (Exception ignored){}
        }

        public String get(String key){
            return values.get(key);
        }

        public boolean contains(String key){
            return values.containsKey(key);
        }
    }
}
