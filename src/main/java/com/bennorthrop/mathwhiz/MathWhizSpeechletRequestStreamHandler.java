package com.bennorthrop.mathwhiz;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public final class MathWhizSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {

    public MathWhizSpeechletRequestStreamHandler() {
        super(new MathWhizSpeechlet(),
                Stream.of("amzn1.ask.skill.1d12f956-242c-435e-9a4f-437b53838f6f").collect(Collectors.toSet()));
    }
}
