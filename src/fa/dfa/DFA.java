package fa.dfa;

import fa.State;
import java.util.*;

/**
 * CS361 P1 
 * This Java project implements a program that models a deterministic finite automaton (DFA).
 * A DFA is a theoretical model of computation that recognizes regular languages.
 * It consists of states, transitions, an alphabet, a start state, and final states.
 * 
 * @author Schylar Davis / Daniel Aguilar
 */
public class DFA implements DFAInterface {
    
    /** The alphabet (sigma) of the DFA - set of valid input symbols */
    private Set<Character> sigma;
    
    /** Set of all states in the DFA */
    private Set<DFAState> states;
    
    /** The designated start state of the DFA */
    private DFAState startState;
    
    /** Set of all final (accepting) states in the DFA */
    private Set<DFAState> finalStates;
    
    /** Map for quick state lookup by name */
    private Map<String, DFAState> stateMap;
    
    /** List to maintain the order in which states were added (for toString formatting) */
    private List<String> stateOrder;
    
    /** List to maintain the order in which alphabet symbols were added (for toString formatting) */
    private List<Character> sigmaOrder;
    
    /**
     * Default constructor that creates an empty DFA.
     * Initializes all data structures but adds no states, symbols, or transitions.
     */
    public DFA() {
        this.sigma = new HashSet<>();
        this.states = new HashSet<>();
        this.finalStates = new HashSet<>();
        this.stateMap = new HashMap<>();
        this.startState = null;
        this.stateOrder = new ArrayList<>();
        this.sigmaOrder = new ArrayList<>();
    }
    
    /**
     * Adds a new state to the DFA with the given name.
     * Each state must have a unique name within the DFA.
     * 
     * @param name the unique identifier for the new state
     * @return true if the state was successfully added, false if a state with this name already exists
     */
    @Override
    public boolean addState(String name) {
        if (stateMap.containsKey(name)) {
            return false;
        }
        
        DFAState state = new DFAState(name); 
        states.add(state);
        stateMap.put(name, state);
        stateOrder.add(name);
        return true;
    }
    
    /**
     * Marks an existing state as a final (accepting) state.
     * A DFA accepts a string if it ends in a final state after processing all input symbols.
     * 
     * @param name the name of the state to mark as final
     * @return true if the state exists and was successfully marked as final, false if no such state exists
     */
    @Override
    public boolean setFinal(String name) {
        DFAState state = stateMap.get(name);
        if (state == null) {
            return false;
        }
        
        state.setFinal(true);
        finalStates.add(state);
        return true;
    }
    
    /**
     * Sets the start state of the DFA.
     * A DFA must have exactly one start state. If a start state already exists,
     * it will be replaced by the new one.
     * 
     * @param name the name of the state to set as the start state
     * @return true if the state exists and was successfully set as start state, false if no such state exists
     */
    @Override
    public boolean setStart(String name) {
        DFAState state = stateMap.get(name);
        if (state == null) {
            return false;
        }
        
        // Remove start flag from current start state if exists
        if (startState != null) {
            startState.setStart(false);
        }
        
        state.setStart(true);
        startState = state;
        return true;
    }
    
    /**
     * Adds a symbol to the alphabet (sigma) of the DFA.
     * The alphabet defines the set of valid input symbols that the DFA can process.
     * Duplicate symbols are ignored.
     * 
     * @param symbol the character to add to the alphabet
     */
    @Override
    public void addSigma(char symbol) {
        if (!sigma.contains(symbol)) {
            sigma.add(symbol);
            sigmaOrder.add(symbol);
        }
    }
    
    /**
     * Simulates the DFA on the given input string to determine if it's accepted.
     * The DFA starts at the start state and follows transitions for each character
     * in the input string. The string is accepted if the DFA ends in a final state.
     * 
     * @param s the input string to test for acceptance
     * @return true if the string is accepted by the DFA, false otherwise
     */
    @Override
    public boolean accepts(String s) {
        if (startState == null) {
            return false;
        }
        
        DFAState currentState = startState;
        
        for (char c : s.toCharArray()) {
            // Reject if character not in alphabet
            if (!sigma.contains(c)) {
                return false;
            }
            
            // Follow transition for this character
            DFAState nextState = currentState.getTo(c);
            if (nextState == null) {
                return false; // No transition defined
            }
            currentState = nextState;
        }
        
        // Accept if we end in a final state
        return currentState.isFinal();
    }
    
    /**
     * Returns a copy of the alphabet (sigma) of the DFA.
     * 
     * @return a Set containing all symbols in the DFA's alphabet
     */
    @Override
    public Set<Character> getSigma() {
        return new HashSet<>(sigma);
    }
    
    /**
     * Retrieves a state by its name.
     * 
     * @param name the name of the state to retrieve
     * @return the State object with the given name, or null if no such state exists
     */
    @Override
    public State getState(String name) {
        return stateMap.get(name);
    }
    
