# MorseBST + Visualizador (JavaFX)

Aplicação para **codificar/decodificar Código Morse** e **visualizar a árvore binária** usada (ponto `.` vai para a esquerda, traço `-` vai para a direita).

---

## Estrutura dos arquivos

* **`MorseApp.java`** – Aplicação JavaFX (janela, campos de entrada e botões).
* **`MorseBST.java`** – Árvore Morse (inserção, encode, decode e métodos de navegação).
* **`TreeVisualizer.java`** – Desenha a árvore em um `Canvas`.
* **`Node.java`** – Estrutura que representa cada nó da árvore (valor, filhos e controle se possui caractere).

---

## Como funciona

* **Inserção**: percorre a árvore caractere por caractere em Morse.

  * `.` → vai para o filho da esquerda
  * `-` → vai para o filho da direita
  * Ao final, grava a letra no nó.

* **Encode (texto → Morse)**: busca cada caractere na árvore e monta o código Morse correspondente.

* **Decode (Morse → texto)**: percorre a árvore seguindo `.` e `-` e retorna a letra encontrada no nó.

* **Visualização**: o `TreeVisualizer` recebe a raiz da árvore e usa os métodos de navegação (`leftOf`, `rightOf`, `valueOf`, `hasValue`) para desenhar nós e conexões no `Canvas`.

---

## Exemplo

* Texto: `SOS` → Morse: `... --- ...`
* Morse: `.-` → Texto: `A`

---

## Dicas

* Se a árvore estiver vazia, o visualizador mostra uma mensagem de aviso.
* O código já vem preparado para inserir o alfabeto e números na árvore.
