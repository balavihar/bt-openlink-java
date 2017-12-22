package com.bt.openlink.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;

public class RequestActionResultBuilderTest {

    private static class Builder extends RequestActionResultBuilder<Builder, String, CoreFixtures.typeEnum> {
        protected Builder() {
            super(CoreFixtures.typeEnum.class);
        }
    }

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private Builder builder;

    @Before
    public void setUp() {

        builder = new Builder();

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
    }

    @Test
    public void willValidateAPopulatedBuilder() {

        final List<String> errors = new ArrayList<>();
        builder.addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getCalls(), contains(CoreFixtures.CALL_INCOMING_ORIGINATED));
    }

    @Test
    public void willAddMultipleCalls() {

        final List<String> errors = new ArrayList<>();
        builder.addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .addCall(CoreFixtures.CALL_INCOMING_ORIGINATED)
                .validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getCalls(), contains(CoreFixtures.CALL_INCOMING_ORIGINATED, CoreFixtures.CALL_INCOMING_ORIGINATED));
    }

    @Test
    public void willValidateTheCallIsSet() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The make-call result has no calls");

        builder.validate();
    }

    @Test
    public void willCheckThatTheCallIsSet() {

        final List<String> errors = new ArrayList<>();

        builder.validate(errors);

        assertThat(errors, contains("Invalid make-call result stanza; missing or invalid calls"));
        assertThat(builder.getCalls(), is(empty()));
    }


}