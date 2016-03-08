package controller.slogoparser;

import model.movement.Movement;
import model.usercontrol.MakeUserInstruction;
import model.usercontrol.Repeat;
import model.treenode.ConstantNode;
import model.treenode.TreeNode;
import model.treenode.TurtleCommandNode;
import model.turtle.Turtle;
import model.usercontrol.MakeVariable;
import observables.MapObservable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Created by rhondusmithwick on 2/25/16.
 *
 * @author Rhondu Smithwick
 */
public class ExpressionTree {

    private final ResourceBundle commandLocations = ResourceBundle.getBundle("model/commandLocations");

    private final Queue<Entry<String, String>> parsedText;

    private final List<TreeNode> rootList;

    private final MapObservable<String, TreeNode> variables;
    private final MapObservable<String, MakeUserInstruction> definedCommands;

    private final Turtle myTurtle;

    public ExpressionTree(Turtle myTurtle, MapObservable<String, TreeNode> variables, MapObservable<String, MakeUserInstruction> definedCommands,
                          Queue<Entry<String, String>> parsedText) {
        this.myTurtle = myTurtle;
        this.variables = variables;
        this.definedCommands = definedCommands;
        this.parsedText = parsedText;
        rootList = createRootList();
    }

    public void executeAll() {
        rootList.stream().forEach(TreeNode::getValue);
    }

    private List<TreeNode> createRootList() {
        System.out.println(parsedText);
        List<TreeNode> rootList = new LinkedList<>();
        while (inBounds()) {
            TreeNode root = createRoot();
            rootList.add(root);
        }
        return rootList;
    }

    private TreeNode createRoot() {
        TreeNode root = createSingleNode();
        createSubTree(root);
        return root;
    }

    private void createSubTree(TreeNode root) {
        while (stillRoot(root)) {
            TreeNode n = createSingleNode();
            root.addChild(n);
            createSubTree(n);
        }
    }

    private TreeNode createSingleNode() {
        Entry<String, String> curr = parsedText.poll();
        TreeNode n;
        if (variables.containsKey(curr.getValue())) {
            n = variables.get(curr.getValue());
        } else {
            n = createNode(curr);
        }
        return n;
    }

    private TreeNode createNode(Entry<String, String> curr) {
        TreeNode n;
        if (isConstant(curr.getKey())) {
            String doubleText = curr.getValue();
            Double constant = Double.parseDouble(doubleText);
            n = new ConstantNode(constant);
        } else if (definedCommands.containsKey(curr.getValue())) {
            n = definedCommands.get(curr.getValue());
            setValuesForCommand((MakeUserInstruction) n);
        } else {
            n = createNodeInstance(curr.getKey());
        }
        return n;
    }


    private void setValuesForCommand(MakeUserInstruction n) {
        int numAssigned = 0;
        while (numAssigned < n.numVariables()) {
            Entry<String, String> curr = parsedText.poll();
            n.setValue(numAssigned, curr.getValue());
            numAssigned++;
        }
    }

    private TreeNode createNodeInstance(String className) {
        TreeNode n;
        try {
            Class<?> theClass = Class.forName(commandLocations.getString(className));
            n = (TreeNode) theClass.newInstance();
        } catch (Exception e) {
            System.out.println(className);
            n = new ConstantNode(0);
        }
        addTurtleIfShould(n);
        addVariableIfShould(n);
        makeRepeat(n);
        makeUserInstruction(n);
        return n;
    }

    private boolean stillRoot(TreeNode root) {
        return inBounds()
                && (root.needsMoreChildren());
    }

    private boolean inBounds() {
        return !parsedText.isEmpty();
    }

    private boolean isConstant(String className) {
        return (className.equals("Constant"));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, rootList.size()).forEach(i ->
                sb.append(i).append("th Node:\n").append(rootList.get(i)).append("\n")
        );
        return sb.toString();
    }

    private void addTurtleIfShould(TreeNode n) {
        if (n instanceof TurtleCommandNode) {
            TurtleCommandNode turtleNode = (TurtleCommandNode) n;
            turtleNode.setTurtle(myTurtle);
        }
    }

    private void addVariableIfShould(TreeNode n) {
        if (n instanceof MakeVariable) {
            Entry<String, String> curr = parsedText.poll();
            variables.put(curr.getValue(), n);
            createSubTree(n);
        }
    }
  
    private void makeRepeat(TreeNode n) {
        if (n instanceof Repeat) {
            TreeNode numTimes = createRoot();
            n.addChild(numTimes);
            List<TreeNode> nRoots = getComamndsList();
            nRoots.stream().forEach(n::addChild);
        }
    }

    private void makeUserInstruction(TreeNode n) {
        if (n instanceof MakeUserInstruction) {
            MakeUserInstruction mn = (MakeUserInstruction) n;
            String name = parsedText.poll().getValue();
            definedCommands.put(name, mn);
            makeVariables(mn);
            Map<String, MakeVariable> currVariableMap = mn.getVariableMap();
            variables.putAll(currVariableMap);
            List<TreeNode> myRoots = getComamndsList();
            myRoots.stream().forEach(mn::addChild);
            Predicate<String> pred = (currVariableMap::containsKey);
            currVariableMap.keySet().stream().filter(pred).forEach(variables::remove);
        }
    }

    private void makeVariables(MakeUserInstruction n) {
        if (parsedText.peek().getKey().equals("ListStart")) {
            parsedText.poll();
            int index = 0;
            while (true) {
                if (parsedText.peek().getKey().equals("ListEnd")) {
                    parsedText.poll();
                    break;
                }
                Entry<String, String> curr = parsedText.poll();
                n.putInMap(index, curr.getValue());
                index++;
            }
        }
    }

    private List<TreeNode> getComamndsList() {
        List<TreeNode> myRoots = new LinkedList<>();
        if (parsedText.peek().getKey().equals("ListStart")) {
            parsedText.poll();
            while (true) {
                if (parsedText.peek().getKey().equals("ListEnd")) {
                    parsedText.poll();
                    break;
                }
                TreeNode root = createRoot();
                myRoots.add(root);
            }
        }
        return myRoots;
    }
}
