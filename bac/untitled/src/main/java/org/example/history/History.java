package org.example.history;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class History {
    private final List<ResultValidation> resultList = new ArrayList<>();

    public void add(ResultValidation result){
        resultList.add(result);
        log.debug("history size {}", resultList.size());
    }

    public void clear(){
        int removed = resultList.size();
        resultList.clear();
        log.debug("history cleared, {} entries removed", removed);
    }

    public List<ResultValidation> reverse(){
        return List.copyOf(resultList.reversed());
    }
}
