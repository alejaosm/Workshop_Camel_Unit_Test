package co.com.bancolombia.domain.entities.model.dtos.out.genericretrievecardholderinformation;

import co.com.bancolombia.domain.entities.model.dtos.enums.TypeEnum;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveCardHolderResDataCustomerIdentification {

    @SerializedName("documentType")
    private TypeEnum documentType;

    @SerializedName("documentNumber")
    private String documentNumber;

}
