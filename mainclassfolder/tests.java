package mainclassfolder;
import java.io.File;
import java.io.FileNotFoundException;

import engine.external_interaction.txt_reader;
import engine.parsers.pdx_to_llpl;
import engine.parsers.llpl_to_pdx;
import engine.tokens.Token;
public class tests {
	public static void start_tests() {
		test_token_relations();
		test_read_write();
	}
	public static void test_token_relations() {
		Token.TokenRelation[] relations = new Token.TokenRelation[] {
			Token.TokenRelation.GREATER,
			Token.TokenRelation.EQUAL
		};
		System.out.println(Token.TokenRelation.get_short_relation(relations));
		if (!Token.TokenRelation.pdx_sould_be_short(relations)) {
			System.out.println(Token.TokenRelation.get_long_relation(relations));
		}
	}
	public static void test_read_write() {
		try {
			System.out.println(
				llpl_to_pdx.raw_parse_llpl_to_pdx(
					pdx_to_llpl.raw_parse_pdx_to_llpl(
						txt_reader.read(new File("tests/pdx_test_file.txt"))
					)
				)
			);
		} catch (FileNotFoundException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
