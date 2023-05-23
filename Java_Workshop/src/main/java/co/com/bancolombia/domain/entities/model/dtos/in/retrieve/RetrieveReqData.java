package co.com.bancolombia.domain.entities.model.dtos.in.retrieve;

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
public class RetrieveReqData {

    @SerializedName("card")
    @Valid
    @NotNull(message = "El campo card no debe ser nulo.")
    private RetrieveReqDataCard card;

}