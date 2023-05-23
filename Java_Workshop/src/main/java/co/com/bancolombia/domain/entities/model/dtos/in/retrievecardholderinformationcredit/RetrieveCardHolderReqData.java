package co.com.bancolombia.domain.entities.model.dtos.in.retrievecardholderinformationcredit;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveCardHolderReqData {

    @SerializedName("user")
    @Valid
    @NotNull(message = "El user no debe ser nulo.")
    private RetrieveCardHolderReqDataUser user;

    @SerializedName("customerCard")
    @Valid
    @NotNull(message = "El customerCard no debe ser nulo.")
    private RetrieveCardHolderReqDataCustomerCard customerCard;
}
