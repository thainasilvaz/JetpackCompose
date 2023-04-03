package com.example.basics_codelab

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basics_codelab.ui.theme.Basics_CodelabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Basics_CodelabTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
/*
* Nota de boas práticas: inclua, assim que possível, nas funções composable um parâmetro modificador atribuido
* a um modificador vazio po padrão
*
* Isso porque, o elemento pode adaptar instruções e comportamentos de layout fora da função composable.
* Agora, podemos reutilizar essa composição (Observe "onCreate")
*/

/*
* Não se oculta elementos de IU, em vez disso, você adiciona ou não esse elemento à composição, o que pode ser
* feito através de uma estrutura condicional
* */
@Composable
//MyApp(modifier: nome Parâmetro = Tipo)
fun MyApp(modifier: Modifier = Modifier) {
    /*
    * Apps com Compose transformam dados em interface, e se esses dados mudam, o Compose reexecuta essas
    * funções e as atualiza (recomposição), como também nalisa quais dados são necessários para uma composição individual
    * Basicamente, o Compose analisa os dados que foram mudandos e onde ele tem que mudar
    * Mas, durante o uso do app, o estado de uma composição, por exemplo, não é mudado, pois o Compose não tem a capacidade
    * de analisar um mudança de estado interno
    *
    * Então, usamos o mutableStateOf (ou State) para fazer o Compose enxergar essa mudança
    * Mas não é só isso, o Compose pode se "esquecer" de que aquela variável tinha um valor true, em um
    * dado momento, e quando for requesitada para se redefinir, retornar a variável ao estado false, ou vice e versa
    * Por isso, usamo o "remember", como forma do Compose "se lembrar" do estado da variável
    *
    * Nesse caso, "rememberSaveable", essa função vai fazer o Compose se lembrar dela independente da circunstancia
    * Girar o celular, mudar alguma configuração, tentar eliminar o processo... O compose ainda vai se lembrar (a menos
    * que você tenha fechado o aplicativo antes da ação se concretizar)
    * */

    //"by" é como um "="
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    /*
    Surface: uma espécie de container; uma de suas funções pode ser aplicar uma cor de background(ou color) ou padding
    Quando a Surface entende que o valor passado em "color" é definida como Primary, os textos dentro da surface
    automaticamente de adequam a cor onPrimary também definida no tema
    */
    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        //Elementos UI como Surface e Text aceitam parâmetros modifier
        //modifier: pode ser utilizado para espaçamento, tamanho, formato, comportamento, estado...

        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            Greetings()
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier ) {
    Column(
        //Nota: Weight = fillMaxWidth()
        modifier = modifier.fillMaxSize(),
        // Alinhamento em relação a y é Centralizado
        verticalArrangement = Arrangement.Center,
        // Alinhamento em relação a x é Centralizado Horizontalmente
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    //Construtor de lista que cria 10 saudações (nesse contexto, $it significa o índice)
    names: List<String> = List(10) { "$it" }
) {
    //Para exibir uma coluna rolável, usamos LazyColumn, que renderiza somente os itens visíveis na tela, gerando um aumneto no desempenho
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
private fun Greeting(name: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name)
    }
}

@Composable
private fun CardContent(name: String) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        //Para adicionar mais de um modificador, basta escrever um abaixo do outro
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                //Weight: modificador que faz o elemento preencher o espaço por completo, o que o torna flexível
                //Nota: Weight = fillMaxWidth()
                //Essa coluna terá o tamanho de uma fração do espaço, ou seja, metade do espaço da linha
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = "Hello, ")
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4),
                )
            }
        }
        //Expanded: valor booleano; nesse contexto, se o botão está no estado de expandido ou não
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Filled.ExpandLess else Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }
}



//Preview: Pré visualização de alguns elementos do app

//Configuração Geral do Preview
@Preview(
    //Mostrar o Background
    showBackground = true,
    //Largura do Preview de 320 dp, semelhante a de um celular, para visualizar melhor a disposição/alinhamento dos elementos
    widthDp = 320,
    //Pré visualização do modo escuro do aplicativo
    //Nota: as cores do modo escuro foram selecionadas em "Theme.kt" e declaradas em "colors.xml"
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)

@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    Basics_CodelabTheme {
        Greetings()
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    Basics_CodelabTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}

@Preview
@Composable
fun MyAppPreview() {
    Basics_CodelabTheme {
        MyApp(Modifier.fillMaxSize())
    }
}