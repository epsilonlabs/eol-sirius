/*********************************************************************
* Copyright (c) 2021 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package org.eclipse.epsilon.sirius.widget.examples.statemachine.design;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.common.dt.console.EpsilonConsole;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.sirius.widget.examples.statemachine.InitialState;
import org.eclipse.epsilon.sirius.widget.examples.statemachine.State;
import org.eclipse.epsilon.sirius.widget.examples.statemachine.StateMachine;
import org.eclipse.epsilon.sirius.widget.examples.statemachine.Transition;
import org.eclipse.sirius.business.api.query.EObjectQuery;
import org.eclipse.sirius.common.ui.tools.api.util.EclipseUIUtil;
import org.eclipse.sirius.ui.business.api.dialect.DialectEditor;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

/**
 * The services class used by VSM.
 */
public class Services {
    
	EolModule module;
	
	public void execute(StateMachine self) { 		
		module = new EolModule();
		module.getContext().setOutputStream(EpsilonConsole.getInstance().getDebugStream());
		module.getContext().setErrorStream(EpsilonConsole.getInstance().getErrorStream());
		module.getContext().setWarningStream(EpsilonConsole.getInstance().getWarningStream());
    	InitialState initial = self.getInitialState(); 	
    	new Thread(() -> runStatemachine(initial)).start();
    }
	
	private void runStatemachine(State currentState) {
		try {
			selectElement(currentState);
			module.parse(currentState.getAction());
			if (module.getParseProblems().isEmpty()) {
				module.execute();			
				for (Transition t : currentState.getOutgoing()) {
					module.parse("return " + t.getGuard());
					if (module.execute().equals(true)) {
						selectElement(t);
						runStatemachine(t.getTo());
						break;
					}
				}	
			} else {
				System.err.println("Errors...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void selectElement(EObject element) throws InterruptedException {
		IWorkbenchPage page = EclipseUIUtil.getActivePage();
		IEditorPart activeEditor = page.getActiveEditor();
		List<DRepresentationElement> elementsRepresentingTarget = new ArrayList<>();
		
		Collection<EObject> result = new EObjectQuery(element).getInverseReferences(ViewpointPackage.Literals.DSEMANTIC_DECORATOR__TARGET);

		for (EObject e : result) {
			if (e instanceof DRepresentationElement) {
				elementsRepresentingTarget.add((DRepresentationElement) e);
			}
		}
		
		if (activeEditor instanceof DialectEditor) {
			Display.getDefault().asyncExec(() -> DialectUIManager.INSTANCE.selectAndReveal((DialectEditor) activeEditor, elementsRepresentingTarget));
			Thread.sleep(500);
		}
	}
}
