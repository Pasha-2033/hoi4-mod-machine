package engine.tokens;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
public class Token {
	public static enum TokenRelation {
		NONE(""),
		EQUAL("="),
		GREATER(">"),
		LESS("<"),
		INVERSIVE("!");
		private String value;
		TokenRelation(String value) {
			this.value = value;
		}
		public static TokenRelation[] parse_relation(String relation) throws IllegalArgumentException {
			if (relation.isEmpty()){
				return new TokenRelation[]{NONE, NONE};
			}
			else if (relation.length() > 2){
				throw new IllegalArgumentException("Can`t parse more than 2 symbols - it can`t be more than 2 TokenRelation.");
			}
			return new TokenRelation[]{parse_symbol_to_relation(relation.charAt(0)), relation.length() > 1 ? parse_symbol_to_relation(relation.charAt(1)) : NONE};
		}
		public static String get_short_relation(TokenRelation[] relation) throws IllegalArgumentException {
			if (relation.length != 2) {
				throw new IllegalArgumentException("There is must be 2 TokenRelation");
			}
			return must_be_at_start(relation[1]) && relation[0] != INVERSIVE ? relation[1].value + relation[0].value : relation[0].value + relation[1].value;
		}
		private static TokenRelation parse_symbol_to_relation(char c) throws IllegalArgumentException {
			switch (c) {
				case '=':
					return EQUAL;
				case '>':
					return GREATER;
				case '<':
					return LESS;
				case '!':
					return INVERSIVE;
				default:
					throw new IllegalArgumentException(String.format("Can`t parse \"%c\" symbol, it can be only !,=,>,<.", c));
			}
		}
		private static boolean must_be_at_start(TokenRelation relation){
			return !(relation == NONE || relation == EQUAL);
		}
	}
	public static class TokenComent {
		public List<String> v = new ArrayList<String>(0);
	}
	public static final TokenRelation[] NO_RELATION = {TokenRelation.NONE, TokenRelation.NONE};
	public boolean arrayforced = false;
	public TokenRelation[] relations;	//WARNING!!! Token MUST have 2 relations!
	public String value;
	public List<Token> childs = new ArrayList<Token>(0);;
	public Token parent;
	public TokenComent comments = new TokenComent();
	public Token(String value) throws IllegalArgumentException {
		this(value, NO_RELATION);
	}
	public Token(String value, TokenRelation[] relations) throws IllegalArgumentException {
		this(value, relations, null);
	}
	public Token(String value, TokenRelation[] relations, List<Token> children) throws IllegalArgumentException {
		if (relations.length != 2) {
			throw new IllegalArgumentException("There is must be 2 TokenRelation");
		}
		this.value = value == null ? "" : value;
		this.relations = relations;
		addchilds(children);
	}
	public boolean is_arrayforced(){
		if (childs != null) {
			if (childs.size() == 1) {
				if (childs.get(0).childs == null) {
					return arrayforced;
				}
				return arrayforced || !childs.get(0).childs.isEmpty();
			}
			else if (childs.size() > 1) {
				return true;
			}
			else {
				return arrayforced;
			}
		}
		return arrayforced;
	}
	public Token addchild(Token component) {
		//returns this
        return addchild(childs.size(), component);
    }
    public Token addchild(int index, Token component) {
		//returns this
        childs.add(index, component);
        component.parent = this;
        return this;
    }
    public Token addchilds(List<Token> children) {
		//returns this
		if (children != null) {
			for (Token child : children){
				addchild(child);
			}
		}
        return this;
    }
	public Token setchild(int index, Token child) {
		//returns old child
		Token old_child = childs.get(index);
		old_child.parent = null;
        childs.set(index, child);
        child.parent = this;
        return old_child;
    }
	public Token removechild(int index) {
		//returns old child
        Token old_child = childs.get(index);
        old_child.parent = null;
        childs.remove(index);
        return old_child;
    }
}