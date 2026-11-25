package org.mage.test.cards.single.woe;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 * Extraordinary Journey {X}{X}{U}{U}
 * Enchantment
 * When Extraordinary Journey enters the battlefield, exile up to X target creatures. For each of
 * those cards, its owner may play it for as long as it remains exiled.
 * <p>
 * "Up to X" with X = 0 is up to zero targets (CR 601.2c, 115.6): the ability goes on the stack with
 * no targets and its effect does nothing.
 */
public class ExtraordinaryJourneyTest extends CardTestPlayerBase {

    private static final String journey = "Extraordinary Journey";

    @Test
    public void testXZeroResolvesWithNoTargets() {
        setStrictChooseMode(true);

        addCard(Zone.BATTLEFIELD, playerA, "Island", 2);
        addCard(Zone.HAND, playerA, journey);
        addCard(Zone.BATTLEFIELD, playerB, "Grizzly Bears");

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, journey);
        setChoice(playerA, "X=0");

        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();

        assertPermanentCount(playerA, journey, 1);
        assertPermanentCount(playerB, "Grizzly Bears", 1);
        assertExileCount(playerB, 0);
    }

    @Test
    public void testXOneStillExilesATarget() {
        setStrictChooseMode(true);

        addCard(Zone.BATTLEFIELD, playerA, "Island", 4);
        addCard(Zone.HAND, playerA, journey);
        addCard(Zone.BATTLEFIELD, playerB, "Grizzly Bears");

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, journey);
        setChoice(playerA, "X=1");
        addTarget(playerA, "Grizzly Bears");

        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();

        assertPermanentCount(playerA, journey, 1);
        assertPermanentCount(playerB, "Grizzly Bears", 0);
        assertExileCount(playerB, "Grizzly Bears", 1);
    }
}
