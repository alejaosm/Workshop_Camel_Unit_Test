package co.com.bancolombia.infrastructure.utils;

public final class Util {

    private Util(){
        super();
    }

    public static StringBuilder uriParameters(){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?throwExceptionOnFailure");
        stringBuilder.append("=false");
        stringBuilder.append("&bridgeEndpoint");
        stringBuilder.append("=true");
        stringBuilder.append("&socketTimeout");
        stringBuilder.append("=6500");
        stringBuilder.append("&connectTimeout");
        stringBuilder.append("=6500");

        return stringBuilder;
    }

}