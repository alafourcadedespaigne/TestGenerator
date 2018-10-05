package model;

import java.util.Date;
import java.util.List;

public class Test {

    private String name;
    private Date createDate;
    private int countQuestion;
    private List<Question>questions;

    public Test() {
    }

    public Test(String name, Date createDate, int countQuestion, List<Question> questions) {
        this.name = name;
        this.createDate = createDate;
        this.countQuestion = countQuestion;
        this.questions = questions;
    }

    public Test(List<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getCountQuestion() {
        return countQuestion;
    }

    public void setCountQuestion(int countQuestion) {
        this.countQuestion = countQuestion;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
