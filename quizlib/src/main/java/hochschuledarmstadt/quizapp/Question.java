package hochschuledarmstadt.quizapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Andreas Schattney on 22.12.2015.
 */
class Question implements Parcelable {

    public static final String JSON_KEY_TEXT = "text";
    public static final String JSON_KEY_POSSIBLE_ANSWERS = "possibleAnswers";
    public static final String JSON_KEY_CORRECT_ANSWER = "correctAnswer";

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    private final String correctAnswer;
    private final ArrayList<String> possibleAnswers;
    private final String questionText;

    public Question(String questionText, ArrayList<String> possibleAnswers, String correctAnswer) {
        this.questionText = questionText;
        this.possibleAnswers = possibleAnswers;
        this.correctAnswer = correctAnswer;
    }

    public Question(Parcel in) {
        questionText = in.readString();
        possibleAnswers = new ArrayList<>();
        in.readStringList(possibleAnswers);
        correctAnswer = in.readString();
    }

    public static Question createFromJson(JSONObject questionObj) throws JSONException {
        JSONArray jsonPossibleAnswers = questionObj.getJSONArray(JSON_KEY_POSSIBLE_ANSWERS);
        ArrayList<String> possibleAnswers = buildPossibleAnswersFromJsonArray(jsonPossibleAnswers);
        String questionText = questionObj.getString(JSON_KEY_TEXT);
        String correctAnswer = questionObj.getString(JSON_KEY_CORRECT_ANSWER);
        return new Question(questionText, possibleAnswers, correctAnswer);
    }

    @NonNull
    private static ArrayList<String> buildPossibleAnswersFromJsonArray(JSONArray jsonPossibleAnswers) throws JSONException {
        ArrayList<String> possibleAnswers = new ArrayList<>();
        for (int answerPosition = 0; answerPosition < jsonPossibleAnswers.length(); answerPosition++) {
            possibleAnswers.add(jsonPossibleAnswers.getString(answerPosition));
        }
        return possibleAnswers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getPossibleAnswer(int index) {
        return possibleAnswers.get(index);
    }

    public String getQuestionText() {
        return questionText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionText);
        dest.writeStringList(possibleAnswers);
        dest.writeString(correctAnswer);
    }

    public void shufflePossibleAnswers() {
        Collections.shuffle(possibleAnswers);
    }

}
