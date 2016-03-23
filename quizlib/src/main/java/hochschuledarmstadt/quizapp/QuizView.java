package hochschuledarmstadt.quizapp;

/**
 * Created by Andreas Schattney on 14.01.2016.
 */
public interface QuizView {
    void renderPossibleAnswer(int position, String possibleAnswerText);
    void clearCheckedRadioButton();
    void renderQuestion(String questionText);
    void renderActionBarSubtitle(String subtitleText);
    void checkPossibleAnswer(int radioButtonId);
    void setAnswerButtonEnabled();
    void setAnswerButtonDisabled();
    void onQuizEnd(int correctAnswers, int wrongAnswers);
    int getIndexOfRadioButtonInRadioGroupFor(int radioButtonId);
}
