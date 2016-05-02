package EntryPoint;

import exception.*;
import fr.familiar.interpreter.VariableNotExistingException;
import fr.familiar.parser.VariableAmbigousConflictException;
import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.FeatureVariable;
import fr.familiar.variable.Variable;
import kernel.Pilot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 10/07/2014.
 */
public class Library extends Universe{
    private List<Widget> widgets;

    /* Launch the evaluation on the file widgetsFormulaPath, line by line.
         * It should declare the "atomic" features models (products)
         * we store their fmID in familiar and their formula used to instantiate them in Widgets
         */
    public Library(String inputMultipleFormulas) throws IOException, UnhandledFamiliarException, VariableNotExistingException, VariableAmbigousConflictException {
        String widgetsFormulaPath = Library.class.getClassLoader().getResource(inputMultipleFormulas+"_fms_functions.fml").getPath();

        widgets = new ArrayList<>();
        // test the existence of the file
        if (!new File(widgetsFormulaPath).exists())
            throw new IOException("The given path -"+widgetsFormulaPath+"- does not exist !");
        List<String> inlineFMs = pilot.extractFMsByFile(widgetsFormulaPath);
        List<String> fmIDs = new ArrayList<>();

        for(String formula:inlineFMs){
            Implementation i = new Implementation(formula);
            FeatureVariable fv = pilot.getFVariable(i.getFmID()+".Name");
            String widgetName = fv.children().getVars().toArray(new Variable[]{})[0].getValue();
            Widget widget = new Widget(widgetName);
            if(!this.widgets.contains(widget))
                this.widgets.add(widget);
            else
                widget=this.widgets.get(this.widgets.indexOf(widget));
            widget.addImplementation(i);
            fmIDs.add(i.getFmID());
        }
        this.fmID = pilot.merge(fmIDs);
    }

    public static Library merge(Library u1, Library u2){
        List<Widget> widgetList = new ArrayList<>();
        widgetList.addAll(u1.getWidgets());
        widgetList.addAll(u2.getWidgets());


      List<String> widgetIDList = new ArrayList<>();
        widgetIDList.addAll(u1.getFMIDs());
        widgetIDList.addAll(u2.getFMIDs());

        String newID = pilot.merge(widgetIDList);
        System.out.println("Merging fms "+u1.fmID+" and "+u2.fmID+ "has been performed in "+newID);

/*        List<String> widgetIDList = new ArrayList<>();
        widgetIDList.add(u1.fmID);
        widgetIDList.add(u2.fmID);
        String newID = pilot.merge(widgetIDList);*/

        return new Library(newID, widgetList);
    }



    /*
     * This function take a list of features to select on a given configuration
     */
    public void reduceByFeatures(List<String> ls) throws ReductionException, BadIDException, EmptyUniverseException, ReducingException {
        for(String s : ls)
            reduceByFeature(s);
    }


    /*
 * This function take a feature to select on a given configuration
 */
    public void reduceByFeature(String feature) throws ReductionException, BadIDException, EmptyUniverseException, ReducingException {
        try {
            String configID = pilot.newConfig(fmID);

            pilot.selectFeatureOnConfiguration(feature,configID);

            this.fmID = pilot.asFM(configID);
            List<String> remains = getImplementationUniques();
            List<String> implementationIDs = new ArrayList<>();
            List<Widget> newWidgetsList = new ArrayList<>();
            List<Implementation> newImplementationList = new ArrayList<>();
            for (String remain : remains) {
                for (Widget w : this.widgets) {
                    for (Implementation i : w.getImplementations()) {
                        if (i.getUnique().equalsIgnoreCase(remain)) {
                            implementationIDs.add(i.getFmID());
                            newImplementationList.add(i);
                            if (!newWidgetsList.contains(w))
                                newWidgetsList.add(w);
                            break;
                        }
                    }
                }
            }

            if(implementationIDs.isEmpty())
                throw new EmptyUniverseException("The reduction by widget should never lead to no widget left !");

            this.fmID = pilot.merge(implementationIDs);
            this.widgets = newWidgetsList;

        } catch (FMEngineException e) {
            throw new ReductionException("Something went bad during reduction by the feature " + feature + " on the Universe " + fmID);
        }
    }



    /* _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _   Getters & Setters  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _*/

    public List<Widget> getWidgets() {
        return widgets;
    }

    /* _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _Private useful methods _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _*/

    private List<String> getFMIDs(){
        List<String> FMIDs = new ArrayList<>();
        for(Widget widget : widgets)
            for(Implementation i : widget.getImplementations())
                FMIDs.add(i.getFmID());
        return FMIDs;
    }


    private Library(String ID, List<Widget> widgets){
        this.fmID = ID;
        this.widgets = new ArrayList<>(widgets);
    }

    private Library(List<String> widgetsFormulas){

    }

}
