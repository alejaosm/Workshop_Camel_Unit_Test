
package co.com.bancolombia.domain.entities.model.dtos.in.retrieveconsolidatedopportunitiesbycustomer;

import co.com.bancolombia.domain.entities.model.dtos.enums.TypeEnum;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * RetrieveConsolidatedCustomerReqDataCustomer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveConsolidatedCustomerReqDataCustomer {

    @SerializedName("type")
    @Valid
    @NotNull(message = "El type no debe ser nulo.")
    private TypeEnum type;

    @SerializedName("number")
    @Valid
    @NotNull(message = "El number no debe ser nulo.")
    @Size(min = 1, max = 255, message = "La longitud máxima del number es 255")
    private String number;

}

