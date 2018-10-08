package model;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private StringProperty name;
    private IntegerProperty count;
    private List<Answer> answers;

    public Question(){}

    public Question(String name, Integer count, List<Answer> answers) {
        this.name = new SimpleStringProperty(name);
        this.count = new SimpleIntegerProperty(count);
        this.answers = new ArrayList<>(answers);

    }


    public String getName() {
        return name.get();
    }

    public void setName(String firstName) {
        this.name.set(firstName);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Integer getCount() {
        return count.get();
    }

    public IntegerProperty countProperty() {
        return count;
    }

    public void setCount(Integer count) {
        this.count.set(count);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
