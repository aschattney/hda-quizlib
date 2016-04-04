/*
 * The MIT License
 *
 * Copyright (c) 2016 Andreas Schattney
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package hochschuledarmstadt.quizapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

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
