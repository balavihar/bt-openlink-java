package com.bt.openlink.tinder.message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.util.ArrayList;
import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.Message;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.PubSubMessageFixtures;
import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.ItemId;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class CallStatusMessageTest {

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.CALL_OUTGOING_CONFERENCED.getInterestId().get())
                .setItemId(PubSubMessageFixtures.ITEM_ID)
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(message.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(message.getTo(), is(Fixtures.TO_JID));
        assertThat(message.getFrom(), is(Fixtures.FROM_JID));
        assertThat(message.getPubSubNodeId().get(), is(PubSubMessageFixtures.NODE_ID));
        assertThat(message.getCallStatus().get(), is(CoreFixtures.CALL_STATUS));
    }

    @Test
    public void willGenerateAnXmppStanza() {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.CALL_OUTGOING_CONFERENCED.getInterestId().get())
                .setItemId(ItemId.from("test-item-id").get())
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .build();

        assertThat(message.toXML(), isIdenticalTo(PubSubMessageFixtures.CALL_STATUS_MESSAGE).ignoreWhitespace());
    }

    @Test
    public void willCreateAStanzaWithoutMandatoryFields() {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .build(new ArrayList<>());

        assertThat(message.toXML(), isIdenticalTo(PubSubMessageFixtures.PUBSUB_MESSAGE_WITH_NO_FIELDS).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() {

        final Message stanza = Fixtures.messageFrom(PubSubMessageFixtures.CALL_STATUS_MESSAGE);

        final CallStatusMessage message = OpenlinkMessageParser.parse(stanza);

        assertThat(message.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(message.getTo(), is(Fixtures.TO_JID));
        assertThat(message.getFrom(), is(Fixtures.FROM_JID));
        assertThat(message.getPubSubNodeId().get(), is(PubSubMessageFixtures.NODE_ID));
        assertThat(message.getItemId().get(), is(PubSubMessageFixtures.ITEM_ID));
        assertThat(EqualsBuilder.reflectionEquals(CoreFixtures.CALL_STATUS, message.getCallStatus().get(), false, null, true), is(true));
        assertThat(message.getParseErrors().size(), is(0));
    }

    @Test
    public void willReturnOriginalMessageForADeviceStatusEvent() {

        final Message stanza = Fixtures.messageFrom(PubSubMessageFixtures.ARBITRARY_PUBSUB_MESSAGE);

        assertThat(OpenlinkMessageParser.parse(stanza), is(sameInstance(stanza)));
    }

    @Test
    public void willBuildAMessageWithADelay() {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(CoreFixtures.CALL_OUTGOING_CONFERENCED.getInterestId().get())
                .setItemId(ItemId.from("test-item-id").get())
                .setCallStatus(CoreFixtures.CALL_STATUS)
                .setDelay(PubSubMessageFixtures.DELAYED_FROM)
                .build();

        assertThat(message.toXML(), isIdenticalTo(PubSubMessageFixtures.CALL_STATUS_MESSAGE_DELAYED).ignoreWhitespace());
    }

    @Test
    public void willParseAMessageWithADelay() {
        final Message stanza = Fixtures.messageFrom(PubSubMessageFixtures.CALL_STATUS_MESSAGE_DELAYED);

        final CallStatusMessage message = OpenlinkMessageParser.parse(stanza);
        assertThat(message.getDelay().get(), is(PubSubMessageFixtures.DELAYED_FROM));
    }

    @Test
    public void willParseAMessageWithBadFields() {
        final Message stanza = Fixtures.messageFrom(PubSubMessageFixtures.CALL_STATUS_MESSAGE_DELAYED_WITH_BAD_TIMESTAMP);

        final CallStatusMessage message = OpenlinkMessageParser.parse(stanza);
        assertThat(message.getDelay(), is(Optional.empty()));
        assertThat(
                message.getParseErrors(),
                contains("Invalid call status; invalid timestamp 'not-a-timestamp'; format should be compliant with XEP-0082"));
    }

    @Test
    public void theLegacyTimestampShouldMatchTheStartTime() {
        final Message stanza = Fixtures.messageFrom(PubSubMessageFixtures.CALL_STATUS_MESSAGE_WITH_MISMATCHED_TIMESTAMPS);

        final CallStatusMessage message = OpenlinkMessageParser.parse(stanza);

        assertThat(message.getParseErrors(), contains("Invalid participant; the legacy timestamp field does not match the start time field", "Invalid participant; the legacy timestamp field does not match the start time field"));
    }
}