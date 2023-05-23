package co.com.bancolombia.domain.entities.model.dtos.out.retrieveconsolidatedopportunitiesbycustomer;

import co.com.bancolombia.domain.entities.model.dtos.out.retrieveconsolidatedopportunities.RetrieveConsolidatedResData;
import co.com.bancolombia.integracion.camel.processors.http.transform.model.Meta;
import com.google.gson.annotations.SerializedName;
import lombok.*;


/**
 * RetrieveConsolidatedCustomerRes
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveConsolidatedCustomerRes {


    @SerializedName("meta")
    private Meta meta;

    @SerializedName("data")
    private RetrieveConsolidatedResData data;

}