## 1. Objetivo do sistema

Organizar os cadastros de animais e veterinários e a agenda de consultas da Clínica Veterinária VidaPet, reduzindo conflitos de horário e consultas esquecidas. O sistema atende cães e gatos e considera dois perfis operacionais: Recepcionista e Veterinário.

## 2. Atores

| Ator | Responsabilidades previstas |
|---|---|
| Recepcionista | Cadastrar animais e veterinários; agendar e cancelar consultas. |
| Veterinário | Consultar a própria agenda do dia; registrar como realizada uma consulta sob sua responsabilidade e informar as observações do atendimento. |

## 3. Entidades e dados principais

| Entidade | Dados mínimos previstos | Observações de modelagem |
|---|---|---|
| Animal | id, nome, espécie, idade, nome do tutor e telefone do tutor | Espécie restrita a `CÃO` ou `GATO`. |
| Veterinário | id, nome e CRMV | Formato do CRMV segue formato da regex *. |
| Consulta | id, animal, veterinário, data, horário de início, horário de termino, duração, situação e observações | A duração é de 30 minutos. A situação inicial é `AGENDADA`, restrita a `AGENDADA`, `REALIZADA` OU `CANCELADA`. |

* formato gerado com IA (gemini): ^CRMV-(AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO)\s\d{3,6}(\s?[SZP]|/[SZP])?$

## 4. Requisitos funcionais (RF)

| ID   | Requisito                                                                                                                                     |
|------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| RF01 | O sistema deve permitir cadastro de animais com nome, espécie (cão ou gato), idade, nome do tutor e telefone do tutor, data de cadastro       |
| RF02 | O sistema deve permitir cadastrar um veterinário com nome, CRMV, data de cadastro.                                                            |
| RF03 | O sistema deve permitir agendar uma consulta associando um animal cadastrado, um veterinário cadastrado, uma data e um horário.               |
| RF04 | O sistema deve atribuir, a novas consultas, a situação inicial `AGENDADA` e a duração de 30 minutos.                                          |
| RF05 | Antes de confirmar o agendamento, o sistema deve validar as regras de dia da semana, horário, data e disponibilidade do veterinário.          |
| RF06 | O sistema deve permitir que o veterinário consulte a agenda do próprio dia, identificando os horários, animais e situações das consultas.     |
| RF07 | O sistema deve permitir que a Recepcionista cancele uma consulta somente quando sua situação for `AGENDADA`.                                  |
| RF08 | O sistema deve permitir que o Veterinário responsável registre a realização de uma consulta agendada e informe as observações do atendimento. |
| RF09 | O sistema deve validar, ao marcar uma consulta como `REALIZADA`, o nome do responsável, situacao da consulta, data e horário.                 |
| RF10 | O sistema deve preservar os cadastros e as consultas entre execuções, por meio de persistência de dados.                                      |
| RF11 | Nas etapas com autenticação, o sistema deve identificar o usuário e restringir as operações conforme o papel de Recepcionista ou Veterinário. |

## 5. Requisitos não funcionais (RNF)

Os requisitos abaixo são propostas de qualidade para discussão. A especificação do professor não apresenta metas quantitativas, volume de usuários ou disponibilidade contratada; por isso, metas numéricas devem ser definidas com o professor/cliente antes de serem tratadas como critérios de aceite.

| ID    | Requisito não funcional proposto                                                        |
|-------|-----------------------------------------------------------------------------------------|
| RNF01 | O sistema deve ser desenvolviod em Java 21 ou C#                                        |
| RNF02 | Os dados devem ser mantido entre execuções                                              |
| RNF03 | As chamadas devem responder em até 2s                                                   |
| RNF04 | O projeto sera versionado e disponibilizado via GitHub.                                 |
| RNF05 | O código deve ser organizado para permitir evolução gradual e manutencão deste projeto. |

## 6. Regras de negócio (RN)

| ID   | Regra                                                                                                                          |
|------|--------------------------------------------------------------------------------------------------------------------------------|
| RN01 | Toda consulta dura 30 minutos.                                                                                                 |
| RN02 | Só são atendidos animais das espécies cão e gato.                                                                              |
| RN03 | Consultas só podem ser marcadas de segunda a sexta-feira.                                                                      |
| RN04 | O expediente para consultas é das 08:00 às 18:00.                                                                              |
| RN05 | Não é permitido agendar consulta em data passada.                                                                              |
| RN06 | Um veterinário não pode ter duas consultas agendadas em intervalos sobrepostos.                                                |
| RN07 | Uma consulta só pode ser cancelada enquanto estiver `AGENDADA`.                                                                |
| RN08 | Somente consulta `AGENDADA` pode ser marcada como `REALIZADA`.                                                                 |
| RN09 | Somente o veterinário associado à consulta pode marcá-la como `REALIZADA`.                                                     |
| RN10 | A Recepcionista executa cadastro, agendamento e cancelamento; o Veterinário consulta a própria agenda e registra a realização. |

