package task.lt.db;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.io.RuntimeIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ParametersAreNonnullByDefault
public class OrgTypesInitializer {

    private static final Logger logger = LoggerFactory.getLogger(OrgTypesInitializer.class);
    private static final String RESOURCE = "org_types.txt";

    private final OrgTypesDao dao;

    public OrgTypesInitializer(OrgTypesDao dao) {
        this.dao = dao;
    }

    /**
     * Inserts the set of types into the table. The data set is taken from text resource.
     */
    public void populateOrgTypes() {
        readLinesResource().stream()
                .filter(StringUtils::isNotBlank)
                .forEach(dao::add);
    }

    private List<String> readLinesResource() {
        try {
            return Files.readAllLines(new File(this.getClass().getResource(RESOURCE).toURI()).toPath());
        } catch (IOException | URISyntaxException e) {
            logger.error("Couldn't read organization types population data from resource.", e);
            throw new RuntimeIOException(e);
        }
    }
}
