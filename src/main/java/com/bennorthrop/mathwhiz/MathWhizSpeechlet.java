package com.bennorthrop.mathwhiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

/**
 * MathWhizSpeechlet.java
 * 
 * Simple speechlet that demonstrates a few features of Alexa, built for presentation delivered to Pittsburgh Tech Fest.
 * 
 * @author Ben Northrop
 */
public class MathWhizSpeechlet implements Speechlet {

    private final static Random RANDOM = new Random();

    private final static String WELCOME = "Welcome to Math Whiz. ";

    private final static String CARD_TITLE = "Math Whiz";

    private final static String REPROMPT_PREFIX = "Sorry, I didn't get that. ";

    private final static String INSTRUCTIONS = "You can say 'tell me a fact' or 'what is 1 plus 1'";

    private final static List<String> FACTS = new ArrayList<>();

    private final static BiFunction<Integer, Integer, Integer> ADDITION = (num1, num2) -> num1 + num2;

    private final static BiFunction<Integer, Integer, Integer> SUBTRACTION = (num1, num2) -> num1 - num2;

    private final static BiFunction<Integer, Integer, Integer> MULTIPLICATION = (num1, num2) -> num1 * num2;

    private final static BiFunction<Integer, Integer, Integer> DIVISION = (num1, num2) -> num1 / num2;

    private final static Map<String, BiFunction<Integer, Integer, Integer>> OPERATIONS = new HashMap<>();

    static {
        FACTS.add("A negative number times another negative number is a postive");
        FACTS.add("Georg Cantor proved that there are many infinities, some vastly larger than others.");
        FACTS.add(
                "A prime number is a whole number greater than 1, whose only two whole-number factors are 1 and itself. ");

        OPERATIONS.put("plus", ADDITION);
        OPERATIONS.put("minus", SUBTRACTION);
        OPERATIONS.put("times", MULTIPLICATION);
        OPERATIONS.put("divided by", DIVISION);
    }

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
        return initAskResponse(WELCOME + INSTRUCTIONS, INSTRUCTIONS);
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("GetFactIntent".equals(intentName)) {
            return initTellResponse(FACTS.get(RANDOM.nextInt(3)), false);
        }

        if ("DoMathIntent".equals(intentName)) {
            Integer firstNumber = Integer.parseInt(intent.getSlot("FirstNumber").getValue());
            String operation = intent.getSlot("Operator").getValue();
            Integer secondNumber = Integer.parseInt(intent.getSlot("SecondNumber").getValue());

            Integer answer = OPERATIONS.get(operation).apply(firstNumber, secondNumber);
            return initTellResponse("The answer is " + answer, false);
        }

        if ("AMAZON.HelpIntent".equals(intentName)) {
            return initAskResponse(WELCOME + INSTRUCTIONS, INSTRUCTIONS);
        }

        throw new SpeechletException("Invalid Intent");

    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {

    }

    /**
     * Creates a simple ask response to prompt the user to take the next step (i.e. say something). Note: a Card is
     * something that is displayed in the app's visual user interface. The first OutputSpeech is what is told to the
     * user. The Reprompt is what is said if the user doesn't say anything (intelligible) back.
     */
    private SpeechletResponse initAskResponse(String speechText, String repromptText) {
        Card card = initSimpleCard(CARD_TITLE, speechText);
        OutputSpeech speech = initOutputSpeech(speechText);
        Reprompt reprompt = initReprompt(REPROMPT_PREFIX + repromptText);
        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    /**
     * Creates a simple tell response which is the end of the flow. Note: a Card is something that is displayed in the
     * app's visual user interface. The first OutputSpeech is what is told to the user. There is no reprompt, because we
     * don't expect the user to say anything back.
     */
    private SpeechletResponse initTellResponse(String speechText, boolean shouldEndSession) {
        Card card = initSimpleCard(CARD_TITLE, speechText);
        OutputSpeech speech = initOutputSpeech(speechText);
        SpeechletResponse response = SpeechletResponse.newTellResponse(speech, card);
        response.setShouldEndSession(shouldEndSession);
        return response;
    }

    /**
     * Convenience initializer.
     */
    private static Reprompt initReprompt(String speechText) {
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(initOutputSpeech(speechText));
        return reprompt;
    }

    /**
     * Convenience initializer.
     */
    private static SimpleCard initSimpleCard(String title, String content) {
        SimpleCard card = new SimpleCard();
        card.setTitle(title);
        card.setContent(content);
        return card;
    }

    /**
     * Convenience initializer.
     */
    private static OutputSpeech initOutputSpeech(String speechText) {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        return speech;
    }
}
