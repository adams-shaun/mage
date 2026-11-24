package org.mage.test.cards.single.blb;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 * Valiant (BLB), Nettle Guard: "Whenever Nettle Guard becomes the target of a
 * spell or ability you control for the first time each turn, it gets +0/+2
 * until end of turn."
 *
 * The count is of spells and abilities its controller controls: a targeting by
 * the opponent's spell earlier in the turn is not a "spell or ability you
 * control" and does not use up the first time (Bloomburrow release notes,
 * Valiant rulings). Forge counts per targeting player (Card.isValiant).
 */
public class ValiantFirstTimeTest extends CardTestPlayerBase {

    // High Stride: target creature gets +1/+3 and gains reach until end of turn. Untap it.
    @Test
    public void testOpponentTargetingFirstDoesNotUseUpValiant() {
        setStrictChooseMode(true);
        addCard(Zone.BATTLEFIELD, playerA, "Forest", 1);
        addCard(Zone.BATTLEFIELD, playerB, "Forest", 1);
        addCard(Zone.BATTLEFIELD, playerA, "Nettle Guard"); // 3/1
        addCard(Zone.HAND, playerA, "High Stride");
        addCard(Zone.HAND, playerB, "High Stride");

        castSpell(1, PhaseStep.UPKEEP, playerB, "High Stride", "Nettle Guard");
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "High Stride", "Nettle Guard");

        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        setStrictChooseMode(true);
        execute();

        // 3/1 +1/+3 +1/+3, valiant +0/+2 for playerA's own targeting only
        assertPowerToughness(playerA, "Nettle Guard", 5, 9);
    }

    @Test
    public void testSecondOwnTargetingDoesNotTriggerValiant() {
        setStrictChooseMode(true);
        addCard(Zone.BATTLEFIELD, playerA, "Forest", 2);
        addCard(Zone.BATTLEFIELD, playerA, "Nettle Guard"); // 3/1
        addCard(Zone.HAND, playerA, "High Stride", 2);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "High Stride", "Nettle Guard");
        waitStackResolved(1, PhaseStep.PRECOMBAT_MAIN);
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "High Stride", "Nettle Guard");

        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();

        // 3/1 +1/+3 +1/+3, valiant +0/+2 once
        assertPowerToughness(playerA, "Nettle Guard", 5, 9);
    }
}
