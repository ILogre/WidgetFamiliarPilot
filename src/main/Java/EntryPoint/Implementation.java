package EntryPoint;

import exception.FMEngineException;
import exception.UnhandledFamiliarException;
import fr.familiar.interpreter.VariableNotExistingException;
import fr.familiar.parser.VariableAmbigousConflictException;
import fr.familiar.variable.FeatureVariable;
import fr.familiar.variable.Variable;
import kernel.Pilot;

/**
 * Created by ivan on 28/04/2016.
 */
public class Implementation {
    // the singleton instance of the pilot
    private static final Pilot pilot = Pilot.getInstance();
    private String fmID;
    private String formula;
    private String unique;

    public Implementation(String formula) throws UnhandledFamiliarException, VariableNotExistingException, VariableAmbigousConflictException {
        this.formula = formula;
        try {
            this.fmID = pilot.declareFM(formula);
            FeatureVariable fv = pilot.getFVariable(this.getFmID()+".Unique");
            this.unique = fv.children().getVars().toArray(new Variable[]{})[0].getValue();

        } catch (FMEngineException e) {
            throw new UnhandledFamiliarException("Failing declaration of a widget feature model.");
        }
        //System.out.println("FM "+fmID+" is declared.");
    }

    public String getFormula() {return formula;}

    public String getFmID() {
        return fmID;
    }

    public String getUnique() {
        return unique;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Implementation that = (Implementation) o;

        if (!fmID.equals(that.fmID)) return false;
        return formula.equals(that.formula);

    }

    @Override
    public int hashCode() {
        int result = fmID.hashCode();
        result = 31 * result + formula.hashCode();
        return result;
    }
}
