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
		System.out.println(Token.TokenRelation.get_short_relation(
			new Token.TokenRelation[] {
				Token.TokenRelation.LESS,
				Token.TokenRelation.INVERSIVE
			}
		));
	}
	public static void test_read_write() {
		try {
			System.out.println(
				llpl_to_pdx.parse_llpl_to_pdx(
					pdx_to_llpl.parse_pdx_to_llpl(
						txt_reader.read(new File("tests/pdx_test_file.txt"))
					)
				)
			);
		} catch (FileNotFoundException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
