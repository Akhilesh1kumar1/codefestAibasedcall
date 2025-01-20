package com.sr.capital.external.truthscreen.service.strategy;

import com.sr.capital.external.truthscreen.adapter.CorpVedaAdapter;
import com.sr.capital.external.truthscreen.adapter.TruthScreenAdapter;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdapterStrategy<T> {

    final TruthScreenAdapter truthScreenAdapter;
    final CorpVedaAdapter corpVedaAdapter;

    public T getAdapter(TruthScreenDocOrchestratorRequest request){
        if(request.getDocType().equals(TruthScreenDocType.CIN_SEARCH)) {
            return (T) corpVedaAdapter;
        }
        return (T) truthScreenAdapter;
    }

}
