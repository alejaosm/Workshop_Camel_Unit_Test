package co.com.bancolombia.domain.entities.model.dtos.out.retrieveconsolidatedopportunities;

import co.com.bancolombia.integracion.camel.processors.http.transform.model.Meta;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RetrieveConsolidatedRes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveConsolidatedRes {

    @SerializedName("meta")
    private Meta meta;

    @SerializedName("data")
    private RetrieveConsolidatedResData data;

}

