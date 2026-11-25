package org.mage.test.cards.single.msh;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 * Madame Web, Clairvoyant
 * You may look at the top card of your library any time.
 * You may cast Spider spells and noncreature spells from the top of your library.
 */
public class MadameWebClairvoyantTest extends CardTestPlayerBase {

    private static final String madame = "Madame Web, Clairvoyant";

    @Test
    public void test_LandOnTopIsNotPlayable() {
        // lands are played, not cast (CR 305.1, 601.1): the noncreature land on top can't be played
        removeAllCardsFromLibrary(playerA);
        removeAllCardsFromHand(playerA);
        skipInitShuffling();

        addCard(Zone.BATTLEFIELD, playerA, madame);
        addCard(Zone.LIBRARY, playerA, "Forest", 1);

        checkPlayableAbility("land on top, can't play", 1, PhaseStep.PRECOMBAT_MAIN, playerA, "Play Forest", false);

        setStrictChooseMode(true);
        setStopAt(1, PhaseStep.END_TURN);
        execute();

        assertLibraryCount(playerA, "Forest", 1);
        assertPermanentCount(playerA, "Forest", 0);
    }

    @Test
    public void test_NoncreatureSpellOnTopIsCastable() {
        removeAllCardsFromLibrary(playerA);
        removeAllCardsFromHand(playerA);
        skipInitShuffling();

        addCard(Zone.BATTLEFIELD, playerA, madame);
        addCard(Zone.BATTLEFIELD, playerA, "Mountain", 1);
        addCard(Zone.LIBRARY, playerA, "Lightning Bolt", 1);

        checkPlayableAbility("noncreature on top, can cast", 1, PhaseStep.PRECOMBAT_MAIN, playerA, "Cast Lightning Bolt", true);
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Lightning Bolt", playerB);

        setStrictChooseMode(true);
        setStopAt(1, PhaseStep.END_TURN);
        execute();

        assertGraveyardCount(playerA, "Lightning Bolt", 1);
        assertLife(playerB, 20 - 3);
    }

    @Test
    public void test_NonSpiderCreatureOnTopIsNotCastable() {
        removeAllCardsFromLibrary(playerA);
        removeAllCardsFromHand(playerA);
        skipInitShuffling();

        addCard(Zone.BATTLEFIELD, playerA, madame);
        addCard(Zone.BATTLEFIELD, playerA, "Forest", 2);
        addCard(Zone.LIBRARY, playerA, "Grizzly Bears", 1);

        checkPlayableAbility("non-Spider creature on top, can't cast", 1, PhaseStep.PRECOMBAT_MAIN, playerA, "Cast Grizzly Bears", false);

        setStrictChooseMode(true);
        setStopAt(1, PhaseStep.END_TURN);
        execute();

        assertLibraryCount(playerA, "Grizzly Bears", 1);
    }
}
