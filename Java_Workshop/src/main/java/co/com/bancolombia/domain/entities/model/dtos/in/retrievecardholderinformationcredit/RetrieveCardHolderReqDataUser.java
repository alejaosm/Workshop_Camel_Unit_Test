package co.com.bancolombia.domain.entities.model.dtos.in.retrievecardholderinformationcredit;

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
public class RetrieveCardHolderReqDataUser {

    @SerializedName("id")
    @Valid
    @NotNull(message = "El id no debe ser nulo.")
    @Size(min = 1, max = 5, message = "La longitud máxima del id es 5")
    private String id;
}
