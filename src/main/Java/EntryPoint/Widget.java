package EntryPoint;

import exception.FMEngineException;
import exception.InconsistentImplementationFormulaException;
import exception.UnhandledFamiliarException;
import fr.familiar.interpreter.VariableNotExistingException;
import fr.familiar.parser.VariableAmbigousConflictException;
import fr.familiar.variable.FeatureVariable;
import fr.familiar.variable.Variable;
import kernel.Pilot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 10/07/2014.
 */
public class Widget {
    // the singleton instance of the pilot
    private static final Pilot pilot = Pilot.getInstance();

    // the feature model's unique ID of the familiar representation of this Widget

    private String name;
    private List<Implementation> implementations;

/*
    public Widget(String... formulas) throws UnhandledFamiliarException, VariableNotExistingException, VariableAmbigousConflictException, InconsistentImplementationFormulaException {
        this.implementations = new ArrayList<>();
        name = "";
        for(String f : formulas) {
            Implementation i = new Implementation(f);
            this.implementations.add(i);
            FeatureVariable f_var = pilot.getFVariable(i.getFmID() +".Name");
            String implemName = f_var.children().getVars().toArray(new Variable[]{})[0].getValue();
            if(name.equalsIgnoreCase(""))
                name=implemName;
            else if(!name.equalsIgnoreCase(implemName))
                throw new InconsistentImplementationFormulaException("Two implementation of the same Widget does not share names");
        }
    }*/

    public Widget(String name) {
        this.name = name;
        this.implementations = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Widget widget = (Widget) o;

        return name.equals(widget.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    public void addImplementation(Implementation i){
        if(!this.implementations.contains(i))
            this.implementations.add(i);
    }

    public List<Implementation> getImplementations() {
        return implementations;
    }
}
