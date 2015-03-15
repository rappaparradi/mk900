package mk900;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;




import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import models.ExpRawsModel;
import models.ExperimentModel;
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
import entities.ExpRaws;
import entities.Experiment;
import entities.Setting;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Point;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries.SeriesType;

public class Main {
	private DataBindingContext m_bindingContext;

	protected Shell shlMk;
	Composite cmpChart;
	private Text StatusText;
	Mk900 mk900_var;
	ExperimentModel expm = new ExperimentModel();
	ExpRawsModel exp_r_m = new ExpRawsModel();

	public static String dbname = "mk900.db";
	public static Connection c = null;
	public static Statement stmt = null;
	private Table tableSettings;
	private TableItem tableItem;
	private Text txCurSetting;
	public SettingsModel sm = new SettingsModel();
	private Text txtCurSetName;
	private Table tableExp;
	private Table tableExpRaws;
	private Table tableExpSettings;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public void addLogString(String str_par) {
		StatusText.getText();
		StatusText.append(str_par);
	}

	public void clearLogStrings() {
		StatusText.setText("");
	}

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
			tableItem.setText(new String[] { s.getName(), s.getValue(),
					s.getDesc() });
		}

	}

	private void filltableExp() {

		tableExp.removeAll();
		ExperimentModel sm = new ExperimentModel();
		for (Experiment s : sm.findAll()) {
			tableItem = new TableItem(tableExp, SWT.NONE);
			tableItem.setText(new String[] { String.valueOf(s.getId()),
					LongToDate(s.getDate()), });
		}

	} 
	
	private void filltableExpRaws(int exp_id) {

		tableExpRaws.removeAll();
		
		if (exp_id == 0) return;
		
		for (ExpRaws s : exp_r_m.findAll(exp_id)) {
			tableItem = new TableItem(tableExpRaws, SWT.NONE);
			tableItem.setText(new String[] { String.valueOf(s.getTsec()),
					String.valueOf(s.getI()),
					String.valueOf(s.getW_sr()),
					String.valueOf(s.getQ_sr()),
					String.valueOf(s.getY_sr()),
					String.valueOf(s.getV()),
					String.valueOf(s.getR()),
					String.valueOf(s.getK_p()),
					String.valueOf(s.getOpt()),});
		}

	}
	
	private void filltableExpSettings(int exp_id) {

		
		
		tableExpSettings.removeAll();
		
		if (exp_id == 0) return;
		
		SettingsModel sm = new SettingsModel();
		for (Setting s : sm.findAll(exp_id)) {
			tableItem = new TableItem(tableExpSettings, SWT.NONE);
			tableItem.setText(new String[] { s.getName(), s.getValue(),
					s.getDesc() });
		}

	}
	
	//tableExpSettings

	public String LongToDate(long date_exp) {

		java.sql.Date dtDateExp = new java.sql.Date(date_exp);

		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
				"dd/MM/yyyy kk:mm:ss");
		String dateFF = DATE_FORMAT.format(dtDateExp);

		return dateFF;

	}
	
	public void showChart(){
		// create a chart
		Chart chart = new Chart(cmpChart, SWT.NONE);
		double[] ySeries = {0.02,0.04,0.05,0.06,0.09,0.18};   
		// set titles
		chart.getTitle().setText("Line Chart Example");
		chart.getAxisSet().getXAxis(0).getTitle().setText("Data Points");
		chart.getAxisSet().getYAxis(0).getTitle().setText("Amplitude");

		// create line series
		ILineSeries lineSeries = (ILineSeries) chart.getSeriesSet()
		    .createSeries(SeriesType.LINE, "line series");
		lineSeries.setYSeries(ySeries);

		// adjust the axis range
		chart.getAxisSet().adjustRange();
		
		// create a chart
				Chart chart1 = new Chart(cmpChart, SWT.NONE);
				double[] ySeries1 = {0.80,0.90,0.50,0.55,0.09,0.18};   
				// set titles
				chart1.getTitle().setText("Line Chart Example");
				chart1.getAxisSet().getXAxis(0).getTitle().setText("Data Points");
				chart1.getAxisSet().getYAxis(0).getTitle().setText("Amplitude");

				// create line series
				ILineSeries lineSeries1 = (ILineSeries) chart1.getSeriesSet()
				    .createSeries(SeriesType.LINE, "line series");
				lineSeries1.setYSeries(ySeries1);

				// adjust the axis range
				chart1.getAxisSet().adjustRange();
				// create a chart
				Chart chart2 = new Chart(cmpChart, SWT.NONE);
				double[] ySeries2 = {12.80,9.90,1.50,20.55,34.09,8.18};   
				// set titles
				chart2.getTitle().setText("Line Chart Example");
				chart2.getAxisSet().getXAxis(0).getTitle().setText("Data Points");
				chart2.getAxisSet().getYAxis(0).getTitle().setText("Amplitude");

				// create line series
				ILineSeries lineSeries2 = (ILineSeries) chart2.getSeriesSet()
				    .createSeries(SeriesType.LINE, "line series");
				lineSeries2.setYSeries(ySeries1);

				// adjust the axis range
				chart2.getAxisSet().adjustRange();
		
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		UIManager.put("OptionPane.yesButtonText", "Да");
		UIManager.put("OptionPane.noButtonText", "Нет");
		fillTableSettings();
		filltableExp();
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
		tabItem.setImage(SWTResourceManager.getImage(Main.class,
				"/com/sun/javafx/webkit/prism/resources/mediaPlayDisabled.png"));
		tabItem.setText("\u042D\u043A\u0441\u043F\u0435\u0440\u0438\u043C\u0435\u043D\u0442");

		SashForm sashForm = new SashForm(tabFolder, SWT.VERTICAL);
		tabItem.setControl(sashForm);

		Composite composite = new Composite(sashForm, SWT.NONE);

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setLocation(10, 10);
		btnNewButton.setSize(99, 26);
		btnNewButton
				.setImage(SWTResourceManager
						.getImage(Main.class,
								"/com/sun/javafx/webkit/prism/resources/mediaPlayDisabled.png"));
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
				mk900_var = new Mk900(Main.this);

				Thread mk900_Thread = new Thread(new Runnable() {
					public void run() // Этот метод будет выполняться в побочном
										// потоке
					{
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								mk900_var.main();
								filltableExp();
								// StatusText.setText(mk900_var.status_str.toString());
							}
						});
					}
				});
				mk900_Thread.start();

				// mk900_var.status_str;

			}

		});
		btnNewButton
				.setText("\u0417\u0430\u043F\u0443\u0441\u0442\u0438\u0442\u044C");

		StatusText = new Text(sashForm, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		sashForm.setWeights(new int[] { 47, 306 });

		TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1
				.setImage(SWTResourceManager
						.getImage(Main.class,
								"/com/sun/javafx/scene/web/skin/FontBackgroundColor_16x16_JFX.png"));
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
		txtCurSetName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		txtCurSetName.setEnabled(false);

		txCurSetting = new Text(composite_1, SWT.BORDER);
		txCurSetting.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Button btSaveSettings = new Button(composite_1, SWT.NONE);
		btSaveSettings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (sm.edit(new Setting(txtCurSetName.getText(), txCurSetting
						.getText(), sm.find(txtCurSetName.getText(), 0).getDesc(), sm.find(txtCurSetName.getText(), 0).getSort_ind(),
						0))) {
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
		btnNewButton_1.setImage(SWTResourceManager.getImage(Main.class, "/org/eclipse/jface/dialogs/images/message_error.gif"));
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (sm.find(txtCurSetName.getText()) != null) {

					if (sm.delete(new Setting(txtCurSetName.getText(),
							txCurSetting.getText(), sm.find(
									txtCurSetName.getText()).getDesc()))) {
						fillTableSettings();
					} else
						JOptionPane.showMessageDialog(
								null,
								"Не удалось удалить настройку "
										+ txtCurSetName.getText());
				}

			}
		});
		btnNewButton_1.setText("\u0423\u0434\u0430\u043B\u0438\u0442\u044C");

		tableSettings = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		tableSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 5, 10));
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
		tableColumn_2
				.setText("\u041E\u043F\u0438\u0441\u0430\u043D\u0438\u0435");

		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setImage(SWTResourceManager.getImage(Main.class,
				"/com/sun/javafx/scene/web/skin/Paste_16x16_JFX.png"));
		tbtmNewItem.setText("\u0416\u0443\u0440\u043D\u0430\u043B");

		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		composite_2.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				filltableExp();
			}
		});
		tbtmNewItem.setControl(composite_2);
		composite_2.setLayout(new FillLayout(SWT.VERTICAL));

		Composite composite_4 = new Composite(composite_2, SWT.NONE);
		composite_4.setLayout(new GridLayout(2, false));

		Label lblNewLabel = new Label(composite_4, SWT.NONE);
		lblNewLabel
				.setText("\u042D\u043A\u0441\u043F\u0435\u0440\u0438\u043C\u0435\u043D\u0442\u044B:");
		new Label(composite_4, SWT.NONE);

		tableExp = new Table(composite_4, SWT.BORDER | SWT.FULL_SELECTION);
		tableExp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				TableItem[] str = tableExp.getSelection();
				Experiment expm_f = expm.find(Integer.valueOf(str[0].getText()));
				int exp_id;
				if (expm_f != null) exp_id = expm_f.getId();
				else exp_id = 0;
				filltableExpRaws(exp_id);
				filltableExpSettings(exp_id);
				
			}
		});
		GridData gd_tableExp = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				2);
		gd_tableExp.widthHint = 747;
		tableExp.setLayoutData(gd_tableExp);
		tableExp.setHeaderVisible(true);
		tableExp.setLinesVisible(true);

		TableColumn tableColumn_3 = new TableColumn(tableExp, SWT.NONE);
		tableColumn_3.setWidth(100);
		tableColumn_3.setText("\u041D\u043E\u043C\u0435\u0440");

		TableColumn tableColumn_4 = new TableColumn(tableExp, SWT.NONE);
		tableColumn_4.setWidth(179);
		tableColumn_4.setText("\u0414\u0430\u0442\u0430");

		Composite composite_5 = new Composite(composite_4, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 2));
		composite_5.setLayout(new GridLayout(1, false));

		Button btnDelExp = new Button(composite_5, SWT.NONE);
		btnDelExp.setImage(SWTResourceManager.getImage(Main.class, "/org/eclipse/jface/dialogs/images/message_error.gif"));
		btnDelExp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {

				TableItem[] str = tableExp.getSelection();
				Experiment expm_f = expm.find(Integer.valueOf(str[0].getText()));
				if (expm_f != null) {

					int qRez = JOptionPane
							.showConfirmDialog(null, "Удалить эксперимен "
									+ expm_f.getId() + "?",
									"Подтверждение удаления",
									JOptionPane.YES_NO_OPTION);
					if (qRez == JOptionPane.YES_OPTION) {
						exp_r_m.deleteBiExpId(expm_f.getId());
						expm.delete(expm_f);
					}

				}

				filltableExp();

			}
		});
		btnDelExp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnDelExp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnDelExp
				.setText("\u0423\u0434\u0430\u043B\u0438\u0442\u044C \u044D\u043A\u0441\u043F\u0435\u0440\u0438\u043C\u0435\u043D\u0442");

		Button button = new Button(composite_5, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
//				 double[] xData = new double[] { 0.0, 1.0, 2.0 };
//				    double[] yData = new double[] { 2.0, 1.0, 0.0 };
//				 
//				    // Create Chart
//				    Chart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
//				 
				    // Show it
				showChart();
				
				
				
				
			}
		});
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		button.setBounds(0, 0, 75, 25);
		button.setText("\u0412\u044B\u0432\u0435\u0441\u0442\u0438 \u0434\u0438\u0430\u0433\u0440\u0430\u043C\u043C\u044B");
		
		TabFolder tabFolder_1 = new TabFolder(composite_2, SWT.NONE);
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder_1, SWT.NONE);
		tbtmNewItem_1.setText("\u0414\u0430\u043D\u043D\u044B\u0435 \u044D\u043A\u0441\u043F\u0435\u0440\u0438\u043C\u0435\u043D\u0442\u0430");
		
				Composite composite_3 = new Composite(tabFolder_1, SWT.NONE);
				tbtmNewItem_1.setControl(composite_3);
				composite_3.setLayout(new GridLayout(2, false));
						
								tableExpRaws = new Table(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
								tableExpRaws.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
										2, 2));
								tableExpRaws.setBounds(0, 0, 85, 45);
								tableExpRaws.setHeaderVisible(true);
								tableExpRaws.setLinesVisible(true);
								
										TableColumn tblclmnNewColumn = new TableColumn(tableExpRaws, SWT.NONE);
										tblclmnNewColumn.setWidth(100);
										tblclmnNewColumn.setText("Tsec [\u0441]");
										
												TableColumn tblclmnI = new TableColumn(tableExpRaws, SWT.NONE);
												tblclmnI.setWidth(100);
												tblclmnI.setText("i [\u0448\u0430\u0433]");
												
														TableColumn tblclmnWsr = new TableColumn(tableExpRaws, SWT.NONE);
														tblclmnWsr.setWidth(100);
														tblclmnWsr.setText("w_sr [\u043E\u0431\u0440/\u0441]");
														
																TableColumn tblclmnQsr = new TableColumn(tableExpRaws, SWT.NONE);
																tblclmnQsr.setWidth(100);
																tblclmnQsr.setText("q_sr [\u043A\u0433/\u0441]");
																
																		TableColumn tblclmnYsr = new TableColumn(tableExpRaws, SWT.NONE);
																		tblclmnYsr.setWidth(100);
																		tblclmnYsr.setText("y_sr [\u0433\u0440./\u0441]");
																		
																				TableColumn tblclmnV = new TableColumn(tableExpRaws, SWT.NONE);
																				tblclmnV.setWidth(100);
																				tblclmnV.setText("v [\u043C/\u0441]");
																				
																						TableColumn tblclmnR = new TableColumn(tableExpRaws, SWT.NONE);
																						tblclmnR.setWidth(100);
																						tblclmnR.setText("R");
																						
																								TableColumn tblclmnKp = new TableColumn(tableExpRaws, SWT.NONE);
																								tblclmnKp.setWidth(100);
																								tblclmnKp.setText("K_p");
																								
																								TableColumn tableColumn_8 = new TableColumn(tableExpRaws, SWT.NONE);
																								tableColumn_8.setWidth(100);
																								tableColumn_8.setText("\u041E\u043F\u0442\u0438\u043C\u0430\u043B\u044C\u043D\u043E");
		
		TabItem tbtmNewItem_2 = new TabItem(tabFolder_1, SWT.NONE);
		tbtmNewItem_2.setText("\u041D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0438");
		
		Composite composite_6 = new Composite(tabFolder_1, SWT.NONE);
		tbtmNewItem_2.setControl(composite_6);
		composite_6.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		tableExpSettings = new Table(composite_6, SWT.BORDER | SWT.FULL_SELECTION);
		tableExpSettings.setLinesVisible(true);
		tableExpSettings.setHeaderVisible(true);
		tableExpSettings.setBounds(0, 0, 889, 315);
		
		TableColumn tableColumn_5 = new TableColumn(tableExpSettings, SWT.NONE);
		tableColumn_5.setWidth(100);
		tableColumn_5.setText("\u0418\u043C\u044F");
		
		TableColumn tableColumn_6 = new TableColumn(tableExpSettings, SWT.NONE);
		tableColumn_6.setWidth(100);
		tableColumn_6.setText("\u0417\u043D\u0430\u0447\u0435\u043D\u0438\u0435");
		
		TableColumn tableColumn_7 = new TableColumn(tableExpSettings, SWT.NONE);
		tableColumn_7.setWidth(343);
		tableColumn_7.setText("\u041E\u043F\u0438\u0441\u0430\u043D\u0438\u0435");
		
		TabItem tabItem_2 = new TabItem(tabFolder, SWT.NONE);
		tabItem_2.setText("\u0414\u0438\u0430\u0433\u0440\u0430\u043C\u043C\u044B");
		
		cmpChart = new Composite(tabFolder, SWT.NONE);
		tabItem_2.setControl(cmpChart);
		cmpChart.setLayout(new FillLayout(SWT.HORIZONTAL));
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

			// if (user_version == 2) {
			//
			// ps = Main.c
			// .prepareStatement("ALTER TABLE SETTINGS ADD COLUMN sort_ind INT;");
			// ps.execute();
			//
			// }
			if (user_version == 3) {

				ps = Main.c
						.prepareStatement("ALTER TABLE SETTINGS ADD COLUMN exp_id INT;");
				ps.execute();

			}

			ps = Main.c.prepareStatement("PRAGMA user_version = 4;");
			ps.execute();
			// while (rs.next()) {
			// For example, to set the user version to 123, execute the SQL
			// statement "PRAGMA user_version = 123;".
			// To check the user version, execute the SQL statement
			// "PRAGMA user_version;" and read the resulting row.

			// Таблица текущих значений исходных данных
			String sql = "CREATE TABLE IF NOT EXISTS SETTINGS "
					+ "(name CHAR(50)     NOT NULL ,"
					+ "id Integer PRIMARY KEY     autoincrement ,"
					+ "desc TEXT," + "sort_ind INT," + "exp_id INT,"
					+ " value       TEXT)";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE IF NOT EXISTS experiments "
					+ "(id INTEGER PRIMARY KEY     AUTOINCREMENT,"
					+ "date long)";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE IF NOT EXISTS exp_raws "
					+ "(id INTEGER PRIMARY KEY     AUTOINCREMENT,"
					+ "exp_id INT not null," + "Tsec INT," + "i INT,"
					+ "w_sr REAL," + "q_sr REAL," + "y_sr REAL," + "v    REAL,"
					+ "R    REAL," + "K_p  REAL," + "opt  TINYINT)";
			stmt.executeUpdate(sql);

			stmt.close();
			SettingsModel sm = new SettingsModel();
			if (sm.find("Tsec") == null)
				sm.create(new Setting("Tsec", "0.0",
						"Текущее время наблюдения процесса [с]", 1));
			else {
				sm.editColumn(new Setting("Tsec", "",
						"Текущее время наблюдения процесса [с]"), "desc");
				sm.editColumn(new Setting("Tsec", "", "", 1), "sort_ind");
			}
			if (sm.find("v") == null)
				sm.create(new Setting("v", "0.0", "Cкорость комбайна [м/с]", 2));
			else {
				sm.editColumn(
						new Setting("v", "0.0", "Cкорость комбайна [м/с]"),
						"desc");
				sm.editColumn(new Setting("v", "", "", 2), "sort_ind");
			}
			if (sm.find("nom") == null)
				sm.create(new Setting("nom", "1", "Номер эксперимента", 3));
			else {
				sm.editColumn(new Setting("nom", "1", "Номер эксперимента"),
						"desc");
				sm.editColumn(new Setting("nom", "", "", 3), "sort_ind");
			}
			if (sm.find("tau") == null)
				sm.create(new Setting("tau", "0.2", "Темп опроса датчиков [с]",
						4));
			else {
				sm.editColumn(new Setting("tau", "0.2",
						"Темп опроса датчиков [с]"), "desc");
				sm.editColumn(new Setting("tau", "", "", 4), "sort_ind");
			}
			if (sm.find("T_H") == null)
				sm.create(new Setting(
						"T_H",
						"5",
						"Время наблюдения для формиров. \"большой\" выборки [с]",
						5));
			else {
				sm.editColumn(
						new Setting("T_H", "5",
								"Время наблюдения для формиров. \"большой\" выборки [с]"),
						"desc");
				sm.editColumn(new Setting("T_H", "", "", 5), "sort_ind");
			}
			if (sm.find("T_L") == null)
				sm.create(new Setting(
						"T_L",
						"1",
						"Время наблюдения для формиров. \"малой\" выборки   [с]",
						6));
			else {
				sm.editColumn(
						new Setting("T_L", "1",
								"Время наблюдения для формиров. \"малой\" выборки   [с]"),
						"desc");
				sm.editColumn(new Setting("T_L", "", "", 6), "sort_ind");
			}
			if (sm.find("p") == null)
				sm.create(new Setting("p", "5",
						"Макс. значение цикла опроса датчиков", 7));
			else {
				sm.editColumn(new Setting("p", "5",
						"Макс. значение цикла опроса датчиков"), "desc");
				sm.editColumn(new Setting("p", "", "", 7), "sort_ind");
			}
			if (sm.find("IpzI") == null)
				sm.create(new Setting("IpzI", "0.015",
						"Допустимые потери зерна (ПЗ)", 8));
			else {
				sm.editColumn(new Setting("IpzI", "0.015",
						"Допустимые потери зерна (ПЗ)"), "desc");
				sm.editColumn(new Setting("IpzI", "", "", 8), "sort_ind");
			}
			if (sm.find("lam") == null)
				sm.create(new Setting("lam", "0.4",
						"Стандартное отношение зерна к соломе", 9));
			else {
				sm.editColumn(new Setting("lam", "0.4",
						"Стандартное отношение зерна к соломе"), "desc");
				sm.editColumn(new Setting("lam", "", "", 9), "sort_ind");
			}
			if (sm.find("lam_1") == null)
				sm.create(new Setting("lam_1", "0.4",
						"Истинное отношение зерна к соломе", 10));
			else {
				sm.editColumn(new Setting("lam_1", "0.4",
						"Истинное отношение зерна к соломе"), "desc");
				sm.editColumn(new Setting("lam_1", "", "", 10), "sort_ind");
			}
			if (sm.find("q_nom") == null)
				sm.create(new Setting("q_nom", "8.0",
						"Номинальная подача хлебной массы (ПХМ)", 11));
			else {
				sm.editColumn(new Setting("q_nom", "8.0",
						"Номинальная подача хлебной массы (ПХМ)"), "desc");
				sm.editColumn(new Setting("q_nom", "", "", 11), "sort_ind");
			}
			if (sm.find("delt") == null)
				sm.create(new Setting("delt", "0.12",
						"Допустимая величина изменения угловой скорости (УС)",
						12));
			else {
				sm.editColumn(new Setting("delt", "0.12",
						"Допустимая величина изменения угловой скорости (УС)"),
						"desc");
				sm.editColumn(new Setting("delt", "", "", 12), "sort_ind");
			}
			if (sm.find("delt_y") == null)
				sm.create(new Setting("delt_y", "0.03", "Допуск на дрейф ПЗ",
						13));
			else {
				sm.editColumn(new Setting("delt_y", "0.03",
						"Допуск на дрейф ПЗ"), "desc");
				sm.editColumn(new Setting("delt_y", "", "", 13), "sort_ind");
			}
			if (sm.find("delt_w") == null)
				sm.create(new Setting("delt_w", "0.03", "Допуск на дрейф УС",
						14));
			else {
				sm.editColumn(new Setting("delt_w", "0.03",
						"Допуск на дрейф УС"), "desc");
				sm.editColumn(new Setting("delt_w", "", "", 14), "sort_ind");
			}
			if (sm.find("delt_K") == null)
				sm.create(new Setting("delt_K", "0.15",
						"Допуск на дрейф целевой функции", 15));
			else {
				sm.editColumn(new Setting("delt_K", "0.15",
						"Допуск на дрейф целевой функции"), "desc");
				sm.editColumn(new Setting("delt_K", "", "", 15), "sort_ind");
			}
			if (sm.find("sigm_w") == null)
				sm.create(new Setting("sigm_w", "0.05",
						"Допуск на значение w_opt", 16));
			else {
				sm.editColumn(new Setting("sigm_w", "0.05",
						"Допуск на значение w_opt"), "desc");
				sm.editColumn(new Setting("sigm_w", "", "", 16), "sort_ind");
			}
			if (sm.find("sigm_q") == null)
				sm.create(new Setting("sigm_q", "0.05",
						"Допуск на значение q_opt", 17));
			else {
				sm.editColumn(new Setting("sigm_q", "0.05",
						"Допуск на значение q_opt"), "desc");
				sm.editColumn(new Setting("sigm_q", "", "", 17), "sort_ind");
			}
			if (sm.find("R") == null)
				sm.create(new Setting("R", "0.157",
						"Коэффициент настройки системы на оптимальный режим",
						18));
			else {
				sm.editColumn(new Setting("R", "0.157",
						"Коэффициент настройки системы на оптимальный режим"),
						"desc");
				sm.editColumn(new Setting("R", "", "", 18), "sort_ind");
			}
			if (sm.find("t_dv") == null)
				sm.create(new Setting("t_dv", "0.2",
						"Время включения исполнительного механизма [с]", 19));
			else {
				sm.editColumn(new Setting("t_dv", "0.2",
						"Время включения исполнительного механизма [с]"),
						"desc");
				sm.editColumn(new Setting("t_dv", "", "", 19), "sort_ind");
			}
			if (sm.find("T0") == null)
				sm.create(new Setting("T0", "1.0",
						"Постоянная времени объекта [с]", 20));
			else {
				sm.editColumn(new Setting("T0", "1.0",
						"Постоянная времени объекта [с]"), "desc");
				sm.editColumn(new Setting("T0", "", "", 20), "sort_ind");
			}
			if (sm.find("r_f") == null)
				sm.create(new Setting("r_f", "0", "Кол-во считываний Q", 21));
			else {
				sm.editColumn(new Setting("r_f", "0", "Кол-во считываний Q"),
						"desc");
				sm.editColumn(new Setting("r_f", "", "", 21), "sort_ind");
			}
			if (sm.find("q_min") == null)
				sm.create(new Setting("q_min", "20.0",
						"Min значение урожайности [ц/га]", 22));
			else {
				sm.editColumn(new Setting("q_min", "20.0",
						"Min значение урожайности [ц/га]"), "desc");
				sm.editColumn(new Setting("q_min", "", "", 22), "sort_ind");
			}
			if (sm.find("q_max") == null)
				sm.create(new Setting("q_max", "40.0",
						"Max значение урожайности [ц/га]", 23));
			else {
				sm.editColumn(new Setting("q_max", "40.0",
						"Max значение урожайности [ц/га]"), "desc");
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
