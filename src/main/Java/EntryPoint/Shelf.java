package EntryPoint;

import exception.FMEngineException;
import fr.familiar.interpreter.VariableNotExistingException;
import fr.familiar.parser.VariableAmbigousConflictException;
import fr.familiar.variable.FeatureVariable;
import fr.familiar.variable.Variable;
import kernel.Pilot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.apache.logging.log4j.core.helpers.Loader.getClassLoader;

/**
 * Created by ivan on 02/05/2016.
 */
public class Shelf {

    // the singleton instance of the pilot
    private static final Pilot pilot = Pilot.getInstance();

    // the unique fmID of the feature model representing the variability of known widgets in this collection of widget
    private String fmID;

    public Shelf() throws VariableNotExistingException, FMEngineException, VariableAmbigousConflictException, IOException {
        String path = Shelf.class.getClassLoader().getResource("precalculated_merged_fm.fml").getPath();
        BufferedReader br = new BufferedReader(new FileReader(path));
        StringBuilder sb = new StringBuilder();
        while (br.ready() ){
            sb.append(br.readLine().replace("\n"," "));
        }
        this.fmID = pilot.declareFM(sb.toString());
    }

    public List<String> getConcernsByReduction(String... features) throws FMEngineException, VariableNotExistingException, VariableAmbigousConflictException {
        List<String> res = new ArrayList<>();
        String configID = pilot.newConfig(fmID);
        for(String f: features) pilot.selectFeatureOnConfiguration(f, configID);
        FeatureVariable fv = pilot.getFVariable(fmID+".Concern");
        Set<Variable> concerns = fv.children().getVars();
        Collection<String> selected = pilot.getSelectedFeature(configID);
        concerns.forEach(v -> res.add(v.getValue()));
        res.retainAll(selected);
        return res;
    }


}
