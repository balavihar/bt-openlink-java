package com.bt.openlink.smack.iq;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bt.openlink.CoreFixtures;
import com.bt.openlink.GetInterestFixtures;
import com.bt.openlink.OpenlinkXmppNamespace;
import com.bt.openlink.smack.Fixtures;

@SuppressWarnings("ConstantConditions")
public class GetInterestRequestTest {

	@Rule
    public final ExpectedException expectedException = ExpectedException.none();
	
    @BeforeClass
    public static void setUpClass() throws Exception {
        ProviderManager.addIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri(), new OpenlinkIQProvider());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        ProviderManager.removeIQProvider("command", OpenlinkXmppNamespace.XMPP_COMMANDS.uri());
    }

    @Test
    public void canCreateAStanza() throws Exception {

        final GetInterestRequest request = GetInterestRequest.Builder.start()
                .setId(CoreFixtures.STANZA_ID)
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setInterestId(CoreFixtures.INTEREST_ID)
                .build();

        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
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

        assertThat(request.toXML().toString(), isIdenticalTo(GetInterestFixtures.GET_INTEREST_REQUEST).ignoreWhitespace());
    }

    @Test
    public void willGenerateAnXmppStanzaWithARandomId() throws Exception {

        final GetInterestsRequest request = GetInterestsRequest.Builder.start()
                .setTo(Fixtures.TO_JID)
                .setFrom(Fixtures.FROM_JID)
                .setProfileId(CoreFixtures.PROFILE_ID)
                .build();

        assertThat(request.getStanzaId(), is(not(nullValue())));
    }

    @Test
    public void willParseAnXmppStanza() throws Exception {

    	final GetInterestRequest request = PacketParserUtils.parseStanza(GetInterestFixtures.GET_INTEREST_REQUEST);
        assertThat(request.getStanzaId(), is(CoreFixtures.STANZA_ID));
        assertThat(request.getTo(), is(Fixtures.TO_JID));
        assertThat(request.getFrom(), is(Fixtures.FROM_JID));
        assertThat(request.getType(), is(IQ.Type.set));
        assertThat(request.getInterestId().get(), is(CoreFixtures.INTEREST_ID));
        assertThat(request.getParseErrors(), is(empty()));
    }

/*    @Test
    public void willReturnParsingErrors() throws Exception {

        final GetInterestRequest request = GetInterestRequest.from(Fixtures.iqFrom(GetInterestFixtures.GET_INTEREST_REQUEST_WITH_BAD_VALUES));

        assertThat(request.getParseErrors(), contains(
                "Invalid stanza; missing 'to' attribute is mandatory",
                "Invalid stanza; missing 'from' attribute is mandatory",
                "Invalid stanza; missing 'id' attribute is mandatory",
                "Invalid stanza; missing or incorrect 'type' attribute",
                "Invalid get-interest request stanza; missing 'interest'"));
    }*/

    @Test
    public void willGenerateAStanzaEvenWithParsingErrors() throws Exception {

        final GetInterestRequest request = PacketParserUtils.parseStanza(GetInterestFixtures.GET_INTEREST_REQUEST_WITH_BAD_VALUES);
        assertThat(request.toXML().toString(), isIdenticalTo(GetInterestFixtures.GET_INTEREST_REQUEST_WITH_BAD_VALUES).ignoreWhitespace());

    }

}
