# Embedding the Epsilon Object Language (EOL) into Sirius

This repository contains a minimal example showing how to embed an EOL Editor into the properties view of a Sirius based modeller in the form of a state-machine language. In this example, states and transitions are specified graphically (Sirius), but guards in transitions and actions are specified textually (EOL). The EOL editor widget provides developer assistance mechanisms such as error markers and syntax highlighting. There is also a minimal example showing the execution of the state machine from within the model editor.

![example](https://user-images.githubusercontent.com/10147329/127321717-b0427f2c-c1da-4ccf-867c-3c25528f11e3.gif)

## Getting started

### Prerequisites
- Eclipse ≥ 2020-06
- Epsilon ≥ 2.0
- Sirius ≥ 6.3

### Running the example

1. Ensure IDE installed with the prerequisites
2. Clone repository
3. Create new workspace
4. Import all projects under plugins and example into the workspace
5. Launch the provided Eclipse runtime configuration (EOL State Machine)
6. Import the project under sample into the workspace
7. Open the diagram (expand the`.statemachine` model in the model explorer and double click "new State Machine Diagram")
![image](https://user-images.githubusercontent.com/10147329/127321103-03dc2c31-2c1c-4f12-b0f1-4d8be079d144.png)
9. Right-click on the editor, and "Run State Machine"
![image](https://user-images.githubusercontent.com/10147329/127321464-d6103dcf-e2af-4650-b8c5-109a66a2dc24.png)
11. View EOL expressions by clicking on a state/transition in the editor, and then opening the EOL tab in the properties view
![image](https://user-images.githubusercontent.com/10147329/127321526-387410de-1562-444d-bb38-e7676ea89691.png)

## Embedding an EOL editor into a Sirius modeller

1. Firstly, the metamodel needs to be modified to store EOL code in the model. In the state machine example, we are storing the EOL code in String attributes. See `org.eclipse.epsilon.sirius.widget.examples.statemachine` for more details.
![image](https://user-images.githubusercontent.com/10147329/127322137-4bf9b34c-7dd2-484c-b407-7526cdacfb3c.png) 
2. You then need to create a new Eclipse plug-in for the widget. This plug-in should use the `org.eclipse.epsilon.sirius.widget` extension point and extend the `AbstractEolEditorWidget` class to provide details of how to load and save the EOL expressions. See `org.eclipse.epsilon.sirius.widget.examples.statemachine.eol.widget` for more details.
3. You need to modify the Sirius viewpoint specification model (odesign) model to add the widget to the properties view. To do this:
    1. Right-click on the group (folder) in the odesign model and select New Properties > Extend Default Properties View. ![image](https://user-images.githubusercontent.com/10147329/127322787-82395483-1e3c-4375-a836-df1ef6f1cc23.png)
    2. Under the category, create a new page and restrict the "Domain Class" to be model elements that contain EOL expressions. ![image](https://user-images.githubusercontent.com/10147329/127322993-7a6bab38-c847-471a-9216-2a40e4ea9161.png) ![image](https://user-images.githubusercontent.com/10147329/127323055-706c35d6-6c62-4d60-9b2b-92e9facb19fb.png)
    3. Create a new group under the category, again restrict the "Domain Class" to be model elements that contain EOL expressions. ![image](https://user-images.githubusercontent.com/10147329/127323200-f8f96fb1-fd5e-4769-a94b-02b9248d9fb7.png) ![image](https://user-images.githubusercontent.com/10147329/127323246-67b8902d-8b51-47a5-a132-f8aa46534920.png)
    4. Go back to the page created above and then add the group created above to the page. ![image](https://user-images.githubusercontent.com/10147329/127323364-1de3f0d3-578a-4ef7-82c2-c2ad8bd2f374.png)
    5. Under the group, create a new Container, this container MUST have an Id of `eol`. ![image](https://user-images.githubusercontent.com/10147329/127323639-d18074b1-d367-4a2d-a197-38e73bfbeaff.png) ![image](https://user-images.githubusercontent.com/10147329/127323458-406cbaad-8556-4db9-8689-d020b8468b44.png)
    6. Under the container, create a new Custom Widget. This custom widget must have an Id of `org.eclipse.epsilon.sirius.widget` ![image](https://user-images.githubusercontent.com/10147329/127323773-b1778d88-5614-44b7-b9fa-9319a5910861.png). ![image](https://user-images.githubusercontent.com/10147329/127323844-4d01d4d5-986d-48f9-ba1f-ebb52f06fe07.png)

After making those changes, save and then launch a new runtime instance of Eclipse. You should now have an embedded EOL editor in your Sirius properties views.

## Running EOL expressions in the editor
We have provided a minimal example of running the state machine from within the editor. 
1. Define a tool in the Sirius viewpoint specification model (odesign model).

![image](https://user-images.githubusercontent.com/10147329/127324268-fba6e367-48f9-4121-89a8-65aaa0a04c3a.png)

2. Define logic in a Java service (Services.java located in the `src` directory of the `org.eclipse.epsilon.sirius.widget.examples.statemachine.design` plug-in) which then calls StatemachineJob.java. ![image](https://user-images.githubusercontent.com/10147329/127324497-c1bd6f0d-7732-46e4-ab08-20b8fd67c348.png)

## Can I embed Xtext in Sirius?

Yes, you can create a widget which uses `org.eclipse.epsilon.sirius.widget` extension point (as above), but extend `AbstractXtextEditorWidget` rather than `AbstractEolEditorWidget`.

You may want to read the [white paper from Obeo and Typefox](https://www.obeodesigner.com/resource/white-paper/WhitePaper_XtextSirius_EN.pdf) and also the [Sirius documentation](https://www.eclipse.org/sirius/doc/developer/extensions-properties_provide_custom_widget_basic.html) detailing how to embed custom widgets into Sirius properties views.





