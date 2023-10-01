package mainclassfolder;
import engine.tokens.Token;
public class tests {
	public static void start_tests(){
		test_token_relations();
	}
	public static void test_token_relations(){
		System.out.println(Token.TokenRelation.get_short_relation(
			new Token.TokenRelation[] {
				Token.TokenRelation.LESS,
				Token.TokenRelation.INVERSIVE
			}
		));
	}
}
