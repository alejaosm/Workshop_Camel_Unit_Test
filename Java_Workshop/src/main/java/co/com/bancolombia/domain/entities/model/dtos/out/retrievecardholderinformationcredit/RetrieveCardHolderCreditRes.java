package co.com.bancolombia.domain.entities.model.dtos.out.retrievecardholderinformationcredit;

import co.com.bancolombia.domain.entities.model.dtos.out.genericretrievecardholderinformation.RetrieveCardHolderResData;
import co.com.bancolombia.integracion.camel.processors.http.transform.model.Meta;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveCardHolderCreditRes {

    @SerializedName("meta")
    private Meta meta;

    @SerializedName("data")
    private RetrieveCardHolderResData data;
}
