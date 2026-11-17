package org.mage.test.cards.single.spm;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 * Molten Man, Inferno Incarnate
 * {2}{R}
 * Legendary Creature - Elemental Villain
 * When this creature enters, search your library for a basic Mountain card and put it onto the battlefield tapped. Then shuffle.
 * This creature gets +1/+1 for each Mountain you control.
 * When this creature leaves the battlefield, sacrifice a land.
 */
public class MoltenManInfernoIncarnateTest extends CardTestPlayerBase {

    private static final String moltenMan = "Molten Man, Inferno Incarnate";

    /**
     * The leaves-the-battlefield trigger has no target, so its controller (not a
     * missing target player) sacrifices the land. The creature is bounced here
     * rather than killed, so the trigger is the only land loss.
     */
    @Test
    public void testLeavesBattlefieldSacrificesALand() {
        addCard(Zone.BATTLEFIELD, playerA, moltenMan);
        addCard(Zone.BATTLEFIELD, playerA, "Mountain", 3);
        addCard(Zone.BATTLEFIELD, playerA, "Forest", 1);
        addCard(Zone.BATTLEFIELD, playerB, "Island", 1);
        addCard(Zone.HAND, playerB, "Unsummon", 1);

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerB, "Unsummon", moltenMan);
        setChoice(playerA, "Forest");

        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        setStrictChooseMode(true);
        execute();

        assertHandCount(playerA, moltenMan, 1);
        assertGraveyardCount(playerA, "Forest", 1);
        assertPermanentCount(playerA, "Mountain", 3);
    }
}
