package it.unitn.disi.azzoiln_carretta_destro.services;

import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * @author Steve
 */
@javax.ws.rs.ApplicationPath("app/services")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(it.unitn.disi.azzoiln_carretta_destro.services.EsamiWB.class);
        resources.add(it.unitn.disi.azzoiln_carretta_destro.services.FarmaciService.class);
        resources.add(it.unitn.disi.azzoiln_carretta_destro.services.Visite_specWB.class);
    }

}
