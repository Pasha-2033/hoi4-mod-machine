package engine.external_interaction;
import java.io.File;
import java.io.IOException;
public class file_manager {
	public static void create_file(File file) throws IOException{
		if (!file.exists()) {
			int file_separation = Math.max(file.getPath().lastIndexOf('\\'), file.getPath().lastIndexOf('/'));
			if (file_separation > -1) {
				create_folder(new File(file.getPath().substring(0, file_separation)));
			}
			file.createNewFile();
		}
	}
	public static void create_folder(File file) {
		if (!file.exists()) {
			file.mkdirs();
		}
	}
}