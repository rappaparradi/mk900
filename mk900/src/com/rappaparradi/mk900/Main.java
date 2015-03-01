package com.rappaparradi.mk900;

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


public class Main {
	private DataBindingContext m_bindingContext;

	protected Shell shell;
	private Text StatusText;
	Mk900 mk900_var;
	private Text txtTsec;
	private Text txtv;
	private Text txtnom;
	private Text txttau;
	private Text txtT_H;
	private Text txtT_L;
	private Text txtp;
	private Text txtIpzI;
	private Text txtlam;
	private Text txtlam_1;
	private Text txtq_nom;

	/**
	 * Launch the application.
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

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellIconified(ShellEvent e) {
			}
			@Override
			public void shellActivated(ShellEvent e) {
			}
		});
		shell.setSize(450, 414);
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("\u0413\u043B\u0430\u0432\u043D\u0430\u044F");
		
		SashForm sashForm = new SashForm(tabFolder, SWT.VERTICAL);
		tabItem.setControl(sashForm);
		
		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(null);
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setImage(SWTResourceManager.getImage(Main.class, "/com/sun/javafx/webkit/prism/resources/mediaPlayDisabled.png"));
		btnNewButton.setBounds(10, 10, 97, 25);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
//				MessageDialog.openWarning(null, "Предупреждение", "Привет, Раппапарради!");
				
				
				mk900_var.main();
				StatusText.setText(mk900_var.status_str.toString());
				txtTsec.setText(Double.toString(mk900_var.Tsec));
//				mk900_var.status_str;
				
			}
			
		});
		btnNewButton.setText("\u0417\u0430\u043F\u0443\u0441\u0442\u0438\u0442\u044C");
		
		StatusText = new Text(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		sashForm.setWeights(new int[] {44, 187});
		
		TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("\u041D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0438");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite_1);
		composite_1.setLayout(new GridLayout(2, false));
		
		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Tsec:");
		
		txtTsec = new Text(composite_1, SWT.BORDER);
		txtTsec.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		
		Label lblV = new Label(composite_1, SWT.NONE);
		lblV.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblV.setText("v:");
		
		txtv = new Text(composite_1, SWT.BORDER);
		txtv.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblNom = new Label(composite_1, SWT.NONE);
		lblNom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNom.setText("nom:");
		
		txtnom = new Text(composite_1, SWT.BORDER);
		txtnom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblTau = new Label(composite_1, SWT.NONE);
		lblTau.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTau.setText("tau:");
		
		txttau = new Text(composite_1, SWT.BORDER);
		txttau.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblTh = new Label(composite_1, SWT.NONE);
		lblTh.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTh.setText("T_H:");
		
		txtT_H = new Text(composite_1, SWT.BORDER);
		txtT_H.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblTl = new Label(composite_1, SWT.NONE);
		lblTl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTl.setText("T_L:");
		
		txtT_L = new Text(composite_1, SWT.BORDER);
		txtT_L.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblP = new Label(composite_1, SWT.NONE);
		lblP.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblP.setText("p:");
		
		txtp = new Text(composite_1, SWT.BORDER);
		txtp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblIpzi = new Label(composite_1, SWT.NONE);
		lblIpzi.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIpzi.setText("IpzI:");
		
		txtIpzI = new Text(composite_1, SWT.BORDER);
		txtIpzI.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblLam = new Label(composite_1, SWT.NONE);
		lblLam.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLam.setText("lam:");
		
		txtlam = new Text(composite_1, SWT.BORDER);
		txtlam.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblLam_1 = new Label(composite_1, SWT.NONE);
		lblLam_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLam_1.setText("lam_1:");
		
		txtlam_1 = new Text(composite_1, SWT.BORDER);
		txtlam_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblQnom = new Label(composite_1, SWT.NONE);
		lblQnom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblQnom.setText("q_nom:");
		
		txtq_nom = new Text(composite_1, SWT.BORDER);
		txtq_nom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		m_bindingContext = initDataBindings();
		
		Mk900 mk900_var = new Mk900();

	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}
}
