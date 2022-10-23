package fa.nfa;
import java.util.*;

import fa.State;
import fa.dfa.DFA;

public class NFA implements NFAInterface{

    // NFA 5-tuple instance variables
    private LinkedHashSet <NFAState> Q;             // finite set of states
    private LinkedHashSet <Character> sigma;        // finite alphabet
    private NFAState q0;                            // start state        
    //private HashMap<String, Set<NFAState>> delta;   // transitions
    //private static char EMPTY = 'e';                // variable for empty string

    /** 
     * Constructor for NFA Class. Instantiates
     */
    public NFA(){
        this.Q = new LinkedHashSet<>();
        this.sigma = new LinkedHashSet<>();
        this.q0 = null;
      //  this.delta = new HashMap<>();
    }


    @Override
    public void addStartState(String name) {
        NFAState state = getQState(name);

        if(state == null){
            state = new NFAState(name);
            this.Q.add(state);
        }

        this.q0 = state; 
    }

    @Override
    public void addState(String name) {
        NFAState state = getQState(name);
        if(state == null){
            this.Q.add(new NFAState(name));
        }else{
            // state is already in Q and does not need to be added
        }
    }

    @Override
    public void addFinalState(String name) {
        NFAState state = getQState(name);

        if(state == null){
            state = new NFAState(name); 
            state.setFinal();
            this.Q.add(state);
        }else{
            state.setFinal(); // Set state's final status to true
        }
    }

    @Override
    public void addTransition(String fromState, char onSymb, String toState) {
		NFAState from = getQState(fromState);
		NFAState to = getQState(toState);

		from.addTransition(onSymb, to);

		if (!sigma.contains(onSymb) && onSymb != 'e') {
			sigma.add(onSymb);
		}
	}
 
    @Override
    public Set<? extends State> getStates() {
        return this.Q;
    }

    @Override
    public Set<? extends State> getFinalStates() {
        Set<NFAState> finalStates = new LinkedHashSet<NFAState>();
        for(NFAState s : Q){
            if(s.isFinal()){
                finalStates.add(s);
            }
        }
        return finalStates;
    }

    @Override
    public State getStartState() {
        return this.q0;
    }

    @Override
    public Set<Character> getABC() {
        return this.sigma;
    }

    @Override
    public DFA getDFA() {
		DFA dfa = new DFA();

		Set<Set<NFAState>> addedStates = new HashSet<Set<NFAState>>();

		Queue<Set<NFAState>> queue = new LinkedList<Set<NFAState>>();
		queue.add(eClosure(q0));

		String startStateName = eClosure(q0).toString();
		dfa.addStartState(startStateName);
		if (isFinal(eClosure(q0)))
			dfa.addFinalState(startStateName);

		addedStates.add(eClosure(q0));

		while (queue.peek() != null) {
			Set<NFAState> s = queue.poll();

			for (Character symb : sigma) {
				Set<NFAState> toState = getToState(s, symb);
				toState = eClosure(toState);

				if (!addedStates.contains(toState)) {
					if (isFinal(toState))
						dfa.addFinalState(toState.toString());
					else
						dfa.addState(toState.toString());

					addedStates.add(toState);
					queue.add(toState);
				}

				dfa.addTransition(s.toString(), symb, toState.toString());
			}
		}

		return dfa;
	}

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTo(onSymb);
    }

	@Override
	public Set<NFAState> eClosure(NFAState s) {

		Set<NFAState> ret = new LinkedHashSet<NFAState>();
		ret = eClosureDFS(s, ret);

		return ret;
	}

    
	/**
	 * A helper method to perform recursive DFS search from a given state.
	 * 
	 * @param s   The state to perform DFS search from.
	 * @param ret The set of values that the results should be added to.
	 * @return The results of the DFS search added to the values in ret.
	 */
	private Set<NFAState> eClosureDFS(NFAState s, Set<NFAState> ret) {
		if (!ret.contains(s)) {
			ret.add(s);

			// perform Depth-First Search
			Set<NFAState> states = getToState(s, 'e');
			for (NFAState state : states) {
				eClosureDFS(state, ret);
			}
		}

		return ret;
	}

    

    // Helper methods
    /**
     * Traverses the set Q to find a specific state
     * @param name
     * @return state from Q with name given
     */
    private NFAState getQState(String name){
        NFAState state = null;

        for(NFAState s : this.Q){
            if(s.toString().equals(name)){
                state = s;
                break;
            }
        }
        return state;
    }

    /**
	 * A helper method to determine if any of the given states are final.
	 * 
	 * @param s   The states to check
	 * @return True if any of the given states are final.
	 */
	private boolean isFinal(Set<NFAState> state) {
		for (NFAState s : state) {
			if (s.isFinal())
				return true;
		}

		return false;
	}

    /**
     * 
     * @param from
     * @param onSymb
     * @return
     */
    private Set<NFAState> getToState(Set<NFAState> from, char onSymb) {
		Set<NFAState> state = new HashSet<NFAState>();

		for (NFAState s : from) {
			state.addAll(s.getTo(onSymb));
		}

		return state;
	}

    /**
	 * performs recursive DFS search on multiple states.
	 * 
	 * @param s   The states to perform DFS search from.
	 * @return The results of the DFS search on all given states.
	 */
	private Set<NFAState> eClosure(Set<NFAState> s) {
		Set<NFAState> ret = new HashSet<NFAState>();

		for (NFAState nfaState : s) {
			ret.addAll(eClosure(nfaState));
		}

		return ret;
	}

}