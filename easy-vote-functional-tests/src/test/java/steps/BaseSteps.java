package steps;

import core.Propriedades;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;

import static core.BasePage.screenshot;
import static core.DriverFactory.killDriver;

public class BaseSteps {

    @After(order = 1)
    public void evidencia(Scenario cenario) {
        screenshot(cenario);
    }

    @After(order = 0)
    public void fecharBrowser() {
        if(Propriedades.FECHAR_BROWSER) {
            killDriver();
        }
    }
}
