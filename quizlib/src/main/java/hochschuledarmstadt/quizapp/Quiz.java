package hochschuledarmstadt.quizapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Andreas Schattney on 14.01.2016.
 */
public class Quiz {

    private static final int AMOUNT_OF_POSSIBLE_ANSWERS = 4;
    public static final String SAVED_STATE_KEY = "QUIZ_KEY";
    public static final String DEFAULT_SUBTITLE_FORMAT = "Question %s / %s";

    public static Quiz createNew(String jsonString) throws JSONException {
        ArrayList<Question> questions = new ArrayList<>();
        JSONArray array = new JSONArray(jsonString);
        for (int position = 0; position < array.length(); position++) {
            JSONObject questionObj = array.getJSONObject(position);
            Question question = Question.createFromJson(questionObj);
            questions.add(question);
        }
        shuffleQuestions(questions);
        shufflePossibleAnswers(questions);
        return new Quiz(questions);
    }

    private static void shuffleQuestions(ArrayList<Question> questions) {
        Collections.shuffle(questions);
    }

    private static void shufflePossibleAnswers(ArrayList<Question> questions) {
        for (Question question : questions)
            question.shufflePossibleAnswers();
    }

    public static Quiz createNew(AssetManager assetManager, String fileName) throws JSONException, IOException {
        String jsonString = readQuestionsAsJsonFromAsset(assetManager, fileName);
        return createNew(jsonString);
    }

    public static Quiz restoreFrom(QuizState quizState){
        return new Quiz(quizState);
    }

    private static String readQuestionsAsJsonFromAsset(AssetManager assetManager, String fileName) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            StringBuilder sb = readFully(reader);
            return sb.toString();
        } finally {
            closeBufferedReader(reader);
        }
    }

    private static void closeBufferedReader(BufferedReader reader) {
        if (reader != null)
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @NonNull
    private static StringBuilder readFully(BufferedReader reader) throws IOException {
        char[] buffer = new char[1024];
        int read;
        StringBuilder sb = new StringBuilder();
        while ((read = reader.read(buffer, 0, buffer.length)) != -1) {
            sb.append(buffer, 0, read);
        }
        return sb;
    }

    private Question currentQuestion;
    private ArrayList<Question> questions;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private int selectedRadioButtonId = -1;
    private String subtitleFormatString = DEFAULT_SUBTITLE_FORMAT;
    private QuizView quizView;

    public Quiz(ArrayList<Question> questions){
        this.questions = questions;
    }

    public int getAmountOfQuestions(){
        return questions.size();
    }

    private Quiz(QuizState quizState){
        this.questions = new ArrayList<>(quizState.getQuestions());
        this.currentQuestionIndex = quizState.getCurrentQuestionIndex();
        this.selectedRadioButtonId = quizState.getSelectedRadioButtonId();
        this.correctAnswers = quizState.getCorrectAnswers();
        this.wrongAnswers = quizState.getWrongAnswers();
    }



    public void setQuizView(QuizView quizView) {
        this.quizView = quizView;
    }

    public void setSubtitleFormatString(String subtitleFormatString) {
        this.subtitleFormatString = subtitleFormatString;
    }

    public QuizState saveInstanceState() {

        QuizState quizState = new QuizState();

        quizState.setQuestions(questions);
        quizState.setSelectedRadioButtonId(selectedRadioButtonId);
        quizState.setCurrentQuestionIndex(currentQuestionIndex);
        quizState.setCorrectAnswers(correctAnswers);
        quizState.setWrongAnswers(wrongAnswers);

        return quizState;
    }

    public void render(){
        selectQuestion();
        if (isRadioButtonChecked())
            quizView.checkRadioButton(selectedRadioButtonId);
        quizView.setAnswerButtonEnabled(isRadioButtonChecked());
    }

    private boolean isRadioButtonChecked() {
        return selectedRadioButtonId != -1;
    }

    private void selectNextQuestion(){
        currentQuestionIndex++;
        selectQuestion();
    }

    private void selectQuestion() {
        currentQuestion = questions.get(currentQuestionIndex);
        for (int index = 0; index < AMOUNT_OF_POSSIBLE_ANSWERS; index++) {
            quizView.setRadioButtonText(index, currentQuestion.getPossibleAnswer(index));
        }
        quizView.setQuestion(currentQuestion.getQuestionText());
        setSubtitle();
    }

    private void setSubtitle() {
        int currentQuestionPosition = currentQuestionIndex + 1;
        int maxQuestionsAvailable = questions.size();
        quizView.setActionBarSubtitle(String.format(subtitleFormatString, currentQuestionPosition, maxQuestionsAvailable));
    }

    public void setCheckedRadioButtonId(int checkedRadioButtonId) {
        this.selectedRadioButtonId = checkedRadioButtonId;
        quizView.setAnswerButtonEnabled(true);
    }

    public void submitAnswer() {
        final int index = quizView.getIndexInRadioGroupFor(selectedRadioButtonId);
        String answer = getSelectedAnswer(index);
        updateAnswersMade(answer);
        if (!isNextQuestionAvailable()) {
            quizView.onQuizEnd(correctAnswers, wrongAnswers);
            reset();
        }else {
            selectNextQuestion();
            quizView.clearCheckedRadioButton();
            selectedRadioButtonId = -1;
            quizView.setAnswerButtonEnabled(false);
        }
    }

    private void updateAnswersMade(String answer) {
        if (isCorrectAnswer(answer))
            correctAnswers++;
        else
            wrongAnswers++;
    }

    private boolean isCorrectAnswer(String answer) {
        return answer.equals(currentQuestion.getCorrectAnswer());
    }

    private void reset() {
        shuffleQuestions(this.questions);
        shufflePossibleAnswers(this.questions);
        correctAnswers = 0;
        wrongAnswers = 0;
        currentQuestionIndex = 0;
        selectedRadioButtonId = -1;
        quizView.clearCheckedRadioButton();
        quizView.setAnswerButtonEnabled(false);
    }

    private String getSelectedAnswer(int index) {
        return currentQuestion.getPossibleAnswer(index);
    }

    private boolean isNextQuestionAvailable() {
        return currentQuestionIndex < (questions.size() - 1);
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public int getSelectedRadioButtonId() {
        return selectedRadioButtonId;
    }

}
