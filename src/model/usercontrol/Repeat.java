package model.usercontrol;

import controller.slogoparser.ExpressionTree;
import model.treenode.CommandNode;
import model.treenode.TreeNode;

import java.util.List;
import java.util.stream.IntStream;


/**
 * Created by rhondusmithwick on 3/1/16.
 *
 * @author Rhondu Smithwick
 */
public class Repeat extends CommandNode {

    private Variable repcount = new Variable();
    private Double value = null;
    private TreeNode numTimesNode;
    private Integer numTimes = null;

    @Override
    protected double execute() {
        repcount.setValue(0);
        if (numTimes == null) {
            getNumTimes();
        }
        IntStream.range(0, numTimes).forEach(i -> doIteration());
        return (value != null) ? value : 0;
    }

    private void doIteration() {
        repcount.setValue(repcount.getValue() + 1);
        value = runChildren();
    }

    private void getNumTimes() {
        numTimes = (int) numTimesNode.getValue();
    }

    public void handleSpecific(ExpressionTree tree) {
        tree.getVariables().put(":repcount", repcount);
        numTimesNode = tree.createRoot();
        System.out.println(tree.getParsedText());
        List<TreeNode> nRoots = tree.getCommandsFromList();
        getChildren().addAll(nRoots);
        tree.getVariables().remove(":repcount");
    }

    public Variable getVariable() {
        return repcount;
    }

    @Override
    protected int getNumChildrenRequired() {
        return 2;
    }
}
