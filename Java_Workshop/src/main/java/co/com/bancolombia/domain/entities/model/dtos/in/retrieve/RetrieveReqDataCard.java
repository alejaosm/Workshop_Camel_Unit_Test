package co.com.bancolombia.domain.entities.model.dtos.in.retrieve;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveReqDataCard {

    @SerializedName("id")
    @Pattern(regexp = "[a-zA-Z]{1,5}", message = "El campo id no debe contener caracteres especiales")
    @Valid()
    @NotNull(message = "El campo id no debe ser nulo.")
    @Size(min = 1, max = 5, message = "La longitud máxima del id del canal es 5")
    private String id;

    @SerializedName("number")
    @Valid
    @NotNull(message = "El campo number no debe ser nulo.")
    @Size(min = 1, max = 19, message = "La longitud máxima del number es de 19")
    private String number;

    @SerializedName("cardType")
    @Valid
    @NotNull(message = "El campo cardType no debe ser nulo.")
    @Pattern(regexp = "^(D|C)$", message = "El Type no tiene un valor válido.")
    private String cardType;

}


