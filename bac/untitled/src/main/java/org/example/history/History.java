package org.example.history;

import lombok.extern.slf4j.Slf4j;
import org.example.BD.Add;
import org.example.BD.Clear;
import org.example.BD.FindAll;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public final class History {

    public void add(ResultValidation result) {
        try {
            Add.add(result);
        } catch (SQLException e) {
            throw new IllegalStateException("failed to save result", e);
        }
    }

    public void clear() {
        try {
            Clear.clear();
        } catch (SQLException e) {
            throw new IllegalStateException("failed to clear history", e);
        }
    }

    public List<ResultValidation> reverse() {
        try {
            return FindAll.findAll();
        } catch (SQLException e) {
            throw new IllegalStateException("failed to load history", e);
        }
    }
}