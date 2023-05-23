package co.com.bancolombia.domain.entities.model.dtos.in.retrieveconsolidatedopportunitiesbycustomer;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveConsolidatedCustomerReqData {

    @SerializedName("customer")
    @Valid
    @NotNull(message = "El customer no debe ser nulo.")
    private RetrieveConsolidatedCustomerReqDataCustomer customer;
}