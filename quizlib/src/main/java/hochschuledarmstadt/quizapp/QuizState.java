package hochschuledarmstadt.quizapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Andreas Schattney on 14.01.2016.
 */
public class QuizState implements Parcelable {

    private ArrayList<Question> questions;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private int selectedRadioButtonId = -1;

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public int getSelectedRadioButtonId() {
        return selectedRadioButtonId;
    }

    public void setSelectedRadioButtonId(int selectedRadioButtonId) {
        this.selectedRadioButtonId = selectedRadioButtonId;
    }

    public QuizState(){}

    protected QuizState(Parcel in) {
        questions = new ArrayList<>();
        in.readTypedList(questions, Question.CREATOR);
        currentQuestionIndex = in.readInt();
        correctAnswers = in.readInt();
        wrongAnswers = in.readInt();
        selectedRadioButtonId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(questions);
        dest.writeInt(currentQuestionIndex);
        dest.writeInt(correctAnswers);
        dest.writeInt(wrongAnswers);
        dest.writeInt(selectedRadioButtonId);
    }

    public static final Creator<QuizState> CREATOR = new Creator<QuizState>() {
        @Override
        public QuizState createFromParcel(Parcel in) {
            return new QuizState(in);
        }

        @Override
        public QuizState[] newArray(int size) {
            return new QuizState[size];
        }
    };
}
