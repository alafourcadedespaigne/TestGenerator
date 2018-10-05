package model;

import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class Answer {

    private SimpleStringProperty name;

    public Answer(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.name.getValue().equals(((Answer) obj).name.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
