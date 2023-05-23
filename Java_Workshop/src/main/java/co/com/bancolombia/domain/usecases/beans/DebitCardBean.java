package co.com.bancolombia.domain.usecases.beans;

import co.com.bancolombia.domain.entities.model.dtos.in.retrieve.RetrieveReq;
import co.com.bancolombia.domain.entities.model.dtos.in.retrievecardholderinformationcredit.RetrieveCardHolderReqData;
import co.com.bancolombia.domain.entities.model.dtos.in.retrievecardholderinformationcredit.RetrieveCardHolderReqDataCustomerCard;
import co.com.bancolombia.domain.entities.model.dtos.in.retrievecardholderinformationcredit.RetrieveCardHolderReqDataUser;
import co.com.bancolombia.domain.entities.model.dtos.in.retrievecardholderinformationdebit.RetrieveCardHolderDebitReq;
import org.springframework.stereotype.Component;

@Component
public class DebitCardBean {

    public RetrieveCardHolderDebitReq transformationDebitRequest(RetrieveReq req) {
        return RetrieveCardHolderDebitReq.builder()
                .data(RetrieveCardHolderReqData.builder()
                        .user(RetrieveCardHolderReqDataUser.builder()
                                .id(req.getData().getCard().getId())
                                .build())
                        .customerCard(RetrieveCardHolderReqDataCustomerCard.builder()
                                .number(req.getData().getCard().getNumber())
                                .build())
                        .build())
                .build();
    }
}