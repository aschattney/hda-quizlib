package hochschuledarmstadt.quizapp;

/**
 * Created by Andreas Schattney on 14.01.2016.
 */
public interface QuizView {
    void renderPossibleAnswer(int index, String text);
    void clearCheckedRadioButton();
    void renderQuestion(String text);
    void renderActionBarSubtitle(String text);
    void checkPossibleAnswer(final int radioButtonId);
    void setAnswerButtonEnabledOrDisabled(boolean enabled);
    void onQuizEnd(final int correctAnswers, final int wrongAnswers);
    int getIndexOfRadioButtonInRadioGroupFor(final int radioButtonId);
}
