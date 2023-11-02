package gui.instances;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import engine.Status;
import engine.code_revisions.optimiser;
import engine.external_interaction.txt_io;
import engine.parsers.llpl_to_pdx;
import engine.parsers.pdx_to_llpl;
import engine.tokens.Token;
import layout.TableLayout;
import gui.designs.Window;
public class MainWindow extends Window {
	private static class status_output extends Status<String> {
		private JLabel label;
		private String prefix;
		public status_output(JLabel label, String prefix) {
			super();
			this.label = label;
			this.prefix = prefix;
		}
		protected void update_by_status() {
			label.setText(prefix + status);
		}
	}

	private static status_output status_read, status_operating, status_write;
	private JTextField in_path, out_path;
	public MainWindow() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		super(new Rectangle(0,0,500,500), JFrame.EXIT_ON_CLOSE, "HOI4MM", null);
		set_centered();
		//set_fullscreen();
		setVisible(true);
	}
	@Override
	protected void init_gui() {
		//create
		double border = 40;
        double size[][] = {
			{border, 0,TableLayout.FILL, border},		// Columns
            {border, 20, 120, 30, 30, 20, 20, 20, 20, 60, TableLayout.FILL, border}		// Rows
		};
        setLayout(new TableLayout(size));
		JLabel version = new JLabel("Версия HOI4MM V0.3");
		JLabel specialisation = new JLabel("<html><pre>Специализация:<br/>- Минимальный GUI приложения<br/>- Минимальный функционал<br/>  - Форматирование кода по стандарту PDX v1.0<br/>  - Экспериментальные функции<br/>  - Поддержка комментариев<pre/><html/>");
		JPanel in_path_container = new JPanel();
		JPanel out_path_container = new JPanel();
		BoxLayout in_path_layout = new BoxLayout(in_path_container, BoxLayout.X_AXIS);
		BoxLayout out_path_layout = new BoxLayout(out_path_container, BoxLayout.X_AXIS);
		JLabel in_path_tt = new JLabel("Путь для чтения:");
		JLabel out_path_tt = new JLabel("Путь для записи:");
		JLabel warnings = new JLabel();
		JLabel experimental = new JLabel("Экспериментальное");
		JCheckBox scope_register = new JCheckBox("Проверка регистра скопа");
		JRadioButton str_no_compression = new JRadioButton("Без сжатия");    ;
		JRadioButton str_light_compression = new JRadioButton("Слабое сжатие");    
		JRadioButton str_hard_compression = new JRadioButton("Сильное сжатие");
		ButtonGroup str_compression_bg = new ButtonGroup();
		JButton init_operations = new JButton("Начать");
		status_read = new status_output(warnings, "Чтение файла: ");
		status_operating = new status_output(warnings, "Обработка файла: ");
		status_write = new status_output(warnings, "Запись файла: ");
		//set
		str_no_compression.setSelected(true);
		scope_register.setToolTipText("Токены-скопы будут приведены к одному регистру");
		str_no_compression.setToolTipText("Строковые значения не будут изменяться");
		str_light_compression.setToolTipText("Строковые значения потеряют избыточные пробелы");
		str_hard_compression.setToolTipText("Строковые значения будут в 1 строку и потеряют избыточные символы разделения");
		str_compression_bg.add(str_no_compression);
		str_compression_bg.add(str_light_compression);
		str_compression_bg.add(str_hard_compression); 
		in_path = new JTextField(40);
		out_path = new JTextField(40);
		in_path_container.setLayout(in_path_layout);
		in_path_container.add(in_path_tt);
		in_path_container.add(in_path);
		out_path_container.setLayout(out_path_layout);
		out_path_container.add(out_path_tt);
		out_path_container.add(out_path);
		add(version, "2,1,c,c");
		add(specialisation, "2,2");
		add(in_path_container, "2,3,c,c");
		add(out_path_container, "2,4,c,c");
		add(warnings, "2,5,c,c");
		add(experimental, "2,6,c,c");
		add(scope_register, "2,7,c,c");
		add(str_no_compression, "2,8,l,c");
		add(str_light_compression, "2,8,c,c");
		add(str_hard_compression, "2,8,r,c");
		add(init_operations, "2,9,c,c");
		//listen
		in_path.addCaretListener(
			new CaretListener() {
				public void caretUpdate(CaretEvent caretEvent) {
				  	if (!(new File(in_path.getText())).exists()){
						warnings.setText("Путь чтения не сущуствует!");
						warnings.setForeground(Color.RED);
						init_operations.setEnabled(false);
					}
					else if (in_path.getText().equals(out_path.getText())) {
						warnings.setText("Файлы будут перезаписаны!");
						warnings.setForeground(Color.BLUE);
						init_operations.setEnabled(true);
					}	
					else {
						warnings.setText("");
						init_operations.setEnabled(true);
					}
				}
			}
		);
		out_path.addCaretListener(
			new CaretListener() {
				public void caretUpdate(CaretEvent caretEvent) {
				  	if ((new File(in_path.getText())).exists()){
						if (in_path.getText().equals(out_path.getText())) {
							warnings.setText("Файлы будут перезаписаны!");
							warnings.setForeground(Color.BLUE);
						}
						else {
							warnings.setText("");
						}
					}
				}
			}
		);
		init_operations.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							warnings.setForeground(Color.BLUE);
							in_path.setEnabled(false);
							out_path.setEnabled(false);
							scope_register.setEnabled(false);
							str_no_compression.setEnabled(false);
							str_light_compression.setEnabled(false);
							str_hard_compression.setEnabled(false);
							init_operations.setEnabled(false);
							HashMap<String, List<String>> in = new HashMap<>(0);
							try {
								File target = new File(in_path.getText());
								if (target.isDirectory()) {
									in = txt_io.read_folder(target, true, status_read);
								}
								else {
									in.put(in_path.getText(), txt_io.read(target, status_read));
								}
								HashMap<String, String> out = new HashMap<>();
								Iterator<Entry<String, List<String>>> it = in.entrySet().iterator();
								while (it.hasNext()) {
									Entry<String, List<String>> current = it.next();
									status_operating.set_status(current.getKey());
									status_operating.update_by_status();
									List<Token> tokens = pdx_to_llpl.raw_parse_pdx_to_llpl(current.getValue());
									List<optimiser.optimiser_function> optimisations = new ArrayList<>(0);
									if (scope_register.isSelected()) {
										optimisations.add(optimiser.notation.identifier_register);
									}
									if (str_light_compression.isSelected()) {
										optimisations.add(optimiser.notation.string_value_compression);
									}
									else if (str_hard_compression.isSelected()) {
										optimisations.add(optimiser.notation.string_value_super_compression);
									}
									if (!optimisations.isEmpty()) {
										for (Token token : tokens) {
											optimiser.optimise(token, optimisations.toArray(new optimiser.optimiser_function[]{}));
										}
									}
									out.put(
										out_path.getText() + current.getKey().substring(in_path.getText().length()),
										llpl_to_pdx.raw_parse_llpl_to_pdx(tokens)
									);
								}
								txt_io.write_files(
									out, false, status_write
								);
								warnings.setText("Завершено");
							} catch (IOException ex) {
								warnings.setForeground(Color.RED);
								warnings.setText("Ошибка: " + ex.getClass().getCanonicalName() + " (" + ex.getMessage() + ")");
							}
							in_path.setEnabled(true);
							out_path.setEnabled(true);
							scope_register.setEnabled(true);
							str_no_compression.setEnabled(true);
							str_light_compression.setEnabled(true);
							str_hard_compression.setEnabled(true);
							init_operations.setEnabled(true);
						}
					}).start();
				}
			}
		);
	}
}