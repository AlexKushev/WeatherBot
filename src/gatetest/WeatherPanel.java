/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gatetest;

import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;

/**
 *
 * @author alexkushev
 */
public class WeatherPanel extends javax.swing.JPanel {

	private APISpeech apiSpeechGenerator;
	private GateRunner gateRunner;

	/**
	 * Creates new form WeatherPanel
	 */
	public WeatherPanel() {
		initComponents();
		apiSpeechGenerator = new APISpeech();
		gateRunner = new GateRunner();
		try {
			gateRunner.runner();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		textArea = new javax.swing.JTextArea();
		textField = new javax.swing.JTextField();
		askButton = new javax.swing.JButton();

		jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		textArea.setEditable(false);
		textArea.setColumns(20);
		textArea.setLineWrap(true);
		textArea.setRows(5);
		jScrollPane1.setViewportView(textArea);

		askButton.setText("Ask");
		askButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				askButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
										.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 441,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(layout.createSequentialGroup().addComponent(textField)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(askButton)))));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, 29,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(askButton))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
	}// </editor-fold>

	private void askButtonActionPerformed(java.awt.event.ActionEvent evt) {
		String userString = textField.getText();
		if (userString.equals("") || userString.equals(" ")) {
			textArea.append("You should enter some text!\n");
			return;
		}
		String generateUserStringRepresentation = apiSpeechGenerator.getUserSpeech(userString);
		textField.setText("");
		textArea.append(generateUserStringRepresentation);
		textArea.append("\n");
		String botAnswer = null;
		try {
			botAnswer = gateRunner.test(userString);
		} catch (ResourceInstantiationException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textArea.append(apiSpeechGenerator.getBotSpeech(botAnswer));
		textArea.append("\n");
	}

	// Variables declaration - do not modify
	private javax.swing.JButton askButton;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea textArea;
	private javax.swing.JTextField textField;
	// End of variables declaration
}
