package com.example.quizgame.question;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "questions")
public class Question {
    public Question(int id, String question, String type, String opt1, String opt2, String opt3, String opt4, String answ) {
        this.id = id;
        this.question = question;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.opt4 = opt4;
        this.answ = answ;
        this.type = type;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    public int id;
    @ColumnInfo(name = "question")
    public String question;
    @ColumnInfo(name = "opt1")
    public String opt1;
    @ColumnInfo(name = "opt2")
    public String opt2;
    @ColumnInfo(name = "opt3")
    public String opt3;
    @ColumnInfo(name = "opt4")
    public String opt4;
    @ColumnInfo(name = "answ")
    public String answ;
    @ColumnInfo(name = "typeq")
    public String type;
}