function() {

    var env = karate.env; // get java system property 'karate.env'
    var keyPath = karate.properties['karate.keyPath'] || 'security/deposits.jks'; // path to keystore
    var keyPassword = karate.properties['karate.keyPassword'] || 'd0c7aaca-deposits'; // password to keystore
    var userAuth = karate.properties['karate.userAuth'] || 'user1'; // user to basic auth
    var passAuth = karate.properties['karate.passAuth'] || 'pass456*'; // pass to basic auth
    var passAuthError = 'nosense'; // pass to basic auth wrong

    if (!env) {
        env = 'local'; // a custom 'intelligent' default
    }

    var temp = userAuth + ':' + passAuth;
    var Base64 = Java.type('java.util.Base64');
    var authToken = 'Basic ' + Base64.getEncoder().encodeToString(temp.getBytes());
    var temp2 = userAuth + ':' + passAuthError;
    var authTokenError = 'Basic ' + Base64.getEncoder().encodeToString(temp2.getBytes());
    var sslStringConfig = '{ "keyStore": "'+ keyPath+'", "keyStorePassword": "'+keyPassword+'", "algorithm": "TLSv1.2"}';
    var sslJSON = JSON.parse(sslStringConfig);

    var config = { // base config JSON
        appId: 'my.app.id',
        appSecret: 'my.secret',
        defUrlBase: 'https://localhost:8081',
        defPathDataError: '../dataFiles/LOCAL_Data_Error.csv',
        defPathDataSuccess: '../dataFiles/LOCAL_Data_Success.csv',
        sslJSON: sslJSON,
        authToken: authToken,
        authTokenError: authTokenError,
    };

    if (env == 'DEV') {
        // over-ride only those that need to be
        config.defUrlBase = 'https://ROUTE_SERVICE-NAMESPACE_OCP.apps.ocpdes.ambientesbc.lab';
        config.defPathDataError = '../dataFiles/DEV_Data_Error.csv';
        config.defPathDataSuccess = '../dataFiles/DEV_Data_Success.csv';
    } else if (env == 'CER') {
        config.defUrlBase = 'https://ROUTE_SERVICE-NAMESPACE_OCP.apps.ocpqa.ambientesbc.lab';
        config.defPathDataError = '../dataFiles/CER_Data_Error.csv';
        config.defPathDataSuccess = '../dataFiles/CER_Data_Success.csv';
    } else if (env == 'PDN') {
        config.defUrlBase = 'https://ROUTE_SERVICE-NAMESPACE_OCP.apps.ocpprod.bancolombia.corp';
        config.defPathDataError = '../dataFiles/PDN_Data_Error.csv';
        config.defPathDataSuccess = '../dataFiles/PDN_Data_Success.csv';
    }

    // don't waste time waiting for a connection or if servers don't respond within 5 seconds
    karate.configure('connectTimeout', 50000);
    karate.configure('readTimeout', 50000);

    return config;

}