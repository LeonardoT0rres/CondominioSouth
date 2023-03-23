package core;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

    private static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<WebDriver>() { // Para deixar um driver para cada thread na execucao em paralelo do maven
        @Override
        protected synchronized WebDriver initialValue() {
            // Cria uma instancia do WebDriver propria para essa thread
            return initDriver();
        }
    };

    private DriverFactory() { // Privado para nao ser instanciada
    }

    public static WebDriver getDriver() {
        // Sempre que alguem pedir um driver, a thread retorna a que esta utilizando
        return threadDriver.get();
    }

    public static WebDriver initDriver() {
        WebDriver driver = null;
        if(Propriedades.TIPO_EXECUCAO == Propriedades.TipoExecucao.LOCAL) {
            switch (Propriedades.BROWSER) { // Instancia de acordo com a propriedade setada
                case FIREFOX:
                    driver = new FirefoxDriver();
                    break;
                case CHROME:
                    driver = new ChromeDriver();
                    break;
            }
        } else if(Propriedades.TIPO_EXECUCAO == Propriedades.TipoExecucao.GRID) {

            DesiredCapabilities capabilities = new DesiredCapabilities();

            switch (Propriedades.BROWSER) { // Instancia de acordo com a propriedade setada
                case FIREFOX:
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
                    firefoxOptions.merge(capabilities);
                    break;
                case CHROME:
                    ChromeOptions chromeOptions = new ChromeOptions();
                    capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                    chromeOptions.merge(capabilities);
                    break;
            }
            try {
                driver = new RemoteWebDriver(new URL("http://192.168.0.252:4444/wd/hub"), capabilities);
            } catch (MalformedURLException e) {
                System.err.println("Falha na conexão com o GRID");
                throw new RuntimeException(e);
            }
        }

        driver.manage().window().setPosition(new Point(0, 0));

        return driver;
    }

    public static void killDriver() {
        WebDriver driver = getDriver();
        if(driver != null) {
            driver.quit();
            driver = null;
        }
        if(threadDriver != null) {
            threadDriver.remove();
        }
    }
}
