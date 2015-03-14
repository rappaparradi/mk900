package mk900;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;

import models.SettingsModel;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import models.*;
import entities.Setting;

public class Main {
	private DataBindingContext m_bindingContext;

	protected Shell shlMk;
	private Text StatusText;
	Mk900 mk900_var;

	public static String dbname = "mk900.db";
	public static Connection c = null;
	public static Statement stmt = null;
	private Table tableSettings;
	private TableItem tableItem;
	private Text txCurSetting;
	public SettingsModel sm = new SettingsModel();
	private Text txtCurSetName;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.open();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void fillTableSettings() {

		tableSettings.removeAll();
		SettingsModel sm = new SettingsModel();
		for (Setting s : sm.findAll()) {
			tableItem = new TableItem(tableSettings, SWT.NONE);
			tableItem.setText(new String[] { s.getName(), s.getValue(), s.getDesc() });
		}

	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		fillTableSettings();
		shlMk.open();
		shlMk.layout();
		while (!shlMk.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlMk = new Shell();
		shlMk.addShellListener(new ShellAdapter() {
			@Override
			public void shellIconified(ShellEvent e) {
			}

			@Override
			public void shellActivated(ShellEvent e) {
			}
		});
		shlMk.setSize(923, 422);
		shlMk.setText("MK900");
		shlMk.setLayout(new FillLayout(SWT.HORIZONTAL));

		TabFolder tabFolder = new TabFolder(shlMk, SWT.NONE);

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("\u0413\u043B\u0430\u0432\u043D\u0430\u044F");

		SashForm sashForm = new SashForm(tabFolder, SWT.VERTICAL);
		tabItem.setControl(sashForm);

		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(null);

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton
				.setImage(SWTResourceManager
						.getImage(Main.class,
								"/com/sun/javafx/webkit/prism/resources/mediaPlayDisabled.png"));
		btnNewButton.setBounds(10, 10, 97, 25);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {

				// MessageDialog.openWarning(null, "Предупреждение",
				// "Привет, Раппапарради!");
				mk900_var = new Mk900();

				mk900_var.main();
				StatusText.setText(mk900_var.status_str.toString());

				// mk900_var.status_str;

			}

		});
		btnNewButton
				.setText("\u0417\u0430\u043F\u0443\u0441\u0442\u0438\u0442\u044C");

		StatusText = new Text(sashForm, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		sashForm.setWeights(new int[] { 44, 187 });

		TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1
				.setText("\u041D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0438");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite_1);
		composite_1.setLayout(new GridLayout(5, false));

		Label label = new Label(composite_1, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("\u0412\u044B\u0431\u0440\u0430\u043D\u043D\u0430\u044F \u043D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0430:");

		txtCurSetName = new Text(composite_1, SWT.BORDER);
		txtCurSetName.setEnabled(false);
		txtCurSetName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		txCurSetting = new Text(composite_1, SWT.BORDER);
		txCurSetting.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Button btSaveSettings = new Button(composite_1, SWT.NONE);
		btSaveSettings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (sm.edit(new Setting(txtCurSetName.getText(), txCurSetting
						.getText(), sm.find(txtCurSetName.getText()).getDesc()))) {
					fillTableSettings();
				} else
					JOptionPane.showMessageDialog(
							null,
							"Не удалось сохранить настройку "
									+ txtCurSetName.getText());

			}
		});
		btSaveSettings
				.setText("\u0418\u0437\u043C\u0435\u043D\u0438\u0442\u044C");
		
		Button btnNewButton_1 = new Button(composite_1, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (sm.delete(new Setting(txtCurSetName.getText(), txCurSetting
						.getText(), sm.find(txtCurSetName.getText()).getDesc()))) {
					fillTableSettings();
				} else
					JOptionPane.showMessageDialog(
							null,
							"Не удалось удалить настройку "
									+ txtCurSetName.getText());

			
			}
		});
		btnNewButton_1.setText("\u0423\u0434\u0430\u043B\u0438\u0442\u044C");

		tableSettings = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		tableSettings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] str = tableSettings.getSelection();
				Setting lset = sm.find(str[0].getText());
				if (lset != null) {
					txtCurSetName.setText(lset.getName());
					txCurSetting.setText(lset.getValue());
				}
			}
		});
		GridData gd_tableSettings = new GridData(SWT.FILL, SWT.FILL, true,
				true, 5, 10);
		gd_tableSettings.widthHint = 187;
		tableSettings.setLayoutData(gd_tableSettings);
		tableSettings.setHeaderVisible(true);
		tableSettings.setLinesVisible(true);

		TableColumn tableColumn = new TableColumn(tableSettings, SWT.NONE);
		tableColumn.setWidth(100);
		tableColumn.setText("\u0418\u043C\u044F");

		TableColumn tableColumn_1 = new TableColumn(tableSettings, SWT.NONE);
		tableColumn_1.setWidth(100);
		tableColumn_1
				.setText("\u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435");
		
		TableColumn tableColumn_2 = new TableColumn(tableSettings, SWT.NONE);
		tableColumn_2.setWidth(343);
		tableColumn_2.setText("\u041E\u043F\u0438\u0441\u0430\u043D\u0438\u0435");
		m_bindingContext = initDataBindings();

		dbInit();

	}

	public static void dbInit() {

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + dbname);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();

			PreparedStatement ps = Main.c
					.prepareStatement("PRAGMA user_version;");
			ResultSet rs = ps.executeQuery();
			int user_version = 0;
			while (rs.next()) {
				user_version = rs.getInt("user_version");
			}

			if (user_version == 1) {

				ps = Main.c
						.prepareStatement("ALTER TABLE SETTINGS ADD COLUMN desc TEXT;");
				ps.execute();

			}
			
