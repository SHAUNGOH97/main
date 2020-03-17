package seedu.foodiebot.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.foodiebot.commons.core.LogsCenter;
import seedu.foodiebot.commons.core.index.Index;
import seedu.foodiebot.logic.commands.BudgetCommand;
import seedu.foodiebot.model.budget.Budget;
import seedu.foodiebot.model.Model;
import seedu.foodiebot.model.food.Food;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/** Select the current list view item. */
public class SelectItemCommand extends Command {
    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD
            + "Parameters: "
            + "FOOD_INDEX \n"
            + "Example: "
            + COMMAND_WORD
            + " "
            + "1 ";;

    public static final String MESSAGE_SUCCESS = "You have selected: %s\n"
            + "Your remaining budget is $%.2f\nWith $%.2f to spend today";
    private static final Logger logger = LogsCenter.getLogger(SelectItemCommand.class);

    private final Optional<String> foodName;
    private final Optional<Index> index;

    /**
     * @param index of the food in the filtered food list to edit
     */
    public SelectItemCommand(Index index) {
        requireNonNull(index);
        this.index = Optional.of(index);
        this.foodName = Optional.empty();
    }

    /**
     * @param foodName from the given food name
     */
    public SelectItemCommand(String foodName) {
        requireNonNull(foodName);
        this.index = Optional.empty();
        this.foodName = Optional.of(foodName);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        String nameOfFood = "";
        float priceOfFood = 0;
        if (index.isPresent()) {
            List<Food> foodList;
            foodList = model.getFilteredFoodList();
            Food food = foodList.get(index.get().getZeroBased());
            nameOfFood = food.getName();
            priceOfFood = food.getPrice();
            logger.info("Enter " + food.getName());
        } else if (foodName.isPresent()) {
            List<Food> foodList = model.getFilteredFoodList();
            for (Food f : foodList) {
                if (f.getName().equalsIgnoreCase(foodName.get())) {
                    nameOfFood = foodName.get();
                    priceOfFood = f.getPrice();
                    break;
                }
            }
        }
        Budget savedBudget = model.getBudget().get();
        savedBudget.subtractFromRemainingBudget(priceOfFood);
        model.setBudget(savedBudget);
        return new CommandResult(COMMAND_WORD, String.format(
                MESSAGE_SUCCESS,nameOfFood,
                savedBudget.getRemainingBudget(),
                savedBudget.getRemainingDailyBudget()));
    }

    @Override
    public boolean needToSaveCommand() {
        return false;
    }
}
