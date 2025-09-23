# Jogo da Forca - Arquitetura de Software

Este documento descreve a arquitetura de software para um sistema de Jogo da Forca, com base nos diagramas UML do projeto. O design é altamente estruturado, seguindo os princípios da arquitetura em camadas e empregando diversos padrões de projeto para garantir flexibilidade, manutenibilidade e baixo acoplamento.

## Visão Geral da Arquitetura

O sistema é modularizado em camadas distintas, cada uma com uma responsabilidade clara e identificada por uma cor nos diagramas de modelo:

* **Modelo de Domínio (Sem Cor):** Contém as regras de negócio e as entidades centrais do jogo[cite: 1].
* **Repositórios (Amarelo):** Camada de acesso a dados, responsável pela persistência das entidades[cite: 147].
* **Fábricas (Verde):** Responsáveis pela criação e instanciação de objetos complexos[cite: 220, 288, 323, 342, 363].
* **Serviços de Aplicação (Azul):** Orquestram os casos de uso do sistema, atuando como uma fachada para o domínio[cite: 387].
* **Configuração (Roxo):** Ponto central que inicializa e interliga todos os componentes da aplicação[cite: 431].

## Modelo de Domínio

O núcleo do sistema é composto pelas seguintes entidades de negócio:

* **`Jogador`**: Representa o usuário que participa do jogo, contendo seu nome e pontuação total[cite: 97, 98, 99].
* **`Rodada`**: Modela uma partida do jogo. É a classe central que gerencia as palavras, as tentativas do jogador e o estado atual do jogo (erros, acertos, pontuação da rodada)[cite: 69].
* **`Palavra`**: Representa a palavra a ser adivinhada, sendo composta por um conjunto de objetos `Letra`[cite: 3].
* **`Tema`**: Agrupa um conjunto de `Palavras` relacionadas (ex: Frutas, Países, etc.)[cite: 12].
* **`Letra`**: Representa um caractere do alfabeto. É implementada com o padrão **Flyweight**, otimizando o uso de memória ao reutilizar a mesma instância para letras iguais[cite: 54, 276].
* **`Boneco`**: Interface para a representação visual do boneco da forca[cite: 111]. Possui implementações para texto (`BonecoTexto`) e imagem (`BonecoImagem`), permitindo que a UI seja facilmente alterada[cite: 113, 116].

## Padrões de Projeto (Design Patterns)

A arquitetura emprega massivamente padrões de projeto para atingir seus objetivos de design:

* **Repository**: Abstrai a fonte de dados. Para cada entidade, existe uma interface (ex: `PalavraRepository`) [cite: 152] e implementações concretas para persistência em memória (`MemoriaPalavraRepository`) [cite: 186] ou em Banco de Dados Relacional (`BDRPalavraRepository`)[cite: 190]. Isso permite trocar o método de armazenamento sem impactar o resto do sistema.

* **Abstract Factory**: O `ElementoGraficoFactory` é uma fábrica abstrata para criar uma família de objetos relacionados à UI (`Boneco` e `Letra`)[cite: 244]. As implementações `ElementoGraficoTextoFactory` [cite: 257] e `ElementoGraficoImagemFactory` [cite: 263] permitem que toda a aparência visual do jogo seja alterada de forma consistente.

* **Factory Method**: Usado internamente nas fábricas para delegar a instanciação de objetos para subclasses, como visto no método `criarLetra` da `LetraFactoryImpl`[cite: 249].

* **Singleton**: Garante que exista apenas uma instância de classes críticas, como as fábricas e os serviços (ex: `getSoleInstance()`)[cite: 228, 241]. O projeto utiliza o conceito de **Singleton Parametrizado**, onde a instância única é criada com parâmetros de configuração específicos[cite: 317, 398].

* **Facade**: Os Serviços de Aplicação (ex: `RodadaAppService`) [cite: 416] atuam como uma fachada, provendo uma interface simplificada para as operações complexas do sistema[cite: 415]. Por exemplo, o método `novaRodada` encapsula toda a lógica de buscar um jogador, sortear palavras e criar um novo objeto `Rodada`[cite: 403, 406].

* **Flyweight**: A classe `Letra` utiliza este padrão para compartilhar objetos e reduzir o consumo de memória, evitando a criação de múltiplos objetos para a mesma letra[cite: 276].

## Configuração e Inicialização

O ponto de entrada e configuração do sistema é a classe `Aplicacao`[cite: 432].

Ela é um **Singleton Parametrizado** que tem como responsabilidade:
1.  **Configurar o Ambiente**: Através do método `configurar()`, a classe lê as configurações definidas (ex: tipo de repositório, tipo de UI) e inicializa as fábricas concretas correspondentes[cite: 432].
2.  **Prover Dependências**: É o único ponto da aplicação que conhece as implementações concretas. O restante do sistema solicita as fábricas através de suas interfaces (ex: `getRepositoryFactory()`), respeitando o Princípio da Inversão de Dependência[cite: 432].

### Exemplo de Uso (Pseudo-código)

```java
// 1. Obter a instância única da aplicação
Aplicacao app = Aplicacao.getSoleInstance();

// 2. Definir os tipos de fábrica a serem usados
app.setTipoRepositoryFactory("memoria"); // ou "relacional"
app.setTipoElementoGraficoFactory("texto"); // ou "imagem"
app.setTipoRodadaFactory("sorteio");

// 3. Efetivar a configuração
app.configurar();

// 4. Obter os serviços/fábricas necessários
RodadaFactory rodadaFactory = app.getRodadaFactory();
RodadaAppService rodadaService = RodadaAppService.getSoleInstance(); // Serviço também configurado via 'Aplicacao'

// 5. Iniciar uma nova rodada
try {
    Rodada novaRodada = rodadaService.novaRodada("Nome do Jogador");
    // ... iniciar a lógica do jogo com o objeto 'novaRodada'
} catch (JogadorNaoEncontradoException e) {
    // Tratar o caso de jogador não existente
}
