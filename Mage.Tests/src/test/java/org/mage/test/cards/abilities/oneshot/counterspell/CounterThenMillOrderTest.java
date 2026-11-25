package org.mage.test.cards.abilities.oneshot.counterspell;

import mage.cards.Card;
import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Assert;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * "Counter target spell. Its controller mills three cards." The instructions
 * run in printed order (CR 608.2c): the countered spell is in the graveyard
 * first (CR 701.6a), then the three milled cards go on top of it. Two separate
 * events, so the owner has no CR 404.3 choice of order.
 * <p>
 * Thought Collapse and Didn't Say Please share the counter-then-mill effect;
 * both used to mill before they countered, which put the countered spell last.
 */
public class CounterThenMillOrderTest extends CardTestPlayerBase {

    private static final String BOLT = "Lightning Bolt";
    private static final String THOUGHT_COLLAPSE = "Thought Collapse";
    private static final String DIDNT_SAY_PLEASE = "Didn't Say Please";

    private List<String> graveyardNames() {
        return currentGame.getPlayer(playerA.getId()).getGraveyard()
                .getCards(currentGame)
                .stream()
                .map(Card::getName)
                .collect(Collectors.toList());
    }

    private void counterThenMill(String counterspell) {
        skipInitShuffling();
        removeAllCardsFromLibrary(playerA);

        addCard(Zone.BATTLEFIELD, playerA, "Mountain");
        addCard(Zone.HAND, playerA, BOLT);
        addCard(Zone.BATTLEFIELD, playerB, "Island", 3);
        addCard(Zone.HAND, playerB, counterspell);
        // addCard(Zone.LIBRARY) puts the last added card on top
        addCard(Zone.LIBRARY, playerA, "Plains");
        addCard(Zone.LIBRARY, playerA, "Island");
        addCard(Zone.LIBRARY, playerA, "Plains");

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, BOLT, playerB);
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerB, counterspell, BOLT);

        setStrictChooseMode(true);
        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();

        Assert.assertEquals("the countered spell is in the graveyard before the three milled cards",
                Arrays.asList(BOLT, "Plains", "Island", "Plains"), graveyardNames());
        assertGraveyardCount(playerB, counterspell, 1);
    }

    @Test
    public void testThoughtCollapseCountersBeforeMilling() {
        counterThenMill(THOUGHT_COLLAPSE);
    }

    @Test
    public void testDidntSayPleaseCountersBeforeMilling() {
        counterThenMill(DIDNT_SAY_PLEASE);
    }
}
