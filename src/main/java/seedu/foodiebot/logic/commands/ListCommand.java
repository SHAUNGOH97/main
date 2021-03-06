package seedu.foodiebot.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.foodiebot.model.Model;

/**
 * Lists all canteens in FoodieBot to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all canteens";
    public static final String MESSAGE_NEAREST_BLOCK = "Canteens near you";

    private final String nearestBlockName;

    public ListCommand() {
        this.nearestBlockName = "";
    }

    public ListCommand(String nearestBlockName) {
        this.nearestBlockName = nearestBlockName;
    }

    @Override
    public CommandResult execute(Model model) {
        boolean isLocationSpecified = false;
        requireNonNull(model);
        if (nearestBlockName.isBlank()) {
            model.updateFilteredCanteenList(Model.PREDICATE_SHOW_ALL);
            model.setLocationSpecified(false);
            return new CommandResult(COMMAND_WORD, MESSAGE_SUCCESS, false, false, isLocationSpecified);
        } else {
            model.updateFilteredCanteenList(c -> c.getBlockName().equalsIgnoreCase(nearestBlockName));
            isLocationSpecified = true;
            model.setLocationSpecified(true);
            return new CommandResult(COMMAND_WORD, MESSAGE_NEAREST_BLOCK, false, false,
                    isLocationSpecified);
        }

    }
}
