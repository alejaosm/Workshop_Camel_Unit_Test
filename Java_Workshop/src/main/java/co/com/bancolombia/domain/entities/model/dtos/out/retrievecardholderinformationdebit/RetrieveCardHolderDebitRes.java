package co.com.bancolombia.domain.entities.model.dtos.out.retrievecardholderinformationdebit;

import co.com.bancolombia.domain.entities.model.dtos.out.genericretrievecardholderinformation.RetrieveCardHolderResData;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveCardHolderDebitRes {

    @SerializedName("data")
    private RetrieveCardHolderResData data;
}
