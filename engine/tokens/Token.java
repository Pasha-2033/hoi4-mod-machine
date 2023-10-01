package engine.tokens;
import java.lang.IllegalArgumentException;
import java.util.List;
public class Token {
	static public enum TokenRelation {
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
			return !(relation == NONE || relation.value == EQUAL.value);
		}
	}
	public TokenRelation[] relations;	//WARNING!!! Token MUST have 2 relations!
	public String value;
	public List<Token> childs;
}