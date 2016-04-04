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

/**
 * Definiert die zu implementierenden Methoden in der Activity. <br>
 * Diese Methoden werden von der Applikationslogik aufgerufen. <br>
 * Innerhalb dieser Methoden sollen die UI aktualisiert werden.
 */
public interface QuizView {
    /**
     * In dieser Methode soll der Text eines RadioButtons mit der möglichen Antwort {@code possibleAnswerText} aktualisiert werden
     * {@code position} gibt an, welcher RadioButton innerhalb der RadioGroup aktualisiert werden soll.
     * @param position Position des RadioButtons in der RadioGroup
     * @param possibleAnswerText die mögliche Antwort
     */
    void renderPossibleAnswer(int position, String possibleAnswerText);

    /**
     * In dieser Methode soll der aktuell markierte RadioButton demarkiert, also zurückgesetzt werden
     */
    void clearCheckedRadioButton();

    /**
     * In dieser Methode soll die aktuelle Frage gesetzt werden
     * @param questionText die aktuelle Frage
     */
    void renderQuestion(String questionText);

    /**
     * In dieser Methode soll der Untertitel der ActionBar mit dem Text {@code subtitleText} aktualisiert werden
     * @param subtitleText der Text
     */
    void renderActionBarSubtitle(String subtitleText);

    /**
     * In dieser Methode soll der RadioButton mit der Id {@code radioButtonId} als markiert gesetzt werden
     * @param radioButtonId die id des RadioButtons
     */
    void checkPossibleAnswer(int radioButtonId);

    /**
     * In dieser Methode soll der Button aktiviert werden (setEnabled).
     */
    void setAnswerButtonEnabled();

    /**
     * In dieser Methode soll der Button deaktiviert werden
     */
    void setAnswerButtonDisabled();

    /**
     * Diese Methode wird aufgerufen, wenn das Quiz beendet, also alle Fragen beantwortet wurden.
     * @param correctAnswers Anzahl der richtigen Antworten
     * @param wrongAnswers Anzahl der falschen Antworten
     */
    void onQuizEnd(int correctAnswers, int wrongAnswers);

    /**
     * In dieser Methode soll der Index für einen RadioButton mit der Id {@code radioButtonId} innerhalb der RadioGroup zurück gegeben werden.
     * @param radioButtonId id des RadioButtons
     * @return index des RadioButtons in der RadioGroup
     */
    int getIndexOfRadioButtonInRadioGroupFor(int radioButtonId);
}