## 7. Casos de uso e rastreabilidade

O diagrama PlantUML está em `docs/diagramas/casos-de-uso.puml`.

| Caso de uso                             | Ator principal | Requisitos relacionados                                    |
|-----------------------------------------|----------------|------------------------------------------------------------|
| UC01 — Cadastrar animal                 | Recepcionista  | RF01, RN02                                                 |
| UC02 — Cadastrar veterinário            | Recepcionista  | RF02                                                       |
| UC03 — Agendar consulta                 | Recepcionista  | RF03, RF04, RF05, RF06, RN01, RN03, RN04, RN05, RN06, RN10 |
| UC04 — Cancelar consulta                | Recepcionista  | RF08, RN07, RN10                                           |
| UC05 — Consultar agenda do dia          | Veterinário    | RF07, RN10                                                 |
| UC06 — Registrar realização da consulta | Veterinário    | RF09, RN08, RN09, RN10                                     |

### UC01 — Cadastrar animal

- **Ator principal:** Recepcionista.
- **Pré-condição:** a Recepcionista está identificada.
- **Pós-condição de sucesso:** animal cadastrado com identificador e dados informados.
- **Fluxo principal:**
    1. A Recepcionista solicita o cadastro e informa nome, espécie, idade, nome do tutor e telefone do tutor.
    2. O Sistema verifica o preenchimento dos dados, valida se a espécie corresponde a cão ou gato e solicita confirmação do cadastro.
    3. A Recepcionista confirma os dados informados.
    4. O Sistema registra o animal e informa a conclusão do cadastro.
- **Fluxos alternativos:**
    1. A recepcionista informa espécie inválida 
    2. o Sistema informa quais dados precisam ser corrigidos 
    3. A recepcionista pode corrigir o campo ou desistir do cadastro. 
    4. Sem a correção, não cadastra o animal.

### UC02 — Cadastrar veterinário

- **Ator principal:** Recepcionista.
- **Pré-condição:** a Recepcionista está identificada.
- **Pós-condição de sucesso:** veterinário cadastrado com identificador, nome e CRMV.
- **Fluxo principal:**
    1. A Recepcionista solicita o cadastro e informa nome e CRMV do veterinário.
    2. O Sistema verifica se os campos obrigatórios foram informados e solicita confirmação.
    3. A Recepcionista confirma os dados.
    4. O Sistema registra o veterinário e informa a conclusão do cadastro.
- **Fluxos alternativos:**
  1. A recepcionista deixa de informar campo obrigatório
  2. o Sistema informa a ausência a ser corrigida
  3. A recepcionista pode corrigir o campo ou desistir do cadastro.
  4. Sem a correção, não cadastra o veterinário.
    
### UC03 — Consultar agenda do dia

- **Ator principal:** Veterinário.
- **Pré-condição:** o Veterinário está identificado.
- **Pós-condição de sucesso:** agenda do dia apresentada sem alteração dos registros.
- **Fluxo principal:**
    1. O Veterinário solicita a agenda do dia.
    2. O Sistema identifica o veterinário associado ao usuário autenticado, recupera as consultas da data atual, organiza os horários em ordem crescente e apresenta os dados e a situação de cada consulta.
    3. O Veterinário consulta as informações apresentadas.
- **Fluxos alternativos:**
  1. Não há consultas para o dia
  2. o Sistema informa não existem consultas registradas para aquele profissional naquela data
  

## 8. Pontos em aberto

| ID  | Ponto em aberto            | pergunta de validação                                               |
|-----|----------------------------|---------------------------------------------------------------------|
| P01 | Unidade da idade do animal | A idade será armazenada em anos, meses ou data de nascimento?       |
| P02 | CRMV                       | A regex proposta atende o problema?                                 |
| P03 | Observações                | Existe limite de caracteres neste campo? Ele eh obrigatório?        |
| P04 | Agenda diária              | Apresentar todas as consultas da agenda, ou somente as `AGENDADAS`? |
