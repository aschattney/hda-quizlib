package hochschuledarmstadt.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Andreas Schattney on 18.03.2016.
 */
public abstract class QuizActivity extends AppCompatActivity implements QuizView {

    private static final String TAG = QuizActivity.class.getName();
    protected Quiz quiz;

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

    private Quiz buildQuiz(Bundle savedInstanceState) throws IOException, JSONException {
        if (savedInstanceState == null) {
            return createQuiz();
        } else {
            return restoreQuiz(savedInstanceState);
        }
    }

    private Quiz createQuiz() throws IOException, JSONException {
        return Quiz.createNew(getAssets(), getString(R.string.questions_filename));
    }

    private Quiz restoreQuiz(Bundle savedInstanceState) {
        QuizState quizState = savedInstanceState.getParcelable(Quiz.SAVED_STATE_KEY);
        return Quiz.restoreFrom(quizState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Quiz.SAVED_STATE_KEY, quiz.saveInstanceState());
    }

}
