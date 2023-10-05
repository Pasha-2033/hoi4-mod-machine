package mainclassfolder;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import engine.external_interaction.txt_io;
import engine.parsers.llpl_to_pdx;
import engine.parsers.pdx_to_llpl;
public class main {
    public static void main(String[] args) throws IOException {
		//tests.start_tests();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String in_path, out_path;
		tests.status_output logger = new tests.status_output();
		main_loop: for(;;) {
			System.out.print("\nPath with source: ");
			in_path = reader.readLine();
			if (in_path.isEmpty()) {
				return;
			}
			System.out.print("\nPath to save: ");
			out_path = reader.readLine();
			if (out_path.isEmpty()) {
				return;
			}
			HashMap<String, List<String>> in;
			try {
				in = txt_io.read_folder(new File(in_path), true, logger);
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
					out, false, logger
				);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}