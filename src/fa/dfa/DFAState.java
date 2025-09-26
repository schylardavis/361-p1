package fa.dfa;

import fa.State;
import java.util.HashMap;
import java.util.Map;

public class DFAState extends State {
    
    private boolean isFinal;
    private boolean isStart;
    private Map<Character, DFAState> transitions;
    
    public DFAState(String name) {
        super(name);
        this.isFinal = false;
        this.isStart = false;
        this.transitions = new HashMap<>();
    }
    
    /**
     * Add a transition from this state to another state on a given symbol
     * @param onSymb the symbol that triggers the transition
     * @param toState the destination state
     */
    public void addTransition(char onSymb, DFAState toState) {
        transitions.put(onSymb, toState);
    }
    
    /**
     * Get the destination state for a given symbol
     * @param symb the input symbol
     * @return the destination state, or null if no transition exists
     */
    public DFAState getTo(char symb) {
        return transitions.get(symb);
    }
    
    /**
     * Mark this state as final
     * @param isFinal true to mark as final, false otherwise
     */
    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    /**
     * Check if this state is final
     * @return true if this state is final
     */
    public boolean isFinal() {
        return isFinal;
    }
    
    /**
     * Mark this state as start state
     * @param isStart true to mark as start, false otherwise
     */
    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }
    
    /**
     * Check if this state is the start state
     * @return true if this state is the start state
     */
    public boolean isStart() {
        return isStart;
    }
    
    /**
     * Get all transitions from this state
     * @return map of symbol to destination state
     */
    public Map<Character, DFAState> getTransitions() {
        return new HashMap<>(transitions);
    }
}