//			if (user_version == 2) {
//
//				ps = Main.c
//						.prepareStatement("ALTER TABLE SETTINGS ADD COLUMN sort_ind INT;");
//				ps.execute();
//
//			}

			ps = Main.c.prepareStatement("PRAGMA user_version = 3;");
			ps.execute();
			// while (rs.next()) {
			// For example, to set the user version to 123, execute the SQL
			// statement "PRAGMA user_version = 123;".
			// To check the user version, execute the SQL statement
			// "PRAGMA user_version;" and read the resulting row.

			String sql = "CREATE TABLE IF NOT EXISTS SETTINGS "
					+ "(name CHAR(50) PRIMARY KEY     NOT NULL," + "desc TEXT," + "sort_ind INT,"
					+ " value       TEXT)";
			stmt.executeUpdate(sql);
			stmt.close();
			SettingsModel sm = new SettingsModel();
			if (sm.find("Tsec") == null)
				sm.create(new Setting("Tsec", "0.0", "Текущее время наблюдения процесса [с]", 1));
			else
			{
				sm.editColumn(new Setting("Tsec", "", "Текущее время наблюдения процесса [с]"), "desc");
				sm.editColumn(new Setting("Tsec", "", "", 1), "sort_ind");
			}
			if (sm.find("v") == null)
				sm.create(new Setting("v", "0.0", "Cкорость комбайна [м/с]", 2));
			else
			{
				sm.editColumn(new Setting("v", "0.0", "Cкорость комбайна [м/с]"), "desc");
				sm.editColumn(new Setting("v", "", "", 2), "sort_ind");
			}
			if (sm.find("nom") == null)
				sm.create(new Setting("nom", "1","Номер эксперимента", 3));
			else
			{
				sm.editColumn(new Setting("nom", "1","Номер эксперимента"), "desc");
				sm.editColumn(new Setting("nom", "", "", 3), "sort_ind");
			}
			if (sm.find("tau") == null)
				sm.create(new Setting("tau", "0.2","Темп опроса датчиков [с]", 4));
			else
			{
				sm.editColumn(new Setting("tau", "0.2","Темп опроса датчиков [с]"), "desc");
				sm.editColumn(new Setting("tau", "", "", 4), "sort_ind");
			}
			if (sm.find("T_H") == null)
				sm.create(new Setting("T_H", "5","Время наблюдения для формиров. \"большой\" выборки [с]", 5));
			else
			{
				sm.editColumn(new Setting("T_H", "5","Время наблюдения для формиров. \"большой\" выборки [с]"), "desc");
				sm.editColumn(new Setting("T_H", "", "", 5), "sort_ind");
			}
			if (sm.find("T_L") == null)
				sm.create(new Setting("T_L", "1","Время наблюдения для формиров. \"малой\" выборки   [с]", 6));
			else
			{
				sm.editColumn(new Setting("T_L", "1","Время наблюдения для формиров. \"малой\" выборки   [с]"), "desc");
				sm.editColumn(new Setting("T_L", "", "", 6), "sort_ind");
			}
			if (sm.find("p") == null)
				sm.create(new Setting("p", "5","Макс. значение цикла опроса датчиков", 7));
			else
			{
				sm.editColumn(new Setting("p", "5","Макс. значение цикла опроса датчиков"), "desc");
				sm.editColumn(new Setting("p", "", "", 7), "sort_ind");
			}
			if (sm.find("IpzI") == null)
				sm.create(new Setting("IpzI", "0.015","Допустимые потери зерна (ПЗ)", 8));
			else
			{
				sm.editColumn(new Setting("IpzI", "0.015","Допустимые потери зерна (ПЗ)"), "desc");
				sm.editColumn(new Setting("IpzI", "", "", 8), "sort_ind");
			}
			if (sm.find("lam") == null)
				sm.create(new Setting("lam", "0.4","Стандартное отношение зерна к соломе", 9));
			else
			{
				sm.editColumn(new Setting("lam", "0.4","Стандартное отношение зерна к соломе"), "desc");
				sm.editColumn(new Setting("lam", "", "", 9), "sort_ind");
			}
			if (sm.find("lam_1") == null)
				sm.create(new Setting("lam_1", "0.4","Истинное отношение зерна к соломе", 10));
			else
			{
				sm.editColumn(new Setting("lam_1", "0.4","Истинное отношение зерна к соломе"), "desc");
				sm.editColumn(new Setting("lam_1", "", "", 10), "sort_ind");
			}
			if (sm.find("q_nom") == null)
				sm.create(new Setting("q_nom", "8.0","Номинальная подача хлебной массы (ПХМ)", 11));
			else
			{
				sm.editColumn(new Setting("q_nom", "8.0","Номинальная подача хлебной массы (ПХМ)"), "desc");
				sm.editColumn(new Setting("q_nom", "", "", 11), "sort_ind");
			}
			if (sm.find("delt") == null)
				sm.create(new Setting("delt", "0.12","Допустимая величина изменения угловой скорости (УС)", 12));
			else
			{
				sm.editColumn(new Setting("delt", "0.12","Допустимая величина изменения угловой скорости (УС)"), "desc");
				sm.editColumn(new Setting("delt", "", "", 12), "sort_ind");
			}
			if (sm.find("delt_y") == null)
				sm.create(new Setting("delt_y", "0.03","Допуск на дрейф ПЗ", 13));
			else
			{
				sm.editColumn(new Setting("delt_y", "0.03","Допуск на дрейф ПЗ"), "desc");
				sm.editColumn(new Setting("delt_y", "", "", 13), "sort_ind");
			}
			if (sm.find("delt_w") == null)
				sm.create(new Setting("delt_w", " 0.03","Допуск на дрейф УС", 14));
			else
			{
				sm.editColumn(new Setting("delt_w", " 0.03","Допуск на дрейф УС"), "desc");
				sm.editColumn(new Setting("delt_w", "", "", 14), "sort_ind");
			}
			if (sm.find("delt_K") == null)
				sm.create(new Setting("delt_K", "0.15","Допуск на дрейф целевой функции", 15));
			else
			{
				sm.editColumn(new Setting("delt_K", "0.15","Допуск на дрейф целевой функции"), "desc");
				sm.editColumn(new Setting("delt_K", "", "", 15), "sort_ind");
			}
			if (sm.find("sigm_w") == null)
				sm.create(new Setting("sigm_w", "0.05","Допуск на значение w_opt", 16));
			else
			{
				sm.editColumn(new Setting("sigm_w", "0.05","Допуск на значение w_opt"), "desc");
				sm.editColumn(new Setting("sigm_w", "", "", 16), "sort_ind");
			}
			if (sm.find("sigm_q") == null)
				sm.create(new Setting("sigm_q", "0.05","Допуск на значение q_opt", 17));
			else
			{
				sm.editColumn(new Setting("sigm_q", "0.05","Допуск на значение q_opt"), "desc");
				sm.editColumn(new Setting("sigm_q", "", "", 17), "sort_ind");
			}
			if (sm.find("R") == null)
				sm.create(new Setting("R", "0.157","Коэффициент настройки системы на оптимальный режим", 18));
			else
			{
				sm.editColumn(new Setting("R", "0.157","Коэффициент настройки системы на оптимальный режим"), "desc");
				sm.editColumn(new Setting("R", "", "", 18), "sort_ind");
			}
			if (sm.find("t_dv") == null)
				sm.create(new Setting("t_dv", "0.2","Время включения исполнительного механизма [с]", 19));
			else
			{
				sm.editColumn(new Setting("t_dv", "0.2","Время включения исполнительного механизма [с]"), "desc");
				sm.editColumn(new Setting("t_dv", "", "", 19), "sort_ind");
			}
			if (sm.find("T0") == null)
				sm.create(new Setting("T0", "1.0","Постоянная времени объекта [с]", 20));
			else
			{
				sm.editColumn(new Setting("T0", "1.0","Постоянная времени объекта [с]"), "desc");
				sm.editColumn(new Setting("T0", "", "", 20), "sort_ind");
			}
			if (sm.find("r_f") == null)
				sm.create(new Setting("r_f", "0","Кол-во считываний Q", 21));
			else
			{
				sm.editColumn(new Setting("r_f", "0","Кол-во считываний Q"), "desc");
				sm.editColumn(new Setting("r_f", "", "", 21), "sort_ind");
			}
			if (sm.find("q_min") == null)
				sm.create(new Setting("q_min", "20.0","Min значение урожайности [ц/га]", 22));
			else
			{
				sm.editColumn(new Setting("q_min", "20.0","Min значение урожайности [ц/га]"), "desc");
				sm.editColumn(new Setting("q_min", "", "", 22), "sort_ind");
			}
			if (sm.find("q_max") == null)
				sm.create(new Setting("q_max", "40.0","Max значение урожайности [ц/га]", 23));
			else
			{
				sm.editColumn(new Setting("q_max", "40.0","Max значение урожайности [ц/га]"), "desc");
				sm.editColumn(new Setting("q_max", "", "", 23), "sort_ind");
			}
			// c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
}
