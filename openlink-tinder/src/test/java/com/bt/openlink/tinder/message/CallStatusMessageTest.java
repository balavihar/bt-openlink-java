package com.bt.openlink.tinder.message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.Message;

import com.bt.openlink.tinder.Fixtures;
import com.bt.openlink.type.Call;
import com.bt.openlink.type.CallDirection;
import com.bt.openlink.type.CallState;
import com.bt.openlink.type.PubSubNodeId;

@SuppressWarnings("ConstantConditions")
public class CallStatusMessageTest {
    private static final String CALL_STATUS_MESSAGE = "<message from='" + Fixtures.FROM_JID + "' to='" + Fixtures.TO_JID + "' id='" + Fixtures.STANZA_ID + "'>\n" +
            "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
            "    <items node='" + Fixtures.NODE_ID + "'>\n" +
            "      <item id='sip:6001@uta.bt.com-DirectDial-1sales1@btsm11'>\n" +
            "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status' busy='true'>\n" +
            "          <call>\n" +
            "            <id>" + Fixtures.CALL_ID + "</id>\n" +
            "            <site id='42' type='BTSESSIONMANAGER'>itrader-dev-sm-5</site>\n" +
            "            <profile>" + Fixtures.PROFILE_ID + "</profile>\n" +
            "            <eventTimestamps>\n" +
            "              <switch>1470739100996</switch>\n" +
            "              <received>1470738955156</received>\n" +
            "              <published>1470738955156</published>\n" +
            "            </eventTimestamps>\n" +
            "            <interest>" + Fixtures.INTEREST_ID + "</interest>\n" +
            "            <changed>State</changed>\n" +
            "            <state>CallOriginated</state>\n" +
            "            <direction>Outgoing</direction>\n" +
            "            <caller>\n" +
            "              <number>6001</number>\n" +
            "              <name>6001/1</name>\n" +
            "            </caller>\n" +
            "            <called>\n" +
            "              <number></number>\n" +
            "              <name></name>\n" +
            "            </called>\n" +
            "            <starttime>2017-10-09T08:07:00.000Z</starttime>\n" +
            "            <duration>0</duration>\n" +
            "            <actions/>\n" +
            "            <features>\n" +
            "              <feature id='hs_1' type='HANDSET' label='Handset 1'>false</feature>\n" +
            "              <feature id='hs_2' type='HANDSET' label='Handset 2'>false</feature>\n" +
            "              <feature id='priv_1' type='PRIVACY' label='Privacy'>false</feature>\n" +
            "              <feature id='NetrixHiTouch_sales1' type='DEVICEKEYS' label='NetrixHiTouch'>\n" +
            "                <devicekeys xmlns='http://xmpp.org/protocol/openlink:01:00:00/features#device-keys'>\n" +
            "                  <key>key_1:1:1</key>\n" +
            "                </devicekeys>\n" +
            "              </feature>\n" +
            "            </features>\n" +
            "          </call>\n" +
            "        </callstatus>\n" +
            "      </item>\n" +
            "    </items>\n" +
            "  </event>\n" +
            "</message>";

    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canCreateAStanza() throws Exception {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(Fixtures.CALL.getInterestId().get())
                .addCall(Fixtures.CALL)
                .build();

        assertThat(message.getID(), is(Fixtures.STANZA_ID));
        assertThat(message.getTo(), is(Fixtures.TO_JID));
        assertThat(message.getFrom(), is(Fixtures.FROM_JID));
        assertThat(message.getPubSubNodeId().get(), is(Fixtures.NODE_ID));
        final List<Call> calls = message.getCalls();
        final Call theOnlyCall = calls.get(0);
        assertThat(theOnlyCall, is(sameInstance(Fixtures.CALL)));
        assertThat(calls.size(), is(1));
    }

    @Test
    public void canCreateAStanzaWithANullId() throws Exception {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(Fixtures.CALL.getInterestId().get())
                .addCalls(Collections.singletonList(Fixtures.CALL))
                .build();

        assertThat(message.getID(), is(nullValue()));
    }

