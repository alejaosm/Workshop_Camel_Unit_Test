#!/bin/sh

if ls /app/keystore/*.key >/dev/null 2>&1; then

    for keyfile in `ls /app/keystore/*.key`; do
        if [ -s "${keyfile}" ]; then

            if [ -z "${KEY_PASSWORD}" ]; then
                log "No keystore password defined"
                exit 1
            fi

            filename=$(basename ${keyfile})
            dirname=$(dirname ${keyfile})
            alias=$(echo ${filename} | sed -e 's/\.key$'//)
            certfile=${dirname}/${alias}.cer
            passphrasefile=${dirname}/${alias}.pass

            if [ ! -f ${certfile} ]; then
                log "Certificate file ${certfile} not found."
                exit 1
            fi

            ACE_PRI_KEY_PASS=$(cat ${passphrasefile})

            openssl pkcs12 -export -in ${certfile} -inkey ${keyfile} -passin pass:${ACE_PRI_KEY_PASS} -out /app/keystore/keystore.p12 -name ${alias} -password pass:${KEY_PASSWORD} 2>&1

            keytool -importkeystore -srckeystore /app/keystore/keystore.p12 -destkeystore /app/keystore/keystore.jks -srcstorepass ${KEY_PASSWORD} -deststorepass ${KEY_PASSWORD} -srcalias ${alias} -destalias ${alias} -srcstoretype PKCS12 -deststoretype pkcs12 -noprompt 2>&1

            rm /app/keystore/keystore.p12 2>&1
        fi
    done

    rm /app/keystore/*.key ${certfile} ${passphrasefile}
fi

if ls /app/truststore/*.cer >/dev/null 2>&1; then

    for file in `ls /app/truststore/*.cer`; do
        if [ -s "${file}" ]; then
            if [ -z "${KEY_TRUST_PASSWORD}" ]; then
                log "No truststore password defined"
                exit 1
            fi

            filename=$(basename $file)
            alias=$(echo $filename | sed -e 's/\.cer$'//)
            keytool -importcert -trustcacerts -alias ${filename} -file ${file} -keystore /app/keystore/keystore.jks -storepass ${KEY_PASSWORD} -noprompt 2>&1
            keytool -importcert -trustcacerts -alias ${filename} -file ${file} -keystore ${JAVA_HOME}/lib/security/cacerts -storepass #{changeit}# -noprompt 2>&1
        fi
    done

fi