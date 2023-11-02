package engine.parsers;
import java.util.List;
import engine.tokens.Token;
import engine.tokens.Token.TokenRelation;
public class pdx_to_llpl {
	private static char[] end_token_symbols = {'\"', ' ', '\t', '=', '>', '<', '{', '}', '#'};
	//raw functions is more like traslator, can`t use optimal expression
	public static List<Token> raw_parse_pdx_to_llpl(List<String> lines){
		Token root = new Token("");
		root.arrayforced = true;
		Token node = root;
		Token last_token = root;
		Token.TokenRelation relation = Token.TokenRelation.NONE;
		for (int i = 0; i < lines.size(); i++){
			boolean comment_prev = false;
			line_loop: for(int j = 0; j < lines.get(i).length(); j++) {
				Token new_token;
				int index;
				switch (lines.get(i).charAt(j)) {
					//unation symbol
					case '\"':
						new_token = pick_up_str_token(lines, i, j, relation);
						if (relation == Token.TokenRelation.NONE){
							node.addchild(new_token);
						}
						else {
							last_token.addchild(new_token);
						}
						last_token = new_token;	
						relation = Token.TokenRelation.NONE;
						int jump = last_token.value.length() + j;
						//value can be polyline
						//so \n symbol isn`t in List<String> - we can`t apply its` length
						for (char c : last_token.value.toCharArray()) {
							if (c == '\n') {
								jump--;
							}
						}
						while(jump > lines.get(i).length()) {
							jump -= lines.get(i++).length();
						}
						j = jump - 1;
						comment_prev = true;
						break;
					//non value symbols (need to reduse default case script, DO NOT REMOVE!)
					case ' ':
						break;
					case '\t':
						break;
					//sign symbols
					case '=':
						if (relation == Token.TokenRelation.NONE) {
							relation = Token.TokenRelation.EQUAL;
							comment_prev = true;
						}
						else {
							//error on i line in j symbol - relation is already set, ignored
						}
						break;
					case '>':
						if (relation == Token.TokenRelation.NONE) {
							relation = Token.TokenRelation.GREATER;
							comment_prev = true;
						}
						else {
							//error on i line in j symbol - relation is already set, ignored
						}
						break;
					case '<':
						if (relation == Token.TokenRelation.NONE) {
							relation = Token.TokenRelation.LESS;
							comment_prev = true;
						}
						else {
							//error on i line in j symbol - relation is already set, ignored
						}
						break;
					case '{':
						if (node == last_token) {
							//error on i line in j symbol too much opening brackets, ignored
						}
						else if (last_token == null) {
							break;
						}
						comment_prev = true;
						node = last_token;
						node.arrayforced = true;
						relation = Token.TokenRelation.NONE;
						comment_prev = true;
						break;
					case '}':
						if (node.parent == null) {
							//error on i line in j symbol to much closing brackets, ignored
						}
						else {
							node = node.parent;
							last_token = null;
							comment_prev = false;
						}
						break;
					case '#':
						Token comment = new Token(lines.get(i).substring(j, lines.get(i).length()), new Token.TokenRelation[]{relation, TokenRelation.NONE});
						index = 0;
						if (comment_prev) {
							if (last_token.parent.is_arrayforced()) {
								last_token.parent.addchild(last_token.parent.childs.size() - 1, comment);
							}
							else {
								last_token.parent.parent.addchild(last_token.parent.parent.childs.size() - 1, comment);
							}
						}
						else {
							node.addchild(node.childs.size(), comment);
						}
						break line_loop;
					//value symbols
					default:
						new_token = pick_up_token(lines.get(i), j, relation);
						if (relation == Token.TokenRelation.NONE){
							node.addchild(new_token);
						}
						else {
							last_token.addchild(new_token);
						}
						last_token = new_token;				
						relation = Token.TokenRelation.NONE;
						j += new_token.value.length() - 1;
						comment_prev = true;
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
					value += "\n" + lines.get(i).substring(i == start_line ? start_index : 0, j + 1);
					break symbol_loop;
				}
				prev = lines.get(i).charAt(j);
			}
			value += "\n" + lines.get(i).substring(i == start_line ? start_index : 0, j);
			j = 0;
		}
		return new Token(value.substring(1), new Token.TokenRelation[]{relation, TokenRelation.NONE});
	}
}