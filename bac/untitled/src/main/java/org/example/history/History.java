package org.example.history;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class History {
    private final List<ResultValidation> resultList = new ArrayList<>();

    public void add(ResultValidation result){
        resultList.add(result);
    }

    public void clear(){
        resultList.clear();
    }

    public List<ResultValidation> reverse(){
        return List.copyOf(resultList.reversed());
    }
}
