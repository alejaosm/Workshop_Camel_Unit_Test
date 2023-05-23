package co.com.bancolombia.domain.entities.model.dtos.out.retrieveconsolidatedopportunities;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * RetrieveConsolidatedResData
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveConsolidatedResData {


    @SerializedName("customer")
    private RetrieveConsolidatedResDataCustomer customer;

    @SerializedName("preapproved")
    private RetrieveConsolidatedResDataPreapproved preapproved;

}