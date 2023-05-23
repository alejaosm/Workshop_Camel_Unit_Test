package co.com.bancolombia.domain.entities.model.dtos.in.retrievecardholderinformationdebit;

import co.com.bancolombia.domain.entities.model.dtos.in.retrievecardholderinformationcredit.RetrieveCardHolderReqData;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * RetrieveCardHolderInformation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveCardHolderDebitReq {

    @SerializedName("data")
    @Valid
    @NotNull(message = "El data no debe ser nulo.")
    private RetrieveCardHolderReqData data;
}
