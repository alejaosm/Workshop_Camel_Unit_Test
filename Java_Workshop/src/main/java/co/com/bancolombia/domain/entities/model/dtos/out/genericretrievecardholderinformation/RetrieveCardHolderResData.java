package co.com.bancolombia.domain.entities.model.dtos.out.genericretrievecardholderinformation;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveCardHolderResData {

    @SerializedName("customer")
    private RetrieveCardHolderResDataCustomer customer;

}
