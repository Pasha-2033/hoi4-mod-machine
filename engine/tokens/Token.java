package engine.tokens;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
public class Token {
	public static enum TokenRelation {
		NONE("", 0),
		EQUAL("=", 1),
		GREATER(">", 2),
		LESS("<", 2),
		INVERSIVE("!", 3);
		private String name;
		private int weight;
		TokenRelation(String name, int weight) {
			this.name = name;
			this.weight = weight;
		}
		public static String get_short_relation(TokenRelation[] relation) {
			if (relation[0] == relation[1]) {
				if (relation[0] == INVERSIVE || relation[0] == EQUAL) {
					return EQUAL.name;
				}
				return relation[0].name;
			}
			else if (relation[1].weight > relation[0].weight) {
				return relation[1].name + relation[0].name;
			}
			else {
				return relation[0].name + relation[1].name;
			}
		}
		public static String get_long_relation(TokenRelation[] relation) {
			switch (relation[0].weight + relation[1].weight) {
				case 3:
					if (relation[0] == NONE || relation[1] == NONE) {
						return "not_equals";
					}
					else if (relation[0] == GREATER || relation[1] == GREATER) {
						return "greater_than_or_equals";
					}
					else {
						return "less_than_or_equals";
					}
				case 4:
					return "not_equals";
				case 5:
					if (relation[0] == GREATER || relation[1] == GREATER) {
						return "less_than_or_equals";
					}
					else {
						return "greater_than_or_equals";
					}
				default:
					return "";
			}
		}
		public static boolean pdx_sould_be_short(TokenRelation[] relation) {
			return relation[0].weight + relation[1].weight < 3 || relation[0].name == relation[1].name;
		}
	}
	public static final TokenRelation[] NO_RELATION = {TokenRelation.NONE, TokenRelation.NONE};
	public boolean arrayforced = false;
	public TokenRelation[] relations;	//WARNING!!! Token MUST have 2 relations!
	public String value;
	public List<Token> childs = new ArrayList<>(0);
	public Token parent;
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
		if (childs.size() == 1) {
			if (childs.get(0).childs.isEmpty()) {
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

	//may be should be static function or field enum
	public boolean is_string(){
		return value.startsWith("\"") && value.endsWith("\"");
	}
}