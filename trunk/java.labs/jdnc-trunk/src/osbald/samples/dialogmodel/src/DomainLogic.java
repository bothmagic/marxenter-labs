package uk.co.osbald.sample;

import java.util.Arrays;
import java.util.Collection;

/*
 * Just a bit of fluff to simulate the business logic
 *
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston (osbald.co.uk)
 * Date: 20-Nov-2006
 * Time: 11::43:37
 */

public class DomainLogic {

    static DomainLogic instance = new DomainLogic();

    private DomainLogic() {
    }

    public static DomainLogic getInstance() {
        return instance;
    }

    public Collection<Module> getAvailableModules() {
        return Arrays.asList(
                new Module(643, "Bad Bob", null),
                new Module(32, "Rex", null),
                new Module(666, "Vince", null),
                new Module(234, "Wendy", null),
                new Module(73, "Arthur Dustcart", null)
        );
    }
}
