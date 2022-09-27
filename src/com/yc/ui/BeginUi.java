package com.yc.ui;

import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class BeginUi extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public BeginUi(Composite parent, int style) {
		super(parent, style);
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.setBounds(191, 208, 121, 34);
		btnNewButton.setText("人机对战");
		
		Button btnNewButton_1 = new Button(this, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//弹框连接
				
				

				
//				Thread t1 = new Thread(new Listen(), "客户端监听器"); // 线程名
//				t1.setDaemon(true);   //设为守护线程
//				t1.start();
				
				
				LoginUi lu = null;
				try {
					lu = new LoginUi(getShell() , SWT.None);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			
				Start.s1.topControl=lu;
				getShell().layout();
			}
		});
		btnNewButton_1.setText("联网对战");
		btnNewButton_1.setBounds(191, 275, 121, 34);
		
		Button btnNewButton_2 = new Button(this, SWT.NONE);
		btnNewButton_2.setText("关于");
		btnNewButton_2.setBounds(191, 338, 121, 34);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
