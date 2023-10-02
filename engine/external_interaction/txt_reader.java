package engine.external_interaction;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class txt_reader {
	public static List<String> read(File file) throws FileNotFoundException, IllegalArgumentException {	//todo add encoding
		if (!file.isFile()) {
			throw new IllegalArgumentException(); //to do normal output
		}
		List<String> lines = new ArrayList<String>(0);
		Scanner reader = new Scanner(file, "utf-8"); //https://www.rgagnon.com/javadetails/java-handle-utf8-file-with-bom.html ("?" artefact)
		while (reader.hasNextLine()) {
			lines.add(reader.nextLine());
		}
		//temp script-----------------------------------------------------
		//проверка для спецсимвола, если кодировка utf-8 with bom
		if (!lines.isEmpty()) {
			if (lines.get(0).startsWith("\uFEFF")) {
				lines.set(0, lines.get(0).substring(1));
			}
		}
		//----------------------------------------------------------------
		reader.close();
		return lines;
	}
}
