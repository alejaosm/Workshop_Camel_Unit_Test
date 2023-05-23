package co.com.bancolombia.domain.entities.model.dtos.out.retrieveconsolidatedopportunities;

import co.com.bancolombia.domain.entities.model.dtos.enums.TypeEnum;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * RetrieveConsolidatedResDataCustomer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveConsolidatedResDataCustomer {


    @SerializedName("type")
    private TypeEnum type;

    @SerializedName("number")
    private String number;

    @SerializedName("fullName")
    private String fullName;

}