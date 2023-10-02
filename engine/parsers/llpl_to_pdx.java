package engine.parsers;
import java.util.List;
import engine.tokens.Token;
public class llpl_to_pdx {
	public static String parse_llpl_to_pdx(List<Token> tokens){
		String result = "";
		for (Token token : tokens) {
			result += parse_llpl_to_pdx(token, 0);
		}
		return result;
	}
	public static String parse_llpl_to_pdx(Token token, int tab_lvl){
		String result = "\t".repeat(tab_lvl) + token.value;
		if (token.is_arrayforced()) {
			result += " = {";
			if (!token.childs.isEmpty()) {
				result += "\n";
			}
			for (Token child : token.childs) {
				result += parse_llpl_to_pdx(child, tab_lvl + 1);
			}
			if (token.childs.isEmpty()) {
				result += "}";
			}
			else {
				result += "\t".repeat(tab_lvl) + "}";
			}
		}
		else if (token.childs.size() == 1){
			result += String.format(" %s %s", Token.TokenRelation.get_short_relation(token.childs.get(0).relations), token.childs.get(0).value);
		}
		return result + "\n";
	}
}