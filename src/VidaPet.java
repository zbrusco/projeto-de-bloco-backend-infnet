//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class VidaPet {
    public static void main(String[] args) {

        int duracaoMinutos = 30;

        // VARIAVEIS DE ENTRADA
        // Dia da semana: 1 = domingo ... 7 = sabado.
        int diaSemana = 2;
        // Horário proposto para a nova consulta.
        int horaInicio = 17;
        int minutoInicio = 29;

        // 'PERSISTENCIA' DE DADOS
        // Consulta já cadastrada para o mesmo veterinário, ainda agendada para 17:00.
        int inicioConsultaExistenteMin = (17 * 60);
        int fimConsultaExistente = inicioConsultaExistenteMin + duracaoMinutos;


        // VARIAVEIS DERIVADAS
        int inicioMinutos = (horaInicio * 60) + minutoInicio;
        int fimMinutos = inicioMinutos + duracaoMinutos;

        // REGRAS DE NEGOCIO
        boolean diaUtil = diaSemana >= 2 && diaSemana <= 6;
        boolean horarioValido = horaInicio >= 0 && horaInicio <= 23
                && minutoInicio >= 0 && minutoInicio <= 59;
        boolean dentroDoExpediente = inicioMinutos >= (8 * 60)
                && fimMinutos <= (18 * 60);

        // Intervalos consulta [início, fim): consultas consecutivas
        // podem começar exatamente quando a anterior termina.
        boolean existeConflito = inicioMinutos < fimConsultaExistente
                && inicioConsultaExistenteMin < fimMinutos;

        System.out.println("Horário Solicitado — VidaPet");
        System.out.printf("Início proposto: %02d:%02d%n", horaInicio, minutoInicio);
        System.out.printf("Término calculado: %02d:%02d%n",
                (fimMinutos / 60), (fimMinutos % 60));

        if (!horarioValido) {
            System.out.println("Status: horário informado é inválido.");
        } else if (!diaUtil) {
            System.out.println("Status: consultas só podem ocorrer de segunda a sexta.");
        } else if (!dentroDoExpediente) {
            System.out.println("Status: a consulta deve ocorrer entre 08:00 e 18:00.");
        } else if (existeConflito) {
            System.out.println("Status: já existe consulta agendada nesse intervalo.");
        } else {
            System.out.println("Status: horário válido no cenário de teste.");
        }
    }
}