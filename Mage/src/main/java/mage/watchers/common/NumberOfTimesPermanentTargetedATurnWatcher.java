package mage.watchers.common;

import mage.MageObjectReference;
import mage.constants.WatcherScope;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.Permanent;
import mage.game.stack.StackObject;
import mage.watchers.Watcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author LevelX2
 */
public class NumberOfTimesPermanentTargetedATurnWatcher extends Watcher {

    private final Map<MageObjectReference, List<UUID>> permanentsTargeted = new HashMap<>();

    public NumberOfTimesPermanentTargetedATurnWatcher() {
        super(WatcherScope.GAME);
    }

    @Override
    public void watch(GameEvent event, Game game) {
        if (event.getType() != GameEvent.EventType.TARGETED) {
            return;
        }
        StackObject targetingObject = game.findTargetingStackObject(this.getKey(), event);
        if (targetingObject == null) {
            return;
        }
        Permanent permanent = game.getPermanent(event.getTargetId());
        if (permanent != null) {
            MageObjectReference mor = new MageObjectReference(permanent, game);
            permanentsTargeted.computeIfAbsent(mor, k -> new ArrayList<>()).add(targetingObject.getControllerId());
        }
    }

    public int numTimesTargetedThisTurn(Permanent permanent, Game game) {
        return permanentsTargeted.getOrDefault(new MageObjectReference(permanent, game), new ArrayList<>()).size();
    }

    /**
     * Number of times the permanent was targeted this turn by spells and
     * abilities controlled by the given player (CR 603.2: "spell or ability
     * you control" counts only that player's own targeting).
     */
    public int numTimesTargetedThisTurnBy(Permanent permanent, UUID controllerId, Game game) {
        return (int) permanentsTargeted.getOrDefault(new MageObjectReference(permanent, game), new ArrayList<>())
                .stream()
                .filter(controllerId::equals)
                .count();
    }

    @Override
    public void reset() {
        super.reset();
        permanentsTargeted.clear();
    }
}
