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

import com.bt.openlink.Fixtures;

@SuppressWarnings("ConstantConditions")
public class GetFeaturesResultBuilderTest {

    private static class Builder extends GetFeaturesResultBuilder<Builder, String, Fixtures.typeEnum> {
        protected Builder() {
            super(Fixtures.typeEnum.class);
        }
    }

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private Builder builder;

    @Before
    public void setUp() throws Exception {

        builder = new Builder();

        builder.setTo("to");
        builder.setFrom("from");
        builder.setId("id");
    }

    @Test
    public void willValidateAPopulatedBuilder() throws Exception {

        final List<String> errors = new ArrayList<>();
        builder.setProfileId(Fixtures.PROFILE_ID)
                .addFeature(Fixtures.FEATURE);

        builder.validate();
        builder.validate(errors);

        assertThat(errors, is(empty()));
        assertThat(builder.getProfileId().get(), is(Fixtures.PROFILE_ID));
        assertThat(builder.getFeatures(), contains(Fixtures.FEATURE));
    }

    @Test
    public void willValidateProfileIsSet() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The get-features result profile has not been set");

        builder.validate();
    }

    @Test
    public void willValidateProfileUniqueness() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Each feature id must be unique - test-feature-id appears more than once");

        builder.setProfileId(Fixtures.PROFILE_ID)
                .addFeature(Fixtures.FEATURE)
                .addFeature(Fixtures.FEATURE);

        builder.validate();
    }

    @Test
    public void willCheckProfileAndUniqueness() throws Exception {

        final List<String> errors = new ArrayList<>();

        builder.addFeature(Fixtures.FEATURE);
        builder.addFeature(Fixtures.FEATURE);

        builder.validate(errors);

        assertThat(errors, contains(
                "Invalid get-features result stanza; missing profile",
                "Invalid get-features result stanza; each feature id must be unique - test-feature-id appears more than once"
        ));
    }
}