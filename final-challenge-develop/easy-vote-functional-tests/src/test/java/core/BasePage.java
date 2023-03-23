package core;

import io.cucumber.core.api.Scenario;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static core.DriverFactory.getDriver;

public class BasePage {

    public static void screenshot(Scenario cenario) {
        File file = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File("target" + File.separator + "screenshot" +
                    File.separator + cenario.getName() + "." + cenario.getLine() + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void esperarSerClicavel(String id) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
    }

    public void esperarPorId(String id) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    public void esperarNaoVazio(String id) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id(id), "")));
    }

    public void escrever(By by, String texto) {
        getDriver().findElement(by).clear(); // Limpa o campo, caso tenha algo escrito
        getDriver().findElement(by).sendKeys(texto);
    }

    public void escrever(String id_campo, String texto) {
        escrever(By.id(id_campo), texto);
    }

    public String obterValorCampo(By by) {
        return getDriver().findElement(by).getAttribute("value");
    }

    public String obterValorCampo(String id_campo) {
        return obterValorCampo(By.id(id_campo));
    }

    public void clicarBotao(By by) {
        getDriver().findElement(by).click();
    }

    public void clicarBotao(String id) {
        clicarBotao(By.id(id));
    }

    public void clicarBotaoPorTexto(String texto) {
        clicarBotao(By.xpath("//button[.='" + texto + "']"));
    }

    public boolean isBotaoMarcado(By by) {
        return getDriver().findElement(by).isSelected();
    }

    public boolean isBotaoMarcado(String id) {
        return isBotaoMarcado(By.id(id));
    }

    public void selecionarCombo(String id, String valor) {
        WebElement element = getDriver().findElement(By.id(id));
        Select combo = new Select(element);
        combo.selectByVisibleText(valor);
    }

    public void desselecionarCombo(String id, String valor) {
        WebElement element = getDriver().findElement(By.id(id));
        Select combo = new Select(element);
        combo.deselectByVisibleText(valor);
    }

    public void selecionarComboPrime(String radical, String valor) {
        clicarBotao(By.xpath("//*[@id='" + radical + "_input']/../..//span"));
        clicarBotao(By.xpath("//*[@id='" + radical + "_items']//li[.='" + valor + "']"));
    }

    public String obterValorCombo(String id) {
        WebElement element = getDriver().findElement(By.id(id));
        Select combo = new Select(element);
        return combo.getFirstSelectedOption().getText();
    }

    public List<String> obterValoresCombo(String id) {
        WebElement element = getDriver().findElement(By.id(id));
        Select combo = new Select(element);
        List<WebElement> valoresCombo = combo.getAllSelectedOptions();
        List<String> lista = new ArrayList<>();
        for (WebElement valor : valoresCombo) {
            lista.add(valor.getText());
        }
        return lista;
    }

    public int obterQtddOpcoesCombo(String id) {
        WebElement element = getDriver().findElement(By.id(id)); // Acha a questao de opcoes em lista
        Select combo = new Select(element);
        List<WebElement> options = combo.getOptions(); // Retorna uma lista com todas as opcpes disponiveis na questao
        return options.size();
    }

    public boolean verificarOpcaoCombo(String id, String texto) {
        WebElement element = getDriver().findElement(By.id(id)); // Acha a questao de opcoes em lista
        Select combo = new Select(element);
        List<WebElement> options = combo.getOptions();
        for(WebElement option: options) {
            if(option.getText().equals(texto)) {
                return true;
            }
        }
        return false;
    }

    public void clicarLink(By by) {
        getDriver().findElement(by).click();
    }

    public void clicarLink(String link) {
        clicarLink(By.linkText(link));
    }

    public String obterTexto(By by) {
        return getDriver().findElement(by).getText();
    }

    public String obterTextoTitulo() {
        return getDriver().getTitle();
    }

    public String obterTexto(String id) {
        return obterTexto(By.id(id));
    }

    public String obterTextoAlerta() {
        Alert alert = getDriver().switchTo().alert();
        return alert.getText();
    }

    public String obterTextoAlertaEAceita() {
        Alert alert = getDriver().switchTo().alert();
        String texto = alert.getText();
        alert.accept();
        return texto;
    }

    public String obterTextoAlertaECancela() {
        Alert alert = getDriver().switchTo().alert();
        String texto = alert.getText();
        alert.dismiss();
        return texto;
    }

    public void escreverAlertaEAceita(String texto) {
        Alert alert = getDriver().switchTo().alert();
        alert.sendKeys(texto);
        alert.accept();
    }

    public void entrarFrame(String id) {
        getDriver().switchTo().frame(id);
    }

    public void sairFrame() {
        getDriver().switchTo().defaultContent();
    }

    public void trocarJanela(String id) {
        getDriver().switchTo().window(id);
    }

    // Javascript
    public Object executarJS(String cmd, Object... param) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        return js.executeScript(cmd, param);
    }

    // Tabela - metodo complexo e util
    public void clicarBotaoTabela(String colunaBusca, String valor, String colunaBotao, String idTabela) {
        // Encontrar a coluna do registro
        WebElement tabela = getDriver().findElement(By.xpath(".//*[@id='" + idTabela + "']")); // Este xpath leva ate a tabela
        int idColuna = obterIndiceColuna(colunaBusca, tabela);

        // Encontrar a linha do registro
        int idLinha = obterIndiceLinha(valor, tabela, idColuna);

        // Descobrir em qual coluna esta o botao
        int idColunaBotao = obterIndiceColuna(colunaBotao, tabela);

        // Clicar no botao da celula encontrada
        WebElement celula = tabela.findElement(By.xpath(".//tr[" + idLinha + "]/td[" + idColunaBotao + "]"));
        celula.findElement(By.xpath(".//input")).click();
    }

    public int obterIndiceColuna(String coluna, WebElement tabela) {
        List<WebElement> colunas = tabela.findElements(By.xpath(".//th")); // Encontra as colunas
        int idColuna = -1; // Inicia em -1 porque a tabela comeca em 0
        for (int i = 0; i < colunas.size(); i++) {
            if(colunas.get(i).getText().equals(coluna)) {
                idColuna = i + 1; // Coluna encontrada - a lista comeca em 0, mas o xpath comeca em 1
                break;
            }
        }
        return idColuna;
    }

    public int obterIndiceLinha(String valor, WebElement tabela, int idColuna) {
        List<WebElement> linhas = tabela.findElements(By.xpath("./tbody/tr/td[" + idColuna + "]"));
        int idLinha = -1;
        for (int i = 0; i < linhas.size(); i++) {
            if(linhas.get(i).getText().equals(valor)) {
                idLinha = i + 1;
                break;
            }
        }
        return idLinha;
    }
}