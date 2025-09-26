package fa.dfa;

import java.util.*;

import fa.FAInterface;
import fa.State;

public class DFA<DFAState> implements DFAInterface {
    
    private Set<Character> sigma;
    private Set<DFAState> states;
    private State startState;
    private Set<DFAState> finalStates;
    private Map<String, State> stateMap;
    private List<String> stateOrder; // To maintain insertion order for toString
    private List<Character> sigmaOrder; // To maintain insertion order for toString
    
    public DFA() {
        this.sigma = new HashSet<>();
        this.states = new HashSet<>();
        this.finalStates = new HashSet<>();
        this.stateMap = new HashMap<>();
        this.startState = null;
        this.stateOrder = new ArrayList<>();
        this.sigmaOrder = new ArrayList<>();
    }
    
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
    
    @Override
    public boolean setFinal(String name) {
        DFAState state = stateMap.get(name);
        if (state == null) {
            return false;
        }
        
        ((FAInterface) state).setFinal(true);
        finalStates.add(state);
        return true;
    }
    
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
    
    @Override
    public void addSigma(char symbol) {
        if (!sigma.contains(symbol)) {
            sigma.add(symbol);
            sigmaOrder.add(symbol);
        }
    }
    
    @Override
    public boolean accepts(String s) {
        if (startState == null) {
            return false;
        }
        
        DFAState currentState = startState;
        
        for (char c : s.toCharArray()) {
            if (!sigma.contains(c)) {
                return false;
            }
            
            DFAState nextState = currentState.getTo(c);
            if (nextState == null) {
                return false;
            }
            currentState = nextState;
        }
        
        return currentState.isFinal();
    }
    
    @Override
    public Set<Character> getSigma() {
        return new HashSet<>(sigma);
    }
    
    @Override
    public State getState(String name) {
        return stateMap.get(name);
    }
    
    @Override
    public boolean isFinal(String name) {
        DFAState state = stateMap.get(name);
        return state != null && state.isFinal();
    }
    
    @Override
    public boolean isStart(String name) {
        DFAState state = stateMap.get(name);
        return state != null && state.isStart();
    }
    
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
    
    @SuppressWarnings("rawtypes")
    @Override
    public DFA swap(char symb1, char symb2) {
        @SuppressWarnings("rawtypes")
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
        
        // Copy states
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