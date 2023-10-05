package engine.code_revisions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import engine.tokens.Token;
public class optimiser {
	@FunctionalInterface
	public interface optimiser_function {
		void execute(Token token);
	}
	public static final HashMap<String, String> identifier_token_regexr = new HashMap<>(
		Map.of(
			//bool operators
			"OR", "[oO][rR]",
			"AND", "[aA][nN][dD]",
			"NOT", "[nN][oO][tT]",
			//scope stack
			"ROOT", "[rR][oO]{2}[tT]",
			"THIS", "[tT][hH][iI][sS]",
			"FROM", "[fF][rR][oO][mM]",
			"PREV", "[pP][rR][eE][vV]"
		)
	);
	public static void optimise(Token token, optimiser_function[] functions) {
		for (optimiser_function function : functions) {
			function.execute(token);
		}
		for (Token child : token.childs) {
			optimise(child, functions);
		}
	}
	public static class notation {
		public static optimiser_function identifier_register = (token) -> {
			Iterator<Entry<String, String>> it = identifier_token_regexr.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> current = it.next();
				if (token.value.matches(current.getValue())) {
					token.value = current.getKey();
					return;
				}
			}
		};
		//common compression (get rid of spaces)
		public static optimiser_function string_value_compression = (token) -> {
			//Note: no need to call it after string_value_supper_compression
			if (token.is_string()) {	//if not - don`t compress, value can become invalid
				token.value = token.value.replaceAll("(\s{0,}\"\s{0,})", "\"")
				.replaceAll("\s+", "\s")
				.replaceAll("\s\\\\", "\\\\");
			}
		};
		//super compression (write in 1 linewith no tabs)
		public static optimiser_function string_value_super_compression = (token) -> {
			if (token.is_string()) {	//if not - don`t compress, value can become invalid
				token.value = token.value.replaceAll("[\n\t]+", "\s");
				string_value_compression.execute(token);
			}
		};	
		//metacode notation (read string as code and try to write it appropriately)
		//to do
	}
	/*
	public static class bool {
		public static optimiser_function or = (token) -> {};
	}
	*/
}
