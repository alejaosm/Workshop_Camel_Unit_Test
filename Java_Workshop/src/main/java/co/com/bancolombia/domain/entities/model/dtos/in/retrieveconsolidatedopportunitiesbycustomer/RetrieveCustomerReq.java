package co.com.bancolombia.domain.entities.model.dtos.in.retrieveconsolidatedopportunitiesbycustomer;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * RetrieveConsolidatedCustomerReq
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveCustomerReq {

    @SerializedName("data")
    @Valid
    @NotNull(message = "El data no debe ser nulo.")
    private RetrieveConsolidatedCustomerReqData data;

}