    /**
     * Checks if a state with the given name is a final state.
     * 
     * @param name the name of the state to check
     * @return true if the state exists and is final, false otherwise
     */
    @Override
    public boolean isFinal(String name) {
        DFAState state = stateMap.get(name);
        return state != null && state.isFinal();
    }
    
    /**
     * Checks if a state with the given name is the start state.
     * 
     * @param name the name of the state to check
     * @return true if the state exists and is the start state, false otherwise
     */
    @Override
    public boolean isStart(String name) {
        DFAState state = stateMap.get(name);
        return state != null && state.isStart();
    }
    
    /**
     * Adds a transition from one state to another on a given symbol.
     * A transition defines how the DFA moves between states when processing input.
     * 
     * @param fromState the name of the source state
     * @param toState the name of the destination state
     * @param onSymb the symbol that triggers this transition
     * @return true if the transition was successfully added, false if either state doesn't exist or the symbol isn't in the alphabet
     */
    @Override
    public boolean addTransition(String fromState, String toState, char onSymb) {
        DFAState from = stateMap.get(fromState);
        DFAState to = stateMap.get(toState);
        
        if (from == null || to == null || !sigma.contains(onSymb)) {
            return false;
        }
        
        from.addTransition(onSymb, to);
        return true;
    }
    
    /**
     * Creates a deep copy of this DFA with two symbols swapped in all transitions.
     * This operation is useful for testing DFA equivalence and transformations.
     * The original DFA remains unchanged.
     * 
     * @param symb1 the first symbol to swap
     * @param symb2 the second symbol to swap
     * @return a new DFA that is identical to this one except with symb1 and symb2 swapped in all transitions
     */
    @Override
    public DFA swap(char symb1, char symb2) {
        DFA newDFA = new DFA();
        
        // Copy sigma with swapped symbols
        for (char c : sigmaOrder) {
            if (c == symb1) {
                newDFA.addSigma(symb2);
            } else if (c == symb2) {
                newDFA.addSigma(symb1);
            } else {
                newDFA.addSigma(c);
            }
        }
        
        // Copy states in the same order
        for (String stateName : stateOrder) {
            newDFA.addState(stateName);
        }
        
        // Copy start and final state designations
        if (startState != null) {
            newDFA.setStart(startState.getName());
        }
        
        for (DFAState finalState : finalStates) {
            newDFA.setFinal(finalState.getName());
        }
        
        // Copy transitions with swapped symbols
        for (DFAState state : states) {
            Map<Character, DFAState> transitions = state.getTransitions();
            for (Map.Entry<Character, DFAState> entry : transitions.entrySet()) {
                char symbol = entry.getKey();
                DFAState toState = entry.getValue();
                
                // Swap the symbols in transitions
                char newSymbol = symbol;
                if (symbol == symb1) {
                    newSymbol = symb2;
                } else if (symbol == symb2) {
                    newSymbol = symb1;
                }
                
                newDFA.addTransition(state.getName(), toState.getName(), newSymbol);
            }
        }
        
        return newDFA;
    }
    
    /**
     * Creates a textual representation of the DFA in standard format.
     * The format includes:
     * - Q: set of states
     * - Sigma: alphabet
     * - delta: transition table
     * - q0: start state
     * - F: set of final states
     * 
     * @return a String containing the formatted representation of this DFA
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // Q = { states }
        sb.append("Q = { ");
        for (int i = 0; i < stateOrder.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(stateOrder.get(i));
        }
        sb.append(" }\n");
        
        // Sigma = { alphabet }
        sb.append("Sigma = { ");
        for (int i = 0; i < sigmaOrder.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(sigmaOrder.get(i));
        }
        sb.append(" }\n");
        
        // Delta table
        sb.append("delta =\n");
        
        // Header row with symbols
        sb.append("\t\t");
        for (char c : sigmaOrder) {
            sb.append(c).append("\t");
        }
        sb.append("\n");
        
        // Transition rows
        for (String stateName : stateOrder) {
            sb.append("\t").append(stateName).append("\t");
            DFAState state = stateMap.get(stateName);
            for (char c : sigmaOrder) {
                DFAState toState = state.getTo(c);
                if (toState != null) {
                    sb.append(toState.getName());
                } else {
                    sb.append("-"); // No transition
                }
                sb.append("\t");
            }
            sb.append("\n");
        }
        
        // Start state
        sb.append("q0 = ");
        if (startState != null) {
            sb.append(startState.getName());
        }
        sb.append("\n");
        
        // Final states
        sb.append("F = { ");
        boolean first = true;
        for (String stateName : stateOrder) {
            if (isFinal(stateName)) {
                if (!first) sb.append(" ");
                sb.append(stateName);
                first = false;
            }
        }
        sb.append(" }");
        
        return sb.toString();
    }
}