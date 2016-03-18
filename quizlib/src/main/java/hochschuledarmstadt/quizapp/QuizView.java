package hochschuledarmstadt.quizapp;

/**
 * Created by Andreas Schattney on 14.01.2016.
 */
public interface QuizView {
    void setRadioButtonText(int index, String text);
    void clearCheckedRadioButton();
    void setQuestion(String text);
    void setActionBarSubtitle(String text);
    void checkRadioButton(final int radioButtonId);
    void setAnswerButtonEnabled(boolean enabled);
    void onQuizEnd(final int correctAnswers, final int wrongAnswers);
    int getIndexInRadioGroupFor(final int radioButtonId);
}
