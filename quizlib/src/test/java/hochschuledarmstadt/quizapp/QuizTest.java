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

import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class QuizTest {

    private static final int CHECKED_RADIO_BUTTON_ID = 2000;

    public static final String FIRST_QUESTION = "first_question";
    public static final String SECOND_QUESTION = "second_question";
    public static final String THIRD_QUESTION = "third_question";

    public static final String FIRST_ANSWER = "first_answer";
    public static final String SECOND_ANSWER = "second_answer";
    public static final String THIRD_ANSWER = "third_answer";
    public static final String FOURTH_ANSWER = "fourth_answer";

    public static final int AMOUNT_OF_QUESTIONS_IN_QUIZ = 3;

    private QuizView quizView;
    private QuizImpl quiz;

    private Question buildQuestion(String question) {
        String[] s = {FIRST_ANSWER, SECOND_ANSWER, THIRD_ANSWER, FOURTH_ANSWER};
        ArrayList<String> a = new ArrayList<>(Arrays.asList(s));
        return new Question(question, a, FIRST_ANSWER);
    }

    @Before
    public void setUp() {
        quizView = mock(QuizView.class);
        ArrayList<Question> questions = buildQuestions();
        quiz = new QuizImpl(questions);
        quiz.setQuizView(quizView);
    }

    @NonNull
    private ArrayList<Question> buildQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(buildQuestion(FIRST_QUESTION));
        questions.add(buildQuestion(SECOND_QUESTION));
        questions.add(buildQuestion(THIRD_QUESTION));
        return questions;
    }

    @After
    public void tearDown() {

    }

    @Test
    public void createQuiz() {
        assertEquals(AMOUNT_OF_QUESTIONS_IN_QUIZ, quiz.getAmountOfQuestions());
    }

    @Test
    public void canRenderQuestion() {
        quiz.render();
        verify(quizView).renderQuestion(FIRST_QUESTION);
        verify(quizView).renderActionBarSubtitle(String.format(QuizImpl.DEFAULT_SUBTITLE_FORMAT, 1, 3));
        verify(quizView).setAnswerButtonDisabled();
    }

    @Test
    public void rendersProperly(){
        quiz.render();

        verify(quizView).renderQuestion(FIRST_QUESTION);
        verify(quizView).renderPossibleAnswer(0, FIRST_ANSWER);
        verify(quizView).renderPossibleAnswer(1, SECOND_ANSWER);
        verify(quizView).renderPossibleAnswer(2, THIRD_ANSWER);
        verify(quizView).renderPossibleAnswer(3, FOURTH_ANSWER);
    }

    @Test
    public void canSubmitAnswerAfterSelection() {
        quiz.render();
        quiz.setCheckedRadioButtonId(CHECKED_RADIO_BUTTON_ID);
        quiz.submitAnswer();

        verify(quizView).getIndexOfRadioButtonInRadioGroupFor(CHECKED_RADIO_BUTTON_ID);
        verify(quizView).renderQuestion(SECOND_QUESTION);
    }

    @Test
    public void interpretsAnswerAsCorrectAnswer() {

        when(quizView.getIndexOfRadioButtonInRadioGroupFor(CHECKED_RADIO_BUTTON_ID)).thenReturn(0);

        quiz.render();
        quiz.setCheckedRadioButtonId(CHECKED_RADIO_BUTTON_ID);
        quiz.submitAnswer();

        assertEquals(1, quiz.getCorrectAnswers());
    }

    @Test
    public void interpretsAnswerAsWrongAnswer() {

        quiz.render();
        quiz.setCheckedRadioButtonId(CHECKED_RADIO_BUTTON_ID);

        reset(quizView);
        when(quizView.getIndexOfRadioButtonInRadioGroupFor(CHECKED_RADIO_BUTTON_ID)).thenReturn(1);

        quiz.submitAnswer();

        verify(quizView).setAnswerButtonDisabled();

        assertEquals(1, quiz.getWrongAnswers());
    }

    @Test
    public void notifiesWhenQuizHasFinished() {

        when(quizView.getIndexOfRadioButtonInRadioGroupFor(CHECKED_RADIO_BUTTON_ID)).thenReturn(1);

        quiz.render();
        quiz.setCheckedRadioButtonId(CHECKED_RADIO_BUTTON_ID);
        quiz.submitAnswer();
        quiz.setCheckedRadioButtonId(CHECKED_RADIO_BUTTON_ID);
        quiz.submitAnswer();
        quiz.setCheckedRadioButtonId(CHECKED_RADIO_BUTTON_ID);
        quiz.submitAnswer();

        verify(quizView).onQuizEnd(anyInt(), anyInt());

    }

    @Test
    public void quizResetsWhenLastQuestionAnswered() {

        when(quizView.getIndexOfRadioButtonInRadioGroupFor(CHECKED_RADIO_BUTTON_ID)).thenReturn(1);

        quiz.render();
        quiz.setCheckedRadioButtonId(CHECKED_RADIO_BUTTON_ID);
        quiz.submitAnswer();
        quiz.setCheckedRadioButtonId(CHECKED_RADIO_BUTTON_ID);
        quiz.submitAnswer();
        quiz.setCheckedRadioButtonId(CHECKED_RADIO_BUTTON_ID);
        quiz.submitAnswer();

        assertEquals(0, quiz.getCorrectAnswers());
        assertEquals(0, quiz.getWrongAnswers());
        assertEquals(0, quiz.getCurrentQuestionIndex());
        assertEquals(-1, quiz.getSelectedRadioButtonId());

    }

    @Test
    public void clearsRadioButtonSelectionAfterQuestionAnswered() {
        quiz.render();
        quiz.setCheckedRadioButtonId(CHECKED_RADIO_BUTTON_ID);
        quiz.submitAnswer();
        assertEquals(-1, quiz.getSelectedRadioButtonId());
    }

    @Test
    public void canRestoreQuizState() {

        when(quizView.getIndexOfRadioButtonInRadioGroupFor(CHECKED_RADIO_BUTTON_ID)).thenReturn(1);

        QuizState quizState = new QuizState();
        quizState.setWrongAnswers(0);
        quizState.setCorrectAnswers(1);
        quizState.setCurrentQuestionIndex(1);
        quizState.setSelectedRadioButtonId(CHECKED_RADIO_BUTTON_ID);
        quizState.setQuestions(buildQuestions());
        QuizImpl quiz = QuizImpl.restoreFrom(quizState);
        quiz.setQuizView(quizView);
        quiz.render();
        quiz.submitAnswer();

        assertEquals(1, quiz.getWrongAnswers());

    }

}