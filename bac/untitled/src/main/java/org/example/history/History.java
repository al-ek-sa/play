package org.example.history;

import lombok.extern.slf4j.Slf4j;
import org.example.BD.Add;
import org.example.BD.Clear;
import org.example.BD.FindAll;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public final class History {
    private final Add add = new Add();
    private final Clear clear = new Clear();
    private final FindAll findAll = new FindAll();

    public void add(ResultValidation result) {
        try {
            add.add(result);
        } catch (SQLException e) {
            throw new IllegalStateException("failed to save result", e);
        }
    }

    public void clear() {
        try {
            clear.clear();
        } catch (SQLException e) {
            throw new IllegalStateException("failed to clear history", e);
        }
    }

    public List<ResultValidation> reverse() {
        try {
            return findAll.findAll();
        } catch (SQLException e) {
            throw new IllegalStateException("failed to load history", e);
        }
    }
}