package co.com.bancolombia.domain.entities.model.dtos.in.retrieveconsolidatedopportunitiesbycustomer;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveConsolidatedCustomerReqDataSegment {

    @SerializedName("item")
    @Valid
    @NotNull(message = "El item no debe ser nulo.")
    @Size(min = 1, max = 20, message = "La longitud máxima del item es 20")
    private String item;
}
