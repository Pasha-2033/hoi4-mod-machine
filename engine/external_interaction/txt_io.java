package engine.external_interaction;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;
import engine.Status;
public class txt_io {
	public static List<String> read(File file, Status<String> status_object) throws FileNotFoundException {	//todo add encoding
		if (status_object != null) {
			status_object.set_status(file.getPath());
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
	public static void write(File file, String text, boolean rewrite_or_append, Status<String> status_object) throws IOException {
		//false - rewrite, true - append
		if (status_object != null) {
			status_object.set_status(file.getPath());
		}
		file_manager.create_file(file);
		FileWriter mywriter = new FileWriter(file, rewrite_or_append);
		mywriter.write(text);
		mywriter.close();
	}
	public static HashMap<String, List<String>> read_folder(File target_folder, boolean read_nested, Status<String> status_object) throws FileNotFoundException {
		HashMap<String, List<String>> hmap = new HashMap<>();
        for (File f : target_folder.listFiles()) {
            if (f.isDirectory()) {
                if (read_nested){
                    hmap.putAll(read_folder(f, read_nested, status_object));
                }
            } else {
                //если будет узнать что за файл и его смысловую нагрузку - будет достаточно пропарсерить его ключ
                //последний элемнт - файл, остальное - относительный путь
                hmap.put(f.getPath(), read(f, status_object));
            }
        }
        return hmap;
	}
	public static void write_files(HashMap<String, String> data, boolean rewrite_or_append, Status<String> status_object) throws IOException {
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> current = it.next();
			write(new File(current.getKey()), current.getValue(), rewrite_or_append, status_object);
		}
	}
}
