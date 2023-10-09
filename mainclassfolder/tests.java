package mainclassfolder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import engine.Status;
import engine.code_revisions.optimiser;
import engine.external_interaction.txt_io;
import engine.parsers.pdx_to_llpl;
import engine.parsers.llpl_to_pdx;
import engine.tokens.Token;
public class tests {
	public static class status_output extends Status<String> {
		public status_output() {
			super();
		}
		protected void update_by_status() {
			System.out.println(status);
		}
	}
	public static void start_tests() {
		//status_output s_out = new status_output();
		//test_token_relations();
		//test_read_write();
		test_files();
		//massive_test_files(s_out, "tests/massive_tests/in", "tests/massive_tests/out");
		//optimise_register();
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
						txt_io.read(new File("tests/pdx_test_file.txt"), null)
					)
				)
			);
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	public static void test_files(){
		try {
			txt_io.write(
				new File("tests/pdx_out_file.txt"), 
				llpl_to_pdx.raw_parse_llpl_to_pdx(
					pdx_to_llpl.raw_parse_pdx_to_llpl(
						txt_io.read(new File("tests/pdx_test_file.txt"), null)
					)
				),
				false,
				null
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void massive_test_files(status_output s_out, String in_path, String out_path) {
		HashMap<String, List<String>> in;
		try {
			in = txt_io.read_folder(new File(in_path), true, s_out);
			HashMap<String, String> out = new HashMap<>();
			Iterator<Entry<String, List<String>>> it = in.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, List<String>> current = it.next();
				out.put(
					out_path + current.getKey().substring(in_path.length()),
					llpl_to_pdx.raw_parse_llpl_to_pdx(
						pdx_to_llpl.raw_parse_pdx_to_llpl(
							current.getValue()
						)
					)
				);
			}
			txt_io.write_files(
				out, false, s_out
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void optimise_register() {
		try {
			List<Token> tokens = pdx_to_llpl.raw_parse_pdx_to_llpl(
				txt_io.read(new File("tests/pdx_test_file.txt"), null)
			);
			for (Token token : tokens) {
				optimiser.optimise(token, new optimiser.optimiser_function[]{
					optimiser.notation.identifier_register,
					optimiser.notation.string_value_super_compression
				});
			}
			txt_io.write(
				new File("tests/pdx_out_file.txt"), 
				llpl_to_pdx.raw_parse_llpl_to_pdx(
					tokens
				),
				false,
				null
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
