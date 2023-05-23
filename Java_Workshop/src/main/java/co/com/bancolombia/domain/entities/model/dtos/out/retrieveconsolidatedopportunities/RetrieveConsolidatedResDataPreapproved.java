package co.com.bancolombia.domain.entities.model.dtos.out.retrieveconsolidatedopportunities;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * RetrieveConsolidatedResDataPreapproved
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RetrieveConsolidatedResDataPreapproved {


    @SerializedName("opportunityId")
    private String opportunityId;

    @SerializedName("productCode")
    private String productCode;

    @SerializedName("productName")
    private String productName;

    @SerializedName("preapprovedQuota")
    private Double preapprovedQuota;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("startHour")
    private String startHour;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("endHour")
    private String endHour;

    @SerializedName("priority")
    private Integer priority;

    @SerializedName("totalRecords")
    private Integer totalRecords;

}

