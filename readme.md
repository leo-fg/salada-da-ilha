# Projeto Salada da Ilha - Kotlin e MongoDB

## Estrutura do Projeto

```
app/
├── src/
│   ├── main/
│   │   ├── java/br/com/saladadailha/
│   │   │   ├── data/                 (Repositórios para acesso de dados)
│   │   │   ├── model/                (Classes de modelo)
│   │   │   ├── ui/                   (Interfaces do usuário)
│   │   │   │   ├── adapters/         (Adaptadores para RecyclerViews)
│   │   │   │   └── fragments/        (Fragmentos da interface)
│   │   │   ├── utils/                (Classes utilitárias)
│   │   │   ├── viewmodel/            (ViewModels para lógica de negócio)
│   │   │   └── SaladariaApplication.kt (Classe principal da aplicação)
│   │   └── res/                      (Recursos do app)
└── build.gradle                      (Configuração do projeto)
```

## Tecnologias Utilizadas

- **Kotlin**: Linguagem de programação principal
- **MongoDB Realm**: Banco de dados NoSQL para armazenamento local
- **MVVM**: Padrão de arquitetura Model-View-ViewModel
- **ViewBinding**: Para acesso simplificado às views
- **Navigation Component**: Para navegação entre telas
- **StateFlow e Coroutines**: Para gerenciamento assíncrono de dados
- **RecyclerView com ListAdapter**: Para exibição eficiente de listas
- **Glide**: Para carregamento e cache de imagens

## Funcionalidades

1. **Cardápio categorizado**
   - Visualização de produtos separados por categorias (Saladas, Proteínas, Bebidas)
   - Interface com abas para fácil navegação

2. **Carrinho de compras**
   - Adição de produtos ao carrinho
   - Ajuste de quantidades
   - Remoção de itens
   - Cálculo de subtotal

3. **Checkout**
   - Seleção de método de entrega (Retirada ou Entrega)
   - Endereço para entrega
   - Seleção de método de pagamento (Cartão de Crédito, Débito, PIX, Dinheiro)
   - Detalhes do cartão quando aplicável
   - Resumo do pedido com valores

4. **Finalização e Confirmação**
   - Geração de número de pedido
   - Tela de confirmação do pedido

## Requisitos para Execução

- Android Studio Arctic Fox (2020.3.1) ou superior
- JDK 8 ou superior
- SDK mínimo: API 24 (Android 7.0)
- SDK alvo: API 33 (Android 13)

## Instalação

1. Clone este repositório:
   ```
   git clone https://github.com/leo-fg/salada-da-ilha
   ```

2. Abra o projeto no Android Studio

3. Sincronize o projeto com os arquivos Gradle

4. Execute o aplicativo em um emulador ou dispositivo físico

## Personalização

Para personalizar o aplicativo para suas necessidades:

1. Modifique os produtos em `ProductRepository.kt` na função `addInitialProducts()`
2. Atualize as cores do app em `colors.xml`
3. Modifique os textos em `strings.xml`
4. Atualize o logo e ícones nas pastas `mipmap`
