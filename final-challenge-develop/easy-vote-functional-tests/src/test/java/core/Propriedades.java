package core;

public class Propriedades {

    public static boolean FECHAR_BROWSER = true;
    public static Browsers BROWSER = Browsers.CHROME; // Usar o Chrome por padrao
    public static TipoExecucao TIPO_EXECUCAO = TipoExecucao.LOCAL;

    public enum Browsers { // Define quais browsers podem ser usados
        CHROME,
        FIREFOX
    }

    public enum TipoExecucao { // Define as formas possiveis de execucao
        LOCAL,
        GRID
    }
}
