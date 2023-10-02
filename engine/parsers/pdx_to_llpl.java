package engine.parsers;
import java.util.List;
import engine.tokens.Token;
import engine.tokens.Token.TokenRelation;
public class pdx_to_llpl {
	private static char[] end_token_symbols = {'\"', ' ', '\t', '=', '>', '<', '{', '}', '#'};
	public static List<Token> parse_pdx_to_llpl(List<String> lines){
		Token root = new Token("");
		Token node = root;
		Token last_token = null;
		Token.TokenRelation relation = Token.TokenRelation.NONE;
		for (int i = 0; i < lines.size(); i++){
			line_loop: for(int j = 0; j < lines.get(i).length(); j++) {
				switch (lines.get(i).charAt(j)) {
					//unation symbol
					case '\"':
						last_token = pick_up_str_token(lines, i, j, relation);
						node.addchild(last_token);
						relation = Token.TokenRelation.NONE;
						int jump = last_token.value.length();
						while(jump > lines.get(i).length()) {
							jump -= lines.get(i).length();
							i++;
						}
						j += jump - 1;
						break;
					//non value symbols
					case ' ':
						break;
					case '\t':
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
						node.arrayforced = true;
						relation = Token.TokenRelation.NONE;
						break;
					case '}':
						if (node.parent == null) {
							//error on i line in j symbol to much closing brackets
						}
						node = node.parent;
						break;
					case '#':
						node.comments.v.add(lines.get(i).substring(j, lines.get(i).length()));
						break line_loop;
					//value symbols
					default:
						Token new_token = pick_up_token(lines.get(i), j, relation);
						if (relation == Token.TokenRelation.NONE){
							node.addchild(new_token);
						}
						else {
							last_token.addchild(new_token);
						}
						last_token = new_token;				
						relation = Token.TokenRelation.NONE;
						j += new_token.value.length() - 1;
						break;
				}
			}
		}
		return root.childs;
	}
	private static Token pick_up_token(String line, int start_index, Token.TokenRelation relation) {
		int i;
		symbol_loop: for (i = start_index; i < line.length(); i++){
			for (char c : end_token_symbols) {
				if (c == line.charAt(i)) {
					break symbol_loop;
				}
			}
		}
		return new Token(line.substring(start_index, i), new Token.TokenRelation[]{relation, TokenRelation.NONE});
	}
	private static Token pick_up_str_token(List<String> lines, int start_line, int start_index, Token.TokenRelation relation) {
		int i, j = start_index + 1;
		String value = "";
		char prev = lines.get(start_line).charAt(start_index);
		symbol_loop: for (i = start_line; i < lines.size(); i++) {
			for (; j < lines.get(i).length(); j++) {
				if (lines.get(i).charAt(j) == '\"' && prev != '\\') {
					value += "\n" + lines.get(i).substring(i == start_line ? start_index : 0, j);
					break symbol_loop;
				}
				prev = lines.get(i).charAt(j);
			}
			value += "\n" + lines.get(i).substring(i == start_line ? start_index : 0, j);
			j = 0;
		}
		return new Token((value + '\"').substring(1), new Token.TokenRelation[]{relation, TokenRelation.NONE});
	}
}