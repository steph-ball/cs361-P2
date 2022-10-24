package fa.nfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import fa.State;

public class NFAState extends State {

    private boolean isFinal;
    private HashMap<Character,Set<NFAState>> delta;

    
    public NFAState(String name){
        this.name = name;
        this.isFinal = false;
        this.delta = new HashMap<Character, Set<NFAState>>();
    }

    public void setFinal(){
        isFinal = true;
    }

    public boolean isFinal(){
        return isFinal;
    }

    @Override
    public String toString(){
        return this.name;
    }

    // Helper methods
    /**
     * 
     * @param symb
     * @return
     */
    public Set<NFAState> getTo(char symb){
        Set<NFAState> state = delta.get(symb);
        if(state == null){
            return new HashSet<NFAState>();
        }
        return state;
    }

    	/**
	 * Add the transition from <code> this </code> object
	 * @param onSymb the alphabet symbol
	 * @param toState to NFA state
	 */
	public void addTransition(char onSymb, NFAState toState){
		if (!delta.containsKey(onSymb)) {
			delta.put(onSymb, new HashSet<NFAState>());
		}

		delta.get(onSymb).add(toState);
	}
}
