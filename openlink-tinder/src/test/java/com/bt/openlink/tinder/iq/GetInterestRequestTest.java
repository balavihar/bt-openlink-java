package com.bt.openlink.tinder.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xmpp.packet.IQ;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.tinder.Fixtures;

@SuppressWarnings("ConstantConditions")
public class GetInterestRequestTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private static final String GET_INTEREST_REQUEST = "<iq type=\"set\" id=\"" + CoreFixtures.STANZA_ID + "\" to=\"" + Fixtures.TO_JID + "\" from=\"" + Fixtures.FROM_JID + "\">\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-interest\" action=\"execute\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"input\">\n" +
            "      <in>\n" +
            "        <interest>" + CoreFixtures.INTEREST_ID + "</interest>\n" +
            "      </in>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    private static final String GET_INTEREST_REQUEST_WITH_BAD_VALUES = "<iq type='get'>\n" +
            "  <command xmlns=\"http://jabber.org/protocol/commands\" action=\"execute\" node=\"http://xmpp.org/protocol/openlink:01:00:00#get-interest\">\n" +
            "    <iodata xmlns=\"urn:xmpp:tmp:io-data\" type=\"input\">\n" +
            "      <in/>\n" +
            "    </iodata>\n" +
            "  </command>\n" +
            "</iq>\n";

    @Test
    public void canCreateAStanza() throws Exception {

        final GetInterestRequest request = GetInterestRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .build();

        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
    }

    @Test
    public void willGenerateAnXmppStanza() throws Exception {

        final GetInterestRequest request = GetInterestRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .build();

        assertThat(request.toXML(), isIdenticalTo(GET_INTEREST_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willGenerateAnXmppStanzaWithARandomId() throws Exception {

        final GetInterestsRequest request = GetInterestsRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        assertThat(request.getID(), is(not(nullValue())));
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

        final GetInterestRequest request = (GetInterestRequest) OpenlinkIQParser.parse(Fixtures.iqFrom(GET_INTEREST_REQUEST));
        assertThat(request.getID(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(request.getParseErrors(), is(empty()));
    }

    @Test
    public void willReturnParsingErrors() throws Exception {

        final GetInterestRequest request = GetInterestRequest.from(Fixtures.iqFrom(GET_INTEREST_REQUEST_WITH_BAD_VALUES));

        assertThat(request.getParseErrors(), contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid get-interest request stanza; missing 'interest'"));
    }

    @Test
    public void willGenerateAStanzaEvenWithParsingErrors() throws Exception {

        final GetInterestRequest request = GetInterestRequest.from(Fixtures.iqFrom(GET_INTEREST_REQUEST_WITH_BAD_VALUES));

        assertThat(request.toXML(), isIdenticalTo(Fixtures.iqFrom(GET_INTEREST_REQUEST_WITH_BAD_VALUES).toXML()).ignoreWhitespace());

    }

}