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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

/**
 * Verwaltet das Quiz
 */
public abstract class QuizActivity extends AppCompatActivity implements QuizView {

    private static final String TAG = QuizActivity.class.getName();
    private QuizImpl quiz;

    public Quiz getQuiz() {
        return quiz;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initQuiz(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        quiz.render();
    }

    private void initQuiz(Bundle savedInstanceState) {
        try {
            quiz = buildQuiz(savedInstanceState);
            quiz.setQuizView(this);
        } catch (JSONException e) {
            Log.e(TAG, getString(R.string.error_creating_questions), e);
        } catch (IOException e) {
            Log.e(TAG, getString(R.string.error_reading_questions), e);
        }
    }

    private QuizImpl buildQuiz(Bundle savedInstanceState) throws IOException, JSONException {
        if (savedInstanceState == null) {
            return createQuiz();
        } else {
            return restoreQuiz(savedInstanceState);
        }
    }

    private QuizImpl createQuiz() throws IOException, JSONException {
        return QuizImpl.createNew(getAssets(), getString(R.string.questions_filename));
    }

    private QuizImpl restoreQuiz(Bundle savedInstanceState) {
        QuizState quizState = savedInstanceState.getParcelable(QuizImpl.SAVED_STATE_KEY);
        return QuizImpl.restoreFrom(quizState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(QuizImpl.SAVED_STATE_KEY, quiz.saveInstanceState());
    }

    @Override
    public void renderPossibleAnswer(int position, String possibleAnswerText) {

    }

    @Override
    public void clearCheckedRadioButton() {

    }

    @Override
    public void renderQuestion(String questionText) {

    }

    @Override
    public void renderActionBarSubtitle(String subtitleText) {

    }

    @Override
    public void checkPossibleAnswer(int radioButtonId) {

    }

    @Override
    public void setAnswerButtonEnabled() {

    }

    @Override
    public void setAnswerButtonDisabled() {

    }

    @Override
    public void onQuizEnd(int correctAnswers, int wrongAnswers) {

    }

    @Override
    public int getIndexOfRadioButtonInRadioGroupFor(int radioButtonId) {
        return 0;
    }
}
