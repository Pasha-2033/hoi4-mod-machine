package engine.parsers;
import java.util.List;
import engine.tokens.Token;
public class pdx_to_llpl {
	public static List<Token> parse_pdx_to_llpl(List<String> input){
		Token root = new Token("");
		Token node = root;
		Token last_token = null;
		Token.TokenRelation relation = Token.TokenRelation.NONE;
		int start_index;
		for (int i = 0; i < input.size(); i++){
			start_index = 0;
			loop: for(int j = 0; j < input.get(i).length(); j++) {
				switch (input.get(i).charAt(j)) {
					//unation symbol
					case '\"':
						//set string token + jump
						last_token = pick_up_str_token(input.get(i), start_index, relation);
						node.addchild(last_token);
						j += last_token.value.length() - 1;
						relation = Token.TokenRelation.NONE;
						break;
					//non value symbols
					case ' ':
						start_index = j + 1;
						break;
					case '\t':
						start_index = j + 1;
						break;
					//sign symbols
					case '=':
						if (relation == Token.TokenRelation.NONE) {
							relation = Token.TokenRelation.EQUAL;
						}
						else {
							//error on i line in j symbol - relation is already set, ignored
						}
						break;
					case '>':
						if (relation == Token.TokenRelation.NONE) {
							relation = Token.TokenRelation.GREATER;
						}
						else {
							//error on i line in j symbol - relation is already set, ignored
						}
						break;
					case '<':
						if (relation == Token.TokenRelation.NONE) {
							relation = Token.TokenRelation.LESS;
						}
						else {
							//error on i line in j symbol - relation is already set, ignored
						}
						break;
					case '{':
						if (node == last_token) {
							//error on i line in j symbol to much opening brackets
						}
						node = last_token;
					case '}':
						if (node.parent == null) {
							//error on i line in j symbol to much closing brackets
						}
						node = node.parent;
						break;
					case '#':
						node.comments.v.add(input.get(i).substring(j, input.get(i).length()));
						break loop;
					//value symbols
					default:
						last_token = pick_up_token(input.get(i), start_index, relation);
						node.addchild(last_token);
						j += last_token.value.length() - 1;
						relation = Token.TokenRelation.NONE;
						break;
				}
			}
			
		}
		return root.childs;
	}
	//to do
	private static Token pick_up_token(String line, int start_index, Token.TokenRelation relation) {
		return null;
	}
	//to do
	private static Token pick_up_str_token(String line, int start_index, Token.TokenRelation relation) {
		return null;
	}
}