package org.mage.test.cards.single.trk;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 * Grand Nagus Zek (TRK): "Whenever another creature dies, create a Treasure
 * token. This ability triggers only once each turn."
 *
 * CR 603.10a: leaves-the-battlefield abilities look back in time, so Zek
 * triggers for creatures that die at the same time as it does. CR 603.2: the
 * once-each-turn limit counts the triggerings of this one ability, and Zek
 * dying does not make it a different ability.
 */
public class GrandNagusZekTest extends CardTestPlayerBase {

    @Test
    public void testDiesWithAnotherCreatureAfterTriggeringDoesNotTriggerAgain() {
        setStrictChooseMode(true);
        addCard(Zone.BATTLEFIELD, playerA, "Grand Nagus Zek"); // 1/1, +1/+1 per Treasure
        addCard(Zone.BATTLEFIELD, playerA, "Grizzly Bears");
        addCard(Zone.BATTLEFIELD, playerA, "Runeclaw Bear");
        addCard(Zone.BATTLEFIELD, playerB, "Mountain", 4);
        addCard(Zone.HAND, playerB, "Lightning Bolt");
        addCard(Zone.HAND, playerB, "Fiery Cannonade");

        // the Bears die: Zek triggers (first time this turn), one Treasure, Zek is 2/2
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerB, "Lightning Bolt", "Grizzly Bears");
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN);
        // 2 damage to each non-Pirate creature: Zek (2/2) and Runeclaw Bear die together
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerB, "Fiery Cannonade");

        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();

        assertGraveyardCount(playerA, "Grand Nagus Zek", 1);
        assertGraveyardCount(playerA, "Runeclaw Bear", 1);
        // the ability triggered only once this turn
        assertPermanentCount(playerA, "Treasure Token", 1);
    }
}