    @Test
    public void cannotCreateAStanzaWithoutAToField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'to' has not been set");
        CallStatusMessage.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(Fixtures.CALL.getInterestId().get())
                .addCall(Fixtures.CALL)
                .build();
    }

    @Test
    public void cannotCreateAStanzaWithoutAnPubSubNodeIdField() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The stanza 'pubSubNodeId' has not been set");
        CallStatusMessage.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .addCall(Fixtures.CALL)
                .build();
    }

    @Test
    public void cannotCreateAMessageWithACallOnADifferentNode() throws Exception {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("The call with id 'test-call-id' is not on this pubsub node");
        CallStatusMessage.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(PubSubNodeId.from("not-" + Fixtures.INTEREST_ID).get())
                .addCall(Fixtures.CALL)
                .build();
    }

    @Test
    public void canCreateAStanzaWithMissingFields() throws Exception {

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .build(new ArrayList<>());

        assertThat(message.getID(), is(nullValue()));
        assertThat(message.getTo(), is(nullValue()));
        assertThat(message.getFrom(), is(nullValue()));
        assertThat(message.getCalls(), is(empty()));
        assertThat(message.getDelay(), is(Optional.empty()));
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        //    TODO: (Greg 2017-09-27) Replace this with CALL_STATUS_EVENT when fully implemented
        final String expectedXML = "<message from='" + Fixtures.FROM_JID + "' to='" + Fixtures.TO_JID + "' id='" + Fixtures.STANZA_ID + "'>\n" +
                "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                "    <items node='" + Fixtures.INTEREST_ID + "'>\n" +
                "      <item>\n" +
                "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                "          <call>\n" +
                "            <id>" + Fixtures.CALL_ID + "</id>\n" +
                "            <site default=\"true\" id=\"42\" type=\"BTSM\">test-site-name</site>" +
                "            <profile>" + Fixtures.PROFILE_ID + "</profile>\n" +
                "            <interest>" + Fixtures.INTEREST_ID + "</interest>\n" +
                "            <state>CallOriginated</state>\n" +
                "            <direction>Incoming</direction>\n" +
                "            <starttime>2017-10-09T08:07:00.000Z</starttime>\n" +
                "            <duration>60000</duration>\n" +
                "            <actions>\n" +
                "              <AnswerCall/>\n" +
                "            </actions>\n" +
                "          </call>\n" +
                "        </callstatus>\n" +
                "      </item>\n" +
                "    </items>\n" +
                "  </event>\n" +
                "</message>";

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(Fixtures.CALL.getInterestId().get())
                .addCall(Fixtures.CALL)
                .build();

        //        System.out.println(stanza);
        assertThat(message.toXML(), isIdenticalTo(expectedXML).ignoreWhitespace());
    }

    @Test
    public void willCreateAStanzaWithoutMandatoryFields() throws Exception {

        final String expectedXML = "<message>\n" +
                "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                "    <items>\n" +
                "      <item>\n" +
                "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                "        </callstatus>\n" +
                "      </item>\n" +
                "    </items>\n" +
                "  </event>\n" +
                "</message>";

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .build(new ArrayList<>());

        //System.out.println(generatedStanza);
        assertThat(message.toXML(), isIdenticalTo(expectedXML).ignoreWhitespace());
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final Message stanza = Fixtures.messageFrom(CALL_STATUS_MESSAGE);

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);

        assertThat(message.getID(), is(Fixtures.STANZA_ID));
        assertThat(message.getTo(), is(Fixtures.TO_JID));
        assertThat(message.getFrom(), is(Fixtures.FROM_JID));
        assertThat(message.getPubSubNodeId().get(), is(Fixtures.NODE_ID));
        final List<Call> calls = message.getCalls();
        final Call theOnlyCall = calls.get(0);
        assertThat(theOnlyCall.getId().get(), is(Fixtures.CALL_ID));
        assertThat(theOnlyCall.getProfileId().get(), is(Fixtures.PROFILE_ID));
        assertThat(theOnlyCall.getInterestId().get(), is(Fixtures.INTEREST_ID));
        assertThat(theOnlyCall.getState().get(), is(CallState.CALL_ORIGINATED));
        assertThat(theOnlyCall.getDirection().get(), is(CallDirection.OUTGOING));
        assertThat(theOnlyCall.getStartTime().get(), is(Fixtures.START_TIME));
        assertThat(theOnlyCall.getDuration().get(), is(Duration.ZERO));
        assertThat(calls.size(), is(1));
        assertThat(message.getParseErrors().size(), is(0));
    }

    @Test
    public void willReturnOriginalMessageForADeviceStatusEvent() throws Exception {

        final Message stanza = Fixtures
                .messageFrom(
                "<message from='pubsub.btp194094' to='ucwa.btp194094' id='Sma0SFtv'>\n" +
                        "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                        "    <items node='sip:6004@uta.bt.com-DirectDial-1sales1@btsm2'>\n" +
                        "      <item id='sip:6004@uta.bt.com-DirectDial-1sales1@btsm2'>\n" +
                        "        <devicestatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#device-status'>\n" +
                        "          <profile online='true'>/netrix/Cluster1|/uta/enterprises/bt/users/Sales1/denormalised-profiles/UCSales1/versions/72?build=70&amp;location=global.uk.Ipswich&amp;device=NetrixHiTouch</profile>\n" +
                        "          <interest id='sip:6004@uta.bt.com-DirectDial-1sales1@btsm2' online='true'/>\n" +
                        "        </devicestatus>\n" +
                        "      </item>\n" +
                        "    </items>\n" +
                        "  </event>\n" +
                        "</message>");

        assertThat(OpenlinkMessageParser.parse(stanza), is(sameInstance(stanza)));
    }

    @Test
    public void willBuildAMessageWithADelay() throws Exception {
        final String expectedXML = "<message from='" + Fixtures.FROM_JID + "' to='" + Fixtures.TO_JID + "' id='" + Fixtures.STANZA_ID + "'>\n" +
                "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                "    <items node='" + Fixtures.INTEREST_ID + "'>\n" +
                "      <item>\n" +
                "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                "          <call>\n" +
                "            <id>" + Fixtures.CALL_ID + "</id>\n" +
                "           <site default=\"true\" id=\"42\" type=\"BTSM\">test-site-name</site>" +
                "            <profile>" + Fixtures.PROFILE_ID + "</profile>\n" +
                "            <interest>" + Fixtures.INTEREST_ID + "</interest>\n" +
                "            <state>CallOriginated</state>\n" +
                "            <direction>Incoming</direction>\n" +
                "            <starttime>2017-10-09T08:07:00.000Z</starttime>\n" +
                "            <duration>60000</duration>\n" +
                "            <actions>\n" +
                "              <AnswerCall/>\n" +
                "            </actions>\n" +
                "          </call>\n" +
                "        </callstatus>\n" +
                "      </item>\n" +
                "    </items>\n" +
                "  </event>\n" +
                "  <delay xmlns='urn:xmpp:delay' stamp='2016-09-01T15:18:53.999Z'/>\n" +
                "</message>";

        final CallStatusMessage message = CallStatusMessage.Builder.start()
                .setID(Fixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setPubSubNodeId(Fixtures.CALL.getInterestId().get())
                .addCall(Fixtures.CALL)
                .setDelay(Instant.parse("2016-09-01T15:18:53.999Z"))
                .build();

        assertThat(message.toXML(), isIdenticalTo(expectedXML).ignoreWhitespace());
    }

    @Test
    public void willParseAMessageWithADelay() throws Exception {
        final Message stanza = Fixtures.messageFrom(
                "<message from='" + Fixtures.FROM_JID + "' to='" + Fixtures.TO_JID + "' id='" + Fixtures.STANZA_ID + "'>\n" +
                        "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                        "    <items node='" + Fixtures.INTEREST_ID + "'>\n" +
                        "      <item>\n" +
                        "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                        "          <call>\n" +
                        "            <id>" + Fixtures.CALL_ID + "</id>\n" +
                        "            <profile>" + Fixtures.PROFILE_ID + "</profile>\n" +
                        "            <interest>" + Fixtures.INTEREST_ID + "</interest>\n" +
                        "            <state>CallOriginated</state>\n" +
                        "            <direction>Incoming</direction>\n" +
                        "          </call>\n" +
                        "        </callstatus>\n" +
                        "      </item>\n" +
                        "    </items>\n" +
                        "  </event>\n" +
                        "  <delay xmlns='urn:xmpp:delay' stamp='2016-09-01T15:18:53.999Z'/>\n" +
                        "</message>");

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);
        assertThat(message.getDelay().get(), is(Instant.parse("2016-09-01T15:18:53.999Z")));
    }

    @Test
    public void willParseAMessageWithBadFields() throws Exception {
        final Message stanza = Fixtures.messageFrom(
                "<message from='" + Fixtures.FROM_JID + "' to='" + Fixtures.TO_JID + "' id='" + Fixtures.STANZA_ID + "'>\n" +
                        "  <event xmlns='http://jabber.org/protocol/pubsub#event'>\n" +
                        "    <items node='" + Fixtures.INTEREST_ID + "'>\n" +
                        "      <item>\n" +
                        "        <callstatus xmlns='http://xmpp.org/protocol/openlink:01:00:00#call-status'>\n" +
                        "          <call>\n" +
                        "            <id></id>\n" +
                        "            <profile></profile>\n" +
                        "            <interest></interest>\n" +
                        "            <state></state>\n" +
                        "            <direction></direction>\n" +
                        "            <starttime>yesterday</starttime>\n" +
                        "            <duration>a while</duration>\n" +
                        "          </call>\n" +
                        "        </callstatus>\n" +
                        "      </item>\n" +
                        "    </items>\n" +
                        "  </event>\n" +
                        "  <delay xmlns='urn:xmpp:delay' stamp='not-a-timestamp'/>\n" +
                        "</message>");

        final CallStatusMessage message = (CallStatusMessage) OpenlinkMessageParser.parse(stanza);
        assertThat(message.getDelay(), is(Optional.empty()));
        final List<String> parseErrors = message.getParseErrors();
        int i = 0;
        assertThat(parseErrors.get(i++), is("Invalid Call status message; missing 'id' field is mandatory"));
        assertThat(parseErrors.get(i++), is("Invalid Call status message; missing 'profile' field is mandatory"));
        assertThat(parseErrors.get(i++), is("Invalid Call status message; missing 'interest' field is mandatory"));
        assertThat(parseErrors.get(i++), is("Invalid Call status message; missing 'state' field is mandatory"));
        assertThat(parseErrors.get(i++), is("Invalid Call status message; missing 'direction' field is mandatory"));
        assertThat(parseErrors.get(i++), is("Invalid Call status message; Unable to parse starttime 'yesterday'"));
        assertThat(parseErrors.get(i++), is("Invalid Call status message; invalid duration 'a while'; please supply an integer"));
        assertThat(parseErrors.get(i++), is("Invalid Call status message; invalid timestamp 'not-a-timestamp'; format should be compliant with XEP-0082"));
        assertThat(parseErrors.size(),is(i));
    }
}