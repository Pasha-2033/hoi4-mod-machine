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
	public TokenRelation[] relations;	//WARNING!!! Token MUST have 2 relations!
	public String value;
	public List<Token> childs;
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
		this.value = value;
		this.relations = relations;
		addchilds(children);
	}
	public Token addchild(Token component) {
        return addchild(childs.size(), component);
    }
    public Token addchild(int index, Token component) {
        childs.add(index, component);
        component.parent = this;
        return this;
    }
    public Token addchilds(List<Token> children) {
		if (children != null) {
			if (childs == null) {
				childs = new ArrayList<Token>(0);
			}
			for (Token child : children){
				addchild(child);
			}
		}
        return this;
    }
}