package EntryPoint;

import exception.BadIDException;
import exception.FMEngineException;
import exception.GetUniqueElementOnNonCompleteConfiguration;
import fr.familiar.interpreter.VariableNotExistingException;
import fr.familiar.parser.VariableAmbigousConflictException;
import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.FeatureVariable;
import fr.familiar.variable.Variable;
import kernel.Pilot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 02/05/2016.
 */
public class Universe {


    // the singleton instance of the pilot
    protected static final Pilot pilot = Pilot.getInstance();

    // the unique fmID of the feature model representing the variability of known widgets in this collection of widget
    protected String fmID;


    public void displayUniverseState() throws BadIDException {
        FeatureModelVariable fm_var = null;
        try {
            fm_var = pilot.getFMVariable(this.fmID);
        } catch (VariableAmbigousConflictException | VariableNotExistingException e) {
            throw new BadIDException("");
        }
        System.out.println("__________________________________");
        System.out.println("Feature models merge result :");
        System.out.println(fm_var.toString());
        System.out.println("Number of possible configuration :");
        System.out.println(fm_var.counting());
        System.out.println("__________________________________");
    }

    /*
 * This method return the number of asset still available so far.
 * If their is still a pie chart from AmChart and one from HighChart, their will be counted as two.
 */
    public double getNumberSuitableTargets() throws BadIDException{
        try {
            return pilot.countingOnFM(this.fmID);
        } catch (VariableNotExistingException | VariableAmbigousConflictException e) {
            throw new BadIDException("The fmID " + this.fmID + " appears to be incorrect.");
        }
    }

    /*
     * This method return the number of widgets still available so far.
     * If their is still a pie chart from AmChart and one from HighChart, their will be counted as one.
     */
    public int getNumberOfSuitableWidgets() throws BadIDException {
        try {
            FeatureVariable f_var = pilot.getFVariable(fmID +".Name");
            return f_var.children().getVars().size();
        } catch (VariableNotExistingException | VariableAmbigousConflictException e) {
            throw new BadIDException("The fmID " + fmID + " appears to be incorrect.");
        }
    }

    /*
 * Returns the name of the last widget available, if there is only one.
 * Else : don't use this ! Use getWidgetsNames()
 */
    public String getLastWidgetName() throws GetUniqueElementOnNonCompleteConfiguration, BadIDException {
        FeatureVariable f_var;
        try{
            f_var = pilot.getFVariable(fmID +".Name");
        } catch (VariableNotExistingException | VariableAmbigousConflictException e) {
            throw new BadIDException("The fmID " + fmID + " appears to be incorrect.");
        }
        if(f_var.children().getVars().size()==1){
            Variable[] varSet = new Variable[]{};
            Variable v = f_var.children().getVars().toArray(varSet)[0];
            return v.getValue();
        }
        else throw new GetUniqueElementOnNonCompleteConfiguration("Reduction "+ fmID +" appears not to be complete.");
    }

    /*
     * Returns the names of the widgets still available.
     */
    public List<String> getWidgetsNames() throws BadIDException {
        List<String> res = new ArrayList<>();
        FeatureVariable f_var;
        try{
            f_var = pilot.getFVariable(fmID +".Name");
        } catch (VariableNotExistingException | VariableAmbigousConflictException e) {
            throw new BadIDException("The fmID " + this.fmID + " appears to be incorrect.");
        }
        for(Variable v : f_var.children().getVars()){
            FeatureVariable fv = (FeatureVariable) v;
            res.add(fv.getFtName());
        }
        return res;
    }

    /*
     * Returns  the "Unique" identifiers of the implementations still available.
     */
    public List<String> getImplementationUniques() throws BadIDException {
        List<String> res = new ArrayList<>();
        FeatureVariable f_var;
        try{
            f_var = pilot.getFVariable(fmID +".Unique");
        } catch (VariableNotExistingException | VariableAmbigousConflictException e) {
            throw new BadIDException("The fmID " + this.fmID + " appears to be incorrect.");
        }
        for(Variable v : f_var.children().getVars()){
            FeatureVariable fv = (FeatureVariable) v;
            res.add(fv.getFtName());
        }
        return res;
    }
    /*
 * Returns the name of the last library available, if there is only one.
 * Else : don't use this ! Use getLibrariesNames()
 */
    public String getLastLibraryName() throws GetUniqueElementOnNonCompleteConfiguration, BadIDException {
        FeatureVariable f_var;
        try{
            f_var = pilot.getFVariable(fmID +".Library");
        } catch (VariableNotExistingException | VariableAmbigousConflictException e) {
            throw new BadIDException("The fmID " + this.fmID + " appears to be incorrect.");
        }
        if(f_var.children().getVars().size()==1){
            Variable[] varSet = new Variable[]{};
            Variable v = f_var.children().getVars().toArray(varSet)[0];
            return v.getValue();
        }
        else throw new GetUniqueElementOnNonCompleteConfiguration("Reduction "+ fmID +" appears not to be complete.");
    }

    /*
     * Returns the names of the libraries still available.
     */
    public List<String> getLibrariesNames() throws GetUniqueElementOnNonCompleteConfiguration, BadIDException {
        List<String> res = new ArrayList<>();
        FeatureVariable f_var;
        try{
            f_var = pilot.getFVariable(fmID +".Library");
        } catch (VariableNotExistingException | VariableAmbigousConflictException e) {
            throw new BadIDException("The fmID " + this.fmID + " appears to be incorrect.");
        }
        for(Variable v : f_var.children().getVars())
            res.add(v.getIdentifier());
        return res;
    }

    public boolean isMinimal() throws BadIDException{
        try {
            String configID = pilot.newConfig(fmID);
            return pilot.isComplete(configID);
        } catch (FMEngineException e) {
            throw new BadIDException("The fmID " + fmID + " appears to be incorrect or can't have a configuration instantiated.");
        }
    }


    public List<String> getConcerns() throws BadIDException {
        List<String> res = new ArrayList<>();
        FeatureVariable f_var;
        try{
            f_var = pilot.getFVariable(fmID +".Concern");
        } catch (VariableNotExistingException | VariableAmbigousConflictException e) {
            throw new BadIDException("The fmID " + this.fmID + " appears to be incorrect.");
        }
        for(Variable v : f_var.children().getVars()){
            FeatureVariable fv = (FeatureVariable) v;
            res.add(fv.getFtName());
        }
        return res;
    }
        /* _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _   Getters & Setters  _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _*/

    public String getID() {
        return fmID;
    }
}